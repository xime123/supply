package bd.com.supply.module.transaction.presenter;

import android.text.TextUtils;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import bd.com.appcore.base.ModelCallBack;
import bd.com.appcore.mvp.BasePresenterImpl;
import bd.com.appcore.mvp.IListView;
import bd.com.appcore.rx.LogicException;
import bd.com.appcore.rx.RxTaskCallBack;
import bd.com.appcore.rx.RxTaskScheduler;
import bd.com.appcore.util.AppSettings;
import bd.com.supply.module.transaction.SoSoConfig;
import bd.com.supply.module.transaction.model.SosoModel;
import bd.com.supply.module.transaction.model.domian.ProductListResp;
import bd.com.supply.module.transaction.model.domian.ProductOri;
import bd.com.supply.web3.Web3Proxy;
import bd.com.supply.web3.contract.BigPackingBox;
import bd.com.supply.web3.contract.Category;
import bd.com.supply.web3.contract.PackingBox;
import bd.com.supply.web3.contract.Product;

public class BoxPresenter extends BasePresenterImpl<BoxPresenter.View> {
    private Map<String, String> nameCache = new HashMap<>();

    public void getBoxList(final String bigBoxAddr) {
        RxTaskScheduler.postIoMainTask(new RxTaskCallBack<List<String>>() {
            @Override
            public void onFailed(LogicException e) {
                super.onFailed(e);
                if (mView != null) {
                    mView.loadFailed(e.getErrorMsg());
                }
            }

            @Override
            public void onSuccess(List<String> strings) {
                super.onSuccess(strings);
                if (mView != null) {
                    if (strings.isEmpty()) {
                        mView.loadEmpty();
                    } else {
                        mView.loadSuccess(strings);
                    }

                }
            }

            @Override
            public List<String> doWork() throws Exception {
                BigPackingBox bigBox = BigPackingBox.load(bigBoxAddr, Web3Proxy.getWeb3Proxy().getWeb3j(), Web3Proxy.getWeb3Proxy().getPoolTransactionManager(), Web3Proxy.GAS_PRICE, Web3Proxy.GAS_LIMIT);
                String boxOri = bigBox.boxList().send();
                if (TextUtils.isEmpty(boxOri)) {
                    return new ArrayList<>();
                }
                String boxs[] = boxOri.split(SoSoConfig.PEROID_GAP);
                return Arrays.asList(boxs);
            }
        });


    }


    public interface View extends IListView<String> {

    }
}
