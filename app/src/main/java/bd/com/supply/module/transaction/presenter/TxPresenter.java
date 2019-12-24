package bd.com.supply.module.transaction.presenter;


import android.text.TextUtils;
import android.util.Log;

import org.web3j.utils.Convert;

import java.math.BigDecimal;
import java.math.BigInteger;

import bd.com.appcore.base.ModelCallBack;
import bd.com.appcore.mvp.BasePresenterImpl;
import bd.com.appcore.rx.RxTaskCallBack;
import bd.com.appcore.rx.RxTaskScheduler;
import bd.com.appcore.util.AppSettings;
import bd.com.supply.module.common.GlobConfig;
import bd.com.supply.module.transaction.model.TransactionModel;
import bd.com.supply.module.wallet.ApiConfig;
import bd.com.supply.module.wallet.model.CoinModel;
import bd.com.supply.module.wallet.model.domain.SugGasResp;
import bd.com.walletdb.manager.TxHistoryDBManager;
import bd.com.walletdb.entity.TxHistory;
import bd.com.supply.module.transaction.view.ITxView;

/**
 * author:     labixiaoxin
 * date:       2018/5/25
 * email:      labixiaoxin2@qq.cn
 */
public class TxPresenter extends BasePresenterImpl<ITxView> {
    private static final String TAG = TxPresenter.class.getSimpleName();

    public void translate(final String tokenAddr, final String from, final String to, final String value, String password) {
        showLoading();
        TransactionModel.getTransactionModel().transfer(tokenAddr, from, to, value, password, new ModelCallBack<TxHistory>() {
            @Override
            public void onResponseFailed(int errorCode, String msg) {
                dismissLoading();
                if(errorCode==-100){
                    mView.onTranslateFailed("余额不足");
                }else {
                    mView.onTranslateFailed("交易失败");
                }

            }

            @Override
            public void onResponseSuccess(TxHistory data) {
                dismissLoading();
                //pending状态的记录存到数据库
                TxHistoryDBManager.getManager().insertTxHistory(data);
                mView.onTranslateSuccess(data);
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

    public void getSugGas() {
        CoinModel.getWalletModel().getSugGas(new ModelCallBack<SugGasResp>() {
            @Override
            public void onResponseFailed(int errorCode, String msg) {
                Log.i(TAG, "getSugGas error msg=" + msg);
            }

            @Override
            public void onResponseSuccess(SugGasResp data) {
                if (data != null && data.data != null) {
                    String sugGas = data.data.getSugGasPrice();
                    //这个值，是
                    BigDecimal GASLIMIT = new BigDecimal(sugGas);
                    BigDecimal BIG_GWEI = Convert.fromWei(GASLIMIT, Convert.Unit.GWEI);
                    BigInteger bigInteger = BIG_GWEI.toBigInteger();
                    int progress=bigInteger.intValue();
                    if (mView != null) {
                        mView.onGetSugGasSuccess(progress);
                    }
                }
            }
        });
    }
}
