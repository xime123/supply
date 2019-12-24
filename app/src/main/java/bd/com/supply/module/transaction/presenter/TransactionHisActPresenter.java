package bd.com.supply.module.transaction.presenter;

import android.text.TextUtils;
import android.util.Log;

import org.web3j.protocol.core.methods.response.TransactionReceipt;

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
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.Disposable;
import io.reactivex.functions.Consumer;
import io.reactivex.schedulers.Schedulers;


public class TransactionHisActPresenter extends BasePresenterImpl<ITransactionHisActView> {
    private int count = 0;
    private Timer timer = new Timer(true);
    private TimerTask task;

    public void getNonceAndGetTxList(final Map<String, Object> params, final String tokenAddress) {
        RxTaskScheduler.postIoMainTask(new RxTaskCallBack<Integer>() {
            @Override
            public void onFailed(LogicException e) {
                super.onFailed(e);
                Log.e("getNonceAndGetTxList", "get nonce error " + e.getErrorMsg());
                getTransactionList(params, tokenAddress);
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
                                    startGetPendingState(txHistory, txHistory.getAddress());
                                } else {
                                    int index = remoteDatas.indexOf(txHistory);
                                    TxHistory remoteTx = remoteDatas.get(index);
                                    if (txHistory.getState() != 2) {
                                        remoteDatas.get(index).setState(txHistory.getState());
                                    } else {//
                                        int progress = remoteTx.getLastBlockNumber() - remoteTx.getBlockNumber();
                                        if (GlobConfig.isMianTokenTransaction(tokenAddress) && progress < 11) {
                                            remoteTx.setState(1);
                                        }
                                    }

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
    public void startGetPendingState(final TxHistory history, final String name) {
        Disposable disposable = Observable.timer(3000, TimeUnit.MILLISECONDS).subscribe(new Consumer<Long>() {
            @Override
            public void accept(Long aLong) throws Exception {
                getReceipt(history, name);
            }
        });

        addDispose(disposable);
    }

    private void getReceipt(final TxHistory history, final String tokenAddr) {
        Log.e(TAG, "======================>  getReceipt......count=" + count);
        count++;
        RxTaskScheduler.postLogicMainTask(new RxTaskCallBack<TransactionReceipt>() {
            @Override
            public void onFailed(LogicException e) {
                super.onFailed(e);
                if (mView != null) {
                    mView.onGetReceiptFailed(history);
                }
            }

            @Override
            public void onSuccess(TransactionReceipt transactionReceipt) {
                super.onSuccess(transactionReceipt);
                Log.e(TAG, "======================>  getReceipt......onSuccess  transactionReceipt=" + transactionReceipt);
                if (transactionReceipt != null) {
                    history.setState(1);
                    TxHistoryDBManager.getManager().insertTxHistory(history);
                    //获取详情页
                    getDetail(history.getPkHash());
                    if (mView != null) {
                        final Disposable disposable = Observable.timer(4000, TimeUnit.MILLISECONDS).subscribeOn(Schedulers.computation()).observeOn(AndroidSchedulers.mainThread()).subscribe(new Consumer<Long>() {
                            @Override
                            public void accept(Long aLong) throws Exception {
                                mView.onGetReceiptSuccess();
                            }
                        });
                        addDispose(disposable);
                    }
                } else {
                    startGetPendingState(history, tokenAddr);
                }
            }

            @Override
            public TransactionReceipt doWork() throws Exception {
                if (TextUtils.isEmpty(history.getTokenTransferTo())) {//主币
                    return TransactionModel.getTransactionModel().getEthTransactionReceipt(history.getPkHash(), history.getTransactionFrom());
                } else {//代币
                    return TransactionModel.getTransactionModel().getTokenTransactionReceipt(history.getPkHash(), history.getTransactionFrom(), tokenAddr);
                }

            }
        });
    }

    public void getBlockNum() {
        stopTask();
        timer = new Timer(true);
        task = new GetBlockNumTask();
        timer.schedule(task, 0, 3000);
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

    public void getDetail(String pkHash) {
        TransactionModel.getTransactionModel().getTransactionDetail(pkHash, new ModelCallBack<TransactionDetail>() {
            @Override
            public void onResponseFailed(int errorCode, String msg) {
                //showToast("获取详情失败：" + msg);
                Log.e(TAG, "获取详情失败：" + msg);
            }

            @Override
            public void onResponseSuccess(TransactionDetail data) {
                if (data != null) {
                    TxDetailManager.getInstance().inputTxDetail(data);
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
    }
}
