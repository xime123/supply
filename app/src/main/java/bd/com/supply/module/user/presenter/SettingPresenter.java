package bd.com.supply.module.user.presenter;

import android.app.Activity;
import android.content.Context;
import android.util.Log;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import bd.com.appcore.base.ModelCallBack;
import bd.com.appcore.mvp.BasePresenterImpl;
import bd.com.appcore.mvp.IBaseView;
import bd.com.appcore.rx.RxTaskCallBack;
import bd.com.appcore.rx.RxTaskScheduler;
import bd.com.appupdate.util.UpdateAppUtils;
import bd.com.supply.module.common.APPUpdateManager;
import bd.com.supply.module.common.UpdateInfo;
import bd.com.supply.module.wallet.model.TokenModel;
import bd.com.supply.module.wallet.model.domain.ChainListListResp;
import bd.com.supply.util.APKVersionUtil;
import bd.com.supply.util.AppCacheUitl;
import bd.com.supply.module.common.APPUpdateManager;
import bd.com.supply.module.common.UpdateInfo;
import bd.com.supply.module.wallet.model.TokenModel;
import bd.com.supply.module.wallet.model.domain.ChainListListResp;
import bd.com.walletdb.entity.ChainEntity;
import bd.com.walletdb.manager.ChainManager;

public class SettingPresenter extends BasePresenterImpl<SettingPresenter.View> {
    private static final String YES = "Y";
    private static final String NO = "N";

    public void doClearCache(final Context context) {
        if (mView != null) {
            mView.showLoadingDialog();
            RxTaskScheduler.postLogicMainTask(new RxTaskCallBack<Boolean>() {
                @Override
                public void onSuccess(Boolean aBoolean) {
                    super.onSuccess(aBoolean);
                    if (mView != null) {
                        mView.hideLoadingDialog();
                        mView.onClearSuccess();
                    }
                }

                @Override
                public Boolean doWork() throws Exception {
                    Thread.sleep(500);
                    resetDB();
                    AppCacheUitl.clearAllCache(context);
                    return true;
                }
            });
        }
    }

    public void getCacheSize(final Context context) {
        RxTaskScheduler.postIoMainTask(new RxTaskCallBack<String>() {
            @Override
            public void onSuccess(String s) {
                super.onSuccess(s);
                if (mView != null) {
                    mView.onGetCacheSize(s);
                }
            }

            @Override
            public String doWork() throws Exception {
                return AppCacheUitl.getTotalCacheSize(context);
            }
        });

    }

    private void resetDB() {
        AppCacheUitl.resetDB();
    }


    /**
     * 检查更新
     *
     * @param context
     */
    public void checkUpdate(Context context) {
        int code = APKVersionUtil.getVersionCode(context);
        if (mView != null) {
            mView.showLoadingDialog();
        }
        APPUpdateManager.getUpdateManager().checkUpdate(code, new ModelCallBack<UpdateInfo>() {
            @Override
            public void onResponseFailed(int errorCode, String msg) {
                if (mView != null) {
                    mView.hideLoadingDialog();
                }
                Log.i(TAG, "checkUpdate====================>msg=" + msg);
            }

            @Override
            public void onResponseSuccess(UpdateInfo updateInfo) {
                if (mView != null) {
                    mView.hideLoadingDialog();
                }
                if (updateInfo != null) {
                    if (NO.equals(updateInfo.getIsNew())) {//需要更新
                        mView.checkedUpdate(updateInfo);
                    }else {
                        mView.checkedUpdateFailed("已经是最新版本");
                    }
                }
            }
        });
    }

    public void getChainList() {
        Map<String, Object> params = new HashMap<>();
        params.put("pageNumber", 1);
        params.put("pageSize", 100);
        TokenModel.getModel().getChainList(params, new ModelCallBack<ChainListListResp>() {
            @Override
            public void onResponseFailed(int errorCode, String msg) {
                if (mView != null) {
                    //获取失败从数据库拿缓存
                    List<ChainEntity> chainEntityList = ChainManager.getManager().getChainList();
                    if (chainEntityList != null && !chainEntityList.isEmpty()) {
                        mView.onGetChainListSuccess(chainEntityList);
                        return;
                    }
                    mView.onGetChainListFailed(msg);
                }
            }

            @Override
            public void onResponseSuccess(ChainListListResp data) {
                if (mView != null) {
                    List<ChainEntity> remoteDatas = data.getList();
                    mView.onGetChainListSuccess(remoteDatas);
                }
            }
        });
    }

    public void realUpdate(Activity activity, UpdateInfo info, UpdateAppUtils.OnCancelClicked cancelClicked) {
        UpdateAppUtils.from(activity)
                .setOnCancelClicked(cancelClicked)
                .isForce(YES.equals(info.getIsForceUpgrade()))
                .serverVersionName(info.getDisplayVer())
                .apkPath(info.getDownloadUrl()) //最新apk下载地址
                .needFitAndroidN(true)
                .updateInfo(info.getDescription())
                .apkDigest(info.getChecksum())
                .update();
    }

    public interface View extends IBaseView {
        void onGetCacheSize(String size);

        void onClearSuccess();

        void checkedUpdate(UpdateInfo info);
        void checkedUpdateFailed(String info);

        void onGetChainListSuccess(List<ChainEntity> chainEntityList);

        void onGetChainListFailed(String msg);
    }


}
