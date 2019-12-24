package bd.com.appupdate.util;

import android.app.Notification;
import android.app.NotificationManager;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Build;
import android.support.v4.app.NotificationCompat;
import android.support.v4.content.FileProvider;
import android.text.TextUtils;
import android.util.Log;
import android.widget.Toast;

import java.io.File;

import bd.com.appcore.rx.RxTaskCallBack;
import bd.com.appcore.rx.RxTaskScheduler;


/**
 * Created by Teprinciple on 2017/11/3.
 */

public class UpdateAppReceiver extends BroadcastReceiver {


    @Override
    public void onReceive(final Context context, Intent intent) {

        int notifyId = 1;
        int progress = intent.getIntExtra("progress", 0);
        String title = intent.getStringExtra("title");

        NotificationManager nm = null;
        if (UpdateAppUtils.showNotification) {
            NotificationCompat.Builder builder = new NotificationCompat.Builder(context);
            builder.setContentTitle("正在下载 " + title);
            builder.setSmallIcon(android.R.mipmap.sym_def_app_icon);
            builder.setProgress(100, progress, false);

            Notification notification = builder.build();
            nm = (NotificationManager) context.getSystemService(Context.NOTIFICATION_SERVICE);
            nm.notify(notifyId, notification);
        }


        if (progress == 100) {
            if (nm != null) nm.cancel(notifyId);

            if (DownloadAppUtils.downloadUpdateApkFilePath != null) {
                RxTaskScheduler.postIoMainTask(new RxTaskCallBack<Boolean>() {
                    @Override
                    public void onSuccess(Boolean aBoolean) {
                        if (aBoolean) {
                            Intent i = new Intent(Intent.ACTION_VIEW);
                            File apkFile = new File(DownloadAppUtils.downloadUpdateApkFilePath);
                            if (UpdateAppUtils.needFitAndroidN && Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
                                i.setFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION);
                                Uri contentUri = FileProvider.getUriForFile(
                                        context, context.getPackageName() + ".fileprovider", apkFile);
                                i.setDataAndType(contentUri, "application/vnd.android.package-archive");
                            } else {
                                i.setDataAndType(Uri.fromFile(apkFile),
                                        "application/vnd.android.package-archive");
                            }
                            i.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                            context.startActivity(i);
                        }else {
                            Toast.makeText(context,"apk文件不合法!!!",Toast.LENGTH_LONG).show();
                        }
                    }

                    @Override
                    public Boolean doWork() throws Exception {
                        return checkFileMd5();
                    }
                });

            }
        }
    }

    private boolean checkFileMd5() {
        if (TextUtils.isEmpty(UpdateAppUtils.apkDigest)) {
            return true;//后台返回空，不校验
        }
        File apkFile = new File(DownloadAppUtils.downloadUpdateApkFilePath);
        String md5 = Md5.fileMD5(apkFile);
        Log.i("md5", "新包的Md5为：" + md5 + "=======>旧包的md5为：" + UpdateAppUtils.apkDigest);
        return UpdateAppUtils.apkDigest.equals(md5);
    }
}
