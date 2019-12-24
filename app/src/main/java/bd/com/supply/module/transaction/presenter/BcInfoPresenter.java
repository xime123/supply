package bd.com.supply.module.transaction.presenter;

import java.math.BigInteger;
import java.util.List;

import bd.com.appcore.base.ModelCallBack;
import bd.com.appcore.mvp.BasePresenterImpl;
import bd.com.appcore.mvp.IBaseView;
import bd.com.appcore.rx.LogicException;
import bd.com.appcore.rx.RxTaskCallBack;
import bd.com.appcore.rx.RxTaskScheduler;
import bd.com.supply.module.transaction.model.SosoModel;
import bd.com.supply.module.transaction.model.TransactionModel;
import bd.com.supply.module.transaction.model.domian.HashBeanResp;
import bd.com.supply.module.transaction.model.domian.ProductBean;
import bd.com.supply.web3.Web3Proxy;
import bd.com.supply.web3.contract.Product;

public class BcInfoPresenter extends BasePresenterImpl<BcInfoPresenter.View> {

    public void getTxHashByUUid(String uuid,String opAddr){
        mView.showLoadingDialog();
        SosoModel.getInstance().getTxHashByUUid(uuid, opAddr, new ModelCallBack<HashBeanResp>() {
            @Override
            public void onResponseFailed(int errorCode, String msg) {
                if(mView!=null){
                    mView.onFailed(msg);
                    mView.hideLoadingDialog();
                }
            }

            @Override
            public void onResponseSuccess(HashBeanResp data) {
                if(mView!=null){
                    if(data!=null){
                        mView.onSuccess(data.data.getTxhash());
                    }else {
                        mView.onFailed("data 为null");
                    }
                    mView.hideLoadingDialog();
                }
            }
        });
    }

    public interface View extends IBaseView {
        void onSuccess(String hash);

        void onFailed(String msg);

    }
}
