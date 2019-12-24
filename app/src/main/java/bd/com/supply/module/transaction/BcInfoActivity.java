package bd.com.supply.module.transaction;

import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import java.math.BigInteger;
import java.util.List;

import bd.com.appcore.IntentKey;
import bd.com.appcore.ui.activity.BaseUiActivity;
import bd.com.appcore.util.AppSettings;
import bd.com.supply.R;
import bd.com.supply.module.common.GlobConfig;
import bd.com.supply.module.transaction.model.domian.ProductBean;
import bd.com.supply.module.transaction.presenter.BcInfoPresenter;
import bd.com.supply.module.transaction.presenter.BcInfoPresenter;

public class BcInfoActivity extends BaseUiActivity<BcInfoPresenter, BcInfoPresenter.View> implements BcInfoPresenter.View {
    private TextView blockNumTv, chainIdTv, uuidTv, sureTv;
    private String uuid;
    private String prodAddr;
    private String blockNum;
    private String opAddr;

    @Override
    protected BcInfoPresenter initPresenter() {
        return new BcInfoPresenter();
    }

    @Override
    protected BcInfoPresenter.View initView() {
        return this;
    }

    @Override
    protected int getLayoutId() {
        return R.layout.activity_bcinfo_layout;
    }

    @Override
    protected void initContentView(ViewGroup containerView) {
        super.initContentView(containerView);
        blockNumTv = findViewById(R.id.ch_tv);
        chainIdTv = findViewById(R.id.bc_tv);
        uuidTv = findViewById(R.id.uuid_tv);
        sureTv = findViewById(R.id.sure_tv);
    }

    @Override
    protected void initData() {
        super.initData();
        prodAddr = getIntent().getStringExtra(IntentKey.PRODUCT_ADDRESS);
        uuid = getIntent().getStringExtra(IntentKey.PRODUCT_UUID);
        blockNum = getIntent().getStringExtra(IntentKey.PRODUCT_LOG_BN);
        opAddr = getIntent().getStringExtra(IntentKey.PRODUCT_OP_ADDR);

        sureTv.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
        blockNumTv.setText(blockNum);
        String chainId = AppSettings.getAppSettings().getCurrentChainId();
        chainIdTv.setText(chainId);
        mPresenter.getTxHashByUUid(uuid,opAddr);
    }


    @Override
    protected void initTooBar() {
        super.initTooBar();
        actionBar.setVisibility(View.GONE);
    }

    @Override
    public void onSuccess(String h) {
        uuidTv.setText(h);
    }


    @Override
    public void onFailed(String msg) {
        showToast(msg);
    }
}
