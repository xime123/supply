package bd.com.supply.module.user.lockpattern;

import android.app.Activity;
import android.util.Log;

import java.util.ArrayList;
import java.util.List;

import bd.com.appcore.base.SimpleActivityLifecycleCallbacks;
import bd.com.appcore.util.AppSettings;
import bd.com.supply.module.start.SplashActivity;
import bd.com.supply.util.Navigator;
import bd.com.supply.module.start.SplashActivity;


public class LockPatternCallback extends SimpleActivityLifecycleCallbacks {
    private final static String TAG = LockPatternCallback.class.getSimpleName();
    private long mLastUnlockTimeMillis = -1;
    private static final List<String> IGNORE_ACTIVITIES = new ArrayList<>();

    static {
        IGNORE_ACTIVITIES.add(SplashActivity.class.getName());
    }


    public LockPatternCallback() {
    }

    private List<String> mStartedActivity = new ArrayList<>();

    @Override
    public void onActivityStarted(Activity activity) {
        String className = activity.getClass().getName();
        if (IGNORE_ACTIVITIES.contains(className)) {
            return;
        }


        //boolean hasLogin = AppSettings.getAppSettings().getHasLogin();
        boolean isLockPatternOpen = AppSettings.getAppSettings().isLockPatternOpen();
        //没有已启动的Activity，表示从后台进入。
        Log.i(TAG, "- 打开的activity" + className + activity.getTitle());
        if (/*hasLogin && */!IGNORE_ACTIVITIES.contains(className) && isLockPatternOpen && mStartedActivity.isEmpty()) {

            Log.i(TAG, "- 进入前台" + className + activity.getTitle());
            Navigator.toLockPatternUnlock(activity);
//            long needTime = AppSettings.getAppSettings().getLockPatternTime();
//            if (needTime != -1) {
//                boolean isOverTime = (System.currentTimeMillis() - mLastUnlockTimeMillis) > needTime;
//                if (isOverTime) {
//                    Navigator.toLockPatternUnlock(activity);
//                }
//            }
        }
        mStartedActivity.add(activity.toString());
    }


    @Override
    public void onActivityStopped(Activity activity) {
        String className = activity.getClass().getName();
        if (IGNORE_ACTIVITIES.contains(className)) {
            return;
        }

        mStartedActivity.remove(activity.toString());
        //没有已启动的Activity，表示进入后台。
        if (mStartedActivity.isEmpty()) {
            Log.i(TAG, "- 进入后台" + className + activity.getTitle());
            mLastUnlockTimeMillis = System.currentTimeMillis();
        }
    }


}
