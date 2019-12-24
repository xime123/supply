package bd.com.supply.module.user.lockpattern;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.text.TextUtils;
import android.view.ViewGroup;
import android.widget.TextView;


import java.util.List;

import bd.com.appcore.ui.activity.BaseUiActivity;
import bd.com.appcore.util.AppSettings;
import bd.com.appcore.util.TDString;
import bd.com.supply.R;
import bd.com.supply.widget.lockpattern.LockPatternIndicator;
import bd.com.supply.widget.lockpattern.LockPatternUtil;
import bd.com.supply.widget.lockpattern.LockPatternView;
import bd.com.supply.widget.lockpattern.LockPatternIndicator;
import bd.com.supply.widget.lockpattern.LockPatternUtil;
import bd.com.supply.widget.lockpattern.LockPatternView;

import static bd.com.supply.module.user.lockpattern.LockPatternHelper.updateLockPatternMode;


public class LockPatternSetUpActivity extends BaseUiActivity<LockPatternPresenter, LockPatternPresenter.View> implements LockPatternPresenter.View, LockPatternView.OnPatternListener {
    LockPatternView vLockPattern;
    TextView tvMsg;
    LockPatternIndicator vIndicator;
    private String mFirstPatternText;

    public static Intent getStartIntent(Context context) {
        return IntentHelper.buildActivity(context, LockPatternSetUpActivity.class);
    }


    @Override
    protected LockPatternPresenter initPresenter() {
        return new LockPatternPresenter();
    }

    @Override
    protected LockPatternPresenter.View initView() {
        return this;
    }

    @Override
    protected void initTooBar() {
        super.initTooBar();
        actionBar.setTitle(TDString.getStr(R.string.lockpattern_input));
    }

    @Override
    protected int getLayoutId() {
        return R.layout.activity_lock_pattern_set_up;
    }

    @Override
    protected void initContentView(ViewGroup containerView) {
        super.initContentView(containerView);
        vLockPattern = (LockPatternView) findViewById(R.id.lockpattern_view);
        vIndicator = (LockPatternIndicator) findViewById(R.id.lockpattern_indicator);
        tvMsg = (TextView) findViewById(R.id.lockpattern_msg_tv);
        vLockPattern.setOnPatternListener(this);
        reset();
    }


    @Override
    protected void onPostCreate(@Nullable Bundle savedInstanceState) {
        super.onPostCreate(savedInstanceState);

    }

    private void reset() {
        vIndicator.setDefaultIndicator();
        vLockPattern.setEnabled(true);

        updateLockPatternMode(tvMsg, vLockPattern, getString(R.string.lockpattern_input), LockPatternView.DisplayMode.DEFAULT);
        vLockPattern.removePostClearPatternRunnable();

        mFirstPatternText = null;
    }

    @Override
    public void onPatternStart() {
        vIndicator.setDefaultIndicator();

        vLockPattern.removePostClearPatternRunnable();
        vLockPattern.setPattern(LockPatternView.DisplayMode.DEFAULT);
    }

    @Override
    public void onPatternComplete(List<LockPatternView.Cell> pattern) {
        invalidateOptionsMenu();
        vIndicator.setIndicator(pattern);

        if (LockPatternHelper.isLess4Dot(pattern, tvMsg, vLockPattern)) {
            return;
        }

        String patternText = LockPatternUtil.getPatternText(pattern);

        if (TextUtils.isEmpty(mFirstPatternText)) {
            mFirstPatternText = patternText;
            updateLockPatternMode(tvMsg, vLockPattern, getString(R.string.lockpattern_again_confirm), LockPatternView.DisplayMode.DEFAULT);
        } else if (!TextUtils.equals(mFirstPatternText, patternText)) {
            updateLockPatternMode(tvMsg, vLockPattern, getString(R.string.lockpattern_error_retry), LockPatternView.DisplayMode.ERROR);
        } else {
            vLockPattern.setEnabled(false);
            onSetLockPatternSuccess(patternText);
        }
    }

    private void onSetLockPatternSuccess(String patternText) {
        AppSettings.getAppSettings().setLockPattern(patternText);
        showToast("设置成功");
        finish();
    }

}
