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

public class VerifyPwdDialog extends Dialog {
    private TextView titleTv;
    private TextView desTv;
    private TextView accountTv;
    private Context mContext;
    private EditText pwdCev;
    private TextView cancelTv;
    private TextView okTv;
    private OnOkClickListener listener;
    private String pwdStr;

    public VerifyPwdDialog(@NonNull Context context) {
        super(context, R.style.DialogTranslucentNoTitle);
        mContext = context;
        setContentView(R.layout.dialog_login_pwd);
        pwdCev = (EditText) findViewById(R.id.eidt_pwd_et);
        cancelTv = (TextView) findViewById(R.id.cancel_tv);
        okTv = (TextView) findViewById(R.id.ok_tv);
        setCanceledOnTouchOutside(false);
        titleTv = (TextView) findViewById(R.id.title_tv);
        desTv = (TextView) findViewById(R.id.title_des_tv);
        accountTv = (TextView) findViewById(R.id.account_name_tv);
        setListener();

    }

    private void setListener() {
        cancelTv.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dismiss();
            }
        });

        pwdCev.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {
                pwdStr = s.toString();
                checkCanClick();
            }
        });


        okTv.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (listener != null) {
                    listener.onOkClick(pwdStr);
                }
            }
        });

    }

    public VerifyPwdDialog setTitle(String title) {
        titleTv.setText(title);
        return this;
    }

    public VerifyPwdDialog setDes(String des) {
        desTv.setText(des);
        return this;
    }

    public VerifyPwdDialog setAccount(String account) {
        accountTv.setText(account);
        return this;
    }

    public void setOnOkClickListener(OnOkClickListener listener) {
        this.listener = listener;
        // okTv.setOnClickListener(listener);
    }

    public VerifyPwdDialog(@NonNull Context context, @StyleRes int themeResId) {
        super(context, themeResId);
    }

    public interface OnOkClickListener {
        void onOkClick(String pwd);
    }

    private void checkCanClick() {
        if (!TextUtils.isEmpty(pwdStr)) {
            okTv.setEnabled(true);
        } else {
            okTv.setEnabled(false);
        }
    }
}
