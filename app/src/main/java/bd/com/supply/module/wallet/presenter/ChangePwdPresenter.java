package bd.com.supply.module.wallet.presenter;

import android.text.TextUtils;

import java.util.concurrent.TimeUnit;

import bd.com.appcore.mvp.BasePresenterImpl;
import bd.com.supply.module.wallet.view.IChangePwdView;
import bd.com.supply.module.wallet.view.IChangePwdView;
import bd.com.walletdb.manager.WalletDBManager;
import bd.com.walletdb.entity.WalletEntity;
import io.reactivex.Observable;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.functions.Consumer;
import io.reactivex.schedulers.Schedulers;


public class ChangePwdPresenter extends BasePresenterImpl<IChangePwdView> {
    /**
     * 校验旧密码
     * @param oldPwd
     * @param entity
     */
    public void validatePwd(final String oldPwd, final String newPwd,final WalletEntity entity){
        mView.showLoadingDialog();
        Observable.timer(400, TimeUnit.MILLISECONDS)
                .observeOn(AndroidSchedulers.mainThread())
                .subscribeOn(Schedulers.computation())
                .subscribe(new Consumer<Long>() {
                    @Override
                    public void accept(Long aLong) throws Exception {

                        if(mView!=null) {
                            mView.hideLoadingDialog();
                            if (TextUtils.equals(oldPwd, entity.getPassword())) {
                                entity.setPassword(newPwd);
                                WalletDBManager.getManager().updateWallet(entity);
                                mView.onPwdRight();
                            }else {
                                mView.onPwdError();
                            }
                        }
                    }
                });

    }
}
