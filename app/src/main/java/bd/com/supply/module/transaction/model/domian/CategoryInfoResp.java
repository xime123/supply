package bd.com.supply.module.transaction.model.domian;

import java.util.List;

public class CategoryInfoResp {
    private String name;
    private String categoryId;
    private List<CategoryInfo> categoryInfoList;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getCategoryId() {
        return categoryId;
    }

    public void setCategoryId(String categoryId) {
        this.categoryId = categoryId;
    }

    public List<CategoryInfo> getCategoryInfoList() {
        return categoryInfoList;
    }

    public void setCategoryInfoList(List<CategoryInfo> categoryInfoList) {
        this.categoryInfoList = categoryInfoList;
    }
}
