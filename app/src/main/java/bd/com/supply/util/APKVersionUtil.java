package bd.com.supply.util;

import android.content.Context;
import android.content.pm.PackageManager;

import bd.com.supply.Config;
import bd.com.supply.app.BdApplication;
import bd.com.supply.app.BdApplication;


public class APKVersionUtil {
    /**
     * 获取当前本地apk的版本
     *
     * @param mContext
     * @return
     */
    public static int getVersionCode(Context mContext) {
        int versionCode = 0;
        try {
            //获取软件版本号，对应AndroidManifest.xml下android:versionCode
            versionCode = mContext.getPackageManager().
                    getPackageInfo(mContext.getPackageName(), 0).versionCode;
        } catch (PackageManager.NameNotFoundException e) {
            e.printStackTrace();
        }
        return versionCode;
    }

    /**
     * 获取版本号名称
     *
     * @param context 上下文
     * @return
     */
    public static String getVerName(Context context) {
        String verName = "";
        try {
            verName = context.getPackageManager().
                    getPackageInfo(context.getPackageName(), 0).versionName;
        } catch (PackageManager.NameNotFoundException e) {
            e.printStackTrace();
        }
        return verName;
    }

    public static String getDownLoadUrl() {
        int code = getVersionCode(BdApplication.context);
        String versionName = getVerName(BdApplication.context);
        String url = "http://116.62.160.218/" + code + "/app_versionCode_" + code + "_versionName_" + versionName + ".apk";
        return url;
    }
}
