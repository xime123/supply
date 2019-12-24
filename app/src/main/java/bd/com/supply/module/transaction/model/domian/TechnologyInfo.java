package bd.com.supply.module.transaction.model.domian;

/**
 * 0xxxxxxx&李四&{"bn":"品名","spec":"规格","ssm":"香型","nm":"名称","addr":"地址","ph":"电话","plc":"生产许可编码","sm":"存储方式","pd":"生产日期"}
 */
public class TechnologyInfo {
    private String bn;
    private String spec;
    private String ssm;
    private String nm;
    private String addr;
    private String ph;
    private String plc;
    private String sm;
    private String pd;
    private String bgi;
    private String mat;
    public String getBn() {
        return bn;
    }

    public void setBn(String bn) {
        this.bn = bn;
    }

    public String getSpec() {
        return spec;
    }

    public void setSpec(String spec) {
        this.spec = spec;
    }

    public String getSsm() {
        return ssm;
    }

    public void setSsm(String ssm) {
        this.ssm = ssm;
    }

    public String getNm() {
        return nm;
    }

    public void setNm(String nm) {
        this.nm = nm;
    }

    public String getAddr() {
        return addr;
    }

    public void setAddr(String addr) {
        this.addr = addr;
    }

    public String getPh() {
        return ph;
    }

    public void setPh(String ph) {
        this.ph = ph;
    }

    public String getSm() {
        return sm;
    }

    public void setSm(String sm) {
        this.sm = sm;
    }

    public String getPd() {
        return pd;
    }

    public void setPd(String pd) {
        this.pd = pd;
    }

    public String getPlc() {
        return plc;
    }

    public void setPlc(String plc) {
        this.plc = plc;
    }

    public String getBgi() {
        return bgi;
    }

    public void setBgi(String bgi) {
        this.bgi = bgi;
    }

    public String getMat() {
        return mat;
    }

    public void setMat(String mat) {
        this.mat = mat;
    }
}
