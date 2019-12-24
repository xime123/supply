package bd.com.appcore.base;

/**
 * author:     labixiaoxin
 * date:       2018/5/26
 * email:      labixiaoxin2@qq.cn
 */
public interface ModelCallBack<T> {
    void onResponseFailed(int errorCode,String msg);
    void onResponseSuccess(T data);
}
