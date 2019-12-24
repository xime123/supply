package bd.com.supply.module.transaction;

import android.text.TextUtils;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.EditText;
import android.widget.TextView;

import bd.com.appcore.IntentKey;
import bd.com.appcore.ui.activity.BaseUiActivity;
import bd.com.appcore.ui.view.CommonLineTextView;
import bd.com.supply.R;
import bd.com.supply.module.transaction.model.domian.ProductOri;
import bd.com.supply.module.transaction.presenter.AddProductInfoPresenter;
import bd.com.supply.widget.SelectPerioDialog;
import bd.com.supply.module.transaction.presenter.AddProductInfoPresenter;

public class AddProductInfoActivity extends BaseUiActivity<AddProductInfoPresenter, AddProductInfoPresenter.View> implements AddProductInfoPresenter.View {

    private EditText opContentEt;
    private CommonLineTextView ctv;
    private TextView sureTv;

    private ProductOri productOri;
    private int peroid = -1;
    private String boxAddress;

    @Override
    protected AddProductInfoPresenter initPresenter() {
        return new AddProductInfoPresenter();
    }

    @Override
    protected AddProductInfoPresenter.View initView() {
        return this;
    }


    @Override
    protected int getLayoutId() {
        return R.layout.activity_add_product;
    }

    @Override
    protected void initContentView(ViewGroup containerView) {
        super.initContentView(containerView);
        actionBar.setTitle("追加产品溯源信息");
        opContentEt = findViewById(R.id.opcontent_et);
        ctv = findViewById(R.id.select_ctv);
        ctv.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showPerioDialog();
            }
        });
        sureTv = findViewById(R.id.sure_tv);
        sureTv.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                addProductInfo();
            }
        });
        ctv.setContent(SoSoConfig.PERIOS.get(0).getName());
    }

    private void showPerioDialog() {
        SelectPerioDialog dialog = new SelectPerioDialog(this);
        String currentChainName = "当前选择的产品周期为：" + SoSoConfig.PERIOS.get(0).getName();
        dialog.setData(SoSoConfig.PERIOS, "请选择周期", currentChainName);
        dialog.setOnItemClickListener(new OnSelectOnlineItemClick());
        dialog.show();
    }

    /**
     * 链选择
     */
    class OnSelectOnlineItemClick implements AdapterView.OnItemClickListener {

        @Override
        public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
            String peroid = SoSoConfig.PERIOS.get(position).getName();
            ctv.setContent(peroid);
        }
    }

    private void addProductInfo() {
        String opContent = opContentEt.getText().toString().trim();

        if (TextUtils.isEmpty(opContent)) {
            showToast("操作内容不能为空");
            return;
        }

        String title = ctv.getContent();
        if (TextUtils.isEmpty(title)) {
            showToast("请选择产品周期");
            return;
        }
        mPresenter.addProductInfo(productOri.getPaddr(), productOri.getAaddr(), title, opContent, boxAddress);
    }

    @Override
    protected void initData() {
        super.initData();
        productOri = getIntent().getParcelableExtra(IntentKey.PRODUCT_ORI);
        boxAddress = getIntent().getStringExtra(IntentKey.BOX_ADDRESS);
        if (productOri == null && TextUtils.isEmpty(boxAddress)) {
            finish();
            return;
        }
    }

    @Override
    public void onAddInfoSuccess() {
        showToast("提交成功");
        finish();
    }

    @Override
    public void onAddInfoFailed(String msg) {
        showToast(msg);
    }
}
