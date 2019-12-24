package bd.com.supply.module.transaction.presenter;

import android.text.TextUtils;

import java.util.ArrayList;
import java.util.List;

import bd.com.appcore.util.GsonUtil;
import bd.com.supply.module.transaction.SoSoConfig;
import bd.com.supply.module.transaction.model.domian.ProductBean;

public class ProductParser {
    public static List<ProductBean> getProductBeans(String prodInfo) {
        List<ProductBean> productBeans = new ArrayList<>();
        if (!TextUtils.isEmpty(prodInfo)) {
            if (prodInfo.contains(SoSoConfig.PEROID_GAP)) {
                String prods[] = prodInfo.split(SoSoConfig.PEROID_GAP);
                for (int i = 0; i < prods.length; i++) {
                    List<ProductBean> productBeanList = getProductBeanList(prods[i]);
                    productBeans.addAll(productBeanList);
                }
            } else {
                List<ProductBean> productBeanList = getProductBeanList(prodInfo);
                productBeans.addAll(productBeanList);
            }
        }
        return productBeans;
    }

    private static List<ProductBean> getProductBeanList(String prod) {
        List<ProductBean> productBeanList = new ArrayList<>();
        if (prod.contains(SoSoConfig.CONTENT_GAP)) {
            String items[] = prod.split(SoSoConfig.CONTENT_GAP);
            for (int i = 0; i < items.length; i++) {
                ProductBean productBean = getProductBean(items[1]);
                productBean.setBlockNum(items[2]);
            }
        }
        return productBeanList;
    }

    private static ProductBean getProductBean(String item) {
        ProductBean productBean = GsonUtil.jsonToObject(item, ProductBean.class);
        return productBean;

    }
}
