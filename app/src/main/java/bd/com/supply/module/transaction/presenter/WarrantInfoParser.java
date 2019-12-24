package bd.com.supply.module.transaction.presenter;

import android.text.TextUtils;

import bd.com.appcore.util.GsonUtil;
import bd.com.supply.module.transaction.SoSoConfig;
import bd.com.supply.module.transaction.model.domian.ProductBean;

public class WarrantInfoParser {
    public static ProductBean getProductBean(String warrantInfo, String uuid) {
        ProductBean productBean = new ProductBean();
        productBean.setUuid(uuid);
        if (!TextUtils.isEmpty(warrantInfo)) {
            if (warrantInfo.contains(SoSoConfig.PEROID_GAP)) {
                String itemWraps[] = warrantInfo.split(SoSoConfig.PEROID_GAP);
                parse(productBean, itemWraps[0]);
            } else {
                parse(productBean, warrantInfo);
            }
        }
        return productBean;
    }

    /**
     * 已授权用户列表字符串，遵循特定格式：{地址}@{周期}@{操作内容}
     * 0x005f7d99567b9072d233f240a032f0b5526c6cb5@0@种植
     *
     * @param productBean
     * @param content
     */
    private static void parse(ProductBean productBean, String content) {
        if (content.contains(SoSoConfig.ADDR_GAP)) {
            String items[] = content.split(SoSoConfig.ADDR_GAP);
            if (items.length >= 4) {
                productBean.setOpAddr(items[0]);
                productBean.setPerio(items[1]);
                productBean.setOpcontent(items[2]);
                productBean.setDocaddr(items[3]);
            }
        }
    }
}
