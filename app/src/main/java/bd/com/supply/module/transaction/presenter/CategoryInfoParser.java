package bd.com.supply.module.transaction.presenter;

import android.text.TextUtils;

import org.w3c.dom.Text;

import java.util.ArrayList;
import java.util.List;

import bd.com.appcore.util.GsonUtil;
import bd.com.supply.module.transaction.SoSoConfig;
import bd.com.supply.module.transaction.model.domian.CategoryInfo;
import bd.com.supply.module.transaction.model.domian.CategoryInfoResp;
import bd.com.supply.module.transaction.model.domian.TechnologyInfo;

public class CategoryInfoParser {
    public static CategoryInfoResp parserCategoryInfo(String c_id, String c_name, String c_infos) {
        CategoryInfoResp categoryInfoResp = new CategoryInfoResp();
        categoryInfoResp.setCategoryId(c_id);
        categoryInfoResp.setName(c_name);
        List<CategoryInfo> categoryInfoList=new ArrayList<>();
        categoryInfoResp.setCategoryInfoList(categoryInfoList);
        if (!TextUtils.isEmpty(c_infos)) {
            if (c_infos.contains(SoSoConfig.PEROID_GAP)) {
                String infos[] = c_infos.split(SoSoConfig.PEROID_GAP);
                for (int i = 0; i < infos.length; i++) {
                    CategoryInfo info = getInfoByContent(infos[i]);
                    categoryInfoResp.getCategoryInfoList().add(info);
                }
            } else {
                CategoryInfo info = getInfoByContent(c_infos);
                categoryInfoResp.getCategoryInfoList().add(info);
            }
        }
        return categoryInfoResp;
    }

    private static CategoryInfo getInfoByContent(String c_info) {
        CategoryInfo categoryInfo = new CategoryInfo();
        if (!TextUtils.isEmpty(c_info) && c_info.contains(SoSoConfig.CONTENT_GAP)) {
            String items[] = c_info.split(SoSoConfig.CONTENT_GAP);
            if (items.length >= 4) {
                categoryInfo.setOpAddress(items[0]);
                categoryInfo.setOpName(items[1]);
                TechnologyInfo technologyInfo = getTechnologyInfo(items[2]);
                categoryInfo.setTechnologyInfo(technologyInfo);
                categoryInfo.setMaterial(items[3]);
            }
        }
        return categoryInfo;
    }

    private static TechnologyInfo getTechnologyInfo(String item) {
        TechnologyInfo technologyInfo = GsonUtil.jsonToObject(item, TechnologyInfo.class);
        return technologyInfo;
    }
}
