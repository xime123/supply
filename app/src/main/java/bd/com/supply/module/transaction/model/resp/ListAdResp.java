package bd.com.supply.module.transaction.model.resp;

import java.util.List;

import bd.com.appcore.base.BaseResp;
import bd.com.appcore.network.PostBuilder;
import bd.com.supply.module.transaction.model.domian.SoSoAd;
import bd.com.supply.module.wallet.ApiConfig;
import bd.com.supply.module.transaction.model.domian.SoSoAd;

public class ListAdResp extends BaseResp {
    public List<SoSoAd> list;

    public static class Builder extends PostBuilder<ListAdResp> {

        @Override
        public String getUrl() {
            return ApiConfig.listAd();
        }

    }
}
