package bd.com.supply.module.transaction.model.resp;

import bd.com.appcore.base.BaseListResp;
import bd.com.appcore.network.PostBuilder;
import bd.com.supply.module.transaction.SoSoConfig;
import bd.com.supply.module.transaction.model.domian.AuthBean;
import bd.com.supply.module.wallet.ApiConfig;
import bd.com.supply.module.transaction.model.domian.AuthBean;

public class AuthResp extends BaseListResp<AuthBean> {
    public static class Builder extends PostBuilder<AuthResp> {

        @Override
        public String getUrl() {
            return ApiConfig.getListAucForPage();
        }
    }
}
