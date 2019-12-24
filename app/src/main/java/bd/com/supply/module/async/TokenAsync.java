package bd.com.supply.module.async;

import android.util.Log;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import bd.com.appcore.base.ModelCallBack;
import bd.com.appcore.rx.LogicException;
import bd.com.appcore.rx.RxTaskCallBack;
import bd.com.appcore.rx.RxTaskScheduler;
import bd.com.supply.module.common.GlobConfig;
import bd.com.supply.module.wallet.model.TokenModel;
import bd.com.supply.module.wallet.model.domain.TokenListListResp;
import bd.com.supply.util.FakeDataHelper;
import bd.com.supply.module.wallet.model.TokenModel;
import bd.com.supply.module.wallet.model.domain.TokenListListResp;
import bd.com.walletdb.entity.TokenEntity;
import bd.com.walletdb.manager.TokenManager;

/**
 * token同步
 */
public class TokenAsync {
    private String walletAddr;
    private String chainId;
    private List<AsyncTokenListener> listeners = new ArrayList<>();

    public TokenAsync(String walletAddr, String chainId) {
        this.walletAddr = walletAddr;
        this.chainId = chainId;
    }

    public void asyncTokenList() {
        Map<String, Object> params = new HashMap<>();
        params.put("pageNumber", 1);
        params.put("pageSize", 100);
        TokenModel.getModel().getTokenList(params, new ModelCallBack<TokenListListResp>() {
            @Override
            public void onResponseFailed(int errorCode, String msg) {
                Log.e("TokenAsync", "errorCode=" + errorCode + "   msg=" + msg);
                notifyAsyncListener(null);
            }

            @Override
            public void onResponseSuccess(TokenListListResp data) {
                processResp(data);
            }
        });
    }

    private void processResp(final TokenListListResp data) {
        RxTaskScheduler.postIoMainTask(new RxTaskCallBack<List<TokenEntity>>() {
            @Override
            public List<TokenEntity> doWork() throws Exception {
                List<TokenEntity> dbTokenList = TokenManager.getManager().getAll();
                if (data != null && data.getList() != null && data.getList().size() > 0) {

                    List<TokenEntity> tokenEntities = data.getList();
                    for (TokenEntity entity : tokenEntities) {
                        entity.setChainId(chainId);
                        entity.setWalletAddress(walletAddr);
                        for(TokenEntity dbEntity:dbTokenList){
                            if (entity.getWalletAddress().equals(dbEntity.getWalletAddress())&&entity.getAddress().equals(dbEntity.getAddress())) {
                                entity.setBalance(dbEntity.getBalance());
                                entity.setChecked(dbEntity.getChecked());
                                entity.setValue(dbEntity.getValue());
                                entity.setPrice(dbEntity.getPrice());
                                break;
                            }
                        }
                        //测试链v3全部显示
                        if (GlobConfig.SCT_02_CHAIN_ID.equals(chainId)) {
                            entity.setChecked(true);
                        }
                        //主链的sct要显示
                        if (GlobConfig.ETH_CHAIN_ID.equals(chainId)) {
                            if (entity.getSymbol().equals(FakeDataHelper.MAIN_SCT_SYMBOL)) {
                                entity.setChecked(true);
                            }
                        }
                    }
                    TokenManager.getManager().insertTokenList(tokenEntities);
                    return tokenEntities;
                }
                return dbTokenList;
            }

            @Override
            public void onSuccess(List<TokenEntity> tokenEntityList) {
                super.onSuccess(tokenEntityList);
                notifyAsyncListener(tokenEntityList);
            }

            @Override
            public void onFailed(LogicException e) {
                super.onFailed(e);
                Log.e("TokenAsync", " asyncTokenList error " + e.getErrorMsg());
                notifyAsyncListener(null);
            }
        });

    }

    public interface AsyncTokenListener {
        void onFinished(List<TokenEntity> entityList);
    }

    public void addListener(AsyncTokenListener listener) {
        if (!listeners.contains(listener)) {
            listeners.add(listener);
        }
    }

    public void notifyAsyncListener(List<TokenEntity> entityList) {
        if (listeners.size() <= 0) return;
        for (AsyncTokenListener listener : listeners) {
            listener.onFinished(entityList);
        }
        listeners.clear();
    }

    public String getChainId() {
        return chainId;
    }
}
