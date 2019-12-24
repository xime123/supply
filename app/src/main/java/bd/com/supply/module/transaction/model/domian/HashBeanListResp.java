package bd.com.supply.module.transaction.model.domian;

import java.util.List;

/**
 * author:     labixiaoxin
 * date:       2018/6/30
 * email:      labixiaoxin2@qq.cn
 */
public class HashBeanListResp {
    private int status;
    private String msg;
    private int total;
    private int pageTotal;
    private int pageSize;
    private int pageNumber;
    private int start;
    private List<HashBean> data;

    public int getStatus() {
        return status;
    }

    public void setStatus(int status) {
        this.status = status;
    }

    public String getMsg() {
        return msg;
    }

    public void setMsg(String msg) {
        this.msg = msg;
    }

    public int getTotal() {
        return total;
    }

    public void setTotal(int total) {
        this.total = total;
    }

    public int getPageTotal() {
        return pageTotal;
    }

    public void setPageTotal(int pageTotal) {
        this.pageTotal = pageTotal;
    }

    public int getPageSize() {
        return pageSize;
    }

    public void setPageSize(int pageSize) {
        this.pageSize = pageSize;
    }

    public int getPageNumber() {
        return pageNumber;
    }

    public void setPageNumber(int pageNumber) {
        this.pageNumber = pageNumber;
    }

    public int getStart() {
        return start;
    }

    public void setStart(int start) {
        this.start = start;
    }

    public List<HashBean> getData() {
        return data;
    }

    public void setData(List<HashBean> data) {
        this.data = data;
    }
}
