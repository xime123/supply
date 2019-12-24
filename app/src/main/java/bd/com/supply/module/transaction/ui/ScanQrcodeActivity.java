package bd.com.supply.module.transaction.ui;

import android.content.Intent;
import android.os.Vibrator;
import android.text.TextUtils;
import android.util.Log;
import android.widget.Toast;

import bd.com.appcore.IntentKey;
import bd.com.appcore.R;
import bd.com.appcore.mvp.IBasePresenter;
import bd.com.appcore.mvp.IBaseView;
import bd.com.appcore.qrcode.QrCodeImportEvent;
import bd.com.appcore.qrcode.QrCodePackProdEvent;
import bd.com.appcore.ui.activity.BaseUiActivity;
import bd.com.supply.util.AddressUtil;
import bd.com.supply.util.FakeDataHelper;
import cn.bingoogolapple.qrcode.core.QRCodeView;
import cn.bingoogolapple.qrcode.zxing.ZXingView;
import de.greenrobot.event.EventBus;

/**
 * 溯源扫码默认是4 产品码，不是打包的产品码
 * //0:KEYSTORE信息，1，私钥信息，2，扫码转账信息,3直接扫码,
 * 4 溯源扫码，
 * 5，提币扫码,
 * 6 盒子溯源,7 自动溯源,8 产品装进的盒子，9 打包的产品,
 * 10,大盒子码溯源，11，打包的盒子，12 盒子装进的大盒子码
 */
public class ScanQrcodeActivity extends BaseUiActivity implements QRCodeView.Delegate {
    private ZXingView mQRCodeView;
    private String TAG = "QrcodeTestActivity";
    private int type;

    @Override
    protected IBasePresenter initPresenter() {
        return null;
    }

    @Override
    protected IBaseView initView() {
        return null;
    }

    @Override
    protected void initTooBar() {
        super.initTooBar();
        mQRCodeView = (ZXingView) findViewById(R.id.zxingview);
        mQRCodeView.setDelegate(this);
        actionBar.setTitle("扫一扫");
        type = getIntent().getIntExtra(IntentKey.QRCODE_TYPE, -1);
    }


    @Override
    public void onScanQRCodeSuccess(String result) {
        Log.i(TAG, "result:" + result);
        // Toast.makeText(this, result, Toast.LENGTH_SHORT).show();
        vibrate();
        if (type == 3) {
            Intent intent = new Intent(this, TxActivity.class);
            intent.putExtra(IntentKey.QRCODE_RESULT, result);
            intent.putExtra(IntentKey.TOEKN_ADDR, FakeDataHelper.getEthTokenAddr());
            intent.putExtra(IntentKey.TRANSLATE_COIN_TYPE_SYMBLE, FakeDataHelper.MAIN_ETH_SYMBOL);
            startActivity(intent);
            finish();
            return;
        }
        if (type == 8) {
            if (TextUtils.isEmpty(result) || !result.contains("0x") || !result.contains("box")) {
                finish();
                showToast("请扫盒子码");
                return;
            }
            result = AddressUtil.getAddrFromCode(result);
            EventBus.getDefault().post(new QrCodePackProdEvent(result, type));
            finish();
            return;
        }


        if (type == 9) {
            if (TextUtils.isEmpty(result) || !result.contains("0x") || result.contains("box")) {
                finish();
                showToast("请扫产品码");
                return;
            }
            result = AddressUtil.getAddrFromCode(result);
            EventBus.getDefault().post(new QrCodePackProdEvent(result, type));
            finish();
            return;
        }


        if (type == 11) {
            if (TextUtils.isEmpty(result) || !result.contains("0x") || !result.contains("box") || result.contains("bigbox")) {
                finish();
                showToast("请扫盒子码");
                return;
            }
            result = AddressUtil.getAddrFromCode(result);
            EventBus.getDefault().post(new QrCodePackProdEvent(result, type));
            finish();
            return;
        }

        if (type == 12) {
            if (TextUtils.isEmpty(result) || !result.contains("0x") || !result.contains("bigbox")) {
                finish();
                showToast("请扫大盒子码");
                return;
            }
            result = AddressUtil.getAddrFromCode(result);
            EventBus.getDefault().post(new QrCodePackProdEvent(result, type));
            finish();
            return;
        }

        if (!TextUtils.isEmpty(result) && result.contains("box") && !result.contains("bigbox")) {
            EventBus.getDefault().post(new QrCodeImportEvent(result, 6));
            mQRCodeView.startSpot();
//            Intent intent = new Intent(this, ProductListActivity.class);
//            intent.putExtra(IntentKey.BOX_ADDRESS, result);
//            startActivity(intent);
            finish();
            return;
        }

        if (!TextUtils.isEmpty(result) && result.contains("bigbox")) {
            EventBus.getDefault().post(new QrCodeImportEvent(result, 10));
            mQRCodeView.startSpot();
//            Intent intent = new Intent(this, ProductListActivity.class);
//            intent.putExtra(IntentKey.BOX_ADDRESS, result);
//            startActivity(intent);
            finish();
            return;
        }
        mQRCodeView.startSpot();
        EventBus.getDefault().post(new QrCodeImportEvent(result, type));
        finish();
    }


    @Override
    protected void onStart() {
        super.onStart();
        mQRCodeView.startCamera();
//        mQRCodeView.startCamera(Camera.CameraInfo.CAMERA_FACING_FRONT);

        mQRCodeView.showScanRect();
    }

    @Override
    protected void onResume() {
        super.onResume();
        mQRCodeView.startSpot();
    }

    @Override
    protected void onStop() {
        mQRCodeView.stopCamera();
        super.onStop();
    }

    @Override
    protected void onDestroy() {
        mQRCodeView.onDestroy();
        super.onDestroy();
    }

    @Override
    protected int getLayoutId() {
        return R.layout.activity_scan_qrcode;
    }

    @Override
    public void onScanQRCodeOpenCameraError() {
        Log.e(TAG, "打开相机出错");
        Toast.makeText(this, "打开相机出错", Toast.LENGTH_LONG).show();
    }

    private void vibrate() {
        Vibrator vibrator = (Vibrator) getSystemService(VIBRATOR_SERVICE);
        vibrator.vibrate(200);
    }
}
