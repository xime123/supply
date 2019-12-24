package bd.com.supply.module.transaction.presenter;

import android.text.TextUtils;
import android.util.Log;

import com.tencent.bugly.crashreport.CrashReport;

import java.util.List;
import java.util.Map;
import java.util.Timer;
import java.util.TimerTask;
import java.util.concurrent.TimeUnit;

import bd.com.appcore.base.ModelCallBack;
import bd.com.appcore.mvp.BasePresenterImpl;
import bd.com.appcore.network.RequestParam;
import bd.com.appcore.rx.LogicException;
import bd.com.appcore.rx.RxTaskCallBack;
import bd.com.appcore.rx.RxTaskScheduler;
import bd.com.appcore.util.AppSettings;
import bd.com.supply.module.common.GlobConfig;
import bd.com.supply.module.transaction.model.TransactionModel;
import bd.com.supply.module.transaction.model.domian.TxHistoryListResp;
import bd.com.supply.module.transaction.view.ITransactionHisActView;
import bd.com.supply.module.wallet.ApiConfig;
import bd.com.supply.module.wallet.model.CoinModel;
import bd.com.supply.web3.Web3Proxy;
import bd.com.walletdb.entity.TransactionDetail;
import bd.com.walletdb.entity.TxHistory;
import bd.com.walletdb.manager.TxDetailManager;
import bd.com.walletdb.manager.TxHistoryDBManager;
import io.reactivex.Observable;
import io.reactivex.disposables.Disposable;
import io.reactivex.functions.Consumer;


public class NewTransactionHisActPresenter extends BasePresenterImpl<ITransactionHisActView> {
    private int count = 0;
    private Timer timer = new Timer(true);
    private TimerTask task;

    public void getNonceAndGetTxList(final Map<String, Object> params, final String tokenAddress) {
        RxTaskScheduler.postIoMainTask(new RxTaskCallBack<Integer>() {
            @Override
            public void onFailed(LogicException e) {
                super.onFailed(e);
                Log.e("getNonceAndGetTxList", "get nonce error " + e.getErrorMsg());
                if (mView != null) {
                    mView.loadFailed(e.getErrorMsg());
                }
                //getTransactionList(params, tokenAddress);
            }

            @Override
            public Integer doWork() throws Exception {
                String address = AppSettings.getAppSettings().getCurrentAddress();
                int nonce = TransactionModel.getTransactionModel().getNonce(address);
                params.put("nonce", nonce);
                getTransactionList(params, tokenAddress);
                return nonce;
            }
        });

    }

    private void getTransactionList(final Map<String, Object> params, final String tokenAddress) {
        count = 0;
        //当前钱包地址
        final String address = AppSettings.getAppSettings().getCurrentAddress();
        final String chainId = AppSettings.getAppSettings().getCurrentChainId();
        params.put(RequestParam.ADDRESS, address);
        if (GlobConfig.isMianTokenTransaction(tokenAddress)) {//主币交易
            params.put(RequestParam.CONTRACT_ADDRESS, "");
        } else {
            params.put(RequestParam.CONTRACT_ADDRESS, tokenAddress);
        }
        TransactionModel.getTransactionModel().getTransactionList(params, new ModelCallBack<TxHistoryListResp>() {
            @Override
            public void onResponseFailed(int errorCode, String msg) {
                if (mView != null) {
                    int pageNum = (int) params.get("pageNumber");
                    if (pageNum == 1) {
                        List<TxHistory> txHistoryList = TxHistoryDBManager.getManager().findTxHistory(tokenAddress, AppSettings.getAppSettings().getCurrentAddress(), chainId);
                        if (txHistoryList != null && txHistoryList.size() > 0) {
                            mView.loadSuccess(txHistoryList);
                        } else {
                            mView.loadFailed(msg);
                        }
                    } else {
                        mView.loadMoreFailed();
                    }
                }
            }

            @Override
            public void onResponseSuccess(TxHistoryListResp data) {
                if (mView != null) {
                    List<TxHistory> remoteDatas = data.getList();
                    for (TxHistory txHistory : remoteDatas) {
                        txHistory.setAddress(tokenAddress);
                        txHistory.setWalletAddr(GlobConfig.getCurrentWalletAddress());
                        txHistory.setChainId(AppSettings.getAppSettings().getCurrentChainId());
                    }
                    if (data.getPageNumber() == 1) {
                        //看看有没有本地记录
                        List<TxHistory> txHistories = TxHistoryDBManager.getManager().findPendingTxHistory(tokenAddress, address, chainId);
                        if (txHistories != null && txHistories.size() > 0) {
                            //每个都要去查一下状态
                            for (TxHistory txHistory : txHistories) {
                                if (!remoteDatas.contains(txHistory)) {//pengding状态的
                                    Log.e(TAG, "======================>  来来一个pengding  txHistory.getBlockNumber()......." + txHistory.getBlockNumber());
                                    remoteDatas.add(0, txHistory);
                                    if (txHistory.getState() == 2) {
                                        startGetDetail(txHistory);
                                    }
                                    if (txHistory.getState() == 1) {
                                        getBlockNum();
                                    }
                                } else {
                                    int index = remoteDatas.indexOf(txHistory);
                                    TxHistory remoteTx = remoteDatas.get(index);
                                    if (txHistory.getState() == 2 || txHistory.getState() == 1) {//本地是pengding状态或者确认中状态,说明状态已经变了，要根据远程的状态修改
                                        int progress = remoteTx.getLastBlockNumber() - remoteTx.getBlockNumber();
                                        if (GlobConfig.isMianTokenTransaction(tokenAddress) && progress < 11) {//确认中状态
                                            getBlockNum();
                                            remoteTx.setState(1);
                                        } else {
                                            remoteTx.setState(0);//这一步不要也可以，远程的默认就是0
                                        }
                                    }
                                    TxHistoryDBManager.getManager().deleteTxHistoryByHash(txHistory.getPkHash());
                                    Log.e(TAG, "======================>  有重复的  state=" + txHistory.getState());
                                }
                            }
                        }
                        mView.loadSuccess(remoteDatas);
                    } else {
                        mView.loadMoreSuccess(remoteDatas);
                    }

                    TxHistoryDBManager.getManager().insertTxHistoryListAsync(remoteDatas);
                }
            }
        });
    }

    /**
     * 取余额
     *
     * @param contractAddress
     */
    public void getBalance(final String contractAddress) {
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
                if (GlobConfig.isMianTokenTransaction(contractAddress)) {
                    return CoinModel.getWalletModel().getBalance(ApiConfig.getWeb3jUrlProxy(), AppSettings.getAppSettings().getCurrentAddress());
                } else {
                    return CoinModel.getWalletModel().getERC20Balance(contractAddress);
                }
            }
        });

    }

    /**
     * 查询pending状态
     *
     * @param history
     */
//    public void startGetPendingState(final TxHistory history, final String name) {
//        Disposable disposable = Observable.timer(3000, TimeUnit.MILLISECONDS).subscribe(new Consumer<Long>() {
//            @Override
//            public void accept(Long aLong) throws Exception {
//                getReceipt(history, name);
//            }
//        });
//
//        addDispose(disposable);
//    }

    /**
     * 查询pending状态
     *
     * @param history
     */
    public void startGetDetail(final TxHistory history) {
        if (count > 100) return;
        count++;
        Disposable disposable = Observable.timer(3000, TimeUnit.MILLISECONDS).subscribe(new Consumer<Long>() {
            @Override
            public void accept(Long aLong) throws Exception {
                try {
                    getDetail(history);
                } catch (Exception e) {
                    CrashReport.postCatchedException(e);
                }
            }
        });

        addDispose(disposable);
    }

    private boolean getBlockNumStarted = false;

    public void getBlockNum() {
        if (getBlockNumStarted) {//已经启动了就直接return
            return;
        }
        stopTask();
        timer = new Timer(true);
        task = new GetBlockNumTask();
        timer.schedule(task, 0, 3000);
        getBlockNumStarted = true;
    }

    class GetBlockNumTask extends TimerTask {

        @Override
        public void run() {
            try {
                final int lastBlockNum = Web3Proxy.getWeb3Proxy().getEth_BlockNumber();
                Log.e("getBlockNum", "Timer getLastBlockNum =" + lastBlockNum);

                if (mView != null) {
                    RxTaskScheduler.postUiTask(new RxTaskCallBack<Boolean>() {
                        @Override
                        public Boolean doWork() throws Exception {
                            mView.onBlockNumSuccess(lastBlockNum);
                            return true;
                        }
                    });
                }
            } catch (Exception e) {
                Log.e("getBlockNum", "Timer getLastBlockNum error e=" + e.getMessage());
            }
        }
    }

    public void getDetail(final TxHistory txHistory) {
        TransactionModel.getTransactionModel().getTransactionDetail(txHistory.getPkHash(), new ModelCallBack<TransactionDetail>() {
            @Override
            public void onResponseFailed(int errorCode, String msg) {
                //showToast("获取详情失败：" + msg);
                Log.e(TAG, "获取详情失败：" + msg);
                startGetDetail(txHistory);
            }

            @Override
            public void onResponseSuccess(TransactionDetail data) {
                Log.e(TAG, "获取详情成功");
                if (data != null && !TextUtils.isEmpty(data.getBlockHash())) {
                    txHistory.setBlockNumber((int) data.getBlockNumber());
                    if (GlobConfig.isMianTokenTransaction(txHistory.getAddress())) {
                        txHistory.setState(1);
                    } else {//代币直接完成
                        txHistory.setState(0);
                    }
                    TxHistoryDBManager.getManager().insertTxHistory(txHistory);
                    TxDetailManager.getInstance().inputTxDetail(data);
                    if (mView != null) {
                        mView.onGetReceiptSuccess();
                    }
                } else {
                    Log.e(TAG, "详情为空");
                }
            }
        });
    }

    @Override
    public void onDetachView() {
        Log.e("getBlockNum", "onDetachView");
        super.onDetachView();
        stopTask();
    }

    private void stopTask() {
        if (task != null) {
            task.cancel();
            task = null;
        }

        if (timer != null) {
            timer.cancel();
            timer = null;
        }
        getBlockNumStarted = false;
    }
}
