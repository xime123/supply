package bd.com.supply.module.push.model;

import android.util.Log;

import com.yanzhenjie.nohttp.NoHttp;
import com.yanzhenjie.nohttp.RequestMethod;
import com.yanzhenjie.nohttp.rest.Request;
import com.yanzhenjie.nohttp.rest.Response;
import com.yanzhenjie.nohttp.rest.SimpleResponseListener;

import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

import bd.com.appcore.network.HttpServer;
import bd.com.appcore.network.RequestParam;
import bd.com.appcore.util.GsonUtil;
import bd.com.supply.module.wallet.ApiConfig;
import bd.com.supply.module.wallet.ApiConfig;

public class PushModel {
    private static final String TAG = PushModel.class.getSimpleName();
    private static PushModel pushModel = new PushModel();

    private PushModel() {
    }

    public static PushModel getPushModel() {
        return pushModel;
    }

    public void registerAddress(String addresses,String registerID){
        Request<JSONObject> request = NoHttp.createJsonObjectRequest(ApiConfig.getRegisterDvice(), RequestMethod.POST);
        Map<String, Object> params = new HashMap<>();
        params.put(RequestParam.ADDRESSES, addresses);
        params.put(RequestParam.OS_TYPE, "android");
        params.put(RequestParam.DEVICE_ID, registerID);
        params.put(RequestParam.DEVICE_TYPE, "phone");
        final String json = GsonUtil.mapToJson(params);
        request.setDefineRequestBodyForJson(json);
        HttpServer.getInstance().request(0, request, new SimpleResponseListener<JSONObject>() {

            @Override
            public void onSucceed(int what, Response<JSONObject> response) {
                Log.i(TAG, "registerAddress onSucceed====>" + response.get().toString());
                super.onSucceed(what, response);

            }

            @Override
            public void onFailed(int what, Response<JSONObject> response) {
                super.onFailed(what, response);
                Log.i(TAG, "registerAddress onFailed====>");
            }
        });
    }
}
