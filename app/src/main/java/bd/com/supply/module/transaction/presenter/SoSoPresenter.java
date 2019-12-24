package bd.com.supply.module.transaction.presenter;

import android.graphics.Path;
import android.text.TextUtils;

import com.tencent.bugly.crashreport.BuglyLog;
import com.tencent.bugly.crashreport.CrashReport;

import org.web3j.crypto.WalletUtils;
import org.web3j.protocol.core.methods.response.Log;
import org.web3j.protocol.core.methods.response.TransactionReceipt;

import java.math.BigInteger;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

import bd.com.appcore.mvp.BasePresenterImpl;
import bd.com.appcore.mvp.IListView;
import bd.com.appcore.rx.LogicException;
import bd.com.appcore.rx.RxTaskCallBack;
import bd.com.appcore.rx.RxTaskScheduler;
import bd.com.appcore.util.AppSettings;
import bd.com.appcore.util.GsonUtil;
import bd.com.supply.app.ThreadManager;
import bd.com.supply.module.transaction.SoSoConfig;
import bd.com.supply.module.transaction.model.SosoModel;
import bd.com.supply.module.transaction.model.domian.ArchivesBean;
import bd.com.supply.module.transaction.model.domian.CategoryInfo;
import bd.com.supply.module.transaction.model.domian.CategoryInfoResp;
import bd.com.supply.module.transaction.model.domian.HashBean;
import bd.com.supply.module.transaction.model.domian.HashBeanListResp;
import bd.com.supply.module.transaction.model.domian.Operation;
import bd.com.supply.module.transaction.model.domian.ProductBean;
import bd.com.supply.module.transaction.model.domian.SoSoBean;
import bd.com.supply.util.DateKit;
import bd.com.supply.util.UuidUtil;
import bd.com.supply.web3.Web3Proxy;
import bd.com.supply.web3.contract.Archives;
import bd.com.supply.web3.contract.Authorized;
import bd.com.supply.web3.contract.Category;
import bd.com.supply.web3.contract.Product;

public class SoSoPresenter extends BasePresenterImpl<SoSoPresenter.View> {
    private String TAG = SoSoPresenter.class.getSimpleName();


    public void getOperationLog(final String prodAddress) {
        RxTaskScheduler.postIoMainTask(new RxTaskCallBack<List<Operation>>() {
            @Override
            public void onFailed(LogicException e) {
                super.onFailed(e);
                if (mView != null) {
                    mView.loadFailed(e.getErrorMsg());
                }
            }

            @Override
            public void onSuccess(List<Operation> s) {
                super.onSuccess(s);
                if (mView != null) {
                    //    mView.onOperationLog(s);
                    mView.loadSuccess(s);
                    mView.loadMoreSuccess(new ArrayList<Operation>());
                    AppSettings.getAppSettings().setLastSosoTime(System.currentTimeMillis() / 1000 + "");
                    int count = AppSettings.getAppSettings().getSosoCount() + 1;
                    AppSettings.getAppSettings().setSosoCount(count);
                } else {
                    mView.sosoFailed("数据格式不正确");
                }
            }

            @Override
            public List<Operation> doWork() throws Exception {
                Product product = Product.load(prodAddress, Web3Proxy.getWeb3Proxy().getWeb3j(), Web3Proxy.getCredentials(), Web3Proxy.GAS_PRICE, Web3Proxy.GAS_LIMIT);

                //  getArchivesInfo(product);
                getCategoryInfo(product);
                //操作日志
                String productStr = product.retriveAllEntry().send();
                if (TextUtils.isEmpty(productStr) || !productStr.contains("perio")) {
                    return new ArrayList<>();
                } else {
                    CrashReport.postCatchedException(new LogicException(new Exception("溯源数据格式不正确"), -1));
                    OperationDataParser parser = new OperationDataParser(productStr);
                    List<Operation> soSoBeans = parser.getSoSoBeanList();
                    Collections.sort(soSoBeans, new Comparator<Operation>() {
                        @Override
                        public int compare(Operation o1, Operation o2) {
                            return o1.getPerio().compareTo(o2.getPerio());
                        }
                    });
                    setTime(soSoBeans);
                    getArchivesInfo(soSoBeans);
                    return soSoBeans;
                }

                // List<Operation> soSoBeans = new ArrayList<>();
                // return soSoBeans;
            }
        });
    }


    public void getCategoryInfo(final Product product) throws Exception {
        new Thread(new Runnable() {
            @Override
            public void run() {
                try {
                    final String result = product.categoryIdentify().send();
                    Category category = Category.load(result, Web3Proxy.getWeb3Proxy().getWeb3j(), Web3Proxy.getCredentials(), Web3Proxy.GAS_PRICE, Web3Proxy.GAS_LIMIT);
                    String categoryInfo = category.getCategoryInfo().send();
                    final String name = category.name().send();
                    final CategoryInfoResp categoryInfoResp = CategoryInfoParser.parserCategoryInfo(result, name, categoryInfo);
                    RxTaskScheduler.postUiTask(new RxTaskCallBack<String>() {
                        @Override
                        public String doWork() throws Exception {
                            if (mView != null) {
                                mView.getCategorySuccess(categoryInfoResp);
                            }
                            return null;
                        }
                    });
                } catch (Exception e) {
                    RxTaskScheduler.postUiTask(new RxTaskCallBack<String>() {
                        @Override
                        public String doWork() throws Exception {
                            if (mView != null) {
                                mView.sosoFailed("获取categoryInfoResp失败");
                            }
                            return null;
                        }
                    });
                }

            }
        }).start();


    }

    public void getArchivesInfo(final List<Operation> operationList) throws Exception {
        List<Operation> errorOp = new ArrayList<>();
        for (Operation operation : operationList) {
            String archiveAddr = operation.getProductBean().getDocaddr();
            long startTime = System.currentTimeMillis();
            Archives archives = Archives.load(archiveAddr, Web3Proxy.getWeb3Proxy().getWeb3j(), Web3Proxy.getCredentials(), Web3Proxy.GAS_PRICE, Web3Proxy.GAS_LIMIT);
            String archivesInfo = archives.getArchives().send();
            long endTime = System.currentTimeMillis();
            android.util.Log.i(TAG, "getArchivesInfo cTime=" + (endTime - startTime));
            long startTimeLogoc = System.currentTimeMillis();
            if (!TextUtils.isEmpty(archivesInfo) && !archivesInfo.contains("$&")) {
                final ArchivesBean archivesResp = OperationDataParser.getByInfo(archivesInfo);
                operation.setArchivesBean(archivesResp);
            } else {
                android.util.Log.e(TAG, "getArchivesInfo has error:" + archivesInfo);
                errorOp.add(operation);
            }
            long endTimeLogoc = System.currentTimeMillis();
            android.util.Log.i(TAG, "getArchivesInfo cLogicTime=" + (endTimeLogoc - startTimeLogoc));
        }
        operationList.removeAll(errorOp);
    }

    public void setTime(List<Operation> sosoBeans) {
        if (sosoBeans == null || sosoBeans.size() == 0) return;
        String opAddr = sosoBeans.get(0).getOpAddress();
        StringBuilder uuidSb = new StringBuilder();
        for (int i = 0; i < sosoBeans.size(); i++) {
            Operation operation = sosoBeans.get(i);
            ProductBean productBean = operation.getProductBean();
            if (productBean != null) {
                String uuid = productBean.getUuid();
                uuidSb.append(uuid);
                if (i < sosoBeans.size() - 1) {
                    uuidSb.append(",");
                }
            }
        }
        HashBeanListResp hashBeanList = SosoModel.getInstance().getTxHashByUUidsSync(uuidSb.toString(), opAddr);
        if (hashBeanList != null) {
            List<HashBean> hashBeans = hashBeanList.getData();
            if (hashBeans != null && hashBeans.size() > 0) {
                for (HashBean hashBean : hashBeans) {
                    for (int i = 0; i < sosoBeans.size(); i++) {
                        Operation operation = sosoBeans.get(i);
                        ProductBean productBean = operation.getProductBean();
                        if (productBean != null) {
                            if (hashBean.getUuid().equals(productBean.getUuid())) {
                                productBean.setTime(hashBean.getBlockTime());
                                break;
                            }
                        }
                    }
                }
            }
        }
    }


    public interface View extends IListView<Operation> {


        void getCategorySuccess(CategoryInfoResp resp);

        void getCategoryFailed(String msg);

        void sosoFailed(String msg);

        void onOperationLog(List<Operation> operationList);
    }
}
