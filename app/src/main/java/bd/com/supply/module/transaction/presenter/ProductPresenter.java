package bd.com.supply.module.transaction.presenter;

import android.text.TextUtils;

import java.util.ArrayList;
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
import bd.com.supply.module.transaction.model.domian.ProductOri;
import bd.com.supply.module.transaction.model.domian.ProductListResp;
import bd.com.supply.web3.Web3Proxy;
import bd.com.supply.web3.contract.Category;
import bd.com.supply.web3.contract.PackingBox;
import bd.com.supply.web3.contract.Product;

public class ProductPresenter extends BasePresenterImpl<ProductPresenter.View> {
    private Map<String, String> nameCache = new HashMap<>();

    public void getProductList(Map<String, Object> params) {
        params.put("address", AppSettings.getAppSettings().getCurrentAddress());
        SosoModel.getInstance().getProductList(params, new ModelCallBack<ProductListResp>() {
            @Override
            public void onResponseFailed(int errorCode, String msg) {
                if (mView != null) {
                    mView.loadFailed(msg);
                }
            }

            @Override
            public void onResponseSuccess(ProductListResp data) {
                if (mView != null) {
                    List<ProductOri> productOriList = data.getList();
                    setName(productOriList, false);
                }
            }
        });
    }

    private void setName(final List<ProductOri> productOriList, final boolean isMore) {
        RxTaskScheduler.postLogicMainTask(new RxTaskCallBack<List<ProductOri>>() {

            @Override
            public void onSuccess(List<ProductOri> productOris) {
                super.onSuccess(productOris);
                if (mView != null) {
                    if (!isMore) {
                        mView.loadSuccess(productOriList);
                    } else {
                        mView.loadMoreSuccess(productOriList);
                    }
                }
            }

            @Override
            public List<ProductOri> doWork() throws Exception {
                for (ProductOri ori : productOriList) {
                    String cacheName = nameCache.get(ori.getCaddr());
                    if (!TextUtils.isEmpty(cacheName)) {
                        ori.setName(cacheName);
                    } else {
                        try {
                            Category category = Category.load(ori.getCaddr(), Web3Proxy.getWeb3Proxy().getWeb3j(), Web3Proxy.getCredentials(), Web3Proxy.GAS_PRICE, Web3Proxy.DEPLOY_GAS_LIMIT);
                            String name = category.name().send();
                            ori.setName(name);
                            nameCache.put(ori.getCaddr(), name);
                        } catch (Exception e) {
                            ori.setName("unknow");
                        }

                    }
                }
                return productOriList;
            }
        });
    }

    public void getProductListByBox(final String boxAddres) {
        RxTaskScheduler.postIoMainTask(new RxTaskCallBack<List<ProductOri>>() {
            @Override
            public void onFailed(LogicException e) {
                super.onFailed(e);
                if (mView != null) {
                    mView.loadFailed(e.getErrorMsg());
                }
            }

            @Override
            public void onSuccess(List<ProductOri> productOris) {
                super.onSuccess(productOris);
                if (mView != null) {
                    mView.loadSuccess(productOris);
                }
            }

            @Override
            public List<ProductOri> doWork() throws Exception {
                List<ProductOri> productOriList = new ArrayList<>();
                PackingBox box = PackingBox.load(boxAddres, Web3Proxy.getWeb3Proxy().getWeb3j(), Web3Proxy.getWeb3Proxy().getPoolTransactionManager(), Web3Proxy.GAS_PRICE, Web3Proxy.GAS_LIMIT);
                String productAddres = box.productList().send();
                String productList[] = productAddres.split(SoSoConfig.PEROID_GAP);
                for (String productAddr : productList) {
                    ProductOri productOri = new ProductOri();
                    Product product = Product.load(productAddr, Web3Proxy.getWeb3Proxy().getWeb3j(), Web3Proxy.getWeb3Proxy().getPoolTransactionManager(), Web3Proxy.GAS_PRICE, Web3Proxy.GAS_LIMIT);
                    String categoryAddr = product.categoryIdentify().send();
                    Category category = Category.load(categoryAddr, Web3Proxy.getWeb3Proxy().getWeb3j(), Web3Proxy.getWeb3Proxy().getPoolTransactionManager(), Web3Proxy.GAS_PRICE, Web3Proxy.GAS_LIMIT);
                    String categoryInfo = category.getCategoryInfo().send();
                    String name = category.name().send();
                    productOri.setPaddr(productAddr);
                    productOri.setName(name);
                    productOri.setCaddr(categoryAddr);
                    productOri.setCreateTime(System.currentTimeMillis());
                    productOriList.add(productOri);
                }
                return productOriList;
            }
        });

    }

    public interface View extends IListView<ProductOri> {

    }
}
