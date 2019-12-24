package bd.com.supply.widget;

import android.app.Dialog;
import android.content.Context;
import android.graphics.drawable.Drawable;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ImageView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.DataSource;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.bumptech.glide.load.engine.GlideException;
import com.bumptech.glide.load.resource.gif.GifDrawable;
import com.bumptech.glide.request.RequestListener;
import com.bumptech.glide.request.target.Target;
import com.cunoraz.gifview.library.GifView;

import bd.com.appcore.R;


/**
 * Created by soffice_user on 2015/9/9.
 */
public class SosoLoadingDialog {
    private static SosoLoadingDialog mLoading;
    public static Dialog loadingDialog;
    private static GifView gifView;
    public static void loadingDialog(Context ct) {
        cancleDialog();
        View view = LayoutInflater.from(ct).inflate(R.layout.soso_loading_dialog, null);
        loadingDialog = new Dialog(ct, R.style.style_loading_dialog);
        loadingDialog.setContentView(view);
        loadingDialog.setCanceledOnTouchOutside(false);
        loadingDialog.show();
        gifView=view.findViewById(R.id.av_loading_view);
        gifView.play();
    }



    public static void cancleDialog() {
        if (loadingDialog != null) {
            if(gifView!=null){
                gifView.pause();
            }
            loadingDialog.dismiss();

//            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
//                if (loadingDialog.getWindow().getDecorView().isAttachedToWindow()) {
//                    loadingDialog.dismiss();
//                } else {
//                    Log.d("", " warn dialog not attached to window!!!");
//                }
//            } else {
//                loadingDialog.dismiss();
//            }
        }
        loadingDialog = null;

    }

}
