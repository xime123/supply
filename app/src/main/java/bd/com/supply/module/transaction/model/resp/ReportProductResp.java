package bd.com.supply.module.transaction.model.resp;

import bd.com.appcore.base.BaseResp;
import bd.com.appcore.network.PostBuilder;
import bd.com.supply.module.wallet.ApiConfig;

public class ReportProductResp extends BaseResp {
    public static class Builder extends PostBuilder<ReportProductResp>{

        @Override
        public String getUrl() {
            return ApiConfig.getReportProductForBox();
        }
    }
}
