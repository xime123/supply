package bd.com.supply.module.common;

import android.text.TextUtils;
import android.util.Log;

import com.yanzhenjie.nohttp.NoHttp;
import com.yanzhenjie.nohttp.RequestMethod;
import com.yanzhenjie.nohttp.rest.Request;
import com.yanzhenjie.nohttp.rest.Response;
import com.yanzhenjie.nohttp.rest.SimpleResponseListener;

import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

import bd.com.appcore.base.ModelCallBack;
import bd.com.appcore.network.HttpServer;
import bd.com.appcore.network.RequestParam;
import bd.com.appcore.util.GsonUtil;
import bd.com.supply.module.wallet.ApiConfig;
import bd.com.supply.module.wallet.ApiConfig;

public class APPUpdateManager {
    private static final String TAG = APPUpdateManager.class.getSimpleName();
    private static APPUpdateManager updateManager = new APPUpdateManager();

    private APPUpdateManager() {
    }

    public static APPUpdateManager getUpdateManager() {
        return updateManager;
    }

    /**
     * 检查版本更新
     *
     * @param appVersion
     */
    public void checkUpdate(int appVersion, final ModelCallBack<UpdateInfo> callBack) {
        Request<JSONObject> request = NoHttp.createJsonObjectRequest(ApiConfig.getCheckAppUpdate(), RequestMethod.POST);
        Map<String, Object> params = new HashMap<>();
        params.put(RequestParam.APP_VERSION, appVersion);
        params.put(RequestParam.OS_TYPE, "android");
        params.put(RequestParam.CHANNEL_NO, "DEFAULT");
        params.put(RequestParam.DEVICE_TYPE, "phone");
        final String json = GsonUtil.mapToJson(params);
        request.setDefineRequestBodyForJson(json);
        HttpServer.getInstance().request(0, request, new SimpleResponseListener<JSONObject>() {
            @Override
            public void onSucceed(int what, Response<JSONObject> response) {
                Log.i(TAG, "checkUpdate onSucceed====>" + response.get().toString());
                super.onSucceed(what, response);
                if (response.isSucceed()) {
                    JSONObject jsonObject = response.get();
                    int status = jsonObject.optInt("status");
                    String msg = jsonObject.optString("msg");
                    String dataJson = jsonObject.optString("data");
                    if (status == 0 && !TextUtils.isEmpty(dataJson)) {
                        UpdateInfo resp = GsonUtil.jsonToObject(dataJson, UpdateInfo.class);
                        callBack.onResponseSuccess(resp);
                    } else {
                        callBack.onResponseFailed(status, msg);
                    }
                }

            }

            @Override
            public void onFailed(int what, Response<JSONObject> response) {
                super.onFailed(what, response);
                callBack.onResponseFailed(-1,"请求失败");
                Log.i(TAG, "checkUpdate onFailed====>" + response.toString());
            }
        });
    }
}
