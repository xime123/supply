package bd.com.supply.module.wallet.presenter;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import bd.com.appcore.base.ModelCallBack;
import bd.com.appcore.mvp.BasePresenterImpl;
import bd.com.appcore.mvp.IListView;
import bd.com.supply.module.wallet.model.TokenModel;
import bd.com.supply.util.FakeDataHelper;
import bd.com.walletdb.entity.TokenEntity;
import bd.com.walletdb.manager.TokenManager;


public class TokenPresenter extends BasePresenterImpl<TokenPresenter.ITokenView> {


    public void getTokenList(Map<String, Object> params) {
        TokenModel.getModel().getCurrentTokenList(new ModelCallBack<List<TokenEntity>>() {
            @Override
            public void onResponseFailed(int errorCode, String msg) {
                if (mView != null) {
                    //fakedata
                    // mView.loadSuccess(getFakeDatas());
                    mView.loadFailed(msg);
                }
            }

            @Override
            public void onResponseSuccess(List<TokenEntity> data) {
                if (mView != null) {
                    if (data != null && data.size() > 0) {
                        List<TokenEntity> normalData = new ArrayList<>();
                        for (TokenEntity tokenEntity : data) {
                            if (!tokenEntity.getAddress().startsWith(FakeDataHelper.ETH_TOKEN_ADDR_PRE) && !tokenEntity.getAddress().startsWith(FakeDataHelper.SCT_TOKEN_ADDR_PRE)) {
                                normalData.add(tokenEntity);
                            }
                        }
                        mView.loadSuccess(normalData);
                    } else {
                        mView.loadEmpty();
                    }
                }
            }
        });
    }



    public void updateToken(TokenEntity tokenEntity) {
        TokenManager.getManager().insertToken(tokenEntity);
    }

    public interface ITokenView extends IListView<TokenEntity> {

    }
}
