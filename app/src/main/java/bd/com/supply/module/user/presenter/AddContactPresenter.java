package bd.com.supply.module.user.presenter;

import bd.com.appcore.mvp.BasePresenterImpl;
import bd.com.appcore.mvp.IBaseView;
import bd.com.appcore.rx.RxTaskCallBack;
import bd.com.appcore.rx.RxTaskScheduler;
import bd.com.walletdb.entity.ContactEntity;
import bd.com.walletdb.manager.ContactManager;

public class AddContactPresenter extends BasePresenterImpl<AddContactPresenter.View> {
    public void addContact(String name, String remark, String address) {
        final ContactEntity entity = new ContactEntity();
        entity.setAddress(address);
        entity.setName(name);
        entity.setRemark(remark);
        ContactManager.getManager().insertContact(entity);
        mView.onAddSuccess();
    }

    public interface View extends IBaseView {
        void onAddSuccess();
    }
}
