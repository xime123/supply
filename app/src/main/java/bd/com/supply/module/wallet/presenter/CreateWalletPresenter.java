package bd.com.supply.module.wallet.presenter;

import android.text.TextUtils;

import bd.com.appcore.mvp.BasePresenterImpl;
import bd.com.appcore.rx.RxTaskCallBack;
import bd.com.appcore.rx.RxTaskScheduler;
import bd.com.supply.module.wallet.ui.MyWalletUtil;
import bd.com.supply.module.wallet.view.ICreateWalletView;
import bd.com.supply.module.wallet.ui.MyWalletUtil;
import bd.com.supply.module.wallet.view.ICreateWalletView;
import io.reactivex.disposables.Disposable;


public class CreateWalletPresenter extends BasePresenterImpl<ICreateWalletView> {

    public void createWallet(final String name, final String password, String passowrdAgain) {
        if (!checkParams(name, password, passowrdAgain)) {
            return;
        }


        RxTaskScheduler.postIoMainTask(new RxTaskCallBack<Boolean>() {
            @Override
            public void onStart(Disposable disposable) {
                super.onStart(disposable);
                if (mView != null) {
                    mView.showLoadingDialog();
                }
            }

            @Override
            public void onSuccess(Boolean aBoolean) {
                super.onSuccess(aBoolean);
                if (aBoolean) {
                    mView.onCreateWalletSuccess();
                } else {
                    mView.onCreateWalletFailed("创建钱包失败");
                }
            }

            @Override
            public Boolean doWork() {
                return MyWalletUtil.createWallet(password, name);
            }
        });
    }

    /**
     * 校验参数
     *
     * @param name
     * @param password
     * @param passowrdAgain
     * @return
     */
    private boolean checkParams(String name, String password, String passowrdAgain) {
        if (TextUtils.isEmpty(name)) {
            showTips("钱包名不能为空");
            return false;
        }

        if (TextUtils.isEmpty(password) || TextUtils.isEmpty(passowrdAgain)) {
            showTips("密码不能为空");
            return false;
        }

        if (!TextUtils.equals(password, passowrdAgain)) {
            showTips("两次密码不一致");
            return false;
        }

        if(password.length()<6){
            showTips("钱包密码至少6位字符");
            return false;
        }
        return true;
    }

    private void showTips(String tips) {
        if (mView != null) {
            mView.showTips(tips);
        }
    }
}
