package bd.com.supply.module.transaction.model.domian;

public class CategoryInfo {
    private String opAddress;
    private String opName;
    private TechnologyInfo technologyInfo;
    private String material;

    public String getOpAddress() {
        return opAddress;
    }

    public void setOpAddress(String opAddress) {
        this.opAddress = opAddress;
    }

    public String getOpName() {
        return opName;
    }

    public void setOpName(String opName) {
        this.opName = opName;
    }

    public TechnologyInfo getTechnologyInfo() {
        return technologyInfo;
    }

    public void setTechnologyInfo(TechnologyInfo technologyInfo) {
        this.technologyInfo = technologyInfo;
    }

    public String getMaterial() {
        return material;
    }

    public void setMaterial(String material) {
        this.material = material;
    }
}
