package bd.com.supply.module.wallet.presenter;

import android.text.TextUtils;

import java.util.concurrent.TimeUnit;

import bd.com.appcore.mvp.BasePresenterImpl;
import bd.com.appcore.rx.RxTaskCallBack;
import bd.com.appcore.rx.RxTaskScheduler;
import bd.com.supply.module.wallet.ApiConfig;
import bd.com.supply.module.wallet.model.CoinModel;
import bd.com.supply.module.wallet.view.IWalletDetailView;
import bd.com.supply.module.wallet.ApiConfig;
import bd.com.supply.module.wallet.view.IWalletDetailView;
import bd.com.walletdb.manager.WalletDBManager;
import bd.com.walletdb.entity.WalletEntity;
import io.reactivex.Observable;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.functions.Consumer;
import io.reactivex.schedulers.Schedulers;


public class WalletDetailPresenter extends BasePresenterImpl<IWalletDetailView> {
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

    public void updateWalletName(final String name, final WalletEntity entity) {
        if (mView != null) {
            mView.showLoadingDialog();
        }
        Observable.timer(400, TimeUnit.MILLISECONDS).subscribeOn(Schedulers.computation()).observeOn(AndroidSchedulers.mainThread()).subscribe(new Consumer<Long>() {
            @Override
            public void accept(Long aLong) throws Exception {
                entity.setName(name);
                WalletDBManager.getManager().updateWallet(entity);
                if (mView != null) {
                    mView.exit();
                    mView.hideLoadingDialog();
                }

            }
        });
    }

    public void getBalance(final String address){
        RxTaskScheduler.postIoMainTask(new RxTaskCallBack<String>() {
            @Override
            public void onSuccess(String balance) {
                super.onSuccess(balance);
                if(mView!=null){
                    mView.onGetBalanceSuccess(balance);
                }
            }

            @Override
            public String doWork() throws Exception {
                String balance = CoinModel.getWalletModel().getBalance(ApiConfig.getWeb3jUrlProxy(), address);

                return balance;
            }
        });


    }
}
