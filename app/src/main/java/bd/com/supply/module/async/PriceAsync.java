package bd.com.supply.module.async;

import android.util.Log;

import java.util.ArrayList;
import java.util.List;

import bd.com.appcore.base.ModelCallBack;
import bd.com.supply.module.transaction.model.domian.PriceResponse;
import bd.com.supply.util.FakeDataHelper;
import bd.com.supply.module.transaction.model.domian.PriceResponse;
import bd.com.walletdb.entity.Price;
import bd.com.walletdb.entity.TokenEntity;
import bd.com.walletdb.manager.PriceManager;

/**
 * 价格同步
 */
public class PriceAsync {
    private String chainId;
    private List<TokenEntity> tokenEntityList;
    private List<AsyncPriceListener> listeners = new ArrayList<>();

    public PriceAsync(String chainId, List<TokenEntity> tokenEntityList) {
        this.chainId = chainId;
        this.tokenEntityList = tokenEntityList;
    }

    public void asyncPrice() {
        AsyncPriceReq asyncPriceReq = transfer(this.tokenEntityList);
        new PriceResponse.Builder().addBody(asyncPriceReq).buildAsync(new ModelCallBack<PriceResponse>() {
            @Override
            public void onResponseFailed(int errorCode, String msg) {
                notifyAsyncListener(null);
                Log.e("asyncPrice", "errorCode=" + errorCode + "   msg=" + msg);
            }

            @Override
            public void onResponseSuccess(PriceResponse data) {
                if (data != null) {
                    List<Price> priceList = data.getList();
                    if (priceList != null && priceList.size() > 0) {
                        for (Price price : priceList) {
                            price.setChainId(chainId);
                        }
                        PriceManager.getManager().insertPriceList(priceList);
                    }
                    notifyAsyncListener(priceList);
                } else {
                    notifyAsyncListener(null);
                }
            }
        });
    }

    public AsyncPriceReq transfer(List<TokenEntity> entityList) {
        AsyncPriceReq req = new AsyncPriceReq();
        List<TokenInfo> tokenInfoList = new ArrayList<>();
        if (entityList != null && entityList.size() > 0) {
            for (TokenEntity entity : entityList) {
                TokenInfo tokenInfo = new TokenInfo();
                tokenInfo.setAddress(entity.getAddress());
                tokenInfo.setSymbol(entity.getSymbol());
                tokenInfo.setUnit("CNY");
                tokenInfoList.add(tokenInfo);
            }
        }
        //再加一个主币查询
        TokenInfo info = new TokenInfo();
        info.setAddress(FakeDataHelper.getEthTokenAddr());
        info.setSymbol(FakeDataHelper.MAIN_ETH_SYMBOL);
        info.setUnit("CNY");
        tokenInfoList.add(info);
        req.tokenInfo = tokenInfoList;
        return req;
    }

    public interface AsyncPriceListener {
        void onFinished(List<Price> priceList);
    }

    public void addListener(AsyncPriceListener listener) {
        if (!listeners.contains(listener)) {
            listeners.add(listener);
        }
    }

    public void notifyAsyncListener(List<Price> priceList) {
        if (listeners.size() <= 0) return;
        for (AsyncPriceListener listener : listeners) {
            listener.onFinished(priceList);
        }
        listeners.clear();
    }

    class AsyncPriceReq {
        List<TokenInfo> tokenInfo;
    }

    class TokenInfo {
        private String unit;
        private String symbol;
        private String address;

        public String getUnit() {
            return unit;
        }

        public void setUnit(String unit) {
            this.unit = unit;
        }

        public String getSymbol() {
            return symbol;
        }

        public void setSymbol(String symbol) {
            this.symbol = symbol;
        }

        public String getAddress() {
            return address;
        }

        public void setAddress(String address) {
            this.address = address;
        }
    }
}
