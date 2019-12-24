package bd.com.supply.module.user.lockpattern;

import android.content.Context;
import android.content.Intent;

import bd.com.supply.util.Navigator;


/**
 * 重置图案解锁页面  <br/>
 */
public class LockPatternResetActivity extends LockPatternCloseActivity {

    public static Intent getStartIntent(Context context) {
        return IntentHelper.buildActivity(context, LockPatternResetActivity.class);
    }

    @Override
    public void onAccountValidateSuccess() {
        Navigator.toLockPatternSetUp(this);
        finish();
    }

}
