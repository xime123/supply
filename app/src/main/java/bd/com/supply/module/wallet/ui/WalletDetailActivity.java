package bd.com.supply.module.wallet.ui;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.TextView;

import bd.com.appcore.IntentKey;
import bd.com.appcore.ui.activity.BaseUiActivity;
import bd.com.appcore.ui.view.CircleImageView;
import bd.com.appcore.ui.view.CommonLineTextView;
import bd.com.supply.R;
import bd.com.supply.module.common.GlobConfig;
import bd.com.supply.module.wallet.bus.WalletChangeEvent;
import bd.com.supply.module.wallet.presenter.WalletDetailPresenter;
import bd.com.supply.module.wallet.view.IWalletDetailView;
import bd.com.supply.util.ResourceUtil;
import bd.com.supply.widget.PriveteKeyDialog;
import bd.com.supply.widget.VerifyPwdDialog;
import bd.com.supply.module.wallet.view.IWalletDetailView;
import bd.com.supply.util.ResourceUtil;
import bd.com.supply.widget.PriveteKeyDialog;
import bd.com.supply.widget.VerifyPwdDialog;
import bd.com.walletdb.entity.WalletEntity;
import bd.com.walletdb.manager.WalletDBManager;
import de.greenrobot.event.EventBus;


public class WalletDetailActivity extends BaseUiActivity<WalletDetailPresenter, IWalletDetailView> implements IWalletDetailView {
    private WalletEntity entity;
    private EditText nameEt;
    private CircleImageView walletIv;
    private TextView balanceTV;
    private TextView addressTv;
    private VerifyPwdDialog dialog;
    private PriveteKeyDialog privateKeyDialog;
    private CommonLineTextView exportPriCtv;
    private CommonLineTextView exportKeyStoreCtv;
    private CommonLineTextView pwdCtv;

    @Override
    protected WalletDetailPresenter initPresenter() {
        return new WalletDetailPresenter();
    }

    @Override
    protected IWalletDetailView initView() {
        return this;
    }

//    @Override
//    protected void initData() {
//        super.initData();
//
//    }

    @Override
    protected void onResume() {
        super.onResume();
        Intent intent = getIntent();
        String address = intent.getStringExtra(IntentKey.WALLET_ADDRESS);
        if (TextUtils.isEmpty(address)) {
            finish();
        } else {
            entity = WalletDBManager.getManager().getWalletEntity(address);
            setTitle(entity.getName());
            mPresenter.getBalance(entity.getAddress());
        }
        fillData();
    }

    @Override
    protected void initContentView(ViewGroup containerView) {
        super.initContentView(containerView);
        nameEt = findViewById(R.id.wallet_name_et);
        walletIv = findViewById(R.id.wallet_iv);
        addressTv = findViewById(R.id.address_tv);
        balanceTV = findViewById(R.id.balance_tv);
        pwdCtv = findViewById(R.id.pwd_ctv);
        actionBar.setRightTv("保存");
        actionBar.setOnRightTvClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (TextUtils.isEmpty(nameEt.getText().toString().trim())) {
                    showToast("钱包名不能为空");
                    return;
                }
                mPresenter.updateWalletName(nameEt.getText().toString().trim(), entity);
            }
        });
        exportPriCtv = findViewById(R.id.export_private_ctv);
        exportKeyStoreCtv = findViewById(R.id.export_keystore_ctv);
        exportPriCtv.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showVerifyDialog(1);
            }
        });
        exportKeyStoreCtv.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showVerifyDialog(2);
            }
        });
        pwdCtv.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(WalletDetailActivity.this, ChangePasswordAcitivity.class);
                intent.putExtra(IntentKey.WALLET_ENTITY, entity);
                startActivity(intent);
            }
        });
    }

    @Override
    protected int getLayoutId() {
        return R.layout.activity_wallet_detail_layout;
    }

    private void fillData() {
        nameEt.setText(entity.getName());
        walletIv.setImageResource(TextUtils.isEmpty(entity.getIconStr()) ? R.mipmap.ic_category_32 : ResourceUtil.getDrawbleResIdByName(this, entity.getIconStr()));
        balanceTV.setText(TextUtils.isEmpty(entity.getBalance()) ? 0 + " " + GlobConfig.getMainTokenType() : entity.getBalance() + " " + GlobConfig.getMainTokenType());
        addressTv.setText(entity.getAddress());
    }

    private void showVerifyDialog(final int type) {
        if (dialog != null && dialog.isShowing()) {
            dialog.dismiss();
        }
        dialog = new VerifyPwdDialog(this);

        dialog.setOnOkClickListener(new VerifyPwdDialog.OnOkClickListener() {
            @Override
            public void onOkClick(String pwd) {
                dialog.dismiss();
                showLoadingDialog();
                mPresenter.validAccount(pwd, entity, type);
            }
        });
        dialog.show();
    }

    private void showPrivateKeyDialog(String privatekey) {

        if (privateKeyDialog != null && privateKeyDialog.isShowing()) {
            privateKeyDialog.dismiss();
        }
        privateKeyDialog = new PriveteKeyDialog(this);
        privateKeyDialog.setPrivateKey(privatekey);
        privateKeyDialog.show();

    }

    private void hidePrivateKeyDialog() {
        if (privateKeyDialog != null && privateKeyDialog.isShowing()) {
            privateKeyDialog.dismiss();
        }
    }

    @Override
    public void finish() {
        super.finish();
        if (dialog != null) {
            dialog.dismiss();
            dialog = null;
        }
    }

    @Override
    public void onPassWordRight(int type) {
        hideLoadingDialog();
        if (type == 1) {
            showPrivateKeyDialog(entity.getPrivateKey());
        } else {
            Intent intent = new Intent(this, ExportKeyStoreActivity.class);
            intent.putExtra(IntentKey.WALLET_ENTITY, entity);
            startActivity(intent);
        }

    }

    @Override
    public void onPassWordFailed() {
        hideLoadingDialog();
        showToast("密码不正确");
    }

    @Override
    public void onGetBalanceSuccess(String balance) {
        balanceTV.setText(balance + GlobConfig.getMainTokenType());
    }

    @Override
    protected void onDestroy() {
        EventBus.getDefault().post(new WalletChangeEvent());
        super.onDestroy();
    }
}
