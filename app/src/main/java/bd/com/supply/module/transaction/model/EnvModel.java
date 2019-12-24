package bd.com.supply.module.transaction.model;

import android.util.Log;

import java.util.ArrayList;
import java.util.List;

import bd.com.appcore.base.ModelCallBack;
import bd.com.appcore.util.AppSettings;
import bd.com.supply.module.transaction.model.domian.SosoEnv;
import bd.com.supply.module.transaction.model.req.EnvListReq;
import bd.com.supply.module.transaction.model.resp.EnvListResp;
import bd.com.supply.module.wallet.ApiConfig;
import bd.com.supply.module.transaction.model.domian.SosoEnv;
import bd.com.supply.module.transaction.model.req.EnvListReq;
import bd.com.supply.module.transaction.model.resp.EnvListResp;

/**
 * date:       2019/7/14
 * 最好做一个网络监听
 */

public class EnvModel {
    private List<SosoEnv> sosoEnvs = new ArrayList<>();

    private EnvModel() {
    }


    private static class SingleTon {
        private static EnvModel instance = new EnvModel();
    }

    public static EnvModel getInstance() {
        return SingleTon.instance;
    }

    public void init() {

        EnvListReq req = new EnvListReq();
        req.setAddress(AppSettings.getAppSettings().getCurrentAddress());
        req.setChainId(AppSettings.getAppSettings().getCurrentChainId());
        new EnvListResp.Builder().addBody(req).buildAsync(new ModelCallBack<EnvListResp>() {
            @Override
            public void onResponseFailed(int errorCode, String msg) {
                Log.e("EnvModel", "init error===> errorCode=" + errorCode + "     msg=" + msg);

            }

            @Override
            public void onResponseSuccess(EnvListResp data) {
                if (data != null) {
                    sosoEnvs.clear();
                    sosoEnvs.addAll(data.getList());
                }

            }
        });
    }

    public List<SosoEnv> getSosoEnvs() {
        return sosoEnvs;
    }

    public String getCurrentEnv() {
        String currentChainName = "";
        for (SosoEnv entity : sosoEnvs) {
            if (ApiConfig.getSosoBaseUrl().equals(entity.getPrefix())) {
                currentChainName = currentChainName + entity.getAlias();
                break;
            }
        }
        return currentChainName;
    }

    public void release() {
        sosoEnvs.clear();
    }
}
