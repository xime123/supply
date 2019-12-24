package bd.com.walletdb.entity;

import android.os.Parcel;
import android.os.Parcelable;
import android.text.TextUtils;

import org.greenrobot.greendao.annotation.Entity;
import org.greenrobot.greendao.annotation.Id;
import org.greenrobot.greendao.annotation.Generated;


@Entity
public class WalletEntity implements Parcelable {
    @Id
    private String address;
    private String privateKey;
    private String publicKey;
    private String password;
    private String keystore;
    private String name;
    private String iconStr;
    private String balance;







    protected WalletEntity(Parcel in) {
        address = in.readString();
        privateKey = in.readString();
        publicKey = in.readString();
        password = in.readString();
        keystore = in.readString();
        name = in.readString();
        iconStr = in.readString();
    }
    @Generated(hash = 861183172)
    public WalletEntity(String address, String privateKey, String publicKey, String password,
                        String keystore, String name, String iconStr, String balance) {
        this.address = address;
        this.privateKey = privateKey;
        this.publicKey = publicKey;
        this.password = password;
        this.keystore = keystore;
        this.name = name;
        this.iconStr = iconStr;
        this.balance = balance;
    }
    @Generated(hash = 1363662176)
    public WalletEntity() {
    }

    public static final Creator<WalletEntity> CREATOR = new Creator<WalletEntity>() {
        @Override
        public WalletEntity createFromParcel(Parcel in) {
            return new WalletEntity(in);
        }

        @Override
        public WalletEntity[] newArray(int size) {
            return new WalletEntity[size];
        }
    };

    public String getPrivateKey() {
        return this.privateKey;
    }
    public void setPrivateKey(String privateKey) {
        this.privateKey = privateKey;
    }
    public String getPublicKey() {
        return this.publicKey;
    }
    public void setPublicKey(String publicKey) {
        this.publicKey = publicKey;
    }
    public String getAddress() {
        return this.address;
    }
    public void setAddress(String address) {
        this.address = address;
    }
    public String getPassword() {
        return this.password;
    }
    public void setPassword(String password) {
        this.password = password;
    }
    public String getKeystore() {
        return this.keystore;
    }
    public void setKeystore(String keystore) {
        this.keystore = keystore;
    }
    public String getName() {
        return this.name;
    }
    public void setName(String name) {
        this.name = name;
    }
    public String getIconStr() {
        return this.iconStr;
    }
    public void setIconStr(String iconStr) {
        this.iconStr = iconStr;
    }

    public String getBalance() {
        return balance;
    }

    public void setBalance(String balance) {
        this.balance = balance;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(address);
        dest.writeString(privateKey);
        dest.writeString(publicKey);
        dest.writeString(password);
        dest.writeString(keystore);
        dest.writeString(name);
        dest.writeString(iconStr);
    }
}
