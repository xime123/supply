package bd.com.supply.module.transaction.model.resp;

import bd.com.appcore.base.BaseResp;
import bd.com.appcore.network.PostBuilder;
import bd.com.supply.module.wallet.ApiConfig;

public class ReportRecordResp extends BaseResp {
    public static class Builder extends PostBuilder<ReportRecordResp> {

        @Override
        public String getUrl() {
            return ApiConfig.reportTraceRecord();
        }
    }
}
