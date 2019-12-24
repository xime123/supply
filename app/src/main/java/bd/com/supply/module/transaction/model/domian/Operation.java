package bd.com.supply.module.transaction.model.domian;

import android.os.Parcel;
import android.os.Parcelable;

import java.util.List;

/**
 * {地址01}@{_detailInfo01}@{区块高度}@{周期阶段0}
 * 0xcc77b33f6269eee6d85a729b614d3d6b1bfc98b3@{"docaddr":"0x4f074af5a1ef3b0718a475b0e5acd49358db7333","perio":"0","time":"2019-05-29","title":"厂家出厂","uuid":"b1e58a86770fd301aa3c7f65fa365216ee242de86c5d3790396cf85e8858dea3","opcontent":"出厂了"}@2940@0
 */
public class Operation {
    private String opAddress;
    private ProductBean productBean;
    private String blockNum;
    private String perio;
    private ArchivesBean archivesBean;

    public String getOpAddress() {
        return opAddress;
    }

    public void setOpAddress(String opAddress) {
        this.opAddress = opAddress;
    }

    public ProductBean getProductBean() {
        return productBean;
    }

    public void setProductBean(ProductBean productBean) {
        this.productBean = productBean;
    }

    public String getBlockNum() {
        return blockNum;
    }

    public void setBlockNum(String blockNum) {
        this.blockNum = blockNum;
    }

    public String getPerio() {
        return perio;
    }

    public void setPerio(String perio) {
        this.perio = perio;
    }

    public ArchivesBean getArchivesBean() {
        return archivesBean;
    }

    public void setArchivesBean(ArchivesBean archivesBean) {
        this.archivesBean = archivesBean;
    }
}
