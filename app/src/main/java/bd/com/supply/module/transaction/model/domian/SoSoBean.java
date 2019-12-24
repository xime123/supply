package bd.com.supply.module.transaction.model.domian;

import android.os.Parcel;
import android.os.Parcelable;
import android.text.TextUtils;

import java.io.Serializable;
import java.util.List;

/**
 * 原材料阶段	0
 * PRODUCT	生产阶段	1
 * TRANSPORT	运输阶段	2
 * SALE	销售阶段	3
 */
public class SoSoBean implements Parcelable {
    private String PREPARE = "原材料阶段";
    private String PRODUCT = "生产阶段";
    private String TRANSPORT = "运输阶段";
    private String SALE = "销售阶段";
    private String title;
    private String opAddress;//操作人员地址
    private String periodName;
    private String periodId;
    private List<SoSoInnerItemBean> innerDatas;

    public SoSoBean() {
    }

    protected SoSoBean(Parcel in) {
        PREPARE = in.readString();
        PRODUCT = in.readString();
        TRANSPORT = in.readString();
        SALE = in.readString();
        title = in.readString();
        opAddress = in.readString();
        periodName = in.readString();
        periodId = in.readString();
    }

    public static final Creator<SoSoBean> CREATOR = new Creator<SoSoBean>() {
        @Override
        public SoSoBean createFromParcel(Parcel in) {
            return new SoSoBean(in);
        }

        @Override
        public SoSoBean[] newArray(int size) {
            return new SoSoBean[size];
        }
    };

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public List<SoSoInnerItemBean> getInnerDatas() {
        return innerDatas;
    }

    public void setInnerDatas(List<SoSoInnerItemBean> innerDatas) {
        this.innerDatas = innerDatas;
    }

    public String getOpAddress() {
        return opAddress;
    }

    public void setOpAddress(String opAddress) {
        this.opAddress = opAddress;
    }

    public String getPeriodName() {
        if (TextUtils.isEmpty(periodId)) {
            return "错误阶段";
        }
        switch (periodId) {
            case "0":
                return PREPARE;
            case "1":
                return PRODUCT;
            case "2":
                return TRANSPORT;
            case "3":
                return SALE;
            default:
                return periodName;


        }
    }

    public void setPeriodName(String periodName) {

        this.periodName = periodName;
    }

    public String getPeriodId() {

        return periodId;
    }

    public void setPeriodId(String periodId) {
        this.periodId = periodId;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(PREPARE);
        dest.writeString(PRODUCT);
        dest.writeString(TRANSPORT);
        dest.writeString(SALE);
        dest.writeString(title);
        dest.writeString(opAddress);
        dest.writeString(periodName);
        dest.writeString(periodId);
    }
}
