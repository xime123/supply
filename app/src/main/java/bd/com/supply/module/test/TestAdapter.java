package bd.com.supply.module.test;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;

import java.util.List;

import bd.com.appcore.ui.adapter.CommonAdapter;
import bd.com.appcore.ui.adapter.MultiItemTypeAdapter;
import bd.com.appcore.ui.adapter.base.ItemViewDelegate;
import bd.com.appcore.ui.adapter.base.ViewHolder;
import bd.com.supply.R;

public class TestAdapter extends MultiItemTypeAdapter<TestData> {
    private TestPresenter presenter;

    public TestAdapter(Context context, List<TestData> datas, final int[] layoutIds) {
        super(context, datas);
        addItemViewDelegate(new ItemViewDelegate<TestData>() {
            @Override
            public int getItemViewLayoutId() {
                return layoutIds[0];
            }

            @Override
            public boolean isForViewType(TestData item, int position) {
                return item.getViewType() == 2;
            }

            @Override
            public void convert(ViewHolder holder, TestData t, int position) {
                TestAdapter.this.convert(holder, t, position);
            }
        });

        addItemViewDelegate(new ItemViewDelegate<TestData>() {
            @Override
            public int getItemViewLayoutId() {
                return layoutIds[1];
            }

            @Override
            public boolean isForViewType(TestData item, int position) {
                return item.getViewType() == 1;
            }

            @Override
            public void convert(ViewHolder holder, TestData t, int position) {
                TestAdapter.this.convert2(holder, t, position);
            }
        });

    }

    @Override
    public void onAttachedToRecyclerView(@NonNull RecyclerView recyclerView) {
        super.onAttachedToRecyclerView(recyclerView);
        RecyclerView.LayoutManager manager = recyclerView.getLayoutManager();
        if (manager instanceof GridLayoutManager) {
            final GridLayoutManager gridManager = ((GridLayoutManager) manager);

            gridManager.setSpanSizeLookup(new GridLayoutManager.SpanSizeLookup() {
                @Override
                public int getSpanSize(int position) {
                    if (position > 0) {
                        position = position - 1;
                    }
                    int type = getItemViewType(position);
                    switch (type) {
                        case 1:
                            return 6;
                        default:
                            return 2;
                    }
                }
            });
        }

    }

    public void convert(ViewHolder holder, TestData t, int position) {

    }

    public void setPresenter(TestPresenter presenter) {
        this.presenter = presenter;
    }

    public void convert2(ViewHolder holder, TestData t, final int position) {
        holder.setText(R.id.title, t.getTitle());
        holder.setOnClickListener(R.id.title, new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (presenter != null) {
                    List<TestData> dataList = presenter.getMore();
                    mDatas.addAll(dataList);
                    notifyItemRangeInserted(position, dataList.size());
                }
            }
        });
    }

}
