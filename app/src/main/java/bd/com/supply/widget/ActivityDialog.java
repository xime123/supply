package bd.com.supply.widget;

import android.app.Dialog;
import android.content.Context;
import android.support.annotation.NonNull;
import android.support.annotation.StyleRes;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import bd.com.supply.R;


/**
 * Created by labixiaoxin on 2017/8/16.
 */

public class ActivityDialog extends Dialog {
    private Context mContext;
    private TextView okTv;
    private String privateKey;
    private TextView privateKeyTv;
    private ImageView mCancelIv;
    public ActivityDialog(@NonNull Context context) {
        super(context, R.style.DialogTranslucentNoTitle);
        mContext = context;
        setContentView(R.layout.dialog_activity);
        okTv = (TextView) findViewById(R.id.ok_tv);
        privateKeyTv=findViewById(R.id.private_name_tv);
        mCancelIv=findViewById(R.id.cancel_iv);
        setCanceledOnTouchOutside(false);
        setListener();

    }

    private void setListener() {
        okTv.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dismiss();
            }
        });
        mCancelIv.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dismiss();
            }
        });
    }

    public void setPrivateKey(String privateKey) {
        this.privateKey = privateKey;
        privateKeyTv.setText(privateKey);
    }

    public ActivityDialog(@NonNull Context context, @StyleRes int themeResId) {
        super(context, themeResId);
    }


}
