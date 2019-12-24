package bd.com.supply.module.transaction.model.domian;





public class SosoEnv {
    private boolean isSelected;
    private String alias;
    private String prefix;//环境地址前缀
    private String desc;
    private String chainId;
    private String status;
    private int order;
    private String resPrefix;
    private String isShow;
    public SosoEnv(String alias, String prefix, String desc, String chainId,
                   String status, int order, String resPrefix, String isShow) {
        this.alias = alias;
        this.prefix = prefix;
        this.desc = desc;
        this.chainId = chainId;
        this.status = status;
        this.order = order;
        this.resPrefix = resPrefix;
        this.isShow = isShow;
    }
    public SosoEnv() {
    }
    public String getAlias() {
        return this.alias;
    }
    public void setAlias(String alias) {
        this.alias = alias;
    }
    public String getPrefix() {
        return this.prefix;
    }
    public void setPrefix(String prefix) {
        this.prefix = prefix;
    }
    public String getDesc() {
        return this.desc;
    }
    public void setDesc(String desc) {
        this.desc = desc;
    }
    public String getChainId() {
        return this.chainId;
    }
    public void setChainId(String chainId) {
        this.chainId = chainId;
    }
    public String getStatus() {
        return this.status;
    }
    public void setStatus(String status) {
        this.status = status;
    }
    public int getOrder() {
        return this.order;
    }
    public void setOrder(int order) {
        this.order = order;
    }
    public String getResPrefix() {
        return this.resPrefix;
    }
    public void setResPrefix(String resPrefix) {
        this.resPrefix = resPrefix;
    }
    public String getIsShow() {
        return this.isShow;
    }
    public void setIsShow(String isShow) {
        this.isShow = isShow;
    }

    public boolean isSelected() {
        return isSelected;
    }

    public void setSelected(boolean selected) {
        isSelected = selected;
    }
}
