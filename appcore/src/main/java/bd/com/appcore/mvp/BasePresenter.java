package bd.com.appcore.mvp;

import android.util.Log;

import io.reactivex.disposables.CompositeDisposable;
import io.reactivex.disposables.Disposable;
import io.reactivex.internal.disposables.DisposableContainer;
import io.reactivex.internal.disposables.DisposableHelper;


public class BasePresenter<V extends IView> implements IBasePresenter<V> {
    public static final String TAG = "BasePresenter";
    public V mView;
    private CompositeDisposable container=new CompositeDisposable();
    public void onAttachView(V view) {
        mView = view;
    }

    public void addDispose(Disposable disposable){
        container.add(disposable);
    }
    public void onDetachView() {
        mView = null;
        container.dispose();
        container.clear();
        Log.e(TAG, " Activity onDestroy ,  执行onDetachView() mView = null ");
    }


}
