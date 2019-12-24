package bd.com.supply.module.transaction.model.resp;

import bd.com.appcore.base.BaseListResp;
import bd.com.appcore.network.PostBuilder;
import bd.com.supply.module.transaction.model.domian.SosoEnv;
import bd.com.supply.module.wallet.ApiConfig;
import bd.com.supply.module.transaction.model.domian.SosoEnv;

/**
 * author:     xumin
 * date:       2019/7/14
 * email:      xumin2@evergrande.cn
 */
public class EnvListResp extends BaseListResp<SosoEnv> {
    public static class Builder extends PostBuilder<EnvListResp> {

        @Override
        public String getUrl() {
            return ApiConfig.getListIndexUrl();
        }

    }
}
