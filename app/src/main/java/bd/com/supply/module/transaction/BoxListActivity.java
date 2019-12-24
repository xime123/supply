package bd.com.supply.module.transaction;

import android.content.Intent;
import android.support.annotation.NonNull;
import android.view.View;
import android.view.ViewGroup;

import java.util.Map;

import bd.com.appcore.IntentKey;
import bd.com.appcore.ui.activity.BaseListActivity;
import bd.com.appcore.ui.adapter.CommonAdapter;
import bd.com.appcore.ui.adapter.MultiItemTypeAdapter;
import bd.com.appcore.ui.adapter.base.ViewHolder;
import bd.com.supply.R;
import bd.com.supply.module.transaction.presenter.BoxPresenter;
import bd.com.supply.module.transaction.presenter.BoxPresenter;

public class BoxListActivity extends BaseListActivity<BoxPresenter, BoxPresenter.View, String> implements BoxPresenter.View {
    private String bigBoxAddres;
    @Override
    protected void fetchListItems(@NonNull Map<String, Object> params) {
        mPresenter.getBoxList(bigBoxAddres);
    }

    @Override
    protected void initData() {
        super.initData();
        bigBoxAddres = getIntent().getStringExtra(IntentKey.BIG_BOX_ADDRESS);
        actionBar.setTitle("盒子列表");
    }


    @Override
    protected MultiItemTypeAdapter<String> createAdapter() {
        return new CommonAdapter<String>(this, R.layout.item_box_layout, datas) {
            @Override
            protected void convert(ViewHolder holder, final String boxAddr, int position) {
                holder.setText(R.id.wallet_name_tv, boxAddr);


                holder.getConvertView().setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        Intent intent = new Intent(BoxListActivity.this, ProductListActivity.class);
                        intent.putExtra(IntentKey.BOX_ADDRESS, boxAddr);
                        startActivity(intent);
                    }
                });
            }
        };
    }


    @Override
    protected BoxPresenter initPresenter() {
        return new BoxPresenter();
    }

    @Override
    protected BoxPresenter.View initView() {
        return this;
    }

    @Override
    protected void initContentView(ViewGroup contentView) {
        super.initContentView(contentView);
        mRecyclerView.setLoadingMoreEnabled(false);
    }
}
