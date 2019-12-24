package bd.com.supply.module.transaction.presenter;

import android.text.TextUtils;
import android.util.Log;

import com.tencent.bugly.crashreport.BuglyLog;

import org.web3j.protocol.core.methods.response.TransactionReceipt;
import org.web3j.utils.Convert;

import java.math.BigDecimal;
import java.math.BigInteger;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import bd.com.appcore.base.ModelCallBack;
import bd.com.appcore.mvp.BasePresenterImpl;
import bd.com.appcore.mvp.IBaseView;
import bd.com.appcore.rx.LogicException;
import bd.com.appcore.rx.RxTaskCallBack;
import bd.com.appcore.rx.RxTaskScheduler;
import bd.com.appcore.util.AppSettings;
import bd.com.appcore.util.GsonUtil;
import bd.com.supply.app.ThreadManager;
import bd.com.supply.module.transaction.SoSoConfig;
import bd.com.supply.module.transaction.model.SosoModel;
import bd.com.supply.module.transaction.model.domian.Batch;
import bd.com.supply.module.transaction.model.domian.BatchListResp;
import bd.com.supply.module.transaction.model.domian.ProductBean;
import bd.com.supply.module.transaction.model.domian.ProductListResp;
import bd.com.supply.module.transaction.model.domian.ProductOri;
import bd.com.supply.module.transaction.model.req.ReportRecordReq;
import bd.com.supply.module.transaction.model.resp.ReportRecordResp;
import bd.com.supply.util.UuidUtil;
import bd.com.supply.web3.Web3Proxy;
import bd.com.supply.web3.contract.Authorized;
import bd.com.supply.web3.contract.BigPackingBox;
import bd.com.supply.web3.contract.NulsStandardToken;
import bd.com.supply.web3.contract.PackingBox;
import bd.com.supply.web3.contract.Product;
import io.reactivex.disposables.Disposable;

public class TracePresenter extends BasePresenterImpl<TracePresenter.View> {
    private static final String PROD_SOSO_TYPE = "1";
    private static final String BOX_SOSO_TYPE = "2";
    private static final String BIG_BOX_SOSO_TYPE = "3";
    private static final String CHARGIN_SOSO_TYPE = "4";

    /**
     * 取余额
     *
     * @param
     */
    public void getBalance(final String tokenAddr) {
        BuglyLog.i(TAG, "getBalance===>  tokenAddr" + tokenAddr);
        RxTaskScheduler.postIoMainTask(new RxTaskCallBack<String>() {
            @Override
            public void onSuccess(String s) {
                super.onSuccess(s);
                if (mView != null) {
                    if (!TextUtils.isEmpty(s)) {
                        mView.onGetBalanceSuccess(s);
                    } else {
                        mView.onGetBalanceFailed("0");
                    }
                }
            }

            @Override
            public String doWork() throws Exception {
                //return getTokenBalanceWithName(SoSoConfig.SOSO_TOKEN_ADDRESS);
                return getTokenBalanceWithName(tokenAddr);
            }
        });

    }

    private String getTokenBalanceWithName(String tokenAddr) throws Exception {
        NulsStandardToken token = NulsStandardToken.load(tokenAddr, Web3Proxy.getWeb3Proxy().getWeb3j(), Web3Proxy.getCredentials(), Web3Proxy.GAS_PRICE, Web3Proxy.DEPLOY_GAS_LIMIT);
        BigInteger balance = token.balanceOf(Web3Proxy.getCredentials().getAddress()).send();
        String name = token.name().send();
        if (balance.intValue() != 0) {
            BigDecimal valueBD = Convert.fromWei(balance.toString(), Convert.Unit.ETHER);

            return valueBD.toString() + " " + name;
        } else {
            return "0.00 " + name;
        }
    }


    /**
     * 提币
     * new : http://soso.be.top/mnp/{产品合约地址}/{key}
     * old:
     * {产品合约地址}@{Key}@{代币合约地址}
     *
     * @param
     */
    public void chargeCoin(final String consumer) {
        BuglyLog.i(TAG, "chargeCoin  consumer=" + consumer);

        RxTaskScheduler.postIoMainTask(new RxTaskCallBack<Integer>() {
            @Override
            public void onStart(Disposable disposable) {
                super.onStart(disposable);
                addDispose(disposable);
                mView.showLoadingDialog();
            }

            @Override
            public void onFailed(LogicException e) {
                mView.hideLoadingDialog();
                mView.onGetBalanceFailed("消费失败");
                super.onFailed(e);
            }

            @Override
            public void onSuccess(Integer type) {
                super.onSuccess(type);
                if (mView != null) {
                    mView.hideLoadingDialog();
                    mView.hideSosoDialog();
                    switch (type) {
                        case -1:
                            mView.onChargeFailed("二维码格式不正确");
                            break;
                        case 0:
                            mView.onChargeSuccess();
                            break;
                        case 1:
                            mView.onChargeFailed("该消费码已被消费，无法再次消费");
                            break;
                    }

                }
            }

            @Override
            public Integer doWork() throws Exception {
                if (TextUtils.isEmpty(consumer)) {
                    return -1;
                } else if (!consumer.contains(SoSoConfig.ADDR_GAP) && !consumer.contains("0x")) {
                    return -2;
                } else if (consumer.contains(SoSoConfig.ADDR_GAP)) {
                    String[] datas = consumer.split(SoSoConfig.ADDR_GAP);//productaddress@key@tokenaddress
//                if (!datas[0].startsWith("0x") || !datas[1].startsWith("0x")) {
//                    return -1;
//                }
                    Product product = Product.load(datas[0], Web3Proxy.getWeb3Proxy().getWeb3j(), Web3Proxy.getWeb3Proxy().getPoolTransactionManager(), Web3Proxy.GAS_PRICE, Web3Proxy.PACK_SOSO_GAS_LIMIT);
                    boolean hasDone = product.isDone().send();
                    if (hasDone) {
                        return 1;
                    }
                    TransactionReceipt receipt = product.withdrawERC20Token(datas[1]).send();
                    String hash = receipt.getBlockHash();
                    getBalance(datas[2]);
                    reportRecord(CHARGIN_SOSO_TYPE, datas[0], "0");
                    AppSettings.getAppSettings().setLastSosoTokenAddr(datas[2]);
                    BuglyLog.i(TAG, "chargeCoin====>hash=" + hash);

                } else {
                    /**
                     * new : http://soso.be.top/mnp/{产品合约地址}/{key}
                     * old:
                     * {产品合约地址}@{Key}@{代币合约地址}
                     */
                    if (consumer.startsWith("http") || consumer.startsWith("https")) {
                        try {
                            String[] datas = consumer.split("/");
                            String prodAddr = datas[datas.length - 2];
                            Product product = Product.load(prodAddr, Web3Proxy.getWeb3Proxy().getWeb3j(), Web3Proxy.getWeb3Proxy().getPoolTransactionManager(), Web3Proxy.GAS_PRICE, Web3Proxy.PACK_SOSO_GAS_LIMIT);
                            boolean hasDone = product.isDone().send();
                            if (hasDone) {
                                return 1;
                            }
                            String key = datas[datas.length - 1];
                            TransactionReceipt receipt = product.withdrawERC20Token(key).send();
                            String hash = receipt.getBlockHash();
                            String tokenAddr = product.tokenIdentify().send();
                            getBalance(tokenAddr);
                            reportRecord(CHARGIN_SOSO_TYPE, prodAddr, "0");
                            AppSettings.getAppSettings().setLastSosoTokenAddr(tokenAddr);
                            BuglyLog.i(TAG, "chargeCoin====>hash=" + hash);
                        } catch (Exception e) {
                            e.printStackTrace();
                            return -5;
                        }

                    } else {
                        return -3;
                    }

                }


                return 0;
            }
        });

    }

    public void getProductList(Map<String, Object> params) {
        params.put("pageNumber", 1);
        params.put("pageSize", 20);
        params.put("address", AppSettings.getAppSettings().getCurrentAddress());
        SosoModel.getInstance().getProductList(params, new ModelCallBack<ProductListResp>() {
            @Override
            public void onResponseFailed(int errorCode, String msg) {
                BuglyLog.e("TracePresenter", "getProductList  error ==>msg=" + msg);
            }

            @Override
            public void onResponseSuccess(ProductListResp data) {
                if (mView != null && data.getList() != null) {
                    List<ProductOri> productOriList = data.getList();
                    mView.productListCount(productOriList.size());
                }
            }
        });
    }

    public void getBatchList(Map<String, Object> params) {
        String address = AppSettings.getAppSettings().getCurrentAddress();
        params.put("pageNumber", 1);
        params.put("pageSize", 20);
        params.put("address", address);
        SosoModel.getInstance().getBatchList(params, new ModelCallBack<BatchListResp>() {
            @Override
            public void onResponseFailed(int errorCode, String msg) {
                BuglyLog.e("TracePresenter", "getBatchList  error ==>msg=" + msg);
            }

            @Override
            public void onResponseSuccess(BatchListResp data) {
                if (mView != null && data.getList() != null) {
                    List<Batch> productOriList = data.getList();
                    mView.categoryListCount(productOriList.size());
                }
            }
        });
    }

    public void autoSoso(String result, int type) {
        if (type == 6) {
            sosoPackge(result);
        } else if (type == 10) {
            sosoBigBox(result);
        } else {
            sosoProduct(result);
        }
    }

    private void sosoBigBox(final String addr) {
        mView.showSosoDialog("正在追加溯源用时约0-1分钟");
        ThreadManager.getInstance().postLogicTask(new Runnable() {
            @Override
            public void run() {
                BigPackingBox bixBox = BigPackingBox.load(addr, Web3Proxy.getWeb3Proxy().getWeb3j(), Web3Proxy.getWeb3Proxy().getPoolTransactionManager(), Web3Proxy.GAS_PRICE, Web3Proxy.PACK_SOSO_GAS_LIMIT);
                addProductInfo2BigBox(bixBox);
            }
        });
    }

    private void sosoPackge(final String addr) {
        mView.showSosoDialog("正在追加溯源用时约0-1分钟");
        ThreadManager.getInstance().postLogicTask(new Runnable() {
            @Override
            public void run() {
                long startTime = System.currentTimeMillis();
                PackingBox packingBox = PackingBox.load(addr, Web3Proxy.getWeb3Proxy().getWeb3j(), Web3Proxy.getWeb3Proxy().getPoolTransactionManager(), Web3Proxy.GAS_PRICE, Web3Proxy.PACK_SOSO_GAS_LIMIT);
                long endTime = System.currentTimeMillis();
                Log.i("ttttt", "盒子合约加载耗时:" + (endTime - startTime));
                addProductInfo2Pack(packingBox);
            }
        });
    }

    private void sosoProduct(String result) {
        final String addr = result.substring(2, result.length());
        mView.showSosoDialog("正在追加溯源用时约0-1分钟");
        ThreadManager.getInstance().postLogicTask(new Runnable() {
            @Override
            public void run() {
                try {
                    Product product = Product.load(addr, Web3Proxy.getWeb3Proxy().getWeb3j(), Web3Proxy.getWeb3Proxy().getPoolTransactionManager(), Web3Proxy.GAS_PRICE, Web3Proxy.PACK_SOSO_GAS_LIMIT);
                    addProductInfo(product);
                } catch (Exception e) {
                    hideSosoDialogOnUIThread();
                }

            }
        });
    }

    private void hideSosoDialogOnUIThread() {
        ThreadManager.getInstance().postUITask(new Runnable() {
            @Override
            public void run() {
                if (mView != null) {
                    mView.hideSosoDialog();
                }
            }
        });
    }

    private void addProductInfo(final Product product) {
        try {
            if (product.isDone().send()) {
                ThreadManager.getInstance().postUITask(new Runnable() {
                    @Override
                    public void run() {
                        if (mView != null) {
                            mView.onSosoFailed("当前产品已被消费，无法自动溯源");
                            mView.hideSosoDialog();
                        }
                    }
                });
                return;
            }
            String walletAddr = AppSettings.getAppSettings().getCurrentAddress();
            String authAddr = product.authorizedIdentify().send();
            Authorized authorized = Authorized.load(authAddr, Web3Proxy.getWeb3Proxy().getWeb3j(), Web3Proxy.getCredentials(), Web3Proxy.GAS_PRICE, Web3Proxy.PACK_SOSO_GAS_LIMIT);

            String warrntInfo = authorized.warrantInfo().send();
            if (TextUtils.isEmpty(warrntInfo)) {
                ThreadManager.getInstance().postFrontUITask(new Runnable() {
                    @Override
                    public void run() {
                        if (mView != null) {
                            mView.onSosoFailed("您暂无权限进行操作");
                            mView.hideSosoDialog();
                        }
                    }
                });
                return;
            }
            String uuid = UuidUtil.getRandomUuid();
            final ProductBean productBean = WarrantInfoParser.getProductBean(warrntInfo, uuid);
            String warrantInfoJson = GsonUtil.objectToJson(productBean, ProductBean.class);
            Log.i("addProductInfo", "warrantInfoJson=" + warrantInfoJson);
            TransactionReceipt receipt = product.appendEntry(new BigInteger(productBean.getPerio()), warrantInfoJson).send();
            String hash = receipt.getTransactionHash();
            Log.i("addProductInfo", "hash=" + hash);
            List<org.web3j.protocol.core.methods.response.Log> logs = receipt.getLogs();
            if (TextUtils.isEmpty(hash) || hash.startsWith("0x000") || logs == null || logs.size() == 0) {
                ThreadManager.getInstance().postFrontUITask(new Runnable() {
                    @Override
                    public void run() {
                        if (mView != null) {
                            mView.onSosoFailed("自动追加溯源信息失败");
                            mView.hideSosoDialog();
                        }
                    }
                });
                return;
            }

            ThreadManager.getInstance().postFrontUITask(new Runnable() {
                @Override
                public void run() {
                    reportRecord(PROD_SOSO_TYPE, product.getContractAddress(), productBean.getPerio());
                    if (mView != null) {
                        mView.onSosoSuccess();
                        mView.hideSosoDialog();
                    }
                }
            });
            bindData(uuid, hash);
        } catch (Exception e) {
            ThreadManager.getInstance().postFrontUITask(new Runnable() {
                @Override
                public void run() {
                    if (mView != null) {
                        mView.onSosoFailed("自动追加溯源信息失败");
                        mView.hideSosoDialog();
                    }
                }
            });
            e.printStackTrace();
        }
    }


    private void addProductInfo2Pack(final PackingBox packingBox) {
        try {
            long startTime = System.currentTimeMillis();
            String walletAddr = AppSettings.getAppSettings().getCurrentAddress();
            String authAddr = packingBox.authIdentify().send();
            Authorized authorized = Authorized.load(authAddr, Web3Proxy.getWeb3Proxy().getWeb3j(), Web3Proxy.getWeb3Proxy().getPoolTransactionManager(), Web3Proxy.GAS_PRICE, Web3Proxy.PACK_SOSO_GAS_LIMIT);
            String warrntInfo = authorized.warrantInfo().send();
            long endTime = System.currentTimeMillis();
            Log.i("ttttt", "鉴权合约加载以及调用warrantInfo耗时:" + (endTime - startTime));
            if (TextUtils.isEmpty(warrntInfo)) {
                ThreadManager.getInstance().postFrontUITask(new Runnable() {
                    @Override
                    public void run() {
                        if (mView != null) {
                            mView.onSosoFailed("您暂无权限进行操作");
                            mView.hideSosoDialog();
                        }
                    }
                });
                return;
            }
            String uuid = UuidUtil.getRandomUuid();
            final ProductBean productBean = WarrantInfoParser.getProductBean(warrntInfo, uuid);
            String warrantInfoJson = GsonUtil.objectToJson(productBean, ProductBean.class);
            Log.i("addProductInfo", "warrantInfoJson=" + warrantInfoJson);
            long sTime = System.currentTimeMillis();
            TransactionReceipt receipt = packingBox.appendEntry(new BigInteger(productBean.getPerio()), warrantInfoJson).send();
            long receiptTime = System.currentTimeMillis();
            Log.i("ttttt", "appendEntry:" + (receiptTime - sTime));
            String hash = receipt.getTransactionHash();

            Log.i("addProductInfo", "hash=" + hash);
            List<org.web3j.protocol.core.methods.response.Log> logs = receipt.getLogs();
            long eTime = System.currentTimeMillis();
            Log.i("ttttt", "appendEntry+logs耗时:" + (eTime - sTime));
            if (TextUtils.isEmpty(hash) || hash.startsWith("0x000") || logs == null || logs.size() == 0) {
                ThreadManager.getInstance().postFrontUITask(new Runnable() {
                    @Override
                    public void run() {
                        if (mView != null) {
                            mView.onSosoFailed("自动追加溯源信息失败");
                            mView.hideSosoDialog();
                        }
                    }
                });
                return;
            }

            ThreadManager.getInstance().postFrontUITask(new Runnable() {
                @Override
                public void run() {
                    if (mView != null) {
                        reportRecord(BOX_SOSO_TYPE, packingBox.getContractAddress(), productBean.getPerio());
                        mView.onSosoSuccess();
                        mView.hideSosoDialog();
                    }
                }
            });
            bindData(uuid, hash);
        } catch (Exception e) {
            ThreadManager.getInstance().postFrontUITask(new Runnable() {
                @Override
                public void run() {
                    if (mView != null) {
                        mView.onSosoFailed("自动追加溯源信息失败");
                        mView.hideSosoDialog();
                    }
                }
            });
            e.printStackTrace();
        }
    }

    private void addProductInfo2BigBox(final BigPackingBox packingBox) {
        try {
            String authAddr = packingBox.authIdentify().send();
            Authorized authorized = Authorized.load(authAddr, Web3Proxy.getWeb3Proxy().getWeb3j(), Web3Proxy.getWeb3Proxy().getPoolTransactionManager(), Web3Proxy.GAS_PRICE, Web3Proxy.PACK_SOSO_GAS_LIMIT);

            String warrntInfo = authorized.warrantInfo().send();
            if (TextUtils.isEmpty(warrntInfo)) {
                ThreadManager.getInstance().postFrontUITask(new Runnable() {
                    @Override
                    public void run() {
                        if (mView != null) {
                            mView.onSosoFailed("您暂无权限进行操作");
                            mView.hideSosoDialog();
                        }
                    }
                });
                return;
            }
            String uuid = UuidUtil.getRandomUuid();
            final ProductBean productBean = WarrantInfoParser.getProductBean(warrntInfo, uuid);
            String warrantInfoJson = GsonUtil.objectToJson(productBean, ProductBean.class);
            TransactionReceipt receipt = packingBox.appendEntry(new BigInteger(productBean.getPerio()), warrantInfoJson).send();
            String hash = receipt.getTransactionHash();

            List<org.web3j.protocol.core.methods.response.Log> logs = receipt.getLogs();
            if (TextUtils.isEmpty(hash) || hash.startsWith("0x000") || logs == null || logs.size() == 0) {
                ThreadManager.getInstance().postFrontUITask(new Runnable() {
                    @Override
                    public void run() {
                        if (mView != null) {
                            mView.onSosoFailed("自动追加溯源信息失败");
                            mView.hideSosoDialog();
                        }
                    }
                });
                return;
            }

            ThreadManager.getInstance().postFrontUITask(new Runnable() {
                @Override
                public void run() {
                    if (mView != null) {
                        reportRecord(BIG_BOX_SOSO_TYPE, packingBox.getContractAddress(), productBean.getPerio());
                        mView.onSosoSuccess();
                        mView.hideSosoDialog();
                    }
                }
            });
            bindData(uuid, hash);
        } catch (Exception e) {
            ThreadManager.getInstance().postFrontUITask(new Runnable() {
                @Override
                public void run() {
                    if (mView != null) {
                        mView.onSosoFailed("自动追加溯源信息失败");
                        mView.hideSosoDialog();
                    }
                }
            });
            e.printStackTrace();
        }
    }


    private void bindData(String uuid, String hash) {
        String wAddr = AppSettings.getAppSettings().getCurrentAddress();
        Map<String, Object> params = new HashMap<>();
        params.put("uuid", uuid);
        params.put("txhash", hash);
        params.put("address", wAddr);
        SosoModel.getInstance().bindData(params, new ModelCallBack<String>() {
            @Override
            public void onResponseFailed(int errorCode, final String msg) {
                ThreadManager.getInstance().postFrontUITask(new Runnable() {
                    @Override
                    public void run() {
                        if (mView != null) {
                            mView.onBindResutl(-1);
                        }
                    }
                });
            }

            @Override
            public void onResponseSuccess(String data) {
                ThreadManager.getInstance().postFrontUITask(new Runnable() {
                    @Override
                    public void run() {
                        mView.onBindResutl(0);
                    }
                });
            }
        });
    }

    private void reportRecord(String type, String contractAddr, String period) {
        if (!TextUtils.isEmpty(contractAddr) && !contractAddr.startsWith("0x")) {
            contractAddr="0x"+contractAddr;
        }
        ReportRecordReq req = new ReportRecordReq();
        req.setAddress(AppSettings.getAppSettings().getCurrentAddress());
        req.setContractAddr(contractAddr);
        req.setPeriod(period);
        req.setType(type);
        SosoModel.getInstance().reportTraceRecord(req, new ModelCallBack<ReportRecordResp>() {
            @Override
            public void onResponseFailed(int errorCode, String msg) {
                Log.e(TAG, "reportRecord====>onResponseFailed" + msg);
            }

            @Override
            public void onResponseSuccess(ReportRecordResp data) {
                if (mView != null) {
                    Log.e(TAG, "reportRecord====>onResponseSuccess");
                    mView.onReportRecordSuccess("上报成功");
                }

            }
        });
    }

    public interface View extends IBaseView {
        void onGetBalanceSuccess(String balance);

        void onReportRecordSuccess(String balance);

        void onGetBalanceFailed(String msg);

        void onChargeSuccess();

        void onChargeFailed(String msg);

        void productListCount(int count);

        void categoryListCount(int count);

        void onSosoSuccess();

        void onSosoFailed(String msg);

        void onBindResutl(int type);

        void showSosoDialog(String text);

        void hideSosoDialog();
    }
}
