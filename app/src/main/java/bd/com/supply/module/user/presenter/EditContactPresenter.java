package bd.com.supply.module.user.presenter;

import android.text.TextUtils;
import android.widget.Toast;

import bd.com.appcore.mvp.BasePresenterImpl;
import bd.com.appcore.mvp.IBaseView;
import bd.com.appcore.rx.RxTaskCallBack;
import bd.com.appcore.rx.RxTaskScheduler;
import bd.com.appcore.util.AppSettings;
import bd.com.walletdb.entity.ContactEntity;
import bd.com.walletdb.entity.WalletEntity;
import bd.com.walletdb.manager.ContactManager;
import bd.com.walletdb.manager.WalletDBManager;

public class EditContactPresenter extends BasePresenterImpl<EditContactPresenter.View> {
    public void deleteContactByPwd(String address, final String pwd) {
        if (TextUtils.isEmpty(address)) return;

        String currentAddres= AppSettings.getAppSettings().getCurrentAddress();
        WalletEntity entity = WalletDBManager.getManager().getWalletEntity(currentAddres);
        if (!TextUtils.equals(pwd, entity.getPassword())) {
            mView.onDeleteFailed("交易密码不正确");
            return;
        }
        ContactManager.getManager().delteContact(address);
        mView.onDeleteSuccess();
    }

    public void deleteContact(String address) {
        ContactManager.getManager().delteContact(address);

    }

    public void updateContact(ContactEntity entity) {
        ContactManager.getManager().updateContact(entity);
    }

    public void inputContact(ContactEntity entity) {
        ContactManager.getManager().insertContact(entity);
    }

    public interface View extends IBaseView {
        void onDeleteFailed(String errorMsg);

        void onDeleteSuccess();
    }
}
