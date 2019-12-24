package bd.com.supply.util;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.Drawable;
import android.os.Handler;
import android.os.Message;
import android.support.annotation.Nullable;
import android.util.Base64;
import android.widget.ImageView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.DataSource;
import com.bumptech.glide.load.engine.GlideException;
import com.bumptech.glide.request.RequestListener;
import com.bumptech.glide.request.target.Target;

import java.io.ByteArrayOutputStream;


public class ImageUtils {
    public static final String BITMAP_PREFIX = "data:image/png;base64,";

    public static Bitmap decodeBase64ToBitmap(String base64Content) {
        if (base64Content.startsWith(BITMAP_PREFIX)) {
            base64Content = base64Content.replace(BITMAP_PREFIX, "");
        }
        byte[] decode = Base64.decode(base64Content, Base64.DEFAULT);
        Bitmap bitmap = BitmapFactory.decodeByteArray(decode, 0, decode.length);
        //save to image on sdcard
        return bitmap;
    }

    /**
     * 图片压缩成base64
     * @param bitmap
     * @return
     */
    public static String encode2Base64ByBitmap(Bitmap bitmap) {
        ByteArrayOutputStream baos = new ByteArrayOutputStream();// outputstream
        bitmap.compress(Bitmap.CompressFormat.PNG, 100, baos);
        return Base64.encodeToString(baos.toByteArray(), Base64.NO_WRAP);
    }

    public static void showAdImage(String adPic, ImageView iv, final Handler.Callback callback) {
        Glide.with(iv).load(adPic).addListener(new RequestListener<Drawable>() {
            @Override
            public boolean onLoadFailed(@Nullable GlideException e, Object model,
                                        Target<Drawable> target, boolean isFirstResource) {
                notifyCallback(callback, false);
                return e != null;
            }

            @Override
            public boolean onResourceReady(Drawable resource, Object model, Target<Drawable> target,
                                           DataSource dataSource, boolean isFirstResource) {
                notifyCallback(callback, true);
                return false;
            }
        }).into(iv);
    }

    private static void notifyCallback(Handler.Callback callback, boolean success) {
        if (callback == null) return;
        Message msg = Message.obtain();
        msg.obj = success;
        callback.handleMessage(msg);
    }
}
