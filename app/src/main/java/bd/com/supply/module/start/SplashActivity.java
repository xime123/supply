package bd.com.supply.module.start;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.drawable.ColorDrawable;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.provider.Settings;
import android.support.annotation.NonNull;
import android.support.v4.app.ActivityCompat;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.Timer;
import java.util.TimerTask;

import bd.com.appcore.ui.activity.BaseCoreActivity;
import bd.com.appcore.util.AppSettings;
import bd.com.appupdate.customview.ConfirmDialog;
import bd.com.appupdate.feature.Callback;
import bd.com.supply.MainActivity;
import bd.com.supply.R;
import bd.com.supply.module.wallet.ui.CreateWalletActivity;
import bd.com.supply.module.wallet.ui.ImportWalletActivity;
import bd.com.supply.util.ImageUtils;


public class SplashActivity extends BaseCoreActivity implements View.OnClickListener {
    private Handler mHandler;
    private View mAdImageContainer;
    private ImageView mAdImage;
    private String currentAddress;
    private TextView mTvCountDown;
    private int mCountDownSecond;
    private Timer mTimer;
    private boolean hasLogin;
    private String testUrl="http://pics4.baidu.com/feed/9c16fdfaaf51f3de66d9dd7d4e5d641a3b2979fd.jpeg?token=ff0e58cc014f5bd786c19c652865376f&s=AF624F8342E6B2FF587D850F0300F0C3";
    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        immersee();
        setContentView(R.layout.activity_splash);
        initView();
        currentAddress = AppSettings.getAppSettings().getCurrentAddress();
        Log.e("currentAddress", "currentAddress=" + currentAddress);
        hasLogin = !(TextUtils.isEmpty(currentAddress));


    }

    private void init() {
//        ImageUtils.showAdImage(testUrl, mAdImage, new Handler.Callback() {
//            @Override
//            public boolean handleMessage(Message msg) {
//                boolean success = (boolean) msg.obj;
//                if(success){
//                    countDown(3);
//                }else {
//                    jumpSecondActivity();
//                }
//                return false;
//            }
//        });
        jumpSecondActivity();
    }

    private void initView() {
        mHandler = new Handler(getMainLooper());
        mAdImage = findViewById(R.id.ad_image);
        mAdImageContainer = (View) mAdImage.getParent();

        mTvCountDown = findViewById(R.id.count_down);
        mTvCountDown.setOnClickListener(this);
    }

    private void countDown(int second) {
        mCountDownSecond = second;
        mAdImage.setOnClickListener(this);
        mAdImageContainer.setVisibility(View.VISIBLE);
        mTvCountDown.setVisibility(View.VISIBLE);
        final Runnable timeDown = new Runnable() {
            @Override
            public void run() {
                if (mCountDownSecond == 0) {
                    jumpSecondActivity();
                    return;
                }
                mTvCountDown.setText(String.format("%s秒跳过", mCountDownSecond));
                --mCountDownSecond;
            }
        };
        mTimer = new Timer();
        mTimer.schedule(new TimerTask() {
            @Override
            public void run() {
                if (mCountDownSecond == 0) {
                    mTimer.cancel();
                }
                mHandler.post(timeDown);
            }
        }, 1, 1000);
    }

    private void gotoCreate() {
        Intent intent = new Intent(SplashActivity.this, CreateOrImportActivity.class);
        startActivity(intent);
        finish();
    }

    @Override
    protected void onStart() {
        super.onStart();
        //开启动态权限
        ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE}, 1);
    }



    private void gotoMain(float delaySecond) {
        mHandler.postDelayed(mRunnable, (long) (delaySecond * 1000));
    }


    //权限请求结果
    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if (grantResults.length == 0) {
            init();
            return;
        }
        switch (requestCode) {
            case 1:
                if (grantResults[0] != PackageManager.PERMISSION_GRANTED) {
                    new ConfirmDialog(this, new Callback() {
                        @Override
                        public void callback(int position) {
                            if (position == 1) {
                                Intent intent = new Intent(Settings.ACTION_APPLICATION_DETAILS_SETTINGS);
                                intent.setData(Uri.parse("package:" + getPackageName())); // 根据包名打开对应的设置界面
                                startActivity(intent);
                            }
                        }
                    }).setContent("暂无读写SD卡权限\n是否前往设置？").show();
                } else {
                    jumpSecondActivity();
                }
                break;
        }

    }

    Runnable mRunnable = new Runnable() {

        @Override
        public void run() {
            Intent intent = new Intent(SplashActivity.this, MainActivity.class);
            startActivity(intent);
            overridePendingTransition(0, 0);
            finish();
        }
    };

    private void jumpSecondActivity() {
        hasLogin = !(TextUtils.isEmpty(currentAddress));
        if (hasLogin) {
            gotoMain(0.8F);
        } else {
            gotoCreate();
        }
    }
    @Override
    public void onClick(View v) {

    }
}
