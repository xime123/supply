package bd.com.appcore.ui.view;

import android.graphics.Color;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.helper.ItemTouchHelper;
import android.util.Log;

import com.jcodecraeer.xrecyclerview.XRecyclerView;

import java.util.Collections;
import java.util.List;

import bd.com.appcore.ui.adapter.CommonAdapter;
import bd.com.appcore.ui.adapter.MultiItemTypeAdapter;


/**
 * author:     labixiaoxin
 * date:       2018/5/17
 * email:      labixiaoxin2@qq.cn
 */
public class ItemTouchHelperCallBack<T> extends ItemTouchHelper.Callback {
    private final static String TAG=ItemTouchHelperCallBack.class.getSimpleName();
    private XRecyclerView mRecyclerVIew;
    private List<T> datas;
    private MultiItemTypeAdapter<T> adapter;

    public ItemTouchHelperCallBack(XRecyclerView mRecyclerVIew, List<T> datas, MultiItemTypeAdapter<T> adapter) {
        this.mRecyclerVIew = mRecyclerVIew;
        this.datas = datas;
        this.adapter = adapter;
    }

    /**
     * 是否处理滑动事件 以及拖拽和滑动的方向 如果是列表类型的RecyclerView的只存在UP和DOWN，如果是网格类RecyclerView则还应该多有LEFT和RIGHT
     * @param recyclerView
     * @param viewHolder
     * @return
     */
    @Override
    public int getMovementFlags(RecyclerView recyclerView, RecyclerView.ViewHolder viewHolder) {
        Log.e(TAG,"getMovementFlags");
        if (recyclerView.getLayoutManager() instanceof GridLayoutManager) {
            final int dragFlags = ItemTouchHelper.UP | ItemTouchHelper.DOWN |
                    ItemTouchHelper.LEFT | ItemTouchHelper.RIGHT;
            final int swipeFlags = 0;
            return makeMovementFlags(dragFlags, swipeFlags);
        } else {
            final int dragFlags = ItemTouchHelper.UP | ItemTouchHelper.DOWN;
            final int swipeFlags = 0;
//                    final int swipeFlags = ItemTouchHelper.START | ItemTouchHelper.END;
            return makeMovementFlags(dragFlags, swipeFlags);
        }
    }

    @Override
    public boolean onMove(RecyclerView recyclerView, RecyclerView.ViewHolder viewHolder, RecyclerView.ViewHolder target) {
        Log.e(TAG,"onMove");
        if(datas.size()<=1){
            return false;
        }
        //得到当拖拽的viewHolder的Position
        int fromPosition = viewHolder.getAdapterPosition();

        //拿到当前拖拽到的item的viewHolder
        int toPosition = target.getAdapterPosition();

        int headerCount=mRecyclerVIew.getHeaders_includingRefreshCount();
        Log.e("BaseList","fromPosition="+fromPosition+"   toPosition="+toPosition);
        int fromIndex=fromPosition-headerCount;
        int toIndex=toPosition-headerCount;
        if(fromIndex<0||toIndex<0){
            return false;
        }
        if (fromIndex < toIndex) {
            if(toIndex>=datas.size())return false;
            for (int i = fromIndex; i < toIndex; i++) {
                Collections.swap(datas, i, i + 1);
            }
        } else {
            if(fromIndex>=datas.size())return false;
            for (int i = fromIndex; i > toIndex; i--) {
                Collections.swap(datas, i, i - 1);
            }
        }
        adapter.notifyItemMoved(fromPosition, toPosition);
        return true;
    }

    @Override
    public void onSwiped(RecyclerView.ViewHolder viewHolder, int direction) {
        Log.e(TAG,"onSwiped");
//                int position = viewHolder.getAdapterPosition();
//                myAdapter.notifyItemRemoved(position);
//                datas.remove(position);
    }

    /**
     * 重写拖拽可用
     * @return
     */
    @Override
    public boolean isLongPressDragEnabled() {
        Log.e(TAG,"isLongPressDragEnabled");
        return false;
    }

    /**
     * 长按选中Item的时候开始调用
     *
     * @param viewHolder
     * @param actionState
     */
    @Override
    public void onSelectedChanged(RecyclerView.ViewHolder viewHolder, int actionState) {
        Log.e(TAG,"onSelectedChanged");
        if (actionState != ItemTouchHelper.ACTION_STATE_IDLE) {
            Log.e(TAG,"onSelectedChanged  actionState="+actionState);
            viewHolder.itemView.setBackgroundColor(Color.LTGRAY);
        }
        super.onSelectedChanged(viewHolder, actionState);
    }

    /**
     * 手指松开的时候还原
     * @param recyclerView
     * @param viewHolder
     */
    @Override
    public void clearView(RecyclerView recyclerView, RecyclerView.ViewHolder viewHolder) {
        Log.e(TAG,"clearView");
        super.clearView(recyclerView, viewHolder);
        viewHolder.itemView.setBackgroundColor(0);
        //item长按放开的时候，这个下拉刷新要设置为true
        mRecyclerVIew.setPullRefreshEnabled(true);
    }
}
