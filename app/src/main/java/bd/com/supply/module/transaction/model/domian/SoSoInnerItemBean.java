package bd.com.supply.module.transaction.model.domian;

import android.os.Parcel;
import android.os.Parcelable;

public class SoSoInnerItemBean  implements Parcelable{
    private String key;
    private String value;

    public SoSoInnerItemBean(){}
    protected SoSoInnerItemBean(Parcel in) {
        key = in.readString();
        value = in.readString();
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(key);
        dest.writeString(value);
    }

    @Override
    public int describeContents() {
        return 0;
    }

    public static final Creator<SoSoInnerItemBean> CREATOR = new Creator<SoSoInnerItemBean>() {
        @Override
        public SoSoInnerItemBean createFromParcel(Parcel in) {
            return new SoSoInnerItemBean(in);
        }

        @Override
        public SoSoInnerItemBean[] newArray(int size) {
            return new SoSoInnerItemBean[size];
        }
    };

    public String getKey() {
        return key;
    }

    public void setKey(String key) {
        this.key = key;
    }

    public String getValue() {
        return value;
    }

    public void setValue(String value) {
        this.value = value;
    }
}
