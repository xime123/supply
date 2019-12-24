package bd.com.supply.module.transaction.presenter;

import java.util.List;

import bd.com.supply.module.transaction.model.domian.ArchivesBean;
import bd.com.supply.module.transaction.model.domian.ArchivesBean;


public class ArchivesResp {
    private String opAddress;
    private List<ArchivesBean> archivesBeans;

    public String getOpAddress() {
        return opAddress;
    }

    public void setOpAddress(String opAddress) {
        this.opAddress = opAddress;
    }

    public List<ArchivesBean> getArchivesBeans() {
        return archivesBeans;
    }

    public void setArchivesBeans(List<ArchivesBean> archivesBeans) {
        this.archivesBeans = archivesBeans;
    }
}
