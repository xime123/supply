package bd.com.appupdate.customview;

import android.app.Activity;
import android.app.Dialog;
import android.app.DownloadManager;
import android.content.Context;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.text.TextUtils;
import android.util.Log;
import android.view.Display;
import android.view.Gravity;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.TextView;


import com.liulishuo.filedownloader.BaseDownloadTask;

import java.io.File;
import java.util.concurrent.TimeUnit;


import bd.com.appupdate.R;
import bd.com.appupdate.util.DownLoadCallBack;
import bd.com.appupdate.util.DownloadAppUtils;
import io.reactivex.Flowable;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.functions.Consumer;
import io.reactivex.schedulers.Schedulers;

public class AppUpdateDialog extends Dialog {
    private static final String TAG = "AppUpdateDialog";

    private Context context;

    private TextView tvTitle;
    private TextView tvContent;
    private ImageView tvUpdateCancel;
    private ImageView tvUpdateOk;
    private LinearLayout controller;
    private TextView tvProgress;
    private ProgressBar progressBar;
    private TextView tvVersion;
    private String mVersion;
    private String mUrl;
    private boolean isForce;

    public AppUpdateDialog(@NonNull Context context, int layoutResId) {
        super(context, R.style.CommonDialog);
        setContentView(layoutResId);
        tvTitle = findViewById(R.id.tv_common_dialog_title);
        tvContent = findViewById(R.id.tv_common_dialog_content);
        tvUpdateOk = findViewById(R.id.tv_common_dialog_right_btn);
        tvVersion = findViewById(R.id.tv_common_dialog_version);
        if (layoutResId != R.layout.dialog_app_must_update) {
            tvUpdateCancel = findViewById(R.id.tv_common_dialog_left_btn);
            tvUpdateCancel.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    dismiss();
                }
            });
        } else {
            controller = findViewById(R.id.tv_common_dialog_controller);
            tvProgress = findViewById(R.id.tv_common_dialog_tv_pro);
            progressBar = findViewById(R.id.tv_common_dialog_progress);
        }
        this.context = context;
        tvUpdateOk.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startDownLoad(mUrl);
                if (!isForce) {
                    dismiss();
                }
            }
        });
        tvTitle.setText("版本更新");
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Window window = getWindow();
        if (window != null) {
            window.setGravity(Gravity.CENTER);
//            window.setBackgroundDrawable(new ColorDrawable(android.graphics.Color.TRANSPARENT));
            WindowManager windowManager = ((Activity) context).getWindowManager();
            Display display = windowManager.getDefaultDisplay();
            WindowManager.LayoutParams lp = window.getAttributes();
            lp.width = display.getWidth() * 4 / 5;
            getWindow().setAttributes(lp);
        }
        setCanceledOnTouchOutside(false);


    }

//    public AppUpdateDialog title(CharSequence title) {
//        if (tvTitle != null) {
//            tvTitle.setText(title);
//        }
//        return this;
//    }

    public AppUpdateDialog content(CharSequence content, String version, String url) {
        this.mVersion = version;
        this.mUrl = url;
        if (tvContent != null) {
            if (TextUtils.isEmpty(content)) {
                tvContent.setText("版本更新");
            } else {
                tvContent.setText(content);
            }
        }
        if (tvVersion != null) {
            if (TextUtils.isEmpty(version)) {
                tvVersion.setVisibility(View.GONE);
            } else {
                tvVersion.setVisibility(View.VISIBLE);
                tvVersion.setText("v"+version);
            }
        }
        return this;
    }

    public AppUpdateDialog isForce(boolean isForce) {
        this.isForce = isForce;
        return this;
    }

    /*  public AppUpdateDialog okText(CharSequence resId) {
          tvUpdateOk.setText(resId);
          return this;
      }

      public AppUpdateDialog cancelText(CharSequence content) {
          tvUpdateCancel.setText(content);
          return this;
      }*/
    private void setControllerVisible(boolean visible) {
        if (visible) {
            if (tvUpdateOk != null) {
                tvUpdateOk.setVisibility(View.GONE);
            }
            if (controller != null) {
                controller.setVisibility(View.VISIBLE);
            }
        } else {
            if (tvUpdateOk != null) {
                tvUpdateOk.setVisibility(View.VISIBLE);
            }
            if (controller != null) {
                controller.setVisibility(View.GONE);
            }
        }
    }

    private void startDownLoad(String appUrl) {

        DownloadAppUtils.download(getContext(), appUrl, mVersion, isForce ? new MyDownLoadCallBack() : null);
    }

    class MyDownLoadCallBack implements DownLoadCallBack {

        @Override
        public void pending(BaseDownloadTask task, long soFarBytes, long totalBytes) {
            Log.i(TAG, "startDownLoad=====>pending");
        }

        @Override
        public void progress(BaseDownloadTask task, long p, long totalBytes) {
            Log.i(TAG, "startDownLoad=====>progress   =" + p);
            if (tvProgress != null) {
                tvProgress.setText("正在下载 " + p + "%");
            }
            if (progressBar != null) {
                progressBar.setProgress((int) p);
            }
            setControllerVisible(true);
        }

        @Override
        public void paused(BaseDownloadTask task, long soFarBytes, long totalBytes) {
            Log.i(TAG, "startDownLoad=====>paused");
        }

        @Override
        public void completed(BaseDownloadTask task) {
            Log.i(TAG, "startDownLoad=====>completed");
            Flowable.timer(2, TimeUnit.SECONDS)
                    .subscribeOn(Schedulers.io())
                    .observeOn(AndroidSchedulers.mainThread()).subscribe(new Consumer<Long>() {
                @Override
                public void accept(Long aLong) throws Exception {
                    setControllerVisible(false);
                }
            });
        }

        @Override
        public void error(BaseDownloadTask task, Throwable e) {
            Log.i(TAG, "startDownLoad=====>error");
        }

        @Override
        public void warn(BaseDownloadTask task) {
            Log.i(TAG, "startDownLoad=====>warn");
        }
    }

//    public AppUpdateDialog okButton(View.OnClickListener listener) {
//        if (tvUpdateOk != null) {
//            tvUpdateOk.setOnClickListener(listener);
//        }
//        return this;
//    }
//
//    public AppUpdateDialog cancelButton(View.OnClickListener listener) {
//        if (tvUpdateCancel != null) {
//            tvUpdateCancel.setOnClickListener(listener);
//        }
//        return this;
//    }

}
