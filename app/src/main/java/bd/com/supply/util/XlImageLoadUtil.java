package bd.com.supply.util;

import android.app.Activity;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Matrix;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.os.Build;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.widget.ImageView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.resource.drawable.DrawableTransitionOptions;
import com.bumptech.glide.request.target.SimpleTarget;
import com.bumptech.glide.request.transition.Transition;

import bd.com.supply.app.BdApplication;


/**
 * author: zhuangyuzhou
 * date: 2018/9/28
 * email: zhuangyuzhou@evergrande.com
 */

public class XlImageLoadUtil {
    /**
     * 检查Context有效性
     */
    private static boolean checkNull(Context context) {
        if (context == null) {
            return true;
        } else if (context instanceof Activity) {
            Activity activity = (Activity) context;
            if (Build.VERSION.SDK_INT >= 17 && activity.isDestroyed()) {
                return true;
            }
        }

        return false;
    }



    /**
     * 模糊图像监听事件
     */
    public interface OnBlurImageListener {
        void onBlurReady();
    }

    /**
     * 模糊化图片
     *
     * @param url
     */
    public static void blurImageUrl(String url, final ImageView imageView) {
        //以bitmap形式加载，加载后再模糊化
        Glide.with(BdApplication.context)
                .asBitmap()
                .load(url)
                .into(new SimpleTarget<Bitmap>(1000, 1000) {

                    @Override
                    public void onResourceReady(@NonNull Bitmap resource, @Nullable Transition<? super Bitmap> transition) {
                        blur(resource, imageView);
                    }
                });

    }


    public static void blurImageUrl(String url, int placeHolder, final ImageView imageView) {
        //以bitmap形式加载，加载后再模糊化
        Glide.with(BdApplication.context)
                .asBitmap()
                .load(url)
                .into(new SimpleTarget<Bitmap>(1000, 1000) {

                    @Override
                    public void onResourceReady(@NonNull Bitmap resource, @Nullable Transition<? super Bitmap> transition) {
                        blur(resource, imageView);
                    }
                });

    }

    /**
     * 模糊图像并设置监听事件
     *
     * @param url
     * @param imageView
     * @param onBlurImageListener
     */
    public static void blurImageUrl(String url, final ImageView imageView, final OnBlurImageListener onBlurImageListener) {
        //以bitmap形式加载，加载后再模糊化
        Glide.with(BdApplication.context)
                .asBitmap()
                .load(url)
                .into(new SimpleTarget<Bitmap>(1000, 1000) {

                    @Override
                    public void onResourceReady(@NonNull Bitmap resource, @Nullable Transition<? super Bitmap> transition) {
                        blur(resource, imageView);
                        if (onBlurImageListener != null) {
                            onBlurImageListener.onBlurReady();
                        }
                    }
                });

    }

    /**
     * 模糊,这个方法把图片先缩小一定倍数，然后进行模糊后再放大，大大加快了速度
     * 工具类FastBlur
     *
     * @param bkg
     * @param view
     */
    public static void blur(Bitmap bkg, ImageView view) {
        float radius = 3;
        float scaleFactor = 10;
        int bitmapWidth = bkg.getWidth();
        int bitmapHeight = bkg.getHeight();
        Bitmap overlay = Bitmap.createBitmap((int) (bitmapWidth / scaleFactor),
                (int) (bitmapHeight / scaleFactor), Bitmap.Config.ARGB_8888);
        Matrix matrix = new Matrix();
        matrix.postScale(1 / scaleFactor, 1 / scaleFactor);
        Canvas canvas = new Canvas(overlay);
        canvas.drawBitmap(bkg, matrix, null);
        overlay = doBlur(overlay, (int) radius, true);
        view.setImageDrawable(new BitmapDrawable(BdApplication.context.getResources(), overlay));
    }

    /**
     * 把图片模糊化
     *
     * @param sentBitmap       要模糊的图片
     * @param radius           模糊的半径，类似于PS中高斯模糊选择的模糊半径
     * @param canReuseInBitmap 是否重新使用图片
     * @return
     */
    public static Bitmap doBlur(Bitmap sentBitmap, int radius, boolean canReuseInBitmap) {
        Bitmap bitmap;
        //如果要重用图片就直接赋值
        if (canReuseInBitmap) {
            bitmap = sentBitmap;
        } else {//否则就根据原图片的参数和像素复制一下
            bitmap = sentBitmap.copy(sentBitmap.getConfig(), true);
        }

        if (radius < 1) {//模糊半径小于1
            return (null);
        }

        int w = bitmap.getWidth();
        int h = bitmap.getHeight();

        //获得图片的像素数组
        int[] pix = new int[w * h];
        /*
         *四个参数分别是
         *接收图片像素的数据数组、
         * 写进数组第一个位置的索引     注：它决定图片相对于原图的位置
         *
         *第三个参数比较特别：用来表示pixels[]数组中每行的像素个数，用于行与行之间区分，绝对值必须大于参数width，
         *但不必大于所要读取图片的宽度w（在width < w 时成立）．(stride负数有何作用不知，存疑)．
         *另，pixels.length >= stride * height,否则会抛出ArrayIndexOutOfBoundsException　异常
         *stride > width时，可以在pixels[]数组中添加每行的附加信息，可做它用．
         *
         *开始读取的第一个像素的X的坐标值
         *开始读取的第一个像素的Y坐标值
         *从每一行读取的像素宽度            注：每行的起始都会是X
         *读取的行数
         */
        bitmap.getPixels(pix, 0, w, 0, 0, w, h);

        int wm = w - 1;
        int hm = h - 1;
        int wh = w * h;
        int div = radius + radius + 1;

        int r[] = new int[wh];
        int g[] = new int[wh];
        int b[] = new int[wh];
        int rsum, gsum, bsum, x, y, i, p, yp, yi, yw;
        int vmin[] = new int[Math.max(w, h)];

        int divsum = (div + 1) >> 1;
        divsum *= divsum;
        int dv[] = new int[256 * divsum];
        for (i = 0; i < 256 * divsum; i++) {
            dv[i] = (i / divsum);
        }

        yw = yi = 0;

        int[][] stack = new int[div][3];
        int stackpointer;
        int stackstart;
        int[] sir;
        int rbs;
        int r1 = radius + 1;
        int routsum, goutsum, boutsum;
        int rinsum, ginsum, binsum;

        for (y = 0; y < h; y++) {
            rinsum = ginsum = binsum = routsum = goutsum = boutsum = rsum = gsum = bsum = 0;
            for (i = -radius; i <= radius; i++) {
                p = pix[yi + Math.min(wm, Math.max(i, 0))];
                sir = stack[i + radius];
                sir[0] = (p & 0xff0000) >> 16;
                sir[1] = (p & 0x00ff00) >> 8;
                sir[2] = (p & 0x0000ff);
                rbs = r1 - Math.abs(i);
                rsum += sir[0] * rbs;
                gsum += sir[1] * rbs;
                bsum += sir[2] * rbs;
                if (i > 0) {
                    rinsum += sir[0];
                    ginsum += sir[1];
                    binsum += sir[2];
                } else {
                    routsum += sir[0];
                    goutsum += sir[1];
                    boutsum += sir[2];
                }
            }
            stackpointer = radius;

            for (x = 0; x < w; x++) {

                r[yi] = dv[rsum];
                g[yi] = dv[gsum];
                b[yi] = dv[bsum];

                rsum -= routsum;
                gsum -= goutsum;
                bsum -= boutsum;

                stackstart = stackpointer - radius + div;
                sir = stack[stackstart % div];

                routsum -= sir[0];
                goutsum -= sir[1];
                boutsum -= sir[2];

                if (y == 0) {
                    vmin[x] = Math.min(x + radius + 1, wm);
                }
                p = pix[yw + vmin[x]];

                sir[0] = (p & 0xff0000) >> 16;
                sir[1] = (p & 0x00ff00) >> 8;
                sir[2] = (p & 0x0000ff);

                rinsum += sir[0];
                ginsum += sir[1];
                binsum += sir[2];

                rsum += rinsum;
                gsum += ginsum;
                bsum += binsum;

                stackpointer = (stackpointer + 1) % div;
                sir = stack[(stackpointer) % div];

                routsum += sir[0];
                goutsum += sir[1];
                boutsum += sir[2];

                rinsum -= sir[0];
                ginsum -= sir[1];
                binsum -= sir[2];

                yi++;
            }
            yw += w;
        }
        for (x = 0; x < w; x++) {
            rinsum = ginsum = binsum = routsum = goutsum = boutsum = rsum = gsum = bsum = 0;
            yp = -radius * w;
            for (i = -radius; i <= radius; i++) {
                yi = Math.max(0, yp) + x;

                sir = stack[i + radius];

                sir[0] = r[yi];
                sir[1] = g[yi];
                sir[2] = b[yi];

                rbs = r1 - Math.abs(i);

                rsum += r[yi] * rbs;
                gsum += g[yi] * rbs;
                bsum += b[yi] * rbs;

                if (i > 0) {
                    rinsum += sir[0];
                    ginsum += sir[1];
                    binsum += sir[2];
                } else {
                    routsum += sir[0];
                    goutsum += sir[1];
                    boutsum += sir[2];
                }

                if (i < hm) {
                    yp += w;
                }
            }
            yi = x;
            stackpointer = radius;
            for (y = 0; y < h; y++) {
                // Preserve alpha channel: ( 0xff000000 & pix[yi] )
                pix[yi] = (0xff000000 & pix[yi]) | (dv[rsum] << 16) | (dv[gsum] << 8) | dv[bsum];

                rsum -= routsum;
                gsum -= goutsum;
                bsum -= boutsum;

                stackstart = stackpointer - radius + div;
                sir = stack[stackstart % div];

                routsum -= sir[0];
                goutsum -= sir[1];
                boutsum -= sir[2];

                if (x == 0) {
                    vmin[y] = Math.min(y + r1, hm) * w;
                }
                p = x + vmin[y];

                sir[0] = r[p];
                sir[1] = g[p];
                sir[2] = b[p];

                rinsum += sir[0];
                ginsum += sir[1];
                binsum += sir[2];

                rsum += rinsum;
                gsum += ginsum;
                bsum += binsum;

                stackpointer = (stackpointer + 1) % div;
                sir = stack[stackpointer];

                routsum += sir[0];
                goutsum += sir[1];
                boutsum += sir[2];

                rinsum -= sir[0];
                ginsum -= sir[1];
                binsum -= sir[2];

                yi += w;
            }
        }

        bitmap.setPixels(pix, 0, w, 0, 0, w, h);

        return (bitmap);
    }


    public static int dip2px(Context context, float dpValue) {
        final float scale = context.getResources().getDisplayMetrics().density;
        return (int) (dpValue * scale + 0.5f);
    }
}