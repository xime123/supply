package bd.com.supply.module.user.lockpattern;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.Fragment;


public class IntentHelper {

    public static Intent buildIntent(Context context, Class<?> clazz, Bundle extras) {
        Intent intent = new Intent(context, clazz);
        if (extras != null)
            intent.putExtras(extras);
        return intent;
    }


    public static Intent buildActivity(Fragment fragment, Class<? extends Activity> activity) {
        return buildActivity(fragment.getActivity(), activity, null);
    }
    public static Intent buildActivity(Context context, Class<? extends Activity> activity) {
        return buildActivity(context, activity, null);
    }
//    public static Intent buildActivity(Context context, Class<LockPatternResetActivity> activity) {
//        return buildActivity(context, activity, null);
//    }

    public static Intent buildActivity(Context context, Class<? extends Activity> activity, Bundle extras) {
        Intent intent = buildIntent(context, activity, extras);
        //设置 App 中 启动 Activity 不调用 onUserLeaveHint , 当 App 离开前台时,才会调用 onUserLeaveHint.
        intent.addFlags(Intent.FLAG_ACTIVITY_NO_USER_ACTION);
        return intent;
    }
    /**
     * 获得用于打电话的intent
     */
    public static Intent getCallNumberIntent(String number) {
        Intent intent = new Intent(Intent.ACTION_CALL, Uri.parse("tel:" + number));
        return intent;
    }
}
