package bd.com.supply.module.user.lockpattern;

import android.support.annotation.NonNull;
import android.widget.TextView;

import java.util.List;

import bd.com.supply.R;
import bd.com.supply.widget.lockpattern.LockPatternView;
import bd.com.supply.widget.lockpattern.LockPatternView;


class LockPatternHelper {

    static boolean isLess4Dot(@NonNull List<LockPatternView.Cell> pattern,
                              @NonNull TextView tvMsg, @NonNull LockPatternView vLockPattern) {
        if (pattern.size() < 4) {
            updateLockPatternMode(tvMsg, vLockPattern,
                    tvMsg.getResources().getString(R.string.lockpattern_less_4_dot),
                    LockPatternView.DisplayMode.NORMAL);
            tvMsg.setTextColor(tvMsg.getResources().getColor(R.color.red_click));
            return true;
        }
        return false;
    }

    static void updateLockPatternMode(@NonNull TextView tvMsg, @NonNull LockPatternView vLockPattern,
                                      String message, LockPatternView.DisplayMode displayMode) {
        tvMsg.setText(message);
        switch (displayMode) {
            case DEFAULT:
            case NORMAL:
                tvMsg.setTextColor(tvMsg.getResources().getColor(R.color.black_3));
                break;
            case ERROR:
                tvMsg.setTextColor(tvMsg.getResources().getColor(R.color.red_click));
                break;
        }
        vLockPattern.setPattern(displayMode);
        vLockPattern.postClearPatternRunnable(800);
    }
}
