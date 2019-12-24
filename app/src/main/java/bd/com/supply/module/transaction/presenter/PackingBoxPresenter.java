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
import bd.com.supply.module.transaction.model.req.LegalForBoxReq;
import bd.com.supply.module.transaction.model.req.ReportBixBoxReq;
import bd.com.supply.module.transaction.model.resp.LegalForBoxResp;
import bd.com.supply.module.transaction.model.resp.ReportBigBoxResp;
import bd.com.supply.web3.Web3Proxy;
import bd.com.supply.web3.contract.Authorized;
import bd.com.supply.web3.contract.BigPackingBox;

public class PackingBoxPresenter extends BasePresenterImpl<PackingBoxPresenter.View> {
    public void getProd(String prodAddr) {
        ProductOri productOri = new ProductOri();
        productOri.setPaddr(prodAddr);
        List<ProductOri> productOris = new ArrayList<>();
        productOris.add(productOri);
        mView.loadMoreSuccess(productOris);
    }

    /**
     * 注意 调这个方法要检查密码
     *
     * @param bigBoxAddr
     * @param boxAddrList
     * @param authAddr
     */
    public void packBoxList(final String bigBoxAddr, final List<String> boxAddrList, final String authAddr) {
        //扫码若干个包装码后点击打包，此时需要调用中心服务接口  /sy/isLegalForBox.json  来判断所有选择的包装盒是 否属于同一个鉴权；
        LegalForBoxReq req = new LegalForBoxReq();
        req.aaddr = authAddr;
        req.address = AppSettings.getAppSettings().getCurrentAddress();
        req.bigaddr = bigBoxAddr;
        req.blist = boxAddrList;
        mView.showLoadingDialog();
        new LegalForBoxResp.Builder().addBody(req).buildAsync(new ModelCallBack<LegalForBoxResp>() {
            @Override
            public void onResponseFailed(int errorCode, String msg) {
                if (mView != null) {
                    mView.hideLoadingDialog();
                    mView.packingFailed(msg);
                }

            }

            @Override
            public void onResponseSuccess(LegalForBoxResp data) {
                if (mView != null) {
                    mView.hideLoadingDialog();
                    if (data != null && ("1".equals(data.isLegal))) {
                        startPackBoxList(bigBoxAddr, boxAddrList, authAddr);
                    } else {
                        if (data.plist != null && data.plist.size() > 0) {
                            boxAddrList.removeAll(data.plist);
                        }
                        startPackBoxList(bigBoxAddr, boxAddrList, authAddr);
                        // mView.unLegalForBox("盒子列表地址不合法");
                    }
                }
            }
        });


    }

    private void startPackBoxList(final String bigBoxAddr, final List<String> boxAddrList, final String authAddr) {
        if (mView != null) {
            mView.showLoadingDialog();
        }
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
                    report(authAddr, bigBoxAddr, boxAddrList);
                }
            }

            @Override
            public Boolean doWork() throws Exception {
                BigPackingBox bigPackingBox = BigPackingBox.load(bigBoxAddr, Web3Proxy.getWeb3Proxy().getWeb3j(), Web3Proxy.getWeb3Proxy().getPoolTransactionManager(), Web3Proxy.GAS_PRICE, Web3Proxy.PACK_SOSO_GAS_LIMIT);

                String packAuthAddr = null;
                try {

                    packAuthAddr = bigPackingBox.packAuthIdentify().send();

                } catch (Exception e) {
                    e.printStackTrace();
                }
                if (!TextUtils.isEmpty(packAuthAddr)) {
                    Authorized authorized = Authorized.load(packAuthAddr, Web3Proxy.getWeb3Proxy().getWeb3j(), Web3Proxy.getCredentials(), Web3Proxy.GAS_PRICE, Web3Proxy.PACK_SOSO_GAS_LIMIT);
                    boolean isAuthed = authorized.isAuthorizedForPack(AppSettings.getAppSettings().getCurrentAddress(), new BigInteger("1")).send();
                    if (!isAuthed) {
                        throw new LogicException(new Exception("您暂无权限打包"), -100);
                    }
                }

                if (bigPackingBox.once().send().intValue() == 1) {
                    throw new LogicException(new Exception("该盒子已经打过包"), -100);
                }
                TransactionReceipt receipt = bigPackingBox.addPackingBox(boxAddrList, authAddr).send();
                String txHash = receipt.getTransactionHash();
                List<Log> logs = receipt.getLogs();
                if (logs == null || logs.size() == 0) {
                    throw new LogicException(new Exception("打包失败"), -100);
                }
                return true;
            }
        });
    }

    private void report(String aaddr, String bigaddr, List<String> blist) {
        ReportBixBoxReq req = new ReportBixBoxReq();
        req.address = AppSettings.getAppSettings().getCurrentAddress();
        req.aaddr = aaddr;
        req.bigaddr = bigaddr;
        req.blist = blist;
        mView.showLoadingDialog();
        new ReportBigBoxResp.Builder().addBody(req).buildAsync(new ModelCallBack<ReportBigBoxResp>() {
            @Override
            public void onResponseFailed(int errorCode, String msg) {
                if (mView != null) {
                    mView.hideLoadingDialog();
                    mView.packingFailed(msg);
                }
            }

            @Override
            public void onResponseSuccess(ReportBigBoxResp data) {
                if (mView != null) {
                    mView.hideLoadingDialog();
                    mView.packingSuccess();
                }
            }
        });
    }

    public interface View extends IListView<ProductOri> {
        void packingFailed(String msg);

        void unLegalForBox(String msg);

        void packingSuccess();

        void reportFailed(String msg);

        void reportSuccess();
    }
}
