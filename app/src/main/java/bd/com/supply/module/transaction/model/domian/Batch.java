package bd.com.supply.module.transaction.model.domian;

import android.os.Parcel;
import android.os.Parcelable;

public class Batch implements Parcelable {
    private String alias;
    private String caddr;
    private long createTime;

    public Batch() {
    }


    protected Batch(Parcel in) {
        alias = in.readString();
        caddr = in.readString();
        createTime = in.readLong();
    }

    public static final Creator<Batch> CREATOR = new Creator<Batch>() {
        @Override
        public Batch createFromParcel(Parcel in) {
            return new Batch(in);
        }

        @Override
        public Batch[] newArray(int size) {
            return new Batch[size];
        }
    };

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(alias);
        dest.writeString(caddr);
        dest.writeLong(createTime);
    }

    public String getAlias() {
        return alias;
    }

    public void setAlias(String alias) {
        this.alias = alias;
    }

    public String getCaddr() {
        return caddr;
    }

    public void setCaddr(String caddr) {
        this.caddr = caddr;
    }

    public long getCreateTime() {
        return createTime;
    }

    public void setCreateTime(long createTime) {
        this.createTime = createTime;
    }
}
