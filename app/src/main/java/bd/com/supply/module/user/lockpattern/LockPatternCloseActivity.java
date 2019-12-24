package bd.com.supply.module.user.lockpattern;


import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.os.UserManager;
import android.support.annotation.Nullable;
import android.support.v7.app.AlertDialog;
import android.text.TextUtils;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;


import java.util.List;

import bd.com.appcore.mvp.BasePresenterImpl;
import bd.com.appcore.mvp.IBaseView;
import bd.com.appcore.ui.activity.BaseUiActivity;
import bd.com.appcore.util.AppSettings;
import bd.com.appcore.util.TDString;
import bd.com.supply.R;
import bd.com.supply.util.Navigator;
import bd.com.supply.widget.VerifyPwdDialog;
import bd.com.supply.widget.lockpattern.LockPatternIndicator;
import bd.com.supply.widget.lockpattern.LockPatternUtil;
import bd.com.supply.widget.lockpattern.LockPatternView;
import bd.com.supply.widget.VerifyPwdDialog;
import bd.com.supply.widget.lockpattern.LockPatternIndicator;
import bd.com.supply.widget.lockpattern.LockPatternUtil;
import bd.com.supply.widget.lockpattern.LockPatternView;


/**
 * 图案解锁页面  <br/>
 */
public class LockPatternCloseActivity extends BaseUiActivity<LockPatternPresenter,LockPatternPresenter.View> implements LockPatternPresenter.View,LockPatternView.OnPatternListener {

    private static final int MAX_ERROR_COUNT = 5;
    LockPatternView vLockPattern;
    TextView tvMsg;
    LockPatternIndicator vIndicator;
    private Button closeBtn;
    private int mErrorCount;
    private VerifyPwdDialog dialog;

    public static Intent getStartIntent(Context context) {
        return IntentHelper.buildActivity(context, LockPatternCloseActivity.class);
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
    protected void initContentView(ViewGroup containerView) {
        super.initContentView(containerView);
        vLockPattern=(LockPatternView)findViewById(R.id.lockpattern_view);
        vIndicator=(LockPatternIndicator)findViewById(R.id.lockpattern_indicator);
        tvMsg=(TextView) findViewById(R.id.lockpattern_msg_tv);
        closeBtn=(Button)findViewById(R.id.lockpattern_close_by_pwd_btn);
    }


    @Override
    protected void setListener() {
        super.setListener();
        vLockPattern.setOnPatternListener(this);
        closeBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onCloseByPwdClick();
            }
        });
    }

    @Override
    protected int getLayoutId() {
        return R.layout.activity_lock_pattern_close;
    }

    @Override
    protected void onPostCreate(@Nullable Bundle savedInstanceState) {
        super.onPostCreate(savedInstanceState);

    }


    void reLogin() {
       // Navigator.launchLoginActivityAndClear(this);

        finish();
    }

    private void validAccount(String pwd){
//        String userid= UserDataManager.getManager().getCurrentAccount();
//        boolean code=Web3jServerProxy.shareInstance().validataPasswdAndInit(pwd,userid);
//        if(code){
//            onAccountValidateSuccess();
//        }else {
//            ToastUtil.toast(this,TDString.getStr(R.string.login_password_error));
//            onAccountValidateFailure();
//        }
    }

    public void onAccountValidateSuccess() {
        AppSettings.getAppSettings().setLockPattern(null);
        showToast("关闭成功");
        finish();
    }


    public void onAccountValidateFailure() {
        onCloseByPwdClick();
    }

    private void onCloseByPwdClick(){
        if(dialog!=null&&dialog.isShowing()){
            dialog.dismiss();
        }
        dialog=new VerifyPwdDialog(this);

        dialog.setOnOkClickListener(new VerifyPwdDialog.OnOkClickListener() {
            @Override
            public void onOkClick(String pwd) {
                dialog.dismiss();
                validAccount(pwd);
            }
        });
        dialog.show();
    }


    @Override
    public void onPatternStart() {
        vIndicator.setDefaultIndicator();

        vLockPattern.removePostClearPatternRunnable();
        vLockPattern.setPattern(LockPatternView.DisplayMode.DEFAULT);
    }

    @Override
    public void onPatternComplete(List<LockPatternView.Cell> pattern) {
        vIndicator.setIndicator(pattern);

        if (LockPatternHelper.isLess4Dot(pattern, tvMsg, vLockPattern)) {
            return;
        }

        String patternText = LockPatternUtil.getPatternText(pattern);

        if (TextUtils.equals(patternText, AppSettings.getAppSettings().getLockPattern())) {
            onAccountValidateSuccess();
            return;
        }

        mErrorCount++;

        if (mErrorCount >= MAX_ERROR_COUNT) {
            AppSettings.getAppSettings().setLockPattern(null);
            //AppSettings.getAppSettings().setHasLogin(false);
            tvMsg.setText(null);
            AlertDialog dialog = new AlertDialog.Builder(this)
                    .setMessage(getString(R.string.unlock_pattern_failure_relogin))
                    .setPositiveButton("重新登录", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            reLogin();
                        }
                    })
                    .create();
            dialog.setCanceledOnTouchOutside(false);
            dialog.setCancelable(false);
            dialog.show();
        } else {
            LockPatternHelper.updateLockPatternMode(tvMsg, vLockPattern,
                    getString(R.string.lockpattern_error_count, MAX_ERROR_COUNT - mErrorCount)
                    , LockPatternView.DisplayMode.ERROR);
        }
    }



}
