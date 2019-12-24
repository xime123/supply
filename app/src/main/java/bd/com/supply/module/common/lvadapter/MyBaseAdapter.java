package bd.com.supply.module.common.lvadapter;
/*
 * @创建者      Clarck
 * @创建时间    2016/2/19 15:40
 * @描述       抽取适配器/构造函数传递数据List<TT>
 * @call
 * 
 * @版本        $$Rev$$
 * @更新者      $Author$
 * @更新时间    $Date$
 * @更新描述    ${TODO}
 */

import android.view.View;
import android.view.ViewGroup;

import java.util.ArrayList;
import java.util.List;

public class MyBaseAdapter<TT> extends TBaseAdapter {
    public List<TT> mDateSet = new ArrayList<>();

    public MyBaseAdapter(List<TT> dates) {
        this.mDateSet = dates;
    }

    @Override
    public int getCount() {
        if (mDateSet != null) {
            return mDateSet.size();
        }
        return 0;
    }

    @Override
    public Object getItem(int position) {
        if (mDateSet != null) {
            return mDateSet.get(position);
        }
        return null;
    }

    public void bindtems(List<TT> items) {
        if (items == null)
            return;
        mDateSet = items;
        notifyDataSetChanged();
    }

    public void addItems(List items) {
        if (items == null)
            return;
        if (mDateSet == null)
            mDateSet = new ArrayList<>();
        mDateSet.addAll(items);
        notifyDataSetChanged();
    }

    public void removeItem(int position) {
        if (mDateSet != null)
            mDateSet.remove(position);
        notifyDataSetChanged();
    }

    public void clear() {
        if (mDateSet != null)
            mDateSet.clear();
        notifyDataSetChanged();
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        return null;
    }

}
