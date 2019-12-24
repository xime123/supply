package bd.com.supply.module.transaction.model.resp;

import java.util.List;

import bd.com.appcore.base.BaseResp;
import bd.com.appcore.network.PostBuilder;
import bd.com.supply.module.transaction.model.domian.Produce;
import bd.com.supply.module.transaction.model.domian.TechnologyInfo;
import bd.com.supply.module.wallet.ApiConfig;
import bd.com.supply.module.transaction.model.domian.Produce;
import bd.com.supply.module.transaction.model.domian.TechnologyInfo;

public class SosoInfoResp extends BaseResp {
    public List<Produce> produce;
    public TechnologyInfo spec;

    public static class Builder extends PostBuilder<SosoInfoResp> {

        @Override
        public String getUrl() {
            return ApiConfig.tracingSrc();
        }

    }
}
