package bd.com.supply.module.transaction.presenter;

import java.util.List;
import java.util.Map;

import bd.com.appcore.base.ModelCallBack;
import bd.com.appcore.mvp.BasePresenterImpl;
import bd.com.appcore.mvp.IBaseView;
import bd.com.appcore.mvp.IListView;
import bd.com.appcore.util.AppSettings;
import bd.com.supply.module.transaction.model.domian.AuthBean;
import bd.com.supply.module.transaction.model.resp.AuthResp;
import bd.com.supply.module.wallet.ApiConfig;

public class PackingPresenter extends BasePresenterImpl<PackingPresenter.View> {

    public void getAuthList( Map<String, Object> params){
        String address= AppSettings.getAppSettings().getCurrentAddress();
        params.put("address",address);
        new AuthResp.Builder().addBody(params).buildAsync(new ModelCallBack<AuthResp>() {
            @Override
            public void onResponseFailed(int errorCode, String msg) {
                if(mView!=null){
                    mView.loadFailed(msg);
                }
            }

            @Override
            public void onResponseSuccess(AuthResp data) {
                if(mView!=null){
                    mView.loadMoreSuccess(data.getList());
                }
            }
        });
    }
    public interface View extends IListView<AuthBean> {
    }
}
