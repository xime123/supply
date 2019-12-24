package bd.com.supply;

import android.app.Activity;
import android.content.Context;
import android.util.Log;

import bd.com.appcore.base.ModelCallBack;
import bd.com.appcore.mvp.BasePresenter;
import bd.com.appcore.mvp.IView;
import bd.com.appupdate.util.UpdateAppUtils;
import bd.com.supply.module.common.APPUpdateManager;
import bd.com.supply.module.common.UpdateInfo;
import bd.com.supply.util.APKVersionUtil;
import bd.com.supply.module.common.APPUpdateManager;
import bd.com.supply.module.common.UpdateInfo;


public class MainPresenter extends BasePresenter<MainPresenter.View> {
    private static final String YES = "Y";
    private static final String NO = "N";

    /**
     * 检查更新
     *
     * @param context
     */
    public void checkUpdate(Context context) {
        int code = APKVersionUtil.getVersionCode(context);
        APPUpdateManager.getUpdateManager().checkUpdate(code, new ModelCallBack<UpdateInfo>() {
            @Override
            public void onResponseFailed(int errorCode, String msg) {
                Log.i(TAG, "checkUpdate====================>msg=" + msg);
                if (mView != null) {
                    mView.checkeUpdateFailed();
                }
            }

            @Override
            public void onResponseSuccess(UpdateInfo updateInfo) {
                if (updateInfo != null&&mView!=null) {
                    if (NO.equals(updateInfo.getIsNew())) {//需要更新
                        mView.checkedUpdate(updateInfo);
                    }else {
                        mView.checkeUpdateFailed();
                    }
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


    public interface View extends IView {
        /**
         * 检查到需要版本更新
         */
        void checkedUpdate(UpdateInfo updateInfo);

        void checkeUpdateFailed();
    }
}
