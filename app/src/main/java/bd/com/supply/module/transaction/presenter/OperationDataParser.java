package bd.com.supply.module.transaction.presenter;

import android.text.TextUtils;
import android.util.Log;

import com.tencent.bugly.crashreport.BuglyLog;

import java.util.ArrayList;
import java.util.List;

import bd.com.appcore.util.GsonUtil;
import bd.com.supply.module.transaction.SoSoConfig;
import bd.com.supply.module.transaction.model.SosoModel;
import bd.com.supply.module.transaction.model.domian.ArchivesBean;
import bd.com.supply.module.transaction.model.domian.HashBean;
import bd.com.supply.module.transaction.model.domian.Operation;
import bd.com.supply.module.transaction.model.domian.ProductBean;
import bd.com.supply.module.transaction.model.domian.SoSoInnerItemBean;


public class OperationDataParser {
    private static final String TAG = OperationDataParser.class.getSimpleName();
    private String mSoSOData;

    public OperationDataParser(String mSoSOData) {
        BuglyLog.i(TAG, "SoSoDaterParser====>mSoSOData=" + mSoSOData);
        //mSoSOData = mSoSOData.replace("$", "#");
        this.mSoSOData = mSoSOData;
    }

    public List<Operation> getSoSoBeanList() {
        List<Operation> soSoBeans = new ArrayList<>();
        if (mSoSOData.contains("$")) {//如果含有多个时期
            String[] perios = mSoSOData.split(SoSoConfig.PEROID_GAP);
            for (String perio : perios) {
                addByContent(perio, soSoBeans);
                //soSoBeans.addAll(generateByContent(perio));
            }
        } else {
            addByContent(mSoSOData, soSoBeans);
            // soSoBeans.addAll(generateByContent(mSoSOData));
        }

        return soSoBeans;

    }


    /**
     * 0x9574cd3dbc0fc70d6b560514379a2920e64a2a45@{"title":"销售单位","perio":"0","time":"2019-1-21","opername":"张三","uname":"米其林餐厅","uaddr":"武汉"}
     *
     * @param content
     * @return
     */
    private List<Operation> generateByContent(String content) {
        List<Operation> soSoBeanList = new ArrayList<>();
        if (content.contains(SoSoConfig.CONTENT_GAP)) {
            String oneContents[] = content.split(SoSoConfig.CONTENT_GAP);
            for (int i = 0; i < oneContents.length; i++) {
                Operation operation = generateOneContent(oneContents[i]);
                soSoBeanList.add(operation);
            }
        } else {
            Operation operation = generateOneContent(content);
            soSoBeanList.add(operation);
        }
        return soSoBeanList;
    }

    private void addByContent(String content, List<Operation> soSoBeans) {
        List<Operation> operations = generateByContent(content);
        soSoBeans.addAll(operations);
    }

    private Operation generateOneContent(String content) {
        BuglyLog.i(TAG, "generateByContent====>content=" + content);
        if (content.contains(SoSoConfig.ADDR_GAP)) {
            Operation operation = new Operation();
            String[] addrAndContent = content.split(SoSoConfig.ADDR_GAP);
            if (addrAndContent.length >= 4) {
                operation.setOpAddress(addrAndContent[0]);
                String contentJson = addrAndContent[1];
                ProductBean productBean = GsonUtil.jsonToObject(contentJson, ProductBean.class);
                operation.setProductBean(productBean);
                operation.setBlockNum(addrAndContent[2]);
                operation.setPerio(addrAndContent[3]);
            }

            return operation;
        }
        return null;
    }

    public static ArchivesResp getArchivesResp(String archivesInfo) {
        BuglyLog.i(TAG, "getArchivesList======>archivesInfo==" + archivesInfo);
        if (TextUtils.isEmpty(archivesInfo)) return null;
        ArchivesResp archivesResp = new ArchivesResp();
        List<ArchivesBean> archivesBeanList = new ArrayList<>();
        if (archivesInfo.contains("$")) {
            String dataList[] = archivesInfo.split("\\u0024");
            archivesResp.setOpAddress(dataList[0]);
            for (int i = 1; i < dataList.length; i++) {
                ArchivesBean info = generateByInfo(dataList[i]);
                info.setIndex(i - 1);
                archivesBeanList.add(info);
            }
            archivesResp.setArchivesBeans(archivesBeanList);
        }
        return archivesResp;

    }

    /**
     * private String nm;//种植户或企业名称
     * private String cr;//联系人
     * private String mp;//手机号
     * private String rp;//检测报告
     * private String tx;//标题
     * private String icon;//图标信息
     * private String or;//序列号
     * private String tm;//加工时间
     * private String rt;//检测时间
     * private boolean pass;//是否合格
     *
     * @param info
     * @return
     */
    private static ArchivesBean generateByInfo(String info) {
        BuglyLog.i(TAG, "generateByInfo   ======>info==" + info);
        String[] items = info.split("&");
        ArchivesBean bean = GsonUtil.jsonToObject(items[0], ArchivesBean.class);
        return bean;
    }

    public static ArchivesBean getByInfo(String info) {
        if (TextUtils.isEmpty(info)) return null;
        ArchivesBean bean = new ArchivesBean();
        if (info.contains("$")) {
            String dataList[] = info.split(SoSoConfig.PEROID_GAP);
            bean.setAddr(dataList[0]);
            String json = dataList[1].split(SoSoConfig.CONTENT_GAP)[0];
            bean = GsonUtil.jsonToObject(json, ArchivesBean.class);
        }
        return bean;
    }

}
