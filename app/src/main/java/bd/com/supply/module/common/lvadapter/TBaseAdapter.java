package bd.com.supply.module.common.lvadapter;


import android.widget.BaseAdapter;

import java.util.List;

public abstract class TBaseAdapter extends BaseAdapter {

    abstract public void addItems(List items);

    abstract public void clear();


}
