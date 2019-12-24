package bd.com.supply.module.transaction.model.domian;

import android.os.Parcel;
import android.os.Parcelable;

public class ProductOri implements Parcelable {
    private String paddr;//产品合约地址（产品号
    private String caddr;//批次合约地址（批次号）
    private String aaddr;//鉴权合约地址
    private String taddr;//代币地址
    private long createTime;//创建时间
    private String name;

    public ProductOri() {

    }


    protected ProductOri(Parcel in) {
        paddr = in.readString();
        caddr = in.readString();
        aaddr = in.readString();
        taddr = in.readString();
        createTime = in.readLong();
        name = in.readString();
    }

    public static final Creator<ProductOri> CREATOR = new Creator<ProductOri>() {
        @Override
        public ProductOri createFromParcel(Parcel in) {
            return new ProductOri(in);
        }

        @Override
        public ProductOri[] newArray(int size) {
            return new ProductOri[size];
        }
    };

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(paddr);
        dest.writeString(caddr);
        dest.writeString(aaddr);
        dest.writeString(taddr);
        dest.writeLong(createTime);
        dest.writeString(name);
    }

    public String getPaddr() {
        return paddr;
    }

    public void setPaddr(String paddr) {
        this.paddr = paddr;
    }

    public String getCaddr() {
        return caddr;
    }

    public void setCaddr(String caddr) {
        this.caddr = caddr;
    }

    public String getAaddr() {
        return aaddr;
    }

    public void setAaddr(String aaddr) {
        this.aaddr = aaddr;
    }

    public String getTaddr() {
        return taddr;
    }

    public void setTaddr(String taddr) {
        this.taddr = taddr;
    }

    public long getCreateTime() {
        return createTime;
    }

    public void setCreateTime(long createTime) {
        this.createTime = createTime;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }
}
