package bd.com.supply.module.transaction.presenter;

import android.annotation.SuppressLint;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.TimeUnit;

import bd.com.appcore.mvp.BasePresenter;
import bd.com.appcore.mvp.IListView;
import bd.com.appcore.rx.RxTaskCallBack;
import bd.com.appcore.rx.RxTaskScheduler;
import bd.com.supply.module.wallet.model.TokenModel;
import bd.com.walletdb.entity.TokenEntity;
import bd.com.walletdb.manager.TokenManager;
import io.reactivex.Observable;
import io.reactivex.functions.Consumer;

/**
 * author:     labixiaoxin
 * date:       2018/7/21
 * email:      labixiaoxin2@qq.cn
 */
public class TokenSearchPresenter extends BasePresenter<TokenSearchPresenter.View> {


    @SuppressLint("CheckResult")
    public void search(final String key, final List<TokenEntity> entityList) {
        if (mView != null) {
            mView.showLoadingDialog();
        }
        Observable.timer(500, TimeUnit.MILLISECONDS).subscribe(new Consumer<Long>() {
            @Override
            public void accept(Long aLong) throws Exception {
                if (entityList != null && entityList.size() > 0) {
                    doSearch(key, entityList);
                } else {
                    RxTaskScheduler.postUiTask(new RxTaskCallBack<Boolean>() {
                        @Override
                        public Boolean doWork() throws Exception {
                            if (mView != null) {
                                mView.hideLoadingDialog();
                                mView.loadEmpty();
                            }
                            return true;
                        }
                    });
                }

            }
        });


    }

    private void doSearch(final String key, final List<TokenEntity> entityList) {
        RxTaskScheduler.postLogicMainTask(new RxTaskCallBack<List<TokenEntity>>() {
            @Override
            public void onSuccess(List<TokenEntity> entityList) {
                super.onSuccess(entityList);
                if (mView != null) {
                    mView.hideLoadingDialog();
                    if (entityList.size() > 0) {
                        mView.loadSuccess(entityList);
                    } else {
                        mView.loadEmpty();
                    }
                }
            }

            @Override
            public List<TokenEntity> doWork() throws Exception {
                List<TokenEntity> searchedList = new ArrayList<>();
                for (TokenEntity entity : entityList) {
                    if (entity.getAddress().contains(key) || entity.getSymbol().contains(key)) {
                        searchedList.add(entity);
                    }
                }
                return searchedList;
            }
        });
    }

    public List<TokenEntity> getTokenList() {
        return TokenManager.getManager().getAll();
    }

    public interface View extends IListView<TokenEntity> {

    }
}
