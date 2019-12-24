package bd.com.supply.module.user.presenter;

import android.text.TextUtils;
import android.util.Log;

import org.web3j.crypto.Credentials;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import bd.com.appcore.base.ModelCallBack;
import bd.com.appcore.mvp.BasePresenterImpl;
import bd.com.appcore.mvp.IBaseView;
import bd.com.appcore.rx.LogicException;
import bd.com.appcore.rx.RxTaskCallBack;
import bd.com.appcore.rx.RxTaskScheduler;
import bd.com.appcore.util.AppSettings;
import bd.com.supply.module.common.GlobConfig;
import bd.com.supply.module.wallet.model.TokenModel;
import bd.com.supply.module.wallet.model.domain.ChainListListResp;
import bd.com.supply.module.wallet.model.TokenModel;
import bd.com.supply.module.wallet.model.domain.ChainListListResp;
import bd.com.walletdb.entity.ChainEntity;
import bd.com.supply.web3.Web3Proxy;
import bd.com.walletdb.entity.TokenEntity;
import bd.com.walletdb.entity.WalletEntity;
import bd.com.walletdb.manager.ChainManager;
import bd.com.walletdb.manager.TokenManager;
import bd.com.walletdb.manager.WalletDBManager;
import io.reactivex.disposables.Disposable;

public class PublishTokenPresenter extends BasePresenterImpl<PublishTokenPresenter.View> {
    private String decimal = "000000000000000000";

    public void publishToken(final String name, final String symbol, final String initAmount, final String publisher, final String icon) {
        RxTaskScheduler.postIoMainTask(new RxTaskCallBack<String>() {
            @Override
            public String doWork() throws Exception {
                final String cuurentAddress = AppSettings.getAppSettings().getCurrentAddress();
                WalletEntity entity = WalletDBManager.getManager().getWalletEntity(cuurentAddress);
                final Credentials credentials = org.web3j.crypto.Credentials.create(entity.getPrivateKey());
                String contractAddr = Web3Proxy.getWeb3Proxy().deploy(credentials, initAmount + decimal, name, symbol);
                if (!TextUtils.isEmpty(contractAddr)) {
                    Log.i("PublishTokenPresenter", "publishToken contractAddr==============>" + contractAddr);
                    save2Db(name, symbol, initAmount, publisher, icon, contractAddr);
                }
                return contractAddr;
            }

            @Override
            public void onSuccess(String s) {
                super.onSuccess(s);
                if (mView != null) {
                    mView.hideLoadingDialog();
                    mView.onPublishSuccess();
                }
            }

            @Override
            public void onFailed(LogicException e) {
                super.onFailed(e);
                Log.i("PublishTokenPresenter", "publishToken onFailed==============>" + e.getErrorMsg());
                if (mView != null) {
                    mView.hideLoadingDialog();
                    mView.onPublishFailed();
                }
            }

            @Override
            public void onStart(Disposable disposable) {
                super.onStart(disposable);
                mView.showLoadingDialog();
            }
        });

    }

    private void save2Db(String name, String symbol, String initAmount, String publisher, String icon, String contractAddr) {
        TokenEntity tokenEntity = new TokenEntity();
        tokenEntity.setAddress(contractAddr);
        tokenEntity.setIcon(icon);
        tokenEntity.setName(name);
        tokenEntity.setWalletAddress(AppSettings.getAppSettings().getCurrentAddress());
        tokenEntity.setSymbol(symbol);
        tokenEntity.setVersion("1.0");
        tokenEntity.setDecimals(18);
        tokenEntity.setChecked(true);
        tokenEntity.setPublisher(publisher);
        tokenEntity.setSupplyTotal(initAmount);
        tokenEntity.setChainId(GlobConfig.getCurrentChainId());
        TokenManager.getManager().insertToken(tokenEntity);
    }


    public void getChainList() {
        Map<String, Object> params = new HashMap<>();
        params.put("pageNumber", 1);
        params.put("pageSize", 100);
        mView.showLoadingDialog();
        TokenModel.getModel().getChainList(params, new ModelCallBack<ChainListListResp>() {
            @Override
            public void onResponseFailed(int errorCode, String msg) {
                if (mView != null) {
                    mView.hideLoadingDialog();
                    //获取失败从数据库拿缓存
                    List<ChainEntity> chainEntityList = ChainManager.getManager().getChainList();
                    if (chainEntityList != null && !chainEntityList.isEmpty()) {
                        mView.onGetChainListSuccess(chainEntityList);
                        return;
                    }
                    mView.onGetChainListFailed(msg);
                }
            }

            @Override
            public void onResponseSuccess(ChainListListResp data) {
                if (mView != null) {
                    mView.hideLoadingDialog();
                    List<ChainEntity> remoteDatas = data.getList();
                    mView.onGetChainListSuccess(remoteDatas);
                }
            }
        });
    }


    public interface View extends IBaseView {
        void onPublishSuccess();

        void onPublishFailed();

        void onGetChainListSuccess(List<ChainEntity> chainEntityList);

        void onGetChainListFailed(String msg);
    }
}
