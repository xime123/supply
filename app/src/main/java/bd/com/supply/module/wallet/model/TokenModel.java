package bd.com.supply.module.wallet.model;

import android.util.Log;

import com.yanzhenjie.nohttp.NoHttp;
import com.yanzhenjie.nohttp.RequestMethod;
import com.yanzhenjie.nohttp.rest.Request;
import com.yanzhenjie.nohttp.rest.Response;
import com.yanzhenjie.nohttp.rest.SimpleResponseListener;

import org.json.JSONObject;

import java.util.List;
import java.util.Map;

import bd.com.appcore.base.ModelCallBack;
import bd.com.appcore.network.HttpServer;
import bd.com.appcore.rx.LogicException;
import bd.com.appcore.rx.RxTaskCallBack;
import bd.com.appcore.rx.RxTaskScheduler;
import bd.com.appcore.util.AppSettings;
import bd.com.appcore.util.GsonUtil;
import bd.com.supply.module.wallet.ApiConfig;
import bd.com.supply.module.wallet.model.domain.ChainListListResp;
import bd.com.supply.module.wallet.model.domain.TokenListListResp;
import bd.com.supply.module.wallet.ApiConfig;
import bd.com.supply.module.wallet.model.domain.ChainListListResp;
import bd.com.supply.module.wallet.model.domain.TokenListListResp;
import bd.com.walletdb.entity.TokenEntity;
import bd.com.walletdb.manager.TokenManager;



public class TokenModel {
    private static final String TAG = TokenModel.class.getSimpleName();
    private static TokenModel model = new TokenModel();

    private TokenModel() {
    }

    public static TokenModel getModel() {
        return model;
    }

    /**
     * 获取tokenlist
     *
     * @param params 钱包地址
     * @return
     */
    public void getTokenList(Map<String, Object> params, final ModelCallBack<TokenListListResp> callBack) {
        String currentChainId=AppSettings.getAppSettings().getCurrentChainId();
        params.put("chainId",currentChainId);
        final Request<JSONObject> request = NoHttp.createJsonObjectRequest(ApiConfig.getTokenList(), RequestMethod.POST);
        final String json = GsonUtil.mapToJson(params);
        request.setDefineRequestBodyForJson(json);
        HttpServer.getInstance().request(0, request, new SimpleResponseListener<JSONObject>() {

            @Override
            public void onSucceed(int what, Response<JSONObject> response) {
                Log.i(TAG, "getTransactionList onSucceed====>" + response.get().toString());
                super.onSucceed(what, response);
                if (response.isSucceed()) {
                    JSONObject jsonObject = response.get();
                    int status = jsonObject.optInt("status");
                    String msg = jsonObject.optString("msg");
                    if (status == 0) {
                        TokenListListResp resp = GsonUtil.jsonToObject(jsonObject.toString(), TokenListListResp.class);
                        callBack.onResponseSuccess(resp);
                    } else {
                        callBack.onResponseFailed(status, msg);
                    }
                }
            }

            @Override
            public void onFailed(int what, Response<JSONObject> response) {
                super.onFailed(what, response);
                // Log.i(TAG, "getTransactionList onFailed====>" + response.get().toString());
                callBack.onResponseFailed(response.responseCode(), response.getException().getMessage());
            }
        });
    }

    public void getCurrentTokenList(final ModelCallBack<List<TokenEntity>> callBack){
        RxTaskScheduler.postIoMainTask(new RxTaskCallBack<List<TokenEntity>>() {
            @Override
            public void onFailed(LogicException e) {
                super.onFailed(e);
                callBack.onResponseFailed(e.getCode(),e.getErrorMsg());
            }

            @Override
            public void onSuccess(List<TokenEntity> tokenEntityList) {
                super.onSuccess(tokenEntityList);
                callBack.onResponseSuccess(tokenEntityList);
            }

            @Override
            public List<TokenEntity> doWork() throws Exception {
                Thread.sleep(800);
                List<TokenEntity> tokenEntityList=TokenManager.getManager().getCurrentTokenList(AppSettings.getAppSettings().getCurrentAddress(),AppSettings.getAppSettings().getCurrentChainId());
                return tokenEntityList;
            }
        });

    }

    /**
     * 获取多链列表
     *
     * @param params 钱包地址
     * @return
     */
    public void getChainList(Map<String, Object> params, final ModelCallBack<ChainListListResp> callBack) {
        final Request<JSONObject> request = NoHttp.createJsonObjectRequest(ApiConfig.getChainList(), RequestMethod.POST);
        final String json = GsonUtil.mapToJson(params);
        request.setDefineRequestBodyForJson(json);
        HttpServer.getInstance().request(0, request, new SimpleResponseListener<JSONObject>() {

            @Override
            public void onSucceed(int what, Response<JSONObject> response) {
                Log.i(TAG, "getChainList onSucceed====>" + response.get().toString());
                super.onSucceed(what, response);
                if (response.isSucceed()) {
                    JSONObject jsonObject = response.get();
                    int status = jsonObject.optInt("status");
                    String msg = jsonObject.optString("msg");
                    if (status == 0) {
                        ChainListListResp resp = GsonUtil.jsonToObject(jsonObject.toString(), ChainListListResp.class);
                        callBack.onResponseSuccess(resp);
                    } else {
                        callBack.onResponseFailed(status, msg);
                    }
                }
            }

            @Override
            public void onFailed(int what, Response<JSONObject> response) {
                super.onFailed(what, response);
                // Log.i(TAG, "getTransactionList onFailed====>" + response.get().toString());
                callBack.onResponseFailed(response.responseCode(), response.getException().getMessage());
            }
        });
    }


}
