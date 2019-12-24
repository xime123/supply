package bd.com.supply.module.transaction.presenter;

import android.text.TextUtils;

import org.web3j.protocol.core.methods.response.Log;
import org.web3j.protocol.core.methods.response.TransactionReceipt;

import java.math.BigInteger;
import java.util.ArrayList;
import java.util.List;

import bd.com.appcore.base.ModelCallBack;
import bd.com.appcore.mvp.BasePresenterImpl;
import bd.com.appcore.mvp.IListView;
import bd.com.appcore.rx.LogicException;
import bd.com.appcore.rx.RxTaskCallBack;
import bd.com.appcore.rx.RxTaskScheduler;
import bd.com.appcore.util.AppSettings;
import bd.com.supply.module.transaction.model.domian.ProductOri;
import bd.com.supply.module.transaction.model.req.LegalForProdReq;
import bd.com.supply.module.transaction.model.req.ReportBixBoxReq;
import bd.com.supply.module.transaction.model.req.ReportProductReq;
import bd.com.supply.module.transaction.model.resp.LegalForProductResp;
import bd.com.supply.module.transaction.model.resp.ReportBigBoxResp;
import bd.com.supply.module.transaction.model.resp.ReportProductResp;
import bd.com.supply.web3.Web3Proxy;
import bd.com.supply.web3.contract.Authorized;
import bd.com.supply.web3.contract.BigPackingBox;
import bd.com.supply.web3.contract.PackingBox;

public class PackingProdPresenter extends BasePresenterImpl<PackingProdPresenter.View> {
    public void getProd(String prodAddr) {
        ProductOri productOri = new ProductOri();
        productOri.setPaddr(prodAddr);
        List<ProductOri> productOris = new ArrayList<>();
        productOris.add(productOri);
        mView.loadMoreSuccess(productOris);
    }

    public void getProd(List<String> prodList) {
        List<ProductOri> productOris = new ArrayList<>();
        for (String prodAddr : prodList) {
            ProductOri productOri = new ProductOri();
            productOri.setPaddr(prodAddr);
            productOris.add(productOri);
        }
        if (mView != null) {
            mView.remainProdOri(productOris);
        }
    }


    /**
     * 注意 调这个方法要检查密码
     */
    public void packProdList(final String boxAddr, final List<String> prodAddrList, final String authAddr) {
        //扫码若干个包装码后点击打包，此时需要调用中心服务接口  /sy/isLegalForBox.json  来判断所有选择的包装盒是 否属于同一个鉴权；
        LegalForProdReq req = new LegalForProdReq();
        req.aaddr = authAddr;
        req.address = AppSettings.getAppSettings().getCurrentAddress();
        req.baddr = boxAddr;
        req.plist = prodAddrList;
        mView.showLoadingDialog();
        new LegalForProductResp.Builder().addBody(req).buildAsync(new ModelCallBack<LegalForProductResp>() {
            @Override
            public void onResponseFailed(int errorCode, String msg) {
                if (mView != null) {
                    mView.hideLoadingDialog();
                    mView.packingFailed(msg);
                }

            }

            @Override
            public void onResponseSuccess(LegalForProductResp data) {
                if (mView != null) {
                    mView.hideLoadingDialog();
                    if (data != null && ("1".equals(data.isLegal))) {
                        startPackProdList(boxAddr, prodAddrList, authAddr);
                    } else {
                        if (data.plist != null && data.plist.size() > 0) {
                            prodAddrList.removeAll(data.plist);
                        }
                        getProd(prodAddrList);
                        // startPackProdList(boxAddr, prodAddrList, authAddr);
                        mView.unLegalForProd("产品列表地址不合法，请重新选择");
                    }

                }
            }
        });


    }

    /**
     * 注意 调这个方法要检查密码
     *
     * @param boxAddr
     * @param prodAddrList
     * @param authAddr
     */
    private void startPackProdList(final String boxAddr, final List<String> prodAddrList, final String authAddr) {
        mView.showLoadingDialog();
        RxTaskScheduler.postIoMainTask(new RxTaskCallBack<Boolean>() {
            @Override
            public void onFailed(LogicException e) {
                super.onFailed(e);
                if (mView != null) {
                    mView.hideLoadingDialog();
                    mView.packingFailed(e.getErrorMsg());
                }
            }

            @Override
            public void onSuccess(Boolean aBoolean) {
                super.onSuccess(aBoolean);
                if (mView != null) {
                    mView.hideLoadingDialog();
                    mView.packingSuccess();
                    report(authAddr,boxAddr, prodAddrList);
                }
            }

            @Override
            public Boolean doWork() throws Exception {
                PackingBox packingBox = PackingBox.load(boxAddr, Web3Proxy.getWeb3Proxy().getWeb3j(), Web3Proxy.getWeb3Proxy().getPoolTransactionManager(), Web3Proxy.GAS_PRICE, Web3Proxy.PACK_SOSO_GAS_LIMIT);
                String packAuthAddr = null;
                try {

                    packAuthAddr = packingBox.packAuthIdentify().send();

                } catch (Exception e) {
                    e.printStackTrace();
                }
                if (!TextUtils.isEmpty(packAuthAddr)) {
                    Authorized authorized = Authorized.load(packAuthAddr, Web3Proxy.getWeb3Proxy().getWeb3j(), Web3Proxy.getCredentials(), Web3Proxy.GAS_PRICE, Web3Proxy.PACK_SOSO_GAS_LIMIT);
                    boolean isAuthed = authorized.isAuthorizedForPack(AppSettings.getAppSettings().getCurrentAddress(), new BigInteger("0")).send();
                    if (!isAuthed) {
                        throw new LogicException(new Exception("您暂无权限打包"), -100);
                    }
                }


                if (packingBox.once().send().intValue() == 1) {
                    throw new LogicException(new Exception("该盒子已经打过包"), -100);
                }
                TransactionReceipt receipt = packingBox.addProducts(prodAddrList, authAddr).send();
                String txHash = receipt.getTransactionHash();
                List<Log> logs = receipt.getLogs();
                if (logs == null || logs.size() == 0) {
                    throw new LogicException(new Exception("打包失败"), -100);
                }
                return true;
            }
        });

    }

    private void report(String aaddr,String bAddr, List<String> pList) {
        ReportProductReq req = new ReportProductReq();
        req.address = AppSettings.getAppSettings().getCurrentAddress();
        req.baddr = bAddr;
        req.pList = pList;
        req.aaddr=aaddr;
        mView.showLoadingDialog();
        new ReportProductResp.Builder().addBody(req).buildAsync(new ModelCallBack<ReportProductResp>() {
            @Override
            public void onResponseFailed(int errorCode, String msg) {
                if (mView != null) {
                    mView.hideLoadingDialog();
                    mView.packingFailed(msg);
                }
            }

            @Override
            public void onResponseSuccess(ReportProductResp data) {
                if (mView != null) {
                    mView.hideLoadingDialog();
                    mView.packingSuccess();
                }
            }
        });
    }

    public interface View extends IListView<ProductOri> {
        void packingFailed(String msg);

        void packingSuccess();

        void reportFailed(String msg);

        void reportSuccess();

        void unLegalForProd(String errorMsg);

        void remainProdOri(List<ProductOri> remainOriList);
    }
}
