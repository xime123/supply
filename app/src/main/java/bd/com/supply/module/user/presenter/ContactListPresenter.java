package bd.com.supply.module.user.presenter;

import android.util.Log;

import java.util.List;
import java.util.concurrent.TimeUnit;

import bd.com.appcore.mvp.BasePresenterImpl;
import bd.com.appcore.mvp.IListView;
import bd.com.appcore.rx.RxTaskCallBack;
import bd.com.appcore.rx.RxTaskScheduler;
import bd.com.supply.module.news.domain.News;
import bd.com.supply.util.AssetsDbManager;
import bd.com.walletdb.entity.ContactEntity;
import bd.com.walletdb.manager.ContactManager;
import io.reactivex.Observable;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.functions.Consumer;
import io.reactivex.schedulers.Schedulers;

public class ContactListPresenter extends BasePresenterImpl<ContactListPresenter.View> {

    public void getContactList() {
        RxTaskScheduler.postIoMainTask(new RxTaskCallBack<List<ContactEntity>>() {
            @Override
            public void onSuccess(List<ContactEntity> contactEntities) {
                super.onSuccess(contactEntities);
                if (mView != null) {
                    if (contactEntities == null || contactEntities.size() == 0) {
                        mView.loadEmpty();
                    } else {
                        mView.loadSuccess(contactEntities);
                    }
                }
            }

            @Override
            public List<ContactEntity> doWork() throws Exception {
                Thread.sleep(500);
                List<ContactEntity> contactEntityList = ContactManager.getManager().getContactList();
                return contactEntityList;
            }
        });
    }

    public interface View extends IListView<ContactEntity> {

    }
}
