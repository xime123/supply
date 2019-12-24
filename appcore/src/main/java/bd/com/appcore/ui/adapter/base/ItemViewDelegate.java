package bd.com.appcore.ui.adapter.base;


/**
 * author:     labixiaoxin
 * date:       2018/5/16
 * email:      labixiaoxin2@qq.cn
 */
public interface ItemViewDelegate<T>
{

    int getItemViewLayoutId();

    boolean isForViewType(T item, int position);

    void convert(ViewHolder holder, T t, int position);

}
