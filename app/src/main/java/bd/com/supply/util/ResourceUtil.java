package bd.com.supply.util;

import android.content.ClipData;
import android.content.ClipboardManager;
import android.content.Context;


public class ResourceUtil {
    /**
     * 通过图标资源文件名获取图标资源id
     *
     * @param context     上下文
     * @param iconResName 图标资源文件名
     * @return 图标资源id
     */
    public static int getDrawbleResIdByName(Context context, String iconResName) {
        int resId = context.getResources().getIdentifier(iconResName, "mipmap", context.getPackageName());
        return resId;
    }

    public static void setPrimaryClip(Context context,String content) {
        //获取剪贴板管理器：
        ClipboardManager cm = (ClipboardManager) context.getSystemService(Context.CLIPBOARD_SERVICE);
        // 创建普通字符型ClipData
        ClipData mClipData = ClipData.newPlainText("Label", content);
        // 将ClipData内容放到系统剪贴板里。
        cm.setPrimaryClip(mClipData);
    }
}
