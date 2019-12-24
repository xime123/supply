package bd.com.supply.module.transaction.presenter;

import android.text.TextUtils;
import android.widget.TextView;

import java.util.List;

import bd.com.appcore.base.ModelCallBack;
import bd.com.appcore.mvp.BasePresenterImpl;
import bd.com.appcore.mvp.IListView;
import bd.com.supply.module.transaction.model.SosoModel;
import bd.com.supply.module.transaction.model.domian.Produce;
import bd.com.supply.module.transaction.model.domian.SoSoAd;
import bd.com.supply.module.transaction.model.domian.TechnologyInfo;
import bd.com.supply.module.transaction.model.req.ListAdReq;
import bd.com.supply.module.transaction.model.req.SosoInfoReq;
import bd.com.supply.module.transaction.model.resp.ListAdResp;
import bd.com.supply.module.transaction.model.resp.SosoInfoResp;

public class NewSoSoPresenter extends BasePresenterImpl<NewSoSoPresenter.View> {
    private String TAG = NewSoSoPresenter.class.getSimpleName();

    public void getSosoInfo(String paddr, String address) {
        SosoInfoReq req = new SosoInfoReq();
        req.address = address;
        req.paddr = paddr;
        SosoModel.getInstance().getSosoInfo(req, new ModelCallBack<SosoInfoResp>() {
            @Override
            public void onResponseFailed(int errorCode, String msg) {
                if (mView != null) {
                    mView.sosoFailed(msg);
                }
            }

            @Override
            public void onResponseSuccess(SosoInfoResp data) {
                if (mView != null && data != null) {
                    mView.loadSuccess(data.produce);
                    if(data.produce!=null&&data.produce.size()>0){
                        Produce produce=data.produce.get(0);
                        if(!TextUtils.isEmpty(produce.isDone)){
                            mView.isDone("Y".equals(produce.isDone));
                        }
                    }
                    mView.getCategorySuccess(data.spec);
                }
            }
        });
    }

    public void getAdList() {
        ListAdReq req = new ListAdReq();
        req.setPageNumber(1);
        req.setPageSize(100);
        SosoModel.getInstance().getAdList(req, new ModelCallBack<ListAdResp>() {
            @Override
            public void onResponseFailed(int errorCode, String msg) {
                if (mView != null) {
                    mView.sosoAdFailed("获取广告信息失败");
                }
            }

            @Override
            public void onResponseSuccess(ListAdResp data) {
                if (mView != null && data != null) {
                    if(data.list!=null&&data.list.size()>0){
                        mView.sosoAdSuccess(data.list);
                    }else {
                        mView.sosoAdEmpty("暂无广告信息");
                    }
                }
            }
        });
    }


    public interface View extends IListView<Produce> {

        void getCategorySuccess(TechnologyInfo technologyInfo);

        void sosoFailed(String msg);
        void sosoAdFailed(String msg);
        void sosoAdEmpty(String msg);
        void sosoAdSuccess(List<SoSoAd> soSoAdList);

        void  isDone(boolean isDone);
    }
}
