
package bd.com.supply.module.transaction.ui;

import android.content.Intent;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.SeekBar;
import android.widget.TextView;

import com.tencent.bugly.crashreport.CrashReport;

import org.web3j.crypto.WalletUtils;
import org.web3j.utils.Convert;

import java.math.BigDecimal;

import bd.com.appcore.IntentKey;
import bd.com.appcore.qrcode.QrCodeImportEvent;
import bd.com.appcore.ui.activity.BaseUiActivity;
import bd.com.appcore.util.AppSettings;
import bd.com.supply.R;
import bd.com.supply.main.TransferStartEvent;
import bd.com.supply.module.common.GlobConfig;
import bd.com.supply.module.transaction.dialog.TransactionDialog;
import bd.com.supply.module.transaction.presenter.TxPresenter;
import bd.com.supply.module.transaction.view.ITxView;
import bd.com.supply.module.user.ui.ContactListActivity;
import bd.com.supply.util.DateKit;
import bd.com.supply.util.FakeDataHelper;
import bd.com.supply.util.TransactionLog;
import bd.com.supply.web3.Web3Proxy;
import bd.com.supply.widget.MessageDialog;
import bd.com.supply.module.transaction.dialog.TransactionDialog;
import bd.com.supply.module.transaction.presenter.TxPresenter;
import bd.com.walletdb.entity.ContactEntity;
import bd.com.walletdb.entity.DoubleTxEntity;
import bd.com.walletdb.entity.TxHistory;
import bd.com.walletdb.manager.DoubleTxManager;
import de.greenrobot.event.EventBus;

/**
 * 转账
 */
public class TxActivity extends BaseUiActivity<TxPresenter, ITxView> implements ITxView {
    private TransactionDialog dialog;

    //activity views
    private ImageView addContactIv;
    private EditText addressEt;
    private EditText cointCountEt;
    private EditText remarkEt;
    private SeekBar seekBar;
    private TextView balanceTv;
    private TextView gasTv;
    private BigDecimal gasGWEI = new BigDecimal("1.00");
    private BigDecimal gasSCT = new BigDecimal(0);
    private LinearLayout seekBarRl;
    //dialog views
    private MessageDialog msgDialog;
    private String title;
    private String tokenAddr;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EventBus.getDefault().register(this);
    }

    @Override
    protected TxPresenter initPresenter() {
        return new TxPresenter();
    }

    @Override
    protected ITxView initView() {
        addressEt = findViewById(R.id.rev_address_ev);
        cointCountEt = findViewById(R.id.coint_count_et);
        remarkEt = findViewById(R.id.remark_et);
        seekBar = findViewById(R.id.seek_bar);
        balanceTv = findViewById(R.id.balance_tv);
        gasTv = findViewById(R.id.gas_value_tv);
        addContactIv = findViewById(R.id.contact_iv);
        seekBarRl = findViewById(R.id.seekBar_Rl);
        initSeekBar();

        addContactIv.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(TxActivity.this, ContactListActivity.class);
                intent.putExtra(IntentKey.CONTACT_LIST_INTENT_TYPE, 1);
                startActivityForResult(intent, 0);
            }
        });
        addressEt.addTextChangedListener(new AddressTextWatcher());
        return this;
    }

    private int start = 7;

    private void initSeekBar() {
        String currentChainId = AppSettings.getAppSettings().getCurrentChainId();
        if (!GlobConfig.ETH_CHAIN_ID.equals(currentChainId)) {
            if (!FakeDataHelper.getSctTokenAddr().equals(tokenAddr)) {
                seekBarRl.setVisibility(View.GONE);
                return;
            } else {
                start = 21;
            }
        }
        seekBar.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int i, boolean b) {
                if (i < start) i = start;
                //这个值，是
                BigDecimal GASLIMIT = new BigDecimal(i);
                BigDecimal BIG_GWEI = Convert.toWei(GASLIMIT, Convert.Unit.GWEI);
                gasSCT = BIG_GWEI.multiply(new BigDecimal(Web3Proxy.GAS_LIMIT));
                Web3Proxy.GAS_PRICE = BIG_GWEI.toBigInteger();

                gasGWEI = BIG_GWEI.multiply(GASLIMIT);
                gasTv.setText((Convert.fromWei(gasSCT.toString(), Convert.Unit.ETHER)).toString() + " " + GlobConfig.getMainTokenType());

            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {

            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {

            }
        });
        seekBar.setProgress(1);
    }


    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == 0 && resultCode == 100) {
            ContactEntity entity = data.getParcelableExtra(IntentKey.CONTACT_ENTITY);
            if (entity != null) {
                String address = entity.getAddress();
                addressEt.setText(address + "");
            }
        }
    }

    @Override
    protected void initTooBar() {
        super.initTooBar();
        title = getIntent().getStringExtra(IntentKey.TRANSLATE_COIN_TYPE_SYMBLE);
        tokenAddr = getIntent().getStringExtra(IntentKey.TOEKN_ADDR);
        setTitle(title);

        actionBar.setMenu2Resource(R.mipmap.camera);
        actionBar.setOnMenu2ClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(TxActivity.this, ScanQrcodeActivity.class);
                intent.putExtra(IntentKey.QRCODE_TYPE, 2);
                startActivity(intent);
            }
        });
    }

    @Override
    protected void initData() {
        super.initData();
        mPresenter.getSugGas();
        gasSCT.setScale(4);
        gasGWEI.setScale(2);
        String result = getIntent().getStringExtra(IntentKey.QRCODE_RESULT);
        if (!TextUtils.isEmpty(result)) {
            addressEt.setText(result);
            //String translateResult=result.getResult();
//            String values[] = result.split("#");
//            if (values != null && values.length >= 2) {
//                addressEt.setText(values[0]);
//                cointCountEt.setText(values[1]);
//            }
        }
        cointCountEt.addTextChangedListener(new TxTextWatcher());
        mPresenter.getBalance(tokenAddr);
    }

    @Override
    protected int getLayoutId() {
        return R.layout.activity_duv;
    }

    public void showDialog(View view) {
        dialog = new TransactionDialog(this, R.style.ActionSheetDialogStyle);
        dialog.setListener(new TransactionDialog.OnTransactionListener() {
            @Override
            public void onTransaction(String from, String to, String value, String pwd) {
                if (dialog != null) {
                    dialog.dismiss();
                    dialog = null;
                }
                doTx(from, to, value, pwd);
            }
        });
        if (onDialogshow()) {
            dialog.show();
        }
    }

    private void doTx(String from, String to, String value, String pwd) {
        DoubleTxEntity entity = DoubleTxManager.getManager().getDouTxByTo(to);
        if (entity != null) {
            long timeStamp = System.currentTimeMillis();

            CrashReport.postCatchedException(new TransactionLog("弹出了二次确认：from=" + from + "\n to:" + to + " 时间：" + DateKit.timeStamp2Date(timeStamp / 1000 + "", null)));
            showEnsureDialog(entity, from, to, value, pwd);
        } else {
            //没有重复交易直接转账
            mPresenter.translate(tokenAddr, from, to, value, pwd);
        }
    }

    private boolean onDialogshow() {
        String from = AppSettings.getAppSettings().getCurrentAddress();
        String to = addressEt.getText().toString().trim();
        String value = cointCountEt.getText().toString();
        if (TextUtils.isEmpty(to)) {
            showToast("转账地址不能为空");
            return false;
        }
        if (!WalletUtils.isValidAddress(to)) {
            showToast("转账地址不合法");
            return false;
        }

        if (from.equals(to)) {
            showToast("转出地址与收款地址重复");
            return false;
        }
        if (TextUtils.isEmpty(value)) {
            showToast("转账金额不能为空");
            return false;
        }
        fillDialogData(from, to, value);
        return true;
    }

    private void showEnsureDialog(DoubleTxEntity entity, final String from, final String to, final String value, String pwd) {
        long timeStamp = entity.getTime();
        String time = DateKit.timeStamp2Date(timeStamp / 1000 + "", null);
        String content = "转账地址：" + entity.getTo()
                + "\n最近交易时间：" + time
                + "\n是否继续交易？";

        if (msgDialog != null && msgDialog.isShowing()) {
            msgDialog.dismiss();
        }
        msgDialog = new MessageDialog(this);
        msgDialog.setDes(content);
        msgDialog.setTitle("重复交易地址提醒：");
        msgDialog.setOnOkClickListener(new MessageDialog.OnOkClickListener() {
            @Override
            public void onOkClick(String pwd) {
                mPresenter.translate(tokenAddr, from, to, value, pwd);
            }
        });
        msgDialog.show();
    }


    /**
     * 填充dialog控件
     */
    private void fillDialogData(String from, String to, String value) {
        dialog.fillDialogData(from, to, value, gasGWEI, gasSCT, title);
    }

    /**
     * 收到对端钱包地址 开始转账
     *
     * @param event
     */
    public void onEventMainThread(QrCodeImportEvent event) {
        if (event.getType() == 2) {
            String translateResult = event.getResult();
            if (!TextUtils.isEmpty(translateResult)) {
                addressEt.setText(translateResult);
            }

//            String values[] = translateResult.split("#");
//            if (values != null && values.length >= 2) {
//                addressEt.setText(values[0]);
//                cointCountEt.setText(values[1]);
//            }
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (dialog != null) {
            dialog.dismiss();
        }
        EventBus.getDefault().unregister(this);
    }


    @Override
    public void onTranslateSuccess(TxHistory history) {
        if (dialog != null) {
            dialog.dismiss();
        }
        showToast("交易已发送");
        addressEt.setText("");
        cointCountEt.setText("");
        TransferStartEvent event = new TransferStartEvent();
        event.setHistory(history);
        EventBus.getDefault().post(event);
        finish();
    }

    @Override
    public void onTranslateFailed(String msg) {
        if (dialog != null) {
            dialog.dismiss();
        }
        addressEt.setText("");
        cointCountEt.setText("");
        hideLoadingDialog();
        showToast(msg);
    }

    @Override
    public void onGetBalanceSuccess(String balance) {
        balanceTv.setText("当前余额：" + balance);
    }

    @Override
    public void onGetBalanceFailed(String msg) {
        showToast("余额获取失败");
    }

    @Override
    public void onGetSugGasSuccess(int progess) {
        if (progess < 1) {
            progess = 1;
        }
        if (progess > seekBar.getMax()) {
            progess = seekBar.getMax();
        }
        seekBar.setProgress(progess);
    }

    private class TxTextWatcher implements TextWatcher {

        @Override
        public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {
            balanceTv.setVisibility(View.VISIBLE);
        }

        @Override
        public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {

        }

        @Override
        public void afterTextChanged(Editable editable) {

        }
    }

    private class AddressTextWatcher implements TextWatcher {

        @Override
        public void beforeTextChanged(CharSequence s, int start, int count, int after) {

        }

        @Override
        public void onTextChanged(CharSequence s, int start, int before, int count) {

        }

        @Override
        public void afterTextChanged(Editable s) {
            String address = s.toString();
            if (WalletUtils.isValidAddress(address)) {
                long timeStamp = System.currentTimeMillis();
                CrashReport.postCatchedException(new TransactionLog("用户输入了一次地址：" + address + "    时间：" + DateKit.timeStamp2Date(timeStamp / 1000 + "", null)));
            }
        }
    }
}

