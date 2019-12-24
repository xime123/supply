package bd.com.supply.module.common;


public class UpdateInfo {
    private String isNew;
    private String isForceUpgrade;
    private String downloadUrl;
    private String packageSize;
    private String displayVer;
    private String description;
    private String checksum;

    public String getIsNew() {
        return isNew;
    }

    public void setIsNew(String isNew) {
        this.isNew = isNew;
    }

    public String getIsForceUpgrade() {
        return isForceUpgrade;
    }

    public void setIsForceUpgrade(String isForceUpgrade) {
        this.isForceUpgrade = isForceUpgrade;
    }

    public String getDownloadUrl() {
        return downloadUrl;
    }

    public void setDownloadUrl(String downloadUrl) {
        this.downloadUrl = downloadUrl;
    }

    public String getPackageSize() {
        return packageSize;
    }

    public void setPackageSize(String packageSize) {
        this.packageSize = packageSize;
    }

    public String getDisplayVer() {
        return displayVer;
    }

    public void setDisplayVer(String displayVer) {
        this.displayVer = displayVer;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getChecksum() {
        return checksum;
    }

    public void setChecksum(String checksum) {
        this.checksum = checksum;
    }
}
