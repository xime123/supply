package bd.com.supply.module.transaction;

import android.content.Intent;
import android.support.annotation.NonNull;
import android.text.TextUtils;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import java.util.Map;

import bd.com.appcore.IntentKey;
import bd.com.appcore.ui.activity.BaseListActivity;
import bd.com.appcore.ui.adapter.CommonAdapter;
import bd.com.appcore.ui.adapter.MultiItemTypeAdapter;
import bd.com.appcore.ui.adapter.base.ViewHolder;
import bd.com.supply.R;
import bd.com.supply.module.transaction.model.domian.ProductOri;
import bd.com.supply.module.transaction.presenter.ProductPresenter;
import bd.com.supply.util.DateKit;
import bd.com.supply.module.transaction.presenter.ProductPresenter;

public class ProductListActivity extends BaseListActivity<ProductPresenter, ProductPresenter.View, ProductOri> implements ProductPresenter.View {
    private String boxAddres;
    private TextView addSosoTv;

    @Override
    protected void fetchListItems(@NonNull Map<String, Object> params) {
        if (TextUtils.isEmpty(boxAddres)) {
            mPresenter.getProductList(params);
        } else {
            mPresenter.getProductListByBox(boxAddres);
        }
    }

    @Override
    protected void initData() {
        super.initData();
        boxAddres = getIntent().getStringExtra(IntentKey.BOX_ADDRESS);
        actionBar.setTitle(TextUtils.isEmpty(boxAddres) ? "产品列表" : "盒子产品列表");
        if (!TextUtils.isEmpty(boxAddres)) {
            addBottomView();
        }
    }


    @Override
    protected MultiItemTypeAdapter<ProductOri> createAdapter() {
        return new CommonAdapter<ProductOri>(this, R.layout.item_product_layout, datas) {
            @Override
            protected void convert(ViewHolder holder, final ProductOri productOri, int position) {
                holder.setText(R.id.time_tv, DateKit.timeStamp2Date(productOri.getCreateTime() / 1000 + "", null));
                holder.setText(R.id.prod_address_tv, productOri.getPaddr());
                holder.setText(R.id.product_name_tv, productOri.getName());
                holder.setOnClickListener(R.id.add_prod_info_iv, new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        Intent intent = new Intent(ProductListActivity.this, AddProductInfoActivity.class);
                        intent.putExtra(IntentKey.BOX_ADDRESS, boxAddres);
                        intent.putExtra(IntentKey.PRODUCT_ORI, productOri);
                        startActivity(intent);
                    }
                });
                if (!TextUtils.isEmpty(boxAddres)) {
                    holder.setVisible(R.id.add_prod_info_iv, false);
                }
                holder.setVisible(R.id.add_prod_info_iv, false);
                holder.getConvertView().setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        Intent intent = new Intent(ProductListActivity.this, NewSoSoActivity.class);
                        intent.putExtra(IntentKey.PRODUCT_ADDRESS, productOri.getPaddr());
                        startActivity(intent);
                    }
                });
            }
        };
    }


    @Override
    protected ProductPresenter initPresenter() {
        return new ProductPresenter();
    }

    @Override
    protected ProductPresenter.View initView() {
        return this;
    }

    @Override
    protected void initContentView(ViewGroup contentView) {
        super.initContentView(contentView);
        mRecyclerView.setLoadingMoreEnabled(false);
    }

    private void addBottomView() {
//        ViewGroup bottomView = (ViewGroup) View.inflate(this, R.layout.product_list_bottom_layout, null);
//        addSosoTv = bottomView.findViewById(R.id.add_soso_tv);
//        addSosoTv.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                Intent intent = new Intent(ProductListActivity.this, AddProductInfoActivity.class);
//                intent.putExtra(IntentKey.BOX_ADDRESS, boxAddres);
//                startActivity(intent);
//            }
//        });
//        addBottomFloatView(bottomView);
    }
}
