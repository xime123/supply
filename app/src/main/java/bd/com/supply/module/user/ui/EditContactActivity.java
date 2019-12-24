package bd.com.supply.module.user.ui;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.TextView;

import org.web3j.crypto.WalletUtils;

import bd.com.appcore.IntentKey;
import bd.com.appcore.qrcode.QrCodeImportEvent;
import bd.com.appcore.ui.activity.BaseUiActivity;
import bd.com.supply.R;
import bd.com.supply.module.user.presenter.EditContactPresenter;
import bd.com.supply.widget.VerifyPwdDialog;
import bd.com.supply.widget.VerifyPwdDialog;
import bd.com.walletdb.entity.ContactEntity;
import de.greenrobot.event.EventBus;

public class EditContactActivity extends BaseUiActivity<EditContactPresenter, EditContactPresenter.View> implements EditContactPresenter.View {
    private EditText nameEt;
    private EditText remarkEt;
    private EditText addrEt;
    private TextView deleteTv;
    private ContactEntity entity;
    private VerifyPwdDialog dialog;

    @Override
    protected void initTooBar() {
        super.initTooBar();
        actionBar.setTitle("编辑联系人");
        actionBar.setRightTv("保存");
        actionBar.setOnRightTvClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                checkData();
            }
        });
    }

    @Override
    protected void initData() {
        super.initData();
        entity = getIntent().getParcelableExtra(IntentKey.CONTACT_ENTITY);
        nameEt.setText(entity.getName());
        addrEt.setText(entity.getAddress());
        remarkEt.setText(entity.getRemark());
    }

    @Override
    protected void initContentView(ViewGroup containerView) {
        super.initContentView(containerView);
        nameEt = findViewById(R.id.contact_name_et);
        remarkEt = findViewById(R.id.contact_remark_et);
        addrEt = findViewById(R.id.contact_address_et);
        deleteTv = findViewById(R.id.delete_contact_tv);
        deleteTv.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                deleteContact();
            }
        });
    }

    private void checkData() {
        String name = nameEt.getText().toString().trim();
        String remark = remarkEt.getText().toString().trim();
        String address = addrEt.getText().toString().trim();
        if (TextUtils.isEmpty(remark)) {
            showToast("名称不能为空");
            return;
        }
        if (TextUtils.isEmpty(address)) {
            showToast("地址不能为空");
            return;
        }

        if (!WalletUtils.isValidAddress(address)) {
            showToast("地址不合法");
            return;
        }
        ContactEntity entity = new ContactEntity();
        entity.setRemark(remark);
        entity.setName(name);
        entity.setAddress(address);
        if (this.entity.getAddress().equals(address)) {
            mPresenter.updateContact(entity);
        } else {
            mPresenter.deleteContact(this.entity.getAddress());
            mPresenter.inputContact(entity);
        }
        setResult(100);
        finish();
    }

    @Override
    protected EditContactPresenter initPresenter() {
        return new EditContactPresenter();
    }

    @Override
    protected EditContactPresenter.View initView() {
        return this;
    }

    @Override
    protected int getLayoutId() {
        return R.layout.activity_editcontact_layout;
    }

    private void deleteContact() {
        if (dialog != null && dialog.isShowing()) {
            dialog.dismiss();
        }
        dialog = new VerifyPwdDialog(this);
        dialog.setOnOkClickListener(new VerifyPwdDialog.OnOkClickListener() {
            @Override
            public void onOkClick(String pwd) {
                mPresenter.deleteContactByPwd(entity.getAddress(),pwd);
            }
        });
        dialog.show();
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EventBus.getDefault().register(this);
    }

    /**
     * 收到对端钱包地址 开始转账
     *
     * @param event
     */
    public void onEventMainThread(QrCodeImportEvent event) {
        if (event.getType() == 2) {
            String translateResult = event.getResult();
            addrEt.setText(translateResult + "");
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        EventBus.getDefault().unregister(this);
    }

    @Override
    public void onDeleteFailed(String errorMsg) {
        showToast(errorMsg);
    }

    @Override
    public void onDeleteSuccess() {
        Intent intent=new Intent();
        intent.putExtra(IntentKey.CONTACT_ENTITY,entity);
        setResult(100,intent);
        finish();
    }
}
