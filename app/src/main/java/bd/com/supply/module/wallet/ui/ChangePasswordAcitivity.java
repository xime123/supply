package bd.com.supply.module.wallet.ui;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;

import bd.com.appcore.IntentKey;
import bd.com.appcore.mvp.BasePresenterImpl;
import bd.com.appcore.ui.activity.BaseUiActivity;
import bd.com.supply.R;
import bd.com.supply.module.wallet.presenter.ChangePwdPresenter;
import bd.com.supply.module.wallet.view.IChangePwdView;
import bd.com.supply.module.wallet.view.IChangePwdView;
import bd.com.walletdb.entity.WalletEntity;

public class ChangePasswordAcitivity extends BaseUiActivity<ChangePwdPresenter,IChangePwdView> implements IChangePwdView{
    private EditText oldPwdEt;
    private EditText newPwdEt;
    private EditText newPwdEtAgain;
    private WalletEntity entity;
    @Override
    protected ChangePwdPresenter initPresenter() {
        return new ChangePwdPresenter();
    }

    @Override
    protected IChangePwdView initView() {
        return this;
    }

    @Override
    protected void initContentView(ViewGroup containerView) {
        super.initContentView(containerView);
        oldPwdEt=containerView.findViewById(R.id.old_pwd_et);
        newPwdEt=containerView.findViewById(R.id.pwd_et);
        newPwdEtAgain=containerView.findViewById(R.id.pwd_again_et);

    }

    @Override
    protected void initTooBar() {
        super.initTooBar();
        actionBar.setRightTv("完成");
        actionBar.setOnRightTvClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(checkValidte()){
                    String oldPwd=oldPwdEt.getText().toString().trim();
                    String newPwd=newPwdEt.getText().toString().trim();
                    mPresenter.validatePwd(oldPwd,newPwd,entity);
                }
            }
        });
    }

    @Override
    protected void initData() {
        super.initData();
        Intent intent=getIntent();
        entity = intent.getParcelableExtra(IntentKey.WALLET_ENTITY);
        if(entity==null){
            finish();
        }else {
            setTitle(entity.getName()+"--更改密码");
        }
    }

    @Override
    protected int getLayoutId() {
        return R.layout.activity_change_password_acitivity;
    }

    private boolean checkValidte(){
        String oldPwd=oldPwdEt.getText().toString().trim();
        String newPwd=newPwdEt.getText().toString().trim();
        String newPwdAgain=newPwdEtAgain.getText().toString().trim();
        if(TextUtils.isEmpty(oldPwd)){
            showToast("旧密码不能为空");
            return false;
        }else if(TextUtils.isEmpty(newPwd)||TextUtils.isEmpty(newPwdAgain)){
            showToast("新密码不能为空");
            return false;
        }else if(!TextUtils.equals(newPwd,newPwdAgain)){
            showToast("两次密码不一致");
            return false;
        }
        return true;
    }

    @Override
    public void onPwdRight() {
        showToast("修改成功");
        finish();
    }

    @Override
    public void onPwdError() {
        showToast("原密码不正确");
    }
}
