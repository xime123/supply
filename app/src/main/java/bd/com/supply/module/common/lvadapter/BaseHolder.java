package bd.com.supply.module.common.lvadapter;
/*
 * @创建者      Clarck
 * @创建时间    2016/2/19 16:58
 * @描述       ${TODO}
 * 
 * @版本        $$Rev$$
 * @更新者      $Author$
 * @更新时间    $Date$
 * @更新描述    ${TODO}
 */

import android.content.Context;
import android.view.View;

public abstract class BaseHolder<HOLDERBEAN> {

    public View mHolderView;//提供绑定数据的视图

    private HOLDERBEAN mData;

    protected  Context mContext;

    public BaseHolder(Context context) {
        mContext=context;
        //初始化视图
        mHolderView = initHolderView();

        //提供绑定数据的视图去找一个类的一个对象作为holder
        mHolderView.setTag(this);


    }


    /**
     * @param objectBean
     * @desc 接收数据，绑定数据
     * @call 外界传递数据，让我进行绑定的时候调用
     */
    public void setDataAndRefreshHolderView(HOLDERBEAN objectBean, int position) {
        mData = objectBean;

        refreshHolderView(objectBean, position);
    }

    public void setDataAndRefreshHolderView1(HOLDERBEAN objectBean1, HOLDERBEAN objectBean2) {

        refreshHolderView(objectBean1, objectBean2);
    }
    //**** --------------------------------------------必须实现的----------------------------------------------------**//

    /**
     * @return
     * @desc 1.持有的根视图2.找到根视图中的孩子
     * @call 子类初始化的时候被调用
     */
    public abstract View initHolderView();

    /**
     * @param teanBean
     * @desc 绑定数据
     * @call 外界传递数据，让我进行绑定的时候调用
     */
    public void refreshHolderView(HOLDERBEAN teanBean) {

    }


    public void refreshHolderView(HOLDERBEAN teanBean, int position) {
        refreshHolderView(teanBean);
    }

    public void refreshHolderView(HOLDERBEAN teanBean1, HOLDERBEAN teanBean2) {

    }


}
