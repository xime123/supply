package bd.com.supply.module.transaction;

import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.view.ViewGroup;

import java.util.List;
import java.util.Map;

import bd.com.appcore.IntentKey;
import bd.com.appcore.ui.activity.BaseListActivity;
import bd.com.appcore.ui.adapter.CommonAdapter;
import bd.com.appcore.ui.adapter.MultiItemTypeAdapter;
import bd.com.appcore.ui.adapter.base.ViewHolder;
import bd.com.supply.R;

import bd.com.supply.module.transaction.model.domian.Batch;
import bd.com.supply.module.transaction.presenter.CategoryPresenter;

public class CategoryListActivity extends BaseListActivity<CategoryPresenter, CategoryPresenter.View, Batch> implements CategoryPresenter.View {

    @Override
    protected CategoryPresenter initPresenter() {
        return new CategoryPresenter();
    }

    @Override
    protected CategoryPresenter.View initView() {
        return this;
    }


    @Override
    protected void fetchListItems(@NonNull Map<String, Object> params) {
        mPresenter.getBatchList(params);
    }

    @Override
    protected void initTooBar() {
        super.initTooBar();
        actionBar.setTitle("批次列表");
    }

    @Override
    protected MultiItemTypeAdapter<Batch> createAdapter() {
        return new CommonAdapter<Batch>(this, R.layout.item_category_layout, datas) {
            @Override
            protected void convert(ViewHolder holder, final Batch info, int position) {
                holder.setText(R.id.category_name_tv, info.getAlias());
                holder.setText(R.id.category_address_tv, info.getCaddr());
                holder.setOnClickListener(R.id.add_category_info_tv, new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        Intent intent = new Intent(CategoryListActivity.this, MaterialsActivity.class);
                        intent.putExtra(IntentKey.BATCH, info);
                        startActivity(intent);
                    }
                });
            }
        };
    }

    @Override
    protected void initContentView(ViewGroup contentView) {
        super.initContentView(contentView);
        mRecyclerView.setLoadingMoreEnabled(false);
    }

}
