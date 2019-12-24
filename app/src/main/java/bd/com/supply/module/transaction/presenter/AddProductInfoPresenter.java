package bd.com.supply.module.transaction.presenter;

import android.text.TextUtils;

import com.tencent.bugly.crashreport.BuglyLog;

import org.web3j.protocol.core.methods.response.Log;
import org.web3j.protocol.core.methods.response.TransactionReceipt;

import java.math.BigInteger;
import java.util.List;

import bd.com.appcore.mvp.BasePresenterImpl;
import bd.com.appcore.mvp.IBaseView;
import bd.com.appcore.rx.LogicException;
import bd.com.appcore.rx.RxTaskCallBack;
import bd.com.appcore.rx.RxTaskScheduler;
import bd.com.appcore.util.AppSettings;
import bd.com.appcore.util.GsonUtil;
import bd.com.supply.module.transaction.SoSoConfig;
import bd.com.supply.module.transaction.model.domian.ProductBean;
import bd.com.supply.util.DateKit;
import bd.com.supply.web3.Web3Proxy;
import bd.com.supply.web3.contract.Authorized;
import bd.com.supply.web3.contract.PackingBox;
import bd.com.supply.web3.contract.Product;

public class AddProductInfoPresenter extends BasePresenterImpl<AddProductInfoPresenter.View> {
    private static final String TAG = AddProductInfoPresenter.class.getSimpleName();

    public void addProductInfo(final String productAddr, final String authAddr, final String title, String opContent, String boxAddr) {
        if (!TextUtils.isEmpty(boxAddr)) {
            addProductInfoToBox(authAddr, title, opContent, boxAddr);
            return;
        }
        long timeL = System.currentTimeMillis();
        String timeStr = DateKit.timeStampDate(timeL);
        final String peroid = SoSoConfig.generateByTitle(title);
        ProductBean productBean = new ProductBean();
        productBean.setTitle(title);
        productBean.setTime(timeStr);
        productBean.setPerio(peroid);
        productBean.setOpcontent(opContent);
        final String json = GsonUtil.objectToJson(productBean, ProductBean.class);
        BuglyLog.e(TAG, "addProductInfo =====>json=" + json);
        RxTaskScheduler.postIoMainTask(new RxTaskCallBack<Boolean>() {
            @Override
            public void onFailed(LogicException e) {
                super.onFailed(e);
                if (mView != null) {
                    mView.onAddInfoFailed(e.getErrorMsg());
                }
            }

            @Override
            public void onSuccess(Boolean aBoolean) {
                super.onSuccess(aBoolean);
                if (aBoolean && mView != null) {
                    mView.onAddInfoSuccess();
                } else {
                    mView.onAddInfoFailed("添加失败");
                }
            }

            @Override
            public Boolean doWork() throws Exception {
                String walletAddr = AppSettings.getAppSettings().getCurrentAddress();
                Authorized authorized = Authorized.load(authAddr, Web3Proxy.getWeb3Proxy().getWeb3j(), Web3Proxy.getWeb3Proxy().getPoolTransactionManager(), Web3Proxy.GAS_PRICE, Web3Proxy.GAS_LIMIT);
                boolean hasAuth = authorized.isAuthorized(walletAddr, new BigInteger(peroid + "")).send().booleanValue();
                if (!hasAuth) {
                    throw new LogicException(new Exception("您暂无权限进行操作"), -1);
                }
                Product product = Product.load(productAddr, Web3Proxy.getWeb3Proxy().getWeb3j(), Web3Proxy.getWeb3Proxy().getPoolTransactionManager(), Web3Proxy.GAS_PRICE, Web3Proxy.DEPLOY_GAS_LIMIT);
                TransactionReceipt receipt = product.appendEntry(new BigInteger(peroid + ""), json).send();
                if (receipt != null) {
                    List<Log> logs = receipt.getLogs();
                    if (logs == null || logs.size() == 0) {
                        throw new LogicException(new Exception("添加失败，请检查操作权限"), -1);
                    }
                    String hash = receipt.getBlockHash();
                    if (!TextUtils.isEmpty(hash) && !hash.startsWith("0x0000")) {
                        return true;
                    }
                }
                return false;
            }
        });

    }

    private void addProductInfoToBox(final String authAddr, final String title, String opContent, final String boxAddr) {
        long timeL = System.currentTimeMillis();
        String timeStr = DateKit.timeStampDate(timeL);
        final String peroid = SoSoConfig.generateByTitle(title);
        ProductBean productBean = new ProductBean();
        productBean.setTitle(title);
        productBean.setTime(timeStr);
        productBean.setPerio(peroid);
        productBean.setOpcontent(opContent);
        final String json = GsonUtil.objectToJson(productBean, ProductBean.class);
        BuglyLog.e(TAG, "addProductInfo =====>json=" + json);
        RxTaskScheduler.postIoMainTask(new RxTaskCallBack<Boolean>() {
            @Override
            public void onFailed(LogicException e) {
                super.onFailed(e);
                if (mView != null) {
                    mView.onAddInfoFailed(e.getErrorMsg());
                }
            }

            @Override
            public void onSuccess(Boolean aBoolean) {
                super.onSuccess(aBoolean);
                if (aBoolean && mView != null) {
                    mView.onAddInfoSuccess();
                } else {
                    mView.onAddInfoFailed("添加失败");
                }
            }

            @Override
            public Boolean doWork() throws Exception {
                String walletAddr = AppSettings.getAppSettings().getCurrentAddress();
                Authorized authorized = Authorized.load(authAddr, Web3Proxy.getWeb3Proxy().getWeb3j(), Web3Proxy.getWeb3Proxy().getPoolTransactionManager(), Web3Proxy.GAS_PRICE, Web3Proxy.GAS_LIMIT);
                boolean hasAuth = authorized.isAuthorized(walletAddr, new BigInteger(peroid + "")).send().booleanValue();
                if (!hasAuth) {
                    throw new LogicException(new Exception("您暂无权限进行操作"), -1);
                }
                PackingBox box = PackingBox.load(boxAddr, Web3Proxy.getWeb3Proxy().getWeb3j(), Web3Proxy.getWeb3Proxy().getPoolTransactionManager(), Web3Proxy.GAS_PRICE, Web3Proxy.DEPLOY_GAS_LIMIT);
                TransactionReceipt receipt = box.appendEntry(new BigInteger(peroid + ""), json).send();
                if (receipt != null) {
                    List<Log> logs = receipt.getLogs();
                    if (logs == null || logs.size() == 0) {
                        throw new LogicException(new Exception("添加失败，请检查操作权限"), -1);
                    }
                    String hash = receipt.getBlockHash();
                    if (!TextUtils.isEmpty(hash) && !hash.startsWith("0x0000")) {
                        return true;
                    }
                }
                return false;
            }
        });
    }

    public interface View extends IBaseView {

        void onAddInfoSuccess();

        void onAddInfoFailed(String msg);
    }
}
