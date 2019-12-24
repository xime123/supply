package bd.com.supply.module.transaction.model.domian;

import bd.com.appcore.base.BaseListResp;
import bd.com.appcore.network.PostBuilder;
import bd.com.supply.module.wallet.ApiConfig;
import bd.com.walletdb.entity.Price;

public class PriceResponse extends BaseListResp<Price> {
    public static class Builder extends PostBuilder<PriceResponse> {

        @Override
        public String getUrl() {
            return ApiConfig.getPriceListByMarketName();
        }
    }
}
