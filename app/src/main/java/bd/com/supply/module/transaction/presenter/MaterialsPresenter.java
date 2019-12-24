package bd.com.supply.module.transaction.presenter;

import android.text.TextUtils;

import com.tencent.bugly.crashreport.BuglyLog;

import org.web3j.protocol.core.methods.response.Log;
import org.web3j.protocol.core.methods.response.TransactionReceipt;

import java.util.List;

import bd.com.appcore.mvp.BasePresenterImpl;
import bd.com.appcore.mvp.IBaseView;
import bd.com.appcore.rx.LogicException;
import bd.com.appcore.rx.RxTaskCallBack;
import bd.com.appcore.rx.RxTaskScheduler;
import bd.com.supply.web3.Web3Proxy;
import bd.com.supply.web3.contract.Category;

public class MaterialsPresenter extends BasePresenterImpl<MaterialsPresenter.View> {
    private static final String TAG = MaterialsPresenter.class.getSimpleName();

    public void updateCategoryInfo(final String categoryAddr, final String _technology, final String _material, final String _operatorName) {
        RxTaskScheduler.postIoMainTask(new RxTaskCallBack<Boolean>() {
            @Override
            public void onFailed(LogicException e) {
                super.onFailed(e);
                if (mView != null) {
                    mView.onUpdateFailed(e.getErrorMsg());
                }
            }

            @Override
            public void onSuccess(Boolean aBoolean) {
                super.onSuccess(aBoolean);
                if (mView != null && aBoolean) {
                    mView.onUpdateSuccess();
                }else {
                    mView.onUpdateFailed("添加失败");
                }
            }

            @Override
            public Boolean doWork() throws Exception {
                Category category = Category.load(categoryAddr, Web3Proxy.getWeb3Proxy().getWeb3j(), Web3Proxy.getWeb3Proxy().getPoolTransactionManager(), Web3Proxy.GAS_PRICE, Web3Proxy.DEPLOY_GAS_LIMIT);
                TransactionReceipt receipt = category.updateBaseInfo(_technology, _material, _operatorName).send();
                if (receipt != null) {
                    List<Log> logs = receipt.getLogs();
                    if (logs == null || logs.size() == 0) {
                        return false;
                    }
                    String hash = receipt.getBlockHash();
                    BuglyLog.e(TAG, "updateCategoryInfo ======>hash=" + hash);
                    if (!TextUtils.isEmpty(hash) && !hash.startsWith("0x0000")) {
                        return true;
                    }
                }
                return false;
            }
        });

    }

    public interface View extends IBaseView {
        void onUpdateSuccess();

        void onUpdateFailed(String msg);
    }


}
