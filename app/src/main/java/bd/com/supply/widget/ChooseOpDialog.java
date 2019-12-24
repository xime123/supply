package bd.com.supply.widget;

import android.app.Dialog;
import android.content.Context;
import android.support.annotation.NonNull;
import android.support.annotation.StyleRes;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;

import bd.com.supply.R;


/**
 * Created by 徐敏 on 2017/8/16.
 */

public class ChooseOpDialog extends Dialog {
    private TextView titleTv;
    private TextView desTv;
    private Context mContext;
    private TextView cancelTv;
    private TextView okTv;
    private View.OnClickListener listener;
    private View.OnClickListener cancelListener;

    public ChooseOpDialog(@NonNull Context context) {
        super(context, R.style.DialogTranslucentNoTitle);
        mContext = context;
        setContentView(R.layout.choose_op_layout);
        cancelTv = (TextView) findViewById(R.id.cancel_tv);
        okTv = (TextView) findViewById(R.id.ok_tv);
        setCanceledOnTouchOutside(false);
        titleTv = (TextView) findViewById(R.id.title_tv);
        desTv = (TextView) findViewById(R.id.title_des_tv);
        setListener();

    }

    private void setListener() {
        cancelTv.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (cancelListener != null) {
                    cancelListener.onClick(v);
                }
            }
        });


        okTv.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (listener != null) {
                    listener.onClick(v);
                }
            }
        });


    }

    public ChooseOpDialog setTitle(String title) {
        titleTv.setText(title);
        return this;
    }

    public ChooseOpDialog setDes(String des) {
        desTv.setText(des);
        return this;
    }

    public ChooseOpDialog setCancel(String cancel) {
        cancelTv.setText(cancel);
        return this;
    }

    public ChooseOpDialog setOk(String ok) {
        okTv.setText(ok);
        return this;
    }


    public void setOnOkClickListener(View.OnClickListener listener) {
        this.listener = listener;
        // okTv.setOnClickListener(listener);
    }

    public void setOnCancelClickListener(View.OnClickListener listener) {
        this.cancelListener = listener;
        // okTv.setOnClickListener(listener);
    }

    public ChooseOpDialog(@NonNull Context context, @StyleRes int themeResId) {
        super(context, themeResId);
    }


}
