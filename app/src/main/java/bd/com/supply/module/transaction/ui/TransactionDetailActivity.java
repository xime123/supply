package bd.com.supply.module.transaction.ui;

import android.content.Intent;
import android.graphics.Bitmap;
import android.text.TextUtils;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import org.web3j.utils.Convert;

import java.math.BigDecimal;

import bd.com.appcore.IntentKey;
import bd.com.appcore.base.ModelCallBack;
import bd.com.appcore.mvp.IBasePresenter;
import bd.com.appcore.mvp.IBaseView;
import bd.com.appcore.qrcode.QrcodeGen;
import bd.com.appcore.ui.activity.BaseUiActivity;
import bd.com.supply.R;
import bd.com.supply.module.common.GlobConfig;
import bd.com.supply.module.news.WebViewActivity;
import bd.com.supply.module.transaction.model.TransactionModel;
import bd.com.supply.util.ImageUtils;
import bd.com.supply.module.transaction.model.TransactionModel;
import bd.com.walletdb.entity.TransactionDetail;
import bd.com.supply.module.wallet.ApiConfig;
import bd.com.supply.util.DateKit;
import bd.com.supply.util.ResourceUtil;
import bd.com.walletdb.entity.TxHistory;
import bd.com.walletdb.manager.TxDetailManager;

public class TransactionDetailActivity extends BaseUiActivity {
    private TextView valueTv;
    private TextView fromTv;
    private TextView toTv;
    private TextView chargeTv;
    private TextView transactionNumberTv;
    private TextView bolckNumberTv;
    private TextView timeTv;
    private TextView unitTv;
    private TransactionDetail txHistory;
    private TextView showMoreTv;
    private String url;
    private String name = "NULS";
    private ImageView showMoreIv;

    @Override
    protected IBasePresenter initPresenter() {
        return null;
    }

    @Override
    protected IBaseView initView() {
        valueTv = findViewById(R.id.value_tv);
        fromTv = findViewById(R.id.from_address_tv);
        toTv = findViewById(R.id.to_address_tv);
        chargeTv = findViewById(R.id.gas_charge_tv);
        transactionNumberTv = findViewById(R.id.transaction_number_tv);
        bolckNumberTv = findViewById(R.id.block_number_tv);
        timeTv = findViewById(R.id.time_tv);
        unitTv = findViewById(R.id.unit_tv);
        showMoreTv = findViewById(R.id.show_more_tv);
        showMoreIv = findViewById(R.id.qrcode_iv);
        return null;
    }


    @Override
    protected int getLayoutId() {
        return R.layout.activity_transaction_detail;
    }

    @Override
    protected void initTooBar() {
        super.initTooBar();
        actionBar.setTitle("交易详情");
    }

    @Override
    protected void initData() {
        super.initData();
        Intent intent = getIntent();
        final TxHistory tx = intent.getParcelableExtra(IntentKey.TRANSACTION_HIS);
        name = intent.getStringExtra(IntentKey.TOKEN_NAME);
        if (!GlobConfig.isEth()) {
            showMoreTv.setText("到Scterscan查询更详细信息>");
        }
        if (tx == null) {
            finish();
        } else {
            TransactionModel.getTransactionModel().getTransactionDetail(tx.getPkHash(), new ModelCallBack<TransactionDetail>() {
                @Override
                public void onResponseFailed(int errorCode, String msg) {
                    txHistory = TxDetailManager.getInstance().findTxDetailByHash(tx.getPkHash());
                    if (txHistory == null) {
                        //                        showErrorView();
//                        showToast("获取详情失败：" + msg);
                        genrateByTx(tx);
                    }
                    fillData();
                }

                @Override
                public void onResponseSuccess(TransactionDetail data) {
                    if (data != null) {
                        TxDetailManager.getInstance().inputTxDetail(data);
                        txHistory = data;
                        fillData();
                    } else {
                        showToast("详情为空");
                        finish();
                    }
                }
            });

        }
    }

    private void genrateByTx(TxHistory tx) {
        if (tx == null) {
            return;
        }
        txHistory = new TransactionDetail();
        txHistory.setBlockGasLimit(tx.getBlockGasLimit());
        txHistory.setBlockHash(tx.getBlockHash());
        txHistory.setBlockNumber(tx.getBlockNumber());
        txHistory.setBlockTimestamp(tx.getBlockTimesStr());
        if (!TextUtils.isEmpty(tx.getCumulativeGas())) {
            txHistory.setCumulativeGas(Long.valueOf(tx.getCumulativeGas()));
        }
        if (!TextUtils.isEmpty(tx.getGas())) {
            txHistory.setGas(Long.valueOf(tx.getGas()));
        }
        if (!TextUtils.isEmpty(tx.getGasPrice())) {
            txHistory.setGasPrice(Long.valueOf(tx.getGasPrice()));
        }
        txHistory.setLastBlockNumber(tx.getLastBlockNumber());
        txHistory.setPkHash(tx.getPkHash());
        txHistory.setTokenTransfer(tx.getTokenTransfer());
        txHistory.setTokenTransferTo(tx.getTokenTransferTo());
        txHistory.setTransactionFrom(tx.getTransactionFrom());
        txHistory.setType(tx.getType());
        txHistory.setTransactionIndex(tx.getTransactionIndex());
        txHistory.setTransactionTo(tx.getTransactionTo());
        txHistory.setValue(tx.getValue());
    }

    private void fillData() {
        boolean isPending = txHistory.getBlockNumber() == 0;
        if (TextUtils.isEmpty(txHistory.getTokenTransferTo())) {//非代币
            unitTv.setText(GlobConfig.getMainTokenType());
            String valueStr = txHistory.getValue();
            BigDecimal value = Convert.fromWei(valueStr, Convert.Unit.ETHER);
            valueTv.setText((txHistory.getType() == 1 ? "-" : "+") + value.toString());

        } else {//代币
            unitTv.setText(name.toLowerCase());
            String valueStr = txHistory.getTokenTransfer();
            if ("0".equals(valueStr)) {
                valueTv.setText("0");
            } else {
                BigDecimal valueBD = Convert.fromWei(valueStr, Convert.Unit.ETHER);
                valueTv.setText((txHistory.getType() == 1 ? "-" : "+") + valueBD.toString());
            }
        }

        fromTv.setText(txHistory.getTransactionFrom());

        toTv.setText(TextUtils.isEmpty(txHistory.getTokenTransferTo()) ? txHistory.getTransactionTo() : txHistory.getTokenTransferTo());


        if (!isPending) {
            chargeTv.setText(txHistory.getTxCost() + " " + GlobConfig.getMainTokenType());
        }
        transactionNumberTv.setText(txHistory.getBlockHash() + "");
        bolckNumberTv.setText(txHistory.getBlockNumber() + "");
        String timeStamp = txHistory.getBlockTimestamp();
        if (!TextUtils.isEmpty(timeStamp)) {
            if (timeStamp.contains(":")) {
                timeTv.setText(timeStamp);
            } else {
                if (timeStamp.length() > 10) {
                    timeStamp = timeStamp.substring(0, 10);
                }
                timeTv.setText(DateKit.timeStamp2Date(timeStamp, "yyyy-MM-dd HH:mm:ss"));
            }

        }

        if (GlobConfig.ETH_CHAIN_ID.equals(GlobConfig.getCurrentChainId())) {
            url = ApiConfig.ETHSCAN_TX + txHistory.getPkHash();
        } else {
            url = ApiConfig.getSctTxWebUrl + txHistory.getPkHash();
        }
        Bitmap bitmap = QrcodeGen.genQrcodeBitmap(160, url);
        showMoreIv.setImageBitmap(bitmap);
        showMoreTv.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (txHistory != null) {
                    Intent h5 = new Intent(TransactionDetailActivity.this, WebViewActivity.class);

                    h5.putExtra(IntentKey.NEWS_URL, url);
                    startActivity(h5);
                }
            }
        });
    }

    public void copyFromAddr(View view) {
        String address = fromTv.getText().toString().trim();
        ResourceUtil.setPrimaryClip(this, address);
        showToast("复制地址成功");
    }

    public void copyToAddr(View view) {
        String address = toTv.getText().toString().trim();
        ResourceUtil.setPrimaryClip(this, address);
        showToast("复制地址成功");
    }

    public void copyTxHash(View view) {
        String address = transactionNumberTv.getText().toString().trim();
        ResourceUtil.setPrimaryClip(this, address);
        showToast("复制交易号成功");
    }
}
