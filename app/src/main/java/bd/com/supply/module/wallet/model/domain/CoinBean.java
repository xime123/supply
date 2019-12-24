package bd.com.supply.module.wallet.model.domain;

import android.os.Parcel;
import android.os.Parcelable;


//public class CoinBean implements Parcelable {
//    private String contractAddress;
//    private String name;
//    private String icon;
//    private String version;
//    private String balance;
//    private String value;
//    private String endPoint;
//    public CoinBean() {
//    }
//
//    protected CoinBean(Parcel in) {
//        name = in.readString();
//        icon = in.readString();
//        version = in.readString();
//        balance = in.readString();
//        value = in.readString();
//        endPoint = in.readString();
//    }
//
//    @Override
//    public void writeToParcel(Parcel dest, int flags) {
//        dest.writeString(name);
//        dest.writeString(icon);
//        dest.writeString(version);
//        dest.writeString(balance);
//        dest.writeString(value);
//        dest.writeString(endPoint);
//    }
//
//    @Override
//    public int describeContents() {
//        return 0;
//    }
//
//    public static final Creator<CoinBean> CREATOR = new Creator<CoinBean>() {
//        @Override
//        public CoinBean createFromParcel(Parcel in) {
//            return new CoinBean(in);
//        }
//
//        @Override
//        public CoinBean[] newArray(int size) {
//            return new CoinBean[size];
//        }
//    };
//
//    public String getContractAddress() {
//        return contractAddress;
//    }
//
//    public void setContractAddress(String contractAddress) {
//        this.contractAddress = contractAddress;
//    }
//
//    public String getName() {
//        return name;
//    }
//
//    public void setName(String name) {
//        this.name = name;
//    }
//
//    public String getIcon() {
//        return icon;
//    }
//
//    public void setIcon(String icon) {
//        this.icon = icon;
//    }
//
//    public String getVersion() {
//        return version;
//    }
//
//    public void setVersion(String version) {
//        this.version = version;
//    }
//
//    public String getBalance() {
//        return balance;
//    }
//
//    public void setBalance(String balance) {
//        this.balance = balance;
//    }
//
//    public String getValue() {
//        return value;
//    }
//
//    public void setValue(String value) {
//        this.value = value;
//    }
//
//    public String getEndPoint() {
//        return endPoint;
//    }
//
//    public void setEndPoint(String endPoint) {
//        this.endPoint = endPoint;
//    }
//}
