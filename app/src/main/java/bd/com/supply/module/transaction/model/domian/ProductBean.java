package bd.com.supply.module.transaction.model.domian;

public class ProductBean {
    private String title;
    private String time;
    private String perio;
    private String opername;
    private String opcontent;
    private String uuid;
    private String blockNum;
    private String opAddr;
    private String docaddr;//档案合约地址
    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getPerio() {
        return perio;
    }

    public void setPerio(String perio) {
        this.perio = perio;
    }

    public String getOpername() {
        return opername;
    }

    public void setOpername(String opername) {
        this.opername = opername;
    }

    public String getTime() {
        return time;
    }

    public void setTime(String time) {
        this.time = time;
    }

    public String getOpcontent() {
        return opcontent;
    }

    public void setOpcontent(String opcontent) {
        this.opcontent = opcontent;
    }

    public String getUuid() {
        return uuid;
    }

    public void setUuid(String uuid) {
        this.uuid = uuid;
    }

    public String getBlockNum() {
        return blockNum;
    }

    public void setBlockNum(String blockNum) {
        this.blockNum = blockNum;
    }

    public String getOpAddr() {
        return opAddr;
    }

    public void setOpAddr(String opAddr) {
        this.opAddr = opAddr;
    }

    public String getDocaddr() {
        return docaddr;
    }

    public void setDocaddr(String docaddr) {
        this.docaddr = docaddr;
    }
}
