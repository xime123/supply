package bd.com.walletdb.entity;

import android.os.Parcel;
import android.os.Parcelable;

import org.greenrobot.greendao.annotation.Entity;
import org.greenrobot.greendao.annotation.Id;
import org.greenrobot.greendao.annotation.Generated;
import org.greenrobot.greendao.annotation.Index;

/**
 * author:     labixiaoxin
 * date:       2018/6/9
 * email:      labixiaoxin2@qq.cn
 */
@Entity(indexes = {@Index(value = "address,chainId", unique = true)})
public class Price implements Parcelable {
    @Id(autoincrement = true)
    private Long id;
    private String marketName;
    private String asks;
    private String bids;
    private String unit;
    private String address;
    private String chainId;


    protected Price(Parcel in) {
        if (in.readByte() == 0) {
            id = null;
        } else {
            id = in.readLong();
        }
        marketName = in.readString();
        asks = in.readString();
        bids = in.readString();
        unit = in.readString();
        address = in.readString();
        chainId = in.readString();
    }

    @Generated(hash = 2027481334)
    public Price(Long id, String marketName, String asks, String bids, String unit,
            String address, String chainId) {
        this.id = id;
        this.marketName = marketName;
        this.asks = asks;
        this.bids = bids;
        this.unit = unit;
        this.address = address;
        this.chainId = chainId;
    }

    @Generated(hash = 812905808)
    public Price() {
    }

    public static final Creator<Price> CREATOR = new Creator<Price>() {
        @Override
        public Price createFromParcel(Parcel in) {
            return new Price(in);
        }

        @Override
        public Price[] newArray(int size) {
            return new Price[size];
        }
    };

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel parcel, int i) {
        if (id == null) {
            parcel.writeByte((byte) 0);
        } else {
            parcel.writeByte((byte) 1);
            parcel.writeLong(id);
        }
        parcel.writeString(marketName);
        parcel.writeString(asks);
        parcel.writeString(bids);
        parcel.writeString(unit);
        parcel.writeString(address);
        parcel.writeString(chainId);
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getMarketName() {
        return marketName;
    }

    public void setMarketName(String marketName) {
        this.marketName = marketName;
    }

    public String getAsks() {
        return asks;
    }

    public void setAsks(String asks) {
        this.asks = asks;
    }

    public String getBids() {
        return bids;
    }

    public void setBids(String bids) {
        this.bids = bids;
    }

    public String getUnit() {
        return unit;
    }

    public void setUnit(String unit) {
        this.unit = unit;
    }

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
