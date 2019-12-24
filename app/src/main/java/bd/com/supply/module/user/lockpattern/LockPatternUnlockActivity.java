package bd.com.supply.module.user.lockpattern;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.text.TextUtils;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;


import java.util.List;

import bd.com.appcore.ui.activity.BaseUiActivity;
import bd.com.appcore.util.AppSettings;
import bd.com.appcore.util.TDString;
import bd.com.supply.R;
import bd.com.supply.widget.VerifyPwdDialog;
import bd.com.supply.widget.lockpattern.LockPatternIndicator;
import bd.com.supply.widget.lockpattern.LockPatternUtil;
import bd.com.supply.widget.lockpattern.LockPatternView;
import bd.com.supply.widget.VerifyPwdDialog;
import bd.com.supply.widget.lockpattern.LockPatternIndicator;
import bd.com.supply.widget.lockpattern.LockPatternUtil;
import bd.com.supply.widget.lockpattern.LockPatternView;
import bd.com.walletdb.entity.WalletEntity;
import bd.com.walletdb.manager.WalletDBManager;

import static bd.com.supply.module.user.lockpattern.LockPatternHelper.updateLockPatternMode;


/**
 * 图案解锁页面  <br/>
 */
public class LockPatternUnlockActivity extends BaseUiActivity<LockPatternPresenter, LockPatternPresenter.View> implements LockPatternPresenter.View, LockPatternView.OnPatternListener {

    private static final int MAX_ERROR_COUNT = 5;

    LockPatternView vLockPattern;
    LockPatternIndicator vIndicator;
    TextView tvAccount;
    TextView tvMsg;
    private Button loginBtn;
    private int mErrorCount;

    public static Intent getStartIntent(Context context) {
        return IntentHelper.buildActivity(context, LockPatternUnlockActivity.class);
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
        actionBar.setVisibility(View.GONE);
    }

    @Override
    protected void initContentView(ViewGroup containerView) {
        super.initContentView(containerView);
        vLockPattern = (LockPatternView) findViewById(R.id.lockpattern_view);
        vIndicator = (LockPatternIndicator) findViewById(R.id.lockpattern_indicator);
        tvMsg = (TextView) findViewById(R.id.lockpattern_msg_tv);
        loginBtn = (Button) findViewById(R.id.lockpattern_login_by_pwd_btn);
        tvAccount = (TextView) findViewById(R.id.lockpattern_account_tv);
        String address = AppSettings.getAppSettings().getCurrentAddress();
        String mobileNumber = WalletDBManager.getManager().getWalletEntity(address).getName();
        tvAccount.setText("账号:" + mobileNumber);
        vLockPattern.setOnPatternListener(this);
    }


    @Override
    protected void setListener() {
        super.setListener();
        loginBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onChangeLoginClick();
            }
        });
    }

    @Override
    protected int getLayoutId() {
        return R.layout.activity_lock_pattern_unlock;
    }

    @Override
    protected void onPostCreate(@Nullable Bundle savedInstanceState) {
        super.onPostCreate(savedInstanceState);

    }


    private void onChangeLoginClick() {
//        UserManager.getInstance().logout();
//        Navigator.launchLoginActivity(LockPatternUnlockActivity.this);
//        finish();
        onCloseByPwdClick();
    }


    private void onCloseByPwdClick() {
        final VerifyPwdDialog dialog = new VerifyPwdDialog(this);

        dialog.setOnOkClickListener(new VerifyPwdDialog.OnOkClickListener() {
            @Override
            public void onOkClick(String pwd) {
                dialog.dismiss();
                validAccount(pwd);
            }
        });
        dialog.show();
    }

    private void validAccount(String pwd) {
        String address = AppSettings.getAppSettings().getCurrentAddress();
        WalletEntity entity = WalletDBManager.getManager().getWalletEntity(address);
        if (pwd.equals(entity.getPassword())){
            onAccountValidateSuccess();
        }else {
            showToast("密码错误");
            onAccountValidateFailure();
        }

//        String userid= UserDataManager.getManager().getCurrentAccount();
//        boolean code= Web3jServerProxy.shareInstance().validataPasswdAndInit(pwd,userid);
//        if(code){
//            onAccountValidateSuccess();
//        }else {
//            ToastUtil.toast(this, TDString.getStr(R.string.login_password_error));
//            onAccountValidateFailure();
//        }
    }

    public void onAccountValidateSuccess() {
        finish();
    }


    public void onAccountValidateFailure() {
        onCloseByPwdClick();
    }

    @Override
    public void onPatternStart() {

        vLockPattern.removePostClearPatternRunnable();
        vLockPattern.setPattern(LockPatternView.DisplayMode.DEFAULT);
    }

    @Override
    public void onPatternComplete(List<LockPatternView.Cell> pattern) {
        if (LockPatternHelper.isLess4Dot(pattern, tvMsg, vLockPattern)) {
            return;
        }

        String patternText = LockPatternUtil.getPatternText(pattern);
        boolean isUnlock = TextUtils.equals(patternText, AppSettings.getAppSettings().getLockPattern());
        if (isUnlock) {
            finish();
            return;
        }

        mErrorCount++;

        if (mErrorCount >= MAX_ERROR_COUNT) {
            AppSettings.getAppSettings().setLockPattern(null);
            //AppSettings.getAppSettings().setHasLogin(false);
            tvMsg.setText(null);
            AlertDialog dialog = new AlertDialog.Builder(this)
                    .setMessage(getString(R.string.unlock_pattern_failure_relogin))
                    .setPositiveButton(R.string.re_login, new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            //Navigator.launchLoginActivityAndClear(LockPatternUnlockActivity.this);
                            //  UserManager.getInstance().logout();
                            //finish();
                            exit();
                        }
                    }).create();
            dialog.setCanceledOnTouchOutside(false);
            dialog.setCancelable(false);
            dialog.show();
        } else {
            updateLockPatternMode(tvMsg, vLockPattern,
                    getString(R.string.lockpattern_error_count, MAX_ERROR_COUNT - mErrorCount),
                    LockPatternView.DisplayMode.ERROR);
        }
    }

    @Override
    public void onBackPressed() {
        //强制进入后台
        if (!moveTaskToBack(true)) {
            super.onBackPressed();
        }
    }

}
