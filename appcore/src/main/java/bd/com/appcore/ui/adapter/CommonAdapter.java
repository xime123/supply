package bd.com.appcore.ui.adapter;

import android.content.Context;
import android.view.LayoutInflater;

import java.util.List;

import bd.com.appcore.ui.adapter.base.ItemViewDelegate;
import bd.com.appcore.ui.adapter.base.ViewHolder;


/**
 * author:     labixiaoxin
 * date:       2018/5/16
 * email:      labixiaoxin2@qq.cn
 */
public abstract class CommonAdapter<T> extends MultiItemTypeAdapter<T> {
    protected Context mContext;
    protected int mLayoutId;
    protected List<T> mDatas;
    protected LayoutInflater mInflater;

    public CommonAdapter(final Context context, final int layoutId, List<T> datas) {
        super(context, datas);
        mContext = context;
        mInflater = LayoutInflater.from(context);
        mLayoutId = layoutId;
        mDatas = datas;

        addItemViewDelegate(new ItemViewDelegate<T>() {
            @Override
            public int getItemViewLayoutId() {
                return layoutId;
            }

            @Override
            public boolean isForViewType(T item, int position) {
                return true;
            }

            @Override
            public void convert(ViewHolder holder, T t, int position) {
                CommonAdapter.this.convert(holder, t, position);
            }
        });
    }

    protected abstract void convert(ViewHolder holder, T t, int position);


}
