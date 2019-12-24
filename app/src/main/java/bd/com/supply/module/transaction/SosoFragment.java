package bd.com.supply.module.transaction;

import android.os.Bundle;
import android.os.Parcelable;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.view.ViewGroup;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import bd.com.appcore.mvp.IBasePresenter;
import bd.com.appcore.mvp.IBaseView;
import bd.com.appcore.ui.adapter.CommonAdapter;
import bd.com.appcore.ui.adapter.base.ViewHolder;
import bd.com.appcore.ui.fragment.BaseFragment;
import bd.com.appcore.ui.fragment.BaseListFragment;
import bd.com.supply.R;
import bd.com.supply.module.transaction.model.domian.ArchivesBean;
import bd.com.supply.module.transaction.model.domian.Operation;
import bd.com.supply.module.transaction.model.domian.SoSoBean;
import bd.com.supply.module.transaction.model.domian.SoSoInnerItemBean;
import bd.com.supply.module.transaction.presenter.ProducePresenter;
import bd.com.supply.module.transaction.model.domian.Operation;

public class SosoFragment extends BaseListFragment<ProducePresenter, ProducePresenter.View, SoSoInnerItemBean> implements ProducePresenter.View {
    private static final String SOSO_BEAN_LIST = "soso_bean_list";

    public SosoFragment() {
    }

    public static SosoFragment newInstance(Operation soSoBean) {
        SosoFragment sosoFragment = new SosoFragment();
        Bundle args = new Bundle();
//        args.putParcelable(SOSO_BEAN_LIST, soSoBean);
        sosoFragment.setArguments(args);
        return sosoFragment;
    }

    @Override
    protected ProducePresenter initPresenter() {
        return new ProducePresenter();
    }

    @Override
    protected ProducePresenter.View initView() {
        return this;
    }

    @Override
    protected void initContentView(ViewGroup contentView) {
        super.initContentView(contentView);
        actionBar.setVisibility(View.GONE);
    }

    @Override
    protected void initData() {
        super.initData();
        fetchListItems(null);
    }

    @Override
    protected CommonAdapter<SoSoInnerItemBean> createAdapter() {
        return new CommonAdapter<SoSoInnerItemBean>(getContext(), R.layout.soso_inner_item_view, datas) {
            @Override
            protected void convert(ViewHolder holder, SoSoInnerItemBean soSoInnerItemBean, int position) {
                holder.setText(R.id.key_tv, soSoInnerItemBean.getKey());
                holder.setText(R.id.value_tv, soSoInnerItemBean.getValue());
            }
        };
    }

    @Override
    protected void fetchListItems(@NonNull Map params) {
        Bundle bundle = getArguments();
        if (bundle != null) {
            Operation soSoBean = bundle.getParcelable(SOSO_BEAN_LIST);
            if (soSoBean != null) {
              //  loadSuccess(soSoBean.getInnerDatas());
            }
        }
    }

    @Override
    protected boolean setCanLoadMore() {
        return false;
    }

    @Override
    protected boolean setCanRefresh() {
        return false;
    }
}
