package bd.com.supply.module.wallet.presenter;

import android.text.TextUtils;
import android.util.Log;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.TimeUnit;

import bd.com.appcore.base.ModelCallBack;
import bd.com.appcore.mvp.BasePresenterImpl;
import bd.com.appcore.rx.LogicException;
import bd.com.appcore.rx.RxTaskCallBack;
import bd.com.appcore.rx.RxTaskScheduler;
import bd.com.appcore.util.AppSettings;
import bd.com.supply.module.async.AsyncHelper;
import bd.com.supply.module.async.PriceAsync;
import bd.com.supply.module.async.TokenAsync;
import bd.com.supply.module.common.GlobConfig;
import bd.com.supply.module.user.domian.FunItemBean;
import bd.com.supply.module.wallet.ApiConfig;
import bd.com.supply.module.wallet.model.CoinModel;
import bd.com.supply.module.wallet.model.TokenModel;
import bd.com.supply.module.wallet.model.domain.ChainListListResp;
import bd.com.supply.module.wallet.view.IWalletView;
import bd.com.supply.util.PriceUtil;
import bd.com.supply.module.wallet.model.domain.ChainListListResp;
import bd.com.walletdb.action.WalletAction;
import bd.com.walletdb.entity.ChainEntity;
import bd.com.walletdb.entity.Price;
import bd.com.walletdb.entity.TokenEntity;
import bd.com.walletdb.entity.WalletEntity;
import bd.com.walletdb.greendao.WalletEntityDao;
import bd.com.walletdb.manager.ChainManager;
import bd.com.walletdb.manager.PriceManager;
import bd.com.walletdb.manager.TokenManager;
import bd.com.walletdb.manager.WalletDBManager;
import io.reactivex.Observable;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.functions.Consumer;
import io.reactivex.schedulers.Schedulers;


public class WalletPresenter extends BasePresenterImpl<IWalletView> {
    private static final String TAG = WalletPresenter.class.getSimpleName();

    /**
     * 同步token
     */
    public void startAsyncToken() {
        final String walletAddress = AppSettings.getAppSettings().getCurrentAddress();
        String chainId = AppSettings.getAppSettings().getCurrentChainId();
        TokenAsync tokenAsync = new TokenAsync(walletAddress, chainId);
        AsyncHelper.asyncTokenAndPrice(tokenAsync, new PriceAsync.AsyncPriceListener() {
            @Override
            public void onFinished(List<Price> priceList) {
                startGetCoinList();
            }
        });
    }

    private void startGetCoinList() {
        RxTaskScheduler.postIoMainTask(new RxTaskCallBack<List<TokenEntity>>() {
            @Override
            public void onFailed(LogicException e) {
                super.onFailed(e);
                e.printStackTrace();
                if (mView != null) {
                    mView.loadFailed(e.getMessage());
                }
            }

            @Override
            public void onSuccess(List<TokenEntity> data) {
                super.onSuccess(data);
                String totalMoney = getTotalMoney(data);
                if (mView != null) {
                    mView.loadSuccess(data);
                    mView.setTotalMoney(totalMoney);
                }
            }

            @Override
            public List<TokenEntity> doWork() throws Exception {
                List<TokenEntity> data = CoinModel.getWalletModel().getCoinList();
                getListBalance(data);
                TokenManager.getManager().insertTokenList(data);
                return data;
            }
        });
    }


    /**
     * 得到当前钱包信息
     */
    public void getCurrentWallet() {
        RxTaskScheduler.postIoMainTask(new RxTaskCallBack<WalletEntity>() {
            @Override
            public void onSuccess(WalletEntity walletEntity) {
                super.onSuccess(walletEntity);
                if (walletEntity != null) {
                    if (mView != null) {
                        mView.setWalletInfo(walletEntity);
                    }
                } else {
                    Log.e("getCurrentWallet", "数据库没查到数据");
                }
            }

            @Override
            public WalletEntity doWork() {
                WalletAction action = new WalletAction();
                String currentAddress = AppSettings.getAppSettings().getCurrentAddress();
                Log.e(TAG, "address=" + currentAddress);
                List<WalletEntity> walletEntities = action.eq(WalletEntityDao.Properties.Address, currentAddress).queryAnd();
                if (walletEntities != null && walletEntities.size() > 0) {
                    return walletEntities.get(0);
                }
                return null;
            }
        });
    }

    /**
     * 每个币的列表余额和价值
     */
    private void getListBalance(List<TokenEntity> coinBeans) {
        for (TokenEntity coinBean : coinBeans) {
            getBalance(coinBean);
        }
    }

    /**
     * 计算所有币的总价值
     *
     * @param coinBeans
     * @return
     */
    private String getTotalMoney(List<TokenEntity> coinBeans) {
        String totalMoney = "0.00";
        Price price = PriceManager.getManager().getFirstPrice();
        if (price != null) {
            BigDecimal totalValue = new BigDecimal("0");
            for (TokenEntity coinBean : coinBeans) {
                BigDecimal coinMoeny = PriceUtil.getMoney(coinBean.getAddress(), coinBean.getBalance());
                coinBean.setValue(coinMoeny.setScale(2, BigDecimal.ROUND_HALF_UP).toPlainString());
                totalValue = totalValue.add(coinMoeny);
            }
            BigDecimal totalValueScale = totalValue.setScale(2, BigDecimal.ROUND_HALF_UP);
            totalMoney = totalValueScale.toPlainString() + " " + price.getUnit();

        }
        return totalMoney;
    }

    /**
     * 获取某个币的余额
     *
     * @param coinBean
     */
    private void getBalance(TokenEntity coinBean) {
        String address = AppSettings.getAppSettings().getCurrentAddress();
        String chainId = AppSettings.getAppSettings().getCurrentChainId();
        //获取balance
        String balance;
        if (GlobConfig.isMianTokenTransaction(coinBean.getAddress())) {
            balance = CoinModel.getWalletModel().getBalance(ApiConfig.getWeb3jUrlProxy(), address);

        } else {
            balance = CoinModel.getWalletModel().getERC20Balance(coinBean.getAddress());
        }
        if (!TextUtils.isEmpty(balance)) {
            coinBean.setBalance(balance + "");
        }
        if (TextUtils.isEmpty(coinBean.getBalance())) {
            coinBean.setBalance("0");
        }


        //获取价格
        Price price = PriceManager.getManager().getPrice(coinBean.getAddress(), chainId);
        if (price != null) {
            coinBean.setValue(price.getAsks());
            if (!TextUtils.isEmpty(price.getAsks())) {
                BigDecimal priceDecimal = new BigDecimal(price.getAsks());
                BigDecimal smartPrice = priceDecimal.setScale(3, BigDecimal.ROUND_FLOOR);
                coinBean.setPrice(smartPrice.toPlainString() + " " + price.getUnit());
            }
            return;
        }
//        if (TextUtils.equals(FakeDataHelper.SCT_SYMBOL, coinBean.getSymbol())) {
//
//            Price price = PriceManager.getManager().getPriceByMarketName(Constant.SCT_MARKET);
//            if (price != null) {
//                coinBean.setValue(price.getAsks());
//                coinBean.setPrice(price.getAsks() + " " + price.getUnit());
//                return;
//            }
//        } else if (TextUtils.equals(FakeDataHelper.MAIN_ETH_SYMBOL, coinBean.getSymbol())) {
//            Price price = PriceManager.getManager().getPriceByMarketName(Constant.ETH_MARKET);
//            if (price != null) {
//                coinBean.setPrice(price.getAsks() + " " + price.getUnit());
//                return;
//            }
//        }
        coinBean.setValue("0.00");
        coinBean.setPrice("0.00");

    }


    public void getChainList() {
        Map<String, Object> params = new HashMap<>();
        params.put("pageNumber", 1);
        params.put("pageSize", 100);
        // mView.showLoadingDialog();
        TokenModel.getModel().getChainList(params, new ModelCallBack<ChainListListResp>() {
            @Override
            public void onResponseFailed(int errorCode, String msg) {
                if (mView != null) {
                    mView.hideLoadingDialog();
                    mView.onGetChainListFailed(msg);
                }
            }

            @Override
            public void onResponseSuccess(ChainListListResp data) {
                if (mView != null) {
                    mView.hideLoadingDialog();
                    List<ChainEntity> remoteDatas = data.getList();
                    if (remoteDatas != null && remoteDatas.size() > 0) {
                        ChainManager.getManager().inputChainList(remoteDatas);
                    }
                    mView.onGetChainListSuccess(remoteDatas);
                }
            }
        });
    }

    public void checkWalletBacked() {
        boolean ignoreNotice = AppSettings.getAppSettings().getIgnoreBackUpNotice();
        if (ignoreNotice) return;
        RxTaskScheduler.postIoMainTask(new RxTaskCallBack<WalletEntity>() {
            @Override
            public void onSuccess(WalletEntity entity) {
                super.onSuccess(entity);
                if (mView != null && entity != null) {
                    mView.isCurrentWalletBacked(entity);
                }
            }

            @Override
            public WalletEntity doWork() throws Exception {
                String address = AppSettings.getAppSettings().getCurrentAddress();
                WalletEntity entity = WalletDBManager.getManager().getWalletEntity(address);
                if (entity != null) {
                    return entity;
                }
                return null;
            }
        });


    }

    public void validAccount(final String pwd, final WalletEntity entity, final int type) {
        Observable.timer(400, TimeUnit.MILLISECONDS).subscribeOn(Schedulers.computation()).observeOn(AndroidSchedulers.mainThread()).subscribe(new Consumer<Long>() {
            @Override
            public void accept(Long aLong) throws Exception {
                if (mView != null) {
                    if (TextUtils.equals(pwd, entity.getPassword())) {
                        mView.onPassWordRight(type);
                    } else {
                        mView.onPassWordFailed();
                    }
                }

            }
        });
    }

    public List<FunItemBean> getFunctions(){
        List<FunItemBean> funItemBeanList=new ArrayList<>();
        //1，扫一扫
        FunItemBean scanFun=new FunItemBean();
        scanFun.setName("扫一扫");
        scanFun.setType(FunItemBean.OPEN_SCAN);
        funItemBeanList.add(scanFun);
        //2，收付款
        FunItemBean moneyFun=new FunItemBean();
        moneyFun.setName("收款");
        moneyFun.setType(FunItemBean.MOENY);
        funItemBeanList.add(moneyFun);
        //3，创建钱包
        FunItemBean createFun=new FunItemBean();
        createFun.setName("创建钱包");
        createFun.setType(FunItemBean.CREATE_WALLET);
        funItemBeanList.add(createFun);
        return funItemBeanList;
    }
}
