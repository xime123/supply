package bd.com.supply.module.transaction.presenter;

import java.util.List;
import java.util.Map;

import bd.com.appcore.base.ModelCallBack;
import bd.com.appcore.mvp.BasePresenterImpl;
import bd.com.appcore.mvp.IListView;
import bd.com.appcore.util.AppSettings;
import bd.com.supply.module.transaction.model.SosoModel;
import bd.com.supply.module.transaction.model.domian.Batch;
import bd.com.supply.module.transaction.model.domian.BatchListResp;

public class CategoryPresenter extends BasePresenterImpl<CategoryPresenter.View> {
    public void getBatchList(Map<String, Object> params) {
        String address = AppSettings.getAppSettings().getCurrentAddress();
        params.put("address", address);
        SosoModel.getInstance().getBatchList(params, new ModelCallBack<BatchListResp>() {
            @Override
            public void onResponseFailed(int errorCode, String msg) {
                if (mView != null) {
                    mView.loadFailed(msg);
                }
            }

            @Override
            public void onResponseSuccess(BatchListResp data) {
                if (mView != null) {
                    List<Batch> batchList = data.getList();
                    mView.loadSuccess(batchList);
                }
            }
        });
    }

    public interface View extends IListView<Batch> {

    }
}
