package bd.com.supply.module.transaction.ui;

import android.content.Intent;
import android.graphics.Bitmap;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import bd.com.appcore.qrcode.QrcodeGen;
import bd.com.appcore.ui.activity.BaseUiActivity;
import bd.com.appcore.util.AppSettings;
import bd.com.supply.R;
import bd.com.supply.module.transaction.presenter.ReceiveCoinPresenter;
import bd.com.supply.module.transaction.view.IReceiveCoinView;
import bd.com.appcore.util.ConvertUtils;
import bd.com.supply.util.ResourceUtil;
import bd.com.walletdb.entity.WalletEntity;
import bd.com.walletdb.manager.WalletDBManager;


public class QrcodeActivity extends BaseUiActivity<ReceiveCoinPresenter, IReceiveCoinView> implements IReceiveCoinView {
    private ImageView qrcodeIv;
    private TextView addressTv;
    private TextView walletNameTv;

    @Override
    protected ReceiveCoinPresenter initPresenter() {
        return new ReceiveCoinPresenter();
    }

    @Override
    protected IReceiveCoinView initView() {
        walletNameTv = findViewById(R.id.wallet_name_tv);
        qrcodeIv = findViewById(R.id.qrcode_iv);
        addressTv = findViewById(R.id.rec_address_tv);
        addressTv.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startCopy();
            }
        });
        return this;
    }

    @Override
    protected void initData() {
        super.initData();
        showQrcode("0.0");
        String walletAddr = AppSettings.getAppSettings().getCurrentAddress();
        WalletEntity entity = WalletDBManager.getManager().getWalletEntity(walletAddr);
        walletNameTv.setText(entity.getName());
    }

    private void showQrcode(String value) {
        String address = AppSettings.getAppSettings().getCurrentAddress();
        addressTv.setText(address);
        //暂时做成通用的
        //String content = address + "#" + value;
        String content = address;
        Bitmap bitmap = QrcodeGen.genQrcodeBitmap(ConvertUtils.dp2px(this, (float) 250.0), content);
        qrcodeIv.setImageBitmap(bitmap);
    }

    @Override
    protected void initTooBar() {
        super.initTooBar();
        actionBar.setTitle("收款");
        actionBar.setRightTv("分享");
        actionBar.setOnRightTvClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                sendtext();
            }
        });
        actionBar.setBackgroundColor(getResources().getColor(R.color.black_bg));
        actionBar.setActionbarDividerVisiable(false);
    }

    private void startCopy() {
        String address = AppSettings.getAppSettings().getCurrentAddress();
        ResourceUtil.setPrimaryClip(this, address);
        showToast("复制成功");
    }

    @Override
    protected int getLayoutId() {
        return R.layout.activity_qrcode;
    }

    public void back(View view) {
        finish();
    }


    //创建方法将输入的内容发出去
    public void sendtext() {
        String text = addressTv.getText().toString().trim();
        if (TextUtils.isEmpty(text)) {
            return;
        }
        Intent intent = new Intent();
        /*设置action为发送分享，
         *并判断要发送分享的内容是否为空
         */
        intent.setAction(Intent.ACTION_SEND);
        if (text != null) {
            intent.putExtra(Intent.EXTRA_TEXT, text);
        } else {
            intent.putExtra(Intent.EXTRA_TEXT, "");
        }
        intent.setType("text/plain");//设置分享发送的数据类型
        //未指定选择器，部分定制系统首次选择后，后期将无法再次改变
        //指定选择器选择使用有发送文本功能的App
        startActivity(Intent.createChooser(intent, getResources().getText(R.string.app_name)));
    }
}
