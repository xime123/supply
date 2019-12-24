package bd.com.appcore.network;

import android.util.Log;

import com.yanzhenjie.nohttp.NoHttp;
import com.yanzhenjie.nohttp.rest.Request;
import com.yanzhenjie.nohttp.rest.RequestQueue;
import com.yanzhenjie.nohttp.rest.Response;
import com.yanzhenjie.nohttp.rest.SimpleResponseListener;

import java.util.Map;

import bd.com.appcore.base.ModelCallBack;

/**
 * author:     labixiaoxin
 * date:       2018/5/26
 * email:      labixiaoxin2@qq.cn
 */
public class HttpServer {
    private static String TAG=HttpServer.class.getSimpleName();
    private static HttpServer instance;

    public static HttpServer getInstance() {
        if (instance == null)
            synchronized (HttpServer.class) {
                if (instance == null)
                    instance = new HttpServer();
            }
        return instance;
    }

    private RequestQueue queue;

    private HttpServer() {
        queue = NoHttp.newRequestQueue(5);
    }

    public   <T> void request(int what, Request<T> request, SimpleResponseListener<T> listener) {
        queue.add(what, request, listener);
    }

    public<T> void request(String url, Map params,final ModelCallBack<T> listener){
        Request<String> request = NoHttp.createStringRequest(url);
        request.set(params);
        request(0, request, new SimpleResponseListener<String>() {

            @Override
            public void onSucceed(int what, Response<String> response) {
                super.onSucceed(what, response);
                Log.i(TAG,"onSucceed response="+response.get());
                if (response.isSucceed()) {

                }
            }

            @Override
            public void onFailed(int what, Response<String> response) {
                super.onFailed(what, response);
                Log.e(TAG,"onFailed response="+response.get());
                int errorCode=response.responseCode();
                listener.onResponseFailed(errorCode,response.getException().getMessage());
            }
        });
    }

    // 完全退出app时，调用这个方法释放CPU。
    public void stop() {
        queue.stop();
    }
}
