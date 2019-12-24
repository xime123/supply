package bd.com.supply.module.news.presenter;

import java.util.List;
import java.util.concurrent.TimeUnit;

import bd.com.appcore.mvp.BasePresenterImpl;
import bd.com.appcore.mvp.IListView;
import bd.com.appcore.rx.RxTaskCallBack;
import bd.com.appcore.rx.RxTaskScheduler;
import bd.com.supply.module.news.domain.News;
import bd.com.supply.util.AssetsDbManager;
import bd.com.supply.util.AssetsDbManager;
import io.reactivex.Observable;

import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.functions.Consumer;
import io.reactivex.schedulers.Schedulers;

public class NewsPresenter extends BasePresenterImpl<NewsPresenter.View> {

    public void getNewsList() {
        Observable.timer(500, TimeUnit.MILLISECONDS)
                .observeOn(AndroidSchedulers.mainThread())
                .subscribeOn(Schedulers.io())
                .subscribe(new Consumer<Long>() {
                    @Override
                    public void accept(Long aLong) throws Exception {
                        List<News> newsList = AssetsDbManager.getNewsList();
                        if (mView != null) {
                            mView.loadSuccess(newsList);
                        }
                    }
                });
    }

    public void getBannerList() {
        RxTaskScheduler.postLogicMainTask(new RxTaskCallBack<List<News>>() {
            @Override
            public void onSuccess(List<News> news) {
                super.onSuccess(news);
                if (mView != null) {
                    mView.onGetBannerListSuccess(news);
                }
            }

            @Override
            public List<News> doWork() throws Exception {
                return AssetsDbManager.getBannerList();
            }
        });
    }

    public interface View extends IListView<News> {
        void onGetBannerListSuccess(List<News> banners);

        void onGetBannerListFailed();
    }
}
