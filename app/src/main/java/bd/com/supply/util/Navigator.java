package bd.com.supply.util;

import android.app.Activity;

import bd.com.supply.module.user.lockpattern.LockPatternCloseActivity;
import bd.com.supply.module.user.lockpattern.LockPatternResetActivity;
import bd.com.supply.module.user.lockpattern.LockPatternSetUpActivity;
import bd.com.supply.module.user.lockpattern.LockPatternUnlockActivity;
import bd.com.supply.module.user.lockpattern.LockPatternCloseActivity;
import bd.com.supply.module.user.lockpattern.LockPatternResetActivity;
import bd.com.supply.module.user.lockpattern.LockPatternSetUpActivity;
import bd.com.supply.module.user.lockpattern.LockPatternUnlockActivity;


/**
 * Created by 徐敏 on 2017/8/14.
 */

public class Navigator {

    public static void toLockPatternSetUp(Activity activity) {
        activity.startActivity(LockPatternSetUpActivity.getStartIntent(activity));
    }

    public static void toLockPatternUnlock(Activity activity) {
        activity.startActivity(LockPatternUnlockActivity.getStartIntent(activity));
    }

    public static void toLockPatternClose(Activity activity) {
        activity.startActivity(LockPatternCloseActivity.getStartIntent(activity));
    }

    public static void toLockPatternReset(Activity activity) {
        activity.startActivity(LockPatternResetActivity.getStartIntent(activity));
    }


}
