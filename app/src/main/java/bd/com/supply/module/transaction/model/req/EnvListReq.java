package bd.com.supply.module.transaction.model.req;

/**
 * author:     xumin
 * date:       2019/7/14
 * email:      xumin2@evergrande.cn
 */
public class EnvListReq {
    private String address;
    private String chainId;

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public String getChainId() {
        return chainId;
    }

    public void setChainId(String chainId) {
        this.chainId = chainId;
    }
}
