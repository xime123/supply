package bd.com.supply.module.transaction.model.domian;

import android.os.Parcel;
import android.os.Parcelable;

import java.util.List;

/**
 * 0x009007ee1f215849dea98ae842e944aeec807e53${"gr":"种植户","cr":"联系人","mp":"手机号","rp":"检测报告","tx":"标题","ic":"图标信息"}&true${"gr":"种植户","cr":"联系人","mp":"手机号","rp":"检测报告","tx":"标题","ic":"图标信息"}&false${"gr":"种植户","cr":"联系人","mp":"手机号","rp":"检测报告","tx":"标题","ic":"图标信息"}&true
 */
public class ArchivesBean implements Parcelable {
    private String nm;//种植户或企业名称
    private String cr;//联系人
    private String mp;//手机号
    private String rp;//检测报告
    private String tx;//标题
    private List<String> icon;//图标信息
    private String video;//视频地址
    private String vdoabbr;//视频缩略图地址
    private String or;//序列号
    private String tm;//加工时间
    private String rt;//检测时间
    private String fp;//固定电话
    private String addr;
    private boolean pass;//是否合格
    private int index;//0,厂家，1，省代理，2 市代理，3 商家
    private List<SoSoInnerItemBean> innerDatas;

    public ArchivesBean() {
    }


    protected ArchivesBean(Parcel in) {
        nm = in.readString();
        cr = in.readString();
        mp = in.readString();
        rp = in.readString();
        tx = in.readString();
        icon = in.createStringArrayList();
        video = in.readString();
        vdoabbr = in.readString();
        or = in.readString();
        tm = in.readString();
        rt = in.readString();
        fp = in.readString();
        addr = in.readString();
        pass = in.readByte() != 0;
        index = in.readInt();
        innerDatas = in.createTypedArrayList(SoSoInnerItemBean.CREATOR);
    }

    @Override
    public int describeContents() {
        return 0;
    }

    public static final Creator<ArchivesBean> CREATOR = new Creator<ArchivesBean>() {
        @Override
        public ArchivesBean createFromParcel(Parcel in) {
            return new ArchivesBean(in);
        }

        @Override
        public ArchivesBean[] newArray(int size) {
            return new ArchivesBean[size];
        }
    };

    public String getNm() {
        return nm;
    }

    public void setNm(String nm) {
        this.nm = nm;
    }

    public String getCr() {
        return cr;
    }

    public void setCr(String cr) {
        this.cr = cr;
    }

    public String getMp() {
        return mp;
    }

    public void setMp(String mp) {
        this.mp = mp;
    }

    public String getRp() {
        return rp;
    }

    public void setRp(String rp) {
        this.rp = rp;
    }

    public String getTx() {
        return tx;
    }

    public void setTx(String tx) {
        this.tx = tx;
    }


    public String getOr() {
        return or;
    }

    public void setOr(String or) {
        this.or = or;
    }

    public String getTm() {
        return tm;
    }

    public void setTm(String tm) {
        this.tm = tm;
    }

    public String getRt() {
        return rt;
    }

    public void setRt(String rt) {
        this.rt = rt;
    }

    public boolean isPass() {
        return pass;
    }

    public void setPass(boolean pass) {
        this.pass = pass;
    }

    public String getAddr() {
        return addr;
    }

    public void setAddr(String addr) {
        this.addr = addr;
    }

    public List<String> getIcon() {
        return icon;
    }

    public void setIcon(List<String> icon) {
        this.icon = icon;
    }

    public int getIndex() {
        return index;
    }

    public void setIndex(int index) {
        this.index = index;
    }

    public List<SoSoInnerItemBean> getInnerDatas() {
        return innerDatas;
    }

    public void setInnerDatas(List<SoSoInnerItemBean> innerDatas) {
        this.innerDatas = innerDatas;
    }

    public String getVideo() {
        return video;
    }

    public void setVideo(String video) {
        this.video = video;
    }

    public String getVdoabbr() {
        return vdoabbr;
    }

    public void setVdoabbr(String vdoabbr) {
        this.vdoabbr = vdoabbr;
    }

    public String getFp() {
        return fp;
    }

    public void setFp(String fp) {
        this.fp = fp;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {

        dest.writeString(nm);
        dest.writeString(cr);
        dest.writeString(mp);
        dest.writeString(rp);
        dest.writeString(tx);
        dest.writeStringList(icon);
        dest.writeString(video);
        dest.writeString(vdoabbr);
        dest.writeString(or);
        dest.writeString(tm);
        dest.writeString(rt);
        dest.writeString(fp);
        dest.writeString(addr);
        dest.writeByte((byte) (pass ? 1 : 0));
        dest.writeInt(index);
        dest.writeTypedList(innerDatas);
    }
}
