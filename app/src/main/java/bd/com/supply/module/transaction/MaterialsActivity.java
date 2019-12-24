package bd.com.supply.module.transaction;

import android.text.TextUtils;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.TextView;

import bd.com.appcore.IntentKey;
import bd.com.appcore.ui.activity.BaseUiActivity;
import bd.com.appcore.util.AppSettings;
import bd.com.supply.R;
import bd.com.supply.module.transaction.model.domian.Batch;
import bd.com.supply.module.transaction.presenter.MaterialsPresenter;
import bd.com.supply.module.transaction.presenter.MaterialsPresenter;

public class MaterialsActivity extends BaseUiActivity<MaterialsPresenter, MaterialsPresenter.View> implements MaterialsPresenter.View {
    private EditText materiaslsEt;
    private EditText techniqueEt;
    private EditText nameEt;
    private TextView addressTv;
    private TextView sureTv;
    private Batch batch;

    @Override
    protected MaterialsPresenter initPresenter() {
        return new MaterialsPresenter();
    }

    @Override
    protected MaterialsPresenter.View initView() {
        return this;
    }

    @Override
    protected int getLayoutId() {
        return R.layout.activity_materiasls_layout;
    }

    @Override
    protected void initContentView(ViewGroup containerView) {
        super.initContentView(containerView);
        actionBar.setTitle("添加原材料");
        materiaslsEt = findViewById(R.id.materiasls_et);
        techniqueEt = findViewById(R.id.technique_et);
        nameEt = findViewById(R.id.name_et);
        addressTv = findViewById(R.id.address_et);
        sureTv = findViewById(R.id.sure_tv);
        sureTv.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                updateBaseInfo();
            }
        });
    }

    private void updateBaseInfo() {
        String name = nameEt.getText().toString().trim();
        String technique = techniqueEt.getText().toString().trim();
        String materiasls = materiaslsEt.getText().toString().trim();
        if (TextUtils.isEmpty(name)) {
            showToast("操作人员姓名不能为空");
            return;
        }
        if (TextUtils.isEmpty(technique)) {
            showToast("制作工艺不能为空");
            return;
        }
        if (TextUtils.isEmpty(name)) {
            showToast("原材料信息不能为空");
            return;
        }

        mPresenter.updateCategoryInfo(batch.getCaddr(), technique, materiasls, name);
    }


    @Override
    protected void initData() {
        super.initData();
        batch = getIntent().getParcelableExtra(IntentKey.BATCH);
        if (batch == null) {
            finish();
        }
        addressTv.setText("钱包地址：" + AppSettings.getAppSettings().getCurrentAddress());
    }

    @Override
    public void onUpdateSuccess() {
        showToast("提交成功");
        finish();
    }


    @Override
    public void onUpdateFailed(String msg) {
        showToast(msg);
    }
}
