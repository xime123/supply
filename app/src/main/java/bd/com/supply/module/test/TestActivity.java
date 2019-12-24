package bd.com.supply.module.test;

import android.support.annotation.NonNull;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;

import java.util.List;
import java.util.Map;

import bd.com.appcore.ui.activity.BaseListActivity;
import bd.com.appcore.ui.adapter.CommonAdapter;
import bd.com.appcore.ui.adapter.MultiItemTypeAdapter;
import bd.com.appcore.ui.adapter.base.ViewHolder;
import bd.com.supply.R;

public class TestActivity extends BaseListActivity<TestPresenter, TestPresenter.View, TestData> implements TestPresenter.View {
    @Override
    protected void fetchListItems(@NonNull Map<String, Object> params) {
        mPresenter.getDatas();
    }

    @Override
    protected MultiItemTypeAdapter<TestData> createAdapter() {
        TestAdapter adapter=new TestAdapter(this, datas, new int[]{R.layout.item_test_layout1, R.layout.item_layout_test2});
        adapter.setPresenter(mPresenter);
        return adapter;
    }

    @Override
    protected TestPresenter initPresenter() {
        return new TestPresenter();
    }

    @Override
    protected TestPresenter.View initView() {
        return this;
    }

    @Override
    protected void onItemViewClick(RecyclerView.ViewHolder vh, TestData entity) {
        super.onItemViewClick(vh, entity);
    }

    @Override
    protected List<? extends RecyclerView.ItemDecoration> buildItemDecorations() {
        return buildDefaultItemDecorations();
    }

    @Override
    protected RecyclerView.LayoutManager buildLayoutManager() {
        return new GridLayoutManager(this, 6);
    }
}
