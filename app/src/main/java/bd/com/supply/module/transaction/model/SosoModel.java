package bd.com.supply.module.transaction.model;

import android.util.Log;

import com.tencent.bugly.crashreport.BuglyLog;
import com.yanzhenjie.nohttp.NoHttp;
import com.yanzhenjie.nohttp.RequestMethod;
import com.yanzhenjie.nohttp.rest.Request;
import com.yanzhenjie.nohttp.rest.Response;
import com.yanzhenjie.nohttp.rest.SimpleResponseListener;

import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import bd.com.appcore.base.ModelCallBack;
import bd.com.appcore.network.HttpServer;
import bd.com.appcore.util.GsonUtil;
import bd.com.supply.module.transaction.model.domian.Batch;
import bd.com.supply.module.transaction.model.domian.BatchListResp;
import bd.com.supply.module.transaction.model.domian.HashBean;
import bd.com.supply.module.transaction.model.domian.HashBeanListResp;
import bd.com.supply.module.transaction.model.domian.HashBeanResp;
import bd.com.supply.module.transaction.model.domian.ProductListResp;
import bd.com.supply.module.transaction.model.domian.ProductOri;
import bd.com.supply.module.transaction.model.req.ListAdReq;
import bd.com.supply.module.transaction.model.req.ReportRecordReq;
import bd.com.supply.module.transaction.model.req.SosoInfoReq;
import bd.com.supply.module.transaction.model.resp.ListAdResp;
import bd.com.supply.module.transaction.model.resp.ReportRecordResp;
import bd.com.supply.module.transaction.model.resp.SosoInfoResp;
import bd.com.supply.module.wallet.ApiConfig;
import bd.com.supply.module.transaction.model.domian.BatchListResp;
import bd.com.supply.module.transaction.model.domian.HashBean;
import bd.com.supply.module.transaction.model.domian.HashBeanListResp;
import bd.com.supply.module.transaction.model.domian.HashBeanResp;
import bd.com.supply.module.transaction.model.domian.ProductListResp;
import bd.com.supply.module.transaction.model.req.ListAdReq;
import bd.com.supply.module.transaction.model.req.ReportRecordReq;
import bd.com.supply.module.transaction.model.req.SosoInfoReq;
import bd.com.supply.module.transaction.model.resp.ListAdResp;
import bd.com.supply.module.transaction.model.resp.ReportRecordResp;
import bd.com.supply.module.transaction.model.resp.SosoInfoResp;

public class SosoModel {
    private static String TAG = SosoModel.class.getSimpleName();
    private static SosoModel instance = new SosoModel();


    private SosoModel() {
    }

    public static SosoModel getInstance() {
        return instance;
    }

    public void getBatchList(Map<String, Object> params, final ModelCallBack<BatchListResp> callBack) {
        Request<JSONObject> request = NoHttp.createJsonObjectRequest(ApiConfig.getBatchList(), RequestMethod.POST);
        final String json = GsonUtil.mapToJson(params);
        request.setDefineRequestBodyForJson(json);
        HttpServer.getInstance().request(0, request, new SimpleResponseListener<JSONObject>() {

            @Override
            public void onSucceed(int what, Response<JSONObject> response) {
                BuglyLog.i(TAG, "getBatchList onSucceed====>" + response.get().toString());
                super.onSucceed(what, response);
                if (response.isSucceed()) {
                    JSONObject jsonObject = response.get();
                    int status = jsonObject.optInt("status");
                    String msg = jsonObject.optString("msg");
                    if (status == 0) {
                        BatchListResp resp = GsonUtil.jsonToObject(jsonObject.toString(), BatchListResp.class);
                        List<Batch> batchList = resp.getList();
                        if (batchList == null) {
                            resp.setList(new ArrayList<Batch>());
                            callBack.onResponseSuccess(resp);
                        } else {
                            callBack.onResponseSuccess(resp);
                        }

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

    public void bindData(Map<String, Object> params, final ModelCallBack<String> callBack) {
        Request<JSONObject> request = NoHttp.createJsonObjectRequest(ApiConfig.getBindData(), RequestMethod.POST);
        final String json = GsonUtil.mapToJson(params);
        request.setDefineRequestBodyForJson(json);
        HttpServer.getInstance().request(0, request, new SimpleResponseListener<JSONObject>() {

            @Override
            public void onSucceed(int what, Response<JSONObject> response) {
                BuglyLog.i(TAG, "bindData onSucceed====>" + response.get().toString());
                super.onSucceed(what, response);
                if (response.isSucceed()) {
                    JSONObject jsonObject = response.get();
                    int status = jsonObject.optInt("status");
                    String msg = jsonObject.optString("msg");
                    if (status == 0) {
                        callBack.onResponseSuccess("绑定成功");
                    } else {
                        callBack.onResponseFailed(status, msg);
                    }
                }
            }

            @Override
            public void onFailed(int what, Response<JSONObject> response) {
                super.onFailed(what, response);
                Log.i(TAG, "bindData onFailed====>" + response.getException().getMessage());
                callBack.onResponseFailed(response.responseCode(), response.getException().getMessage());
            }
        });
    }

    public void getTxHashByUUid(String uuid, String opAddr, final ModelCallBack<HashBeanResp> callBack) {
        Map<String, Object> params = new HashMap<>();
        params.put("uuid", uuid);
        params.put("address", opAddr);
        Request<JSONObject> request = NoHttp.createJsonObjectRequest(ApiConfig.getHashByUUid(), RequestMethod.POST);
        final String json = GsonUtil.mapToJson(params);
        request.setDefineRequestBodyForJson(json);
        HttpServer.getInstance().request(0, request, new SimpleResponseListener<JSONObject>() {

            @Override
            public void onSucceed(int what, Response<JSONObject> response) {
                BuglyLog.i(TAG, "getTxHashByUUid onSucceed====>" + response.get().toString());
                super.onSucceed(what, response);
                if (response.isSucceed()) {
                    JSONObject jsonObject = response.get();
                    int status = jsonObject.optInt("status");
                    String msg = jsonObject.optString("msg");
                    if (status == 0) {
                        HashBeanResp resp = GsonUtil.jsonToObject(jsonObject.toString(), HashBeanResp.class);
                        callBack.onResponseSuccess(resp);
                    } else {
                        callBack.onResponseFailed(status, msg);
                    }
                }
            }

            @Override
            public void onFailed(int what, Response<JSONObject> response) {
                super.onFailed(what, response);
                BuglyLog.i(TAG, "getTxHashByUUid onFailed====>" + response.getException().getMessage());
                callBack.onResponseFailed(response.responseCode(), response.getException().getMessage());
            }
        });
    }

    public void reportTraceRecord(ReportRecordReq reportRecordReq, final ModelCallBack<ReportRecordResp> callBack) {

        new ReportRecordResp.Builder().addBody(reportRecordReq).buildAsync(callBack);
    }

    public void getSosoInfo(SosoInfoReq req, final ModelCallBack<SosoInfoResp> callBack) {

        new SosoInfoResp.Builder().addBody(req).buildAsync(new ModelCallBack<SosoInfoResp>() {
            @Override
            public void onResponseFailed(int errorCode, String msg) {
                callBack.onResponseFailed(errorCode, msg);
            }

            @Override
            public void onResponseSuccess(SosoInfoResp data) {
                callBack.onResponseSuccess(data);
            }
        });
    }

    public void getAdList(ListAdReq req, final ModelCallBack<ListAdResp> callBack) {

        new ListAdResp.Builder().addBody(req).buildAsync(new ModelCallBack<ListAdResp>() {
            @Override
            public void onResponseFailed(int errorCode, String msg) {
                callBack.onResponseFailed(errorCode, msg);
            }

            @Override
            public void onResponseSuccess(ListAdResp data) {
                callBack.onResponseSuccess(data);
            }
        });
    }

    public HashBean getTxHashByUUidSync(String uuid, String opAddr) {
        Map<String, Object> params = new HashMap<>();
        params.put("uuid", uuid);
        params.put("address", opAddr);
        Request<JSONObject> request = NoHttp.createJsonObjectRequest(ApiConfig.getHashByUUid(), RequestMethod.POST);
        final String json = GsonUtil.mapToJson(params);
        request.setDefineRequestBodyForJson(json);
        // 调用同步请求，直接拿到请求结果。
        Response<JSONObject> response = NoHttp.startRequestSync(request);
        if (response != null && response.isSucceed()) {
            JSONObject jsonObject = response.get();
            int status = jsonObject.optInt("status");
            String msg = jsonObject.optString("msg");
            if (status == 0) {
                HashBeanResp resp = GsonUtil.jsonToObject(jsonObject.toString(), HashBeanResp.class);
                return resp.data;
            }
        }
        return null;
    }

    public HashBeanListResp getTxHashByUUidsSync(String uuids, String opAddr) {
        Map<String, Object> params = new HashMap<>();
        params.put("uuids", uuids);
        params.put("address", opAddr);
        Request<JSONObject> request = NoHttp.createJsonObjectRequest(ApiConfig.getHashByUUids(), RequestMethod.POST);
        final String json = GsonUtil.mapToJson(params);
        request.setDefineRequestBodyForJson(json);
        // 调用同步请求，直接拿到请求结果。
        Response<JSONObject> response = NoHttp.startRequestSync(request);
        if (response != null && response.isSucceed()) {
            JSONObject jsonObject = response.get();
            int status = jsonObject.optInt("status");
            String msg = jsonObject.optString("msg");
            if (status == 0) {
                HashBeanListResp resp = GsonUtil.jsonToObject(jsonObject.toString(), HashBeanListResp.class);
                return resp;
            }
        }
        return null;
    }

    public void getProductList(Map<String, Object> params, final ModelCallBack<ProductListResp> callBack) {
        Request<JSONObject> request = NoHttp.createJsonObjectRequest(ApiConfig.getProductList(), RequestMethod.POST);
        final String json = GsonUtil.mapToJson(params);
        request.setDefineRequestBodyForJson(json);
        HttpServer.getInstance().request(0, request, new SimpleResponseListener<JSONObject>() {

            @Override
            public void onSucceed(int what, Response<JSONObject> response) {
                BuglyLog.i(TAG, "getProductList onSucceed====>" + response.get().toString());
                super.onSucceed(what, response);
                if (response.isSucceed()) {
                    JSONObject jsonObject = response.get();
                    int status = jsonObject.optInt("status");
                    String msg = jsonObject.optString("msg");
                    if (status == 0) {
                        ProductListResp resp = GsonUtil.jsonToObject(jsonObject.toString(), ProductListResp.class);
                        List<ProductOri> batchList = resp.getList();
                        if (batchList == null) {
                            resp.setList(new ArrayList<ProductOri>());
                            callBack.onResponseSuccess(resp);
                        } else {
                            callBack.onResponseSuccess(resp);
                        }

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
