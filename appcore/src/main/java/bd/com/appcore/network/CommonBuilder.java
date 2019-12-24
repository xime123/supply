package bd.com.appcore.network;

import android.text.TextUtils;
import android.util.Log;

import com.yanzhenjie.nohttp.NoHttp;
import com.yanzhenjie.nohttp.RequestMethod;
import com.yanzhenjie.nohttp.rest.Request;
import com.yanzhenjie.nohttp.rest.Response;
import com.yanzhenjie.nohttp.rest.SimpleResponseListener;

import org.json.JSONObject;

import java.lang.reflect.ParameterizedType;
import java.util.Map;

import bd.com.appcore.base.ModelCallBack;
import bd.com.appcore.util.GsonUtil;

public abstract class CommonBuilder<T> {
    private String TAG = CommonBuilder.class.getSimpleName();

    public abstract String getUrl();

    public abstract RequestMethod getMethod();


    private Map<String, String> headers;
    private Object reqBean;

    public CommonBuilder<T> addHeader(Map<String, Object> headers) {
        headers.putAll(headers);
        return this;
    }

    public CommonBuilder<T> addBody(Object reqBean) {
        this.reqBean = reqBean;
        return this;
    }

    public CommonBuilder<T> addBody(Map<String, Object> reqBean) {
        this.reqBean = reqBean;
        return this;
    }

    /**
     * 同步方法
     *
     * @return
     */
    public T buildSync() {
        Request<JSONObject> request = NoHttp.createJsonObjectRequest(getUrl(), getMethod());
        final String json = GsonUtil.objectToJson(reqBean, reqBean.getClass());
        if (headers != null) {
            setHeader(request);
        }
        request.setDefineRequestBodyForJson(json);
        // 调用同步请求，直接拿到请求结果。
        Response<JSONObject> response = NoHttp.startRequestSync(request);
        if (response != null && response.isSucceed()) {
            JSONObject jsonObject = response.get();
            int status = jsonObject.optInt("status");
            String msg = jsonObject.optString("msg");
            String dataJson = jsonObject.optString("data");
            if (status == 0) {
                T resp = GsonUtil.jsonToObject(dataJson, getTClass());
                return resp;
            }
        }
        return null;
    }

    /**
     * 异步调用
     *
     * @param callBack
     */
    public void buildAsync(final ModelCallBack<T> callBack) {
        Request<JSONObject> request = NoHttp.createJsonObjectRequest(getUrl(), getMethod());
        String json = "";
        if (reqBean instanceof Map) {
            json = GsonUtil.mapToJson((Map<String, Object>) reqBean);
        } else {
            json = GsonUtil.objectToJson(reqBean, reqBean.getClass());
        }
        request.setDefineRequestBodyForJson(json);
        if (headers != null) {
            setHeader(request);
        }
        Log.i(TAG, "buildAsync method=" + getMethod() + "   reqJson===>" + json);
        HttpServer.getInstance().request(request.hashCode(), request, new SimpleResponseListener<JSONObject>() {

            @Override
            public void onSucceed(int what, final Response<JSONObject> response) {
                Log.i(TAG, "buildAsync method=" + getMethod() + "   onSucceed====>" + response.get().toString());
                super.onSucceed(what, response);
                if (response.isSucceed()) {
                    JSONObject jsonObject = response.get();
                    int status = jsonObject.optInt("status");
                    String msg = jsonObject.optString("msg");
                    String dataJson = jsonObject.optString("data");
                    if (status == 0) {
                        T resp = null;
                        if (TextUtils.isEmpty(dataJson)) {
                            resp = GsonUtil.jsonToObject(jsonObject.toString(), getTClass());
                        } else {
                            resp = GsonUtil.jsonToObject(dataJson, getTClass());

                        }

                        callBack.onResponseSuccess(resp);

                    } else {
                        callBack.onResponseFailed(status, msg);
                    }
                }
            }

            @Override
            public void onFailed(int what, Response<JSONObject> response) {
                super.onFailed(what, response);
                Log.i(TAG, "buildAsync method=" + getMethod() + "   onFailed====>code=" + response.responseCode() + "::message=" + response.getException().getMessage());
                callBack.onResponseFailed(response.responseCode(), response.getException().getMessage());
            }
        });
    }

    private void setHeader(Request<JSONObject> request) {
        for (Map.Entry<String, String> entry : headers.entrySet()) {
            request.set(entry.getKey(), entry.getValue());
        }
    }

    private Class<T> getTClass() {
        Class<T> tClass = (Class<T>) ((ParameterizedType) getClass().getGenericSuperclass()).getActualTypeArguments()[0];
        return tClass;
    }
}
