package bd.com.supply.widget;

import android.content.Context;
import android.support.annotation.Nullable;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.AttributeSet;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;

import bd.com.appcore.ui.adapter.CommonAdapter;
import bd.com.appcore.ui.adapter.base.ViewHolder;
import bd.com.supply.R;
import bd.com.supply.module.transaction.model.domian.ArchivesBean;
import bd.com.supply.module.transaction.model.domian.SoSoBean;
import bd.com.supply.module.transaction.model.domian.SoSoInnerItemBean;
import bd.com.supply.module.transaction.model.domian.ArchivesBean;

public class SoSoItemView extends LinearLayout {
    private TextView title;
    private RecyclerView mRev;
    private List<SoSoInnerItemBean> datas = new ArrayList<>(5);
    private CommonAdapter<SoSoInnerItemBean> adapter;
    private Context context;

    public SoSoItemView(Context context) {
        this(context, null);
    }

    public SoSoItemView(Context context, @Nullable AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public SoSoItemView(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        this.context = context;
        View.inflate(context, R.layout.soso_item_view, this);
        initView();
    }

    private void initView() {
        title = findViewById(R.id.soso_title);
        mRev = findViewById(R.id.rev_soso_inner);
        initAdapter();
    }


    private void initAdapter() {
        adapter = new CommonAdapter<SoSoInnerItemBean>(context, R.layout.archives_inner_item_view, datas) {
            @Override
            protected void convert(ViewHolder holder, SoSoInnerItemBean soSoInnerItemBean, int position) {
                holder.setText(R.id.key_tv, soSoInnerItemBean.getKey());
                holder.setText(R.id.value_tv, soSoInnerItemBean.getValue());
            }
        };
        mRev.setLayoutManager(new LinearLayoutManager(context));
        mRev.setAdapter(adapter);
    }

    public void updateData(ArchivesBean soSoBean) {
        this.datas.clear();
        this.datas.addAll(soSoBean.getInnerDatas());
        adapter.notifyDataSetChanged();
        title.setText(soSoBean.getTx());
    }
}
