package bd.com.appcore.rx;

import io.reactivex.disposables.Disposable;

/**
 * author:     labixiaoxin
 * date:       2018/5/4
 * email:      labixiaoxin2@qq.cn
 */
public interface BaseRxTask<T> {

    void onStart(Disposable disposable);

    void onSuccess(T t);

    void onFailed(LogicException e);

    void onComplete();
    T doWork() throws Exception;

}
