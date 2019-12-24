package bd.com.supply.module.push;

import android.app.Activity;
import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.support.v4.app.NotificationCompat;
import android.text.TextUtils;

import bd.com.appcore.ActivityManager;
import bd.com.appcore.IntentKey;
import bd.com.appcore.util.GsonUtil;
import bd.com.supply.MainActivity;
import bd.com.supply.R;
import bd.com.supply.app.BdApplication;
import bd.com.supply.module.common.Constant;
import bd.com.supply.module.push.bean.PushMessage;
import bd.com.supply.widget.MessageDialog;

public class MessageProcessor {
    private String extraJson;
    private String message;

    public MessageProcessor(String extraJson, String message) {
        this.extraJson = extraJson;
        this.message = message;
    }

    public void processMessage() {
        if (!TextUtils.isEmpty(extraJson)) {
            PushMessage message = GsonUtil.jsonToObject(extraJson, PushMessage.class);
            if (message != null) {
                switch (message.getMsgType()) {
                    case Constant.MSG_1001:
                        processMain("转账成功", message);
                        break;
                    case Constant.MSG_1002:
                        processMain("收款通知", message);
                        break;
                    default:
                }
            }
        }
    }

    private void processMain(String title, PushMessage message) {
        showNotify(title, message);
//        Activity activity = ActivityManager.getInstance().getRootActivity();
//        if (activity != null) {
//            showDialog(activity, title, message);
//        } else {
//            showNotify(title, message);
//        }
    }

    private void showDialog(Activity activity, String title, PushMessage message) {
        MessageDialog dialog=new MessageDialog(activity);
        dialog.setDes(message.getMsgContent());
        dialog.setTitle(title);
        dialog.show();
        showNotify(title,message);
    }

    private void showNotify(String title, PushMessage message) {
        NotificationCompat.Builder builder = new NotificationCompat.Builder(BdApplication.getAppInstance().getApplicationContext());
        builder.setSmallIcon(R.mipmap.vec_icon);
        builder.setContentTitle(title);
        builder.setContentText(message.getMsgContent());
        //设置点击通知跳转页面后，通知消失
        builder.setAutoCancel(true);
        Intent intent = new Intent(BdApplication.getAppInstance().getApplicationContext(), MainActivity.class);
        intent.putExtra(IntentKey.MESSAGE_TITLE, title);
        intent.putExtra(IntentKey.MESSAGE_CONTENT, message.getMsgContent());
        PendingIntent pi = PendingIntent.getActivity(BdApplication.getAppInstance().getApplicationContext(), 0, intent, PendingIntent.FLAG_UPDATE_CURRENT);
        builder.setContentIntent(pi);
        Notification notification = builder.build();
        notification.defaults = Notification.DEFAULT_ALL;
        NotificationManager notificationManager = (NotificationManager) BdApplication.getAppInstance().getSystemService(Context.NOTIFICATION_SERVICE);
        notificationManager.notify(1, notification);
    }

//    private void showDefault(String content){
//        NotificationCompat.Builder builder = new NotificationCompat.Builder(BdApplication.getAppInstance().getApplicationContext());
//        builder.setSmallIcon(R.mipmap.vec_icon);
//        builder.setContentTitle("超导钱包");
//        builder.setContentText(content);
//        //设置点击通知跳转页面后，通知消失
//        builder.setAutoCancel(true);
//        Notification notification = builder.build();
//        notification.flags = Notification.FLAG_AUTO_CANCEL;
//        notification.defaults = Notification.DEFAULT_ALL;
//        NotificationManager notificationManager = (NotificationManager) BdApplication.getAppInstance().getSystemService(Context.NOTIFICATION_SERVICE);
//        notificationManager.notify(1, notification);
//    }
}
