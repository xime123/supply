package bd.com.walletdb.entity;

import android.os.Parcel;
import android.os.Parcelable;

import org.greenrobot.greendao.annotation.Entity;
import org.greenrobot.greendao.annotation.Id;
import org.greenrobot.greendao.annotation.Generated;

@Entity
public class ContactEntity implements Parcelable {
    @Id
    private String address;
    private String name;
    private String remark;


    protected ContactEntity(Parcel in) {
        address = in.readString();
        name = in.readString();
        remark = in.readString();
    }

    @Generated(hash = 792408862)
    public ContactEntity(String address, String name, String remark) {
        this.address = address;
        this.name = name;
        this.remark = remark;
    }

    @Generated(hash = 393979869)
    public ContactEntity() {
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(address);
        dest.writeString(name);
        dest.writeString(remark);
    }

    @Override
    public int describeContents() {
        return 0;
    }

    public static final Creator<ContactEntity> CREATOR = new Creator<ContactEntity>() {
        @Override
        public ContactEntity createFromParcel(Parcel in) {
            return new ContactEntity(in);
        }

        @Override
        public ContactEntity[] newArray(int size) {
            return new ContactEntity[size];
        }
    };

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getRemark() {
        return remark;
    }

    public void setRemark(String remark) {
        this.remark = remark;
    }

    @Override
    public boolean equals(Object obj) {
        if(obj instanceof ContactEntity){
            ContactEntity entity=(ContactEntity)obj;
            return this.getAddress().equals(entity.getAddress());
        }
        return false;
    }

    @Override
    public int hashCode() {
        return this.getAddress().hashCode();
    }
}
