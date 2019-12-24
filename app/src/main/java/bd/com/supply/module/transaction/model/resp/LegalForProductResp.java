package bd.com.supply.module.transaction.model.resp;

import java.util.List;

import bd.com.appcore.base.BaseResp;
import bd.com.appcore.network.PostBuilder;
import bd.com.supply.module.wallet.ApiConfig;

public class LegalForProductResp extends BaseResp {
    public String isLegal;// 1 合法 0 非法
    public List<String> plist;//不满足条件的包装盒地址列表

    public static class Builder extends PostBuilder<LegalForProductResp> {

        @Override
        public String getUrl() {
            return ApiConfig.isLegalFoProduct();
        }
    }
}
