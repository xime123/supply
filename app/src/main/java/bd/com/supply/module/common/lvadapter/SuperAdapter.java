package bd.com.supply.module.common.lvadapter;/*
 * @创建者      Clarck
 * @创建时间    2016/2/19 17:48
 * @描述       ${TODO}
 * 
 * @版本        $$Rev$$
 * @更新者      $Author$
 * @更新时间    $Date$
 * @更新描述    ${TODO}
 */

import android.view.View;
import android.view.ViewGroup;

import java.util.List;

public abstract class SuperAdapter extends MyBaseAdapter {
    public SuperAdapter(List dates) {
        super(dates);
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {

        BaseHolder baseHolder = null;
        if (convertView == null) {
            baseHolder = getSpecialHolder();
        } else {
            baseHolder = (BaseHolder) convertView.getTag();
        }
            /*------接收数据，绑定数据-----*/
        baseHolder.setDataAndRefreshHolderView(mDateSet.get(position), position);
        return baseHolder.mHolderView;

    }

    /**
     * @return
     * @call 走到getView方法，并且(convertView==null)的时候
     * @desc 返回一个BaseHolder的子类对象
     */
    public abstract BaseHolder getSpecialHolder();
}
