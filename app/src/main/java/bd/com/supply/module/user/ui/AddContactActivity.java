package bd.com.supply.module.user.ui;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import bd.com.appcore.IntentKey;
import bd.com.appcore.qrcode.QrCodeImportEvent;
import bd.com.appcore.ui.activity.BaseUiActivity;
import bd.com.supply.R;
import bd.com.supply.module.transaction.ui.ScanQrcodeActivity;
import bd.com.supply.module.transaction.ui.TxActivity;
import bd.com.supply.module.user.bus.AddContactEvent;
import bd.com.supply.module.user.presenter.AddContactPresenter;
import bd.com.supply.module.transaction.ui.ScanQrcodeActivity;
import de.greenrobot.event.EventBus;

public class AddContactActivity extends BaseUiActivity<AddContactPresenter, AddContactPresenter.View> implements AddContactPresenter.View {
    private EditText nameEt;
    private EditText remarkEt;
    private EditText addrEt;
    private TextView addTv;
    private ImageView scanQrcodeIv;
    @Override
    protected void initTooBar() {
        super.initTooBar();
        actionBar.setTitle("新建联系人");
    }

    @Override
    protected void initContentView(ViewGroup containerView) {
        super.initContentView(containerView);
        nameEt = findViewById(R.id.contact_name_et);
        remarkEt = findViewById(R.id.contact_remark_et);
        addrEt = findViewById(R.id.contact_address_et);
        addTv=findViewById(R.id.save_contact_tv);
        scanQrcodeIv=findViewById(R.id.scan_qrcode_iv);
        addTv.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                checkData();
            }
        });
        scanQrcodeIv.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(AddContactActivity.this, ScanQrcodeActivity.class);
                intent.putExtra(IntentKey.QRCODE_TYPE, 2);
                startActivity(intent);
            }
        });
    }

    @Override
    protected AddContactPresenter initPresenter() {
        return new AddContactPresenter();
    }

    @Override
    protected AddContactPresenter.View initView() {
        return this;
    }

    @Override
    protected int getLayoutId() {
        return R.layout.activity_addcontact_layout;
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
        mPresenter.addContact(name, remark, address);
    }

    @Override
    public void onAddSuccess() {
        EventBus.getDefault().post(new AddContactEvent());
        finish();
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
            addrEt.setText(translateResult+"");
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        EventBus.getDefault().unregister(this);
    }

}
