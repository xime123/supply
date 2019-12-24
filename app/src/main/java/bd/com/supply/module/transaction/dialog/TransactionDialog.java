package bd.com.supply.module.transaction.dialog;

import android.app.Dialog;
import android.content.Context;
import android.support.annotation.NonNull;
import android.text.TextUtils;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.tencent.bugly.crashreport.CrashReport;

import org.web3j.utils.Convert;

import java.math.BigDecimal;

import bd.com.appcore.util.AppSettings;
import bd.com.supply.R;
import bd.com.supply.module.common.GlobConfig;
import bd.com.supply.util.DateKit;
import bd.com.supply.util.TransactionLog;
import bd.com.supply.web3.Web3Proxy;
import bd.com.walletdb.entity.WalletEntity;
import bd.com.walletdb.manager.WalletDBManager;

/**
 * author:     labixiaoxin
 * date:       2018/6/19
 * email:      labixiaoxin2@qq.cn
 */
public class TransactionDialog extends Dialog {
    private Context mContext;

    private EditText pwdEt;
    private TextView sureBtn;
    private TextView formAddressTv;
    private TextView toAddressTv;
    private TextView translateCoinCountTv;
    private TextView gasCountTv;
    private TextView gasPriceTv;
    private LinearLayout detailContent;
    private LinearLayout passwordContent;

    private OnTransactionListener listener;
    private boolean shouddismiss = false;

    private String from, to, value, title;
    private BigDecimal gasGWEI;
    private BigDecimal gasSCT;

    public TransactionDialog(@NonNull Context context, int themeResId) {
        super(context, themeResId);
        this.mContext = context;
        setContentView(R.layout.dialog_layout);
        initAttr();
        initView();
    }

    private void initView() {
        //初始化控件
        detailContent = findViewById(R.id.content);
        passwordContent = findViewById(R.id.password_content);
        pwdEt = findViewById(R.id.wallet_password_et);
        sureBtn = findViewById(R.id.sure_tv);
        formAddressTv = findViewById(R.id.from_address_tv);
        toAddressTv = findViewById(R.id.to_address_tv);
        translateCoinCountTv = findViewById(R.id.sure_coin_count_tv);
        gasCountTv = findViewById(R.id.sure_gas_count_tv);
        gasPriceTv = findViewById(R.id.gas_charge_tv);
        sureBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                CrashReport.postCatchedException(new TransactionLog(from + "===>点击了一次交易按钮 时间：" + System.currentTimeMillis()));
                processLogic();
            }
        });
    }

    private void initAttr() {
        //获取当前Activity所在的窗体
        Window dialogWindow = getWindow();
        //设置Dialog从窗体底部弹出
        dialogWindow.setGravity(Gravity.BOTTOM);
        //获得窗体的属性
        WindowManager.LayoutParams lp = dialogWindow.getAttributes();
        lp.width = ViewGroup.LayoutParams.MATCH_PARENT;
        lp.x = 0;
        lp.y = 0;//设置Dialog距离底部的距离
//       将属性设置给窗体
        dialogWindow.setAttributes(lp);
    }

    private boolean clicked = false;

    private synchronized void processLogic() {
        if (shouddismiss) {
            if (listener != null) {
                String password = pwdEt.getText().toString().trim();
                if (checkPassword(password)) {
                    if (!clicked) {
                        clicked = true;
                        sureBtn.setEnabled(false);
                        listener.onTransaction(from, to, value, password);
                        listener=null;
                    }
                    dismiss();
                }
            }
        } else {
            passwordContent.setVisibility(View.VISIBLE);
            detailContent.setVisibility(View.INVISIBLE);
            shouddismiss = true;
        }
    }

    /**
     * 考验密码
     *
     * @param password
     * @return
     */
    private boolean checkPassword(String password) {
        if (TextUtils.isEmpty(password)) {
            Toast.makeText(mContext, "交易密码不能为空", Toast.LENGTH_LONG).show();
            return false;
        }
        String currentAddres = AppSettings.getAppSettings().getCurrentAddress();
        WalletEntity entity = WalletDBManager.getManager().getWalletEntity(currentAddres);
        if (!TextUtils.equals(password, entity.getPassword())) {
            Toast.makeText(mContext, "交易密码不正确", Toast.LENGTH_LONG).show();
            return false;
        }
        long timeStamp = System.currentTimeMillis();

        CrashReport.postCatchedException(new TransactionLog(from + "====>输入完成了一次交一密码 to:" + to + " 时间：" + DateKit.timeStamp2Date(timeStamp / 1000 + "", null)));
        return true;
    }

    public void setListener(OnTransactionListener listener) {
        this.listener = listener;
    }

    public interface OnTransactionListener {
        void onTransaction(String from, String to, String value, String pwd);
    }

    /**
     * 填充dialog控件
     */
    public void fillDialogData(String from, String to, String value, BigDecimal gasGWEI, BigDecimal gasSCT, String title) {
        this.title = title;
        this.from = from;
        this.to = to;
        this.value = value;
        this.gasGWEI = gasGWEI;
        this.gasSCT = gasSCT;
        formAddressTv.setText(from);
        toAddressTv.setText(to);
        translateCoinCountTv.setText(value + " " + title);
        gasCountTv.setText(Convert.fromWei(gasSCT, Convert.Unit.ETHER) + " " + GlobConfig.getMainTokenType());
        BigDecimal gasPriceBD = new BigDecimal(Web3Proxy.GAS_PRICE);
        BigDecimal gasPriceGwei = Convert.fromWei(gasPriceBD, Convert.Unit.GWEI);
        gasPriceTv.setText("≈GAS(" + Web3Proxy.GAS_LIMIT.toString() + ")*GAS PRICE(" + gasPriceGwei.toString() + " gwei）");
    }
}
