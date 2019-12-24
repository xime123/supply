package bd.com.walletdb.entity;

import android.os.Parcel;
import android.os.Parcelable;

import org.greenrobot.greendao.annotation.Entity;
import org.greenrobot.greendao.annotation.Id;
import org.greenrobot.greendao.annotation.Generated;
import org.greenrobot.greendao.annotation.Index;


@Entity(indexes = {@Index(value = "address,walletAddress", unique = true)})
public class TokenEntity implements Parcelable {
    @Id(autoincrement = true)
    private Long id;
    private String address;
    private String icon;//代币图标(bas e64 字符串)
    private String name; //代币名称
    private String symbol; //代币符号
    private String supplyTotal;  //代币发行总量
    private int decimals; //代币精度值，如:=18则表示1token=1000000000000000000
    private String version; //合约版本
    private boolean checked; //代币合约地址
    private String publisher;//代币发行商
    private String chainId;//所属的链Id
    private String balance;//币余额
    private String value;// 价值=balance*price
    private String price;//价格
    private String walletAddress;//属于哪个钱包


    protected TokenEntity(Parcel in) {
        if (in.readByte() == 0) {
            id = null;
        } else {
            id = in.readLong();
        }
        address = in.readString();
        icon = in.readString();
        name = in.readString();
        symbol = in.readString();
        supplyTotal = in.readString();
        decimals = in.readInt();
        version = in.readString();
        checked = in.readByte() != 0;
        publisher = in.readString();
        chainId = in.readString();
        balance = in.readString();
        value = in.readString();
        price = in.readString();
        walletAddress = in.readString();
    }

    @Generated(hash = 743266418)
    public TokenEntity(Long id, String address, String icon, String name,
                       String symbol, String supplyTotal, int decimals, String version,
                       boolean checked, String publisher, String chainId, String balance,
                       String value, String price, String walletAddress) {
        this.id = id;
        this.address = address;
        this.icon = icon;
        this.name = name;
        this.symbol = symbol;
        this.supplyTotal = supplyTotal;
        this.decimals = decimals;
        this.version = version;
        this.checked = checked;
        this.publisher = publisher;
        this.chainId = chainId;
        this.balance = balance;
        this.value = value;
        this.price = price;
        this.walletAddress = walletAddress;
    }

    @Generated(hash = 697107377)
    public TokenEntity() {
    }

    public static final Creator<TokenEntity> CREATOR = new Creator<TokenEntity>() {
        @Override
        public TokenEntity createFromParcel(Parcel in) {
            return new TokenEntity(in);
        }

        @Override
        public TokenEntity[] newArray(int size) {
            return new TokenEntity[size];
        }
    };

    /************这段代码需要保留************************/
    @Override
    public boolean equals(Object obj) {
        if (obj instanceof TokenEntity) {
            return ((TokenEntity) obj).address.equals(this.address);
        }
        return super.equals(obj);
    }

    @Override
    public int hashCode() {
        return this.address.hashCode();
    }


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
        parcel.writeString(address);
        parcel.writeString(icon);
        parcel.writeString(name);
        parcel.writeString(symbol);
        parcel.writeString(supplyTotal);
        parcel.writeInt(decimals);
        parcel.writeString(version);
        parcel.writeByte((byte) (checked ? 1 : 0));
        parcel.writeString(publisher);
        parcel.writeString(chainId);
        parcel.writeString(balance);
        parcel.writeString(value);
        parcel.writeString(price);
        parcel.writeString(walletAddress);
    }

    public Long getId() {
        return this.id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getAddress() {
        return this.address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public String getIcon() {
        return this.icon;
    }

    public void setIcon(String icon) {
        this.icon = icon;
    }

    public String getName() {
        return this.name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getSymbol() {
        return this.symbol;
    }

    public void setSymbol(String symbol) {
        this.symbol = symbol;
    }

    public String getSupplyTotal() {
        return this.supplyTotal;
    }

    public void setSupplyTotal(String supplyTotal) {
        this.supplyTotal = supplyTotal;
    }

    public int getDecimals() {
        return this.decimals;
    }

    public void setDecimals(int decimals) {
        this.decimals = decimals;
    }

    public String getVersion() {
        return this.version;
    }

    public void setVersion(String version) {
        this.version = version;
    }

    public boolean getChecked() {
        return this.checked;
    }

    public void setChecked(boolean checked) {
        this.checked = checked;
    }

    public String getPublisher() {
        return this.publisher;
    }

    public void setPublisher(String publisher) {
        this.publisher = publisher;
    }

    public String getChainId() {
        return this.chainId;
    }

    public void setChainId(String chainId) {
        this.chainId = chainId;
    }

    public String getBalance() {
        return this.balance;
    }

    public void setBalance(String balance) {
        this.balance = balance;
    }

    public String getValue() {
        return this.value;
    }

    public void setValue(String value) {
        this.value = value;
    }

    public String getPrice() {
        return this.price;
    }

    public void setPrice(String price) {
        this.price = price;
    }

    public String getWalletAddress() {
        return this.walletAddress;
    }

    public void setWalletAddress(String walletAddress) {
        this.walletAddress = walletAddress;
    }
}
