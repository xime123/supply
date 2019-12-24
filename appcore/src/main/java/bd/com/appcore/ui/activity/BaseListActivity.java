package bd.com.appcore.ui.activity;

import android.app.Service;
import android.os.Vibrator;
import android.support.annotation.NonNull;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.helper.ItemTouchHelper;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.AnimationUtils;
import android.view.animation.LayoutAnimationController;

import com.jcodecraeer.xrecyclerview.XRecyclerView;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.TimeUnit;

import bd.com.appcore.R;
import bd.com.appcore.mvp.IBasePresenter;
import bd.com.appcore.mvp.IBaseView;
import bd.com.appcore.ui.adapter.MultiItemTypeAdapter;
import bd.com.appcore.ui.view.DividerGridItemDecoration;
import bd.com.appcore.ui.view.ItemTouchHelperCallBack;
import bd.com.appcore.ui.view.OnRecyclerItemClickListener;
import io.reactivex.Observable;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.functions.Consumer;

/**
 * author:     labixiaoxin
 * date:       2018/5/16
 * email:      labixiaoxin2@qq.cn
 */
public abstract class BaseListActivity<P extends IBasePresenter<V>, V extends IBaseView, T> extends BaseUiActivity<P, V> implements XRecyclerView.LoadingListener {

    protected XRecyclerView mRecyclerView;
    protected MultiItemTypeAdapter mAdapter;

    protected List<T> datas = new ArrayList<>();
    private ItemTouchHelper mItemTouchHelper;
    //这个暂时这么定，后台可能是根据offset limit这样分页的，到时候再改
    protected int pageNumber = 1;
    protected int pageSize = 50;

    @Override
    protected int getLayoutId() {
        return R.layout.base_list_layout;
    }

    @Override
    protected void initContentView(ViewGroup contentView) {
        super.initContentView(contentView);
        mRecyclerView = findViewById(R.id.recycler_view);
        mAdapter = createAdapter();
        mRecyclerView.setLayoutManager(buildLayoutManager());
        mRecyclerView.setAdapter(mAdapter);
        mRecyclerView.setItemAnimator(new DefaultItemAnimator());
        List<? extends RecyclerView.ItemDecoration> decorations = buildItemDecorations();
        if (decorations != null && decorations.size() > 0) {
            for (RecyclerView.ItemDecoration itemDecoration : buildItemDecorations()) {
                mRecyclerView.addItemDecoration(itemDecoration);
            }
        }
        final LayoutAnimationController controller = new LayoutAnimationController(AnimationUtils.loadAnimation(this, android.R.anim.fade_in));
        controller.setDelay(0);
        mRecyclerView.setLayoutAnimation(controller);
        mRecyclerView.setItemAnimator(null);
        mRecyclerView.setLoadingMoreEnabled(true);
        // mRecyclerView.setLoadingMoreProgressStyle(ProgressStyle.BallZigZag);//设定上拉加载样
        mRecyclerView.setLoadingListener(this);

        mRecyclerView.addOnItemTouchListener(new OnRecyclerItemClickListener(mRecyclerView) {

            @Override
            public void onItemClick(RecyclerView.ViewHolder vh) {
                int pos = vh.getAdapterPosition() - mRecyclerView.getHeaders_includingRefreshCount();
                Log.e("BaseList", "pos=" + pos);
                if (pos >= 0 && pos < datas.size()) {
                    onItemViewClick(vh, datas.get(pos));
                }
            }

            @Override
            public void onItemLongClick(RecyclerView.ViewHolder vh) {
                Log.e(ItemTouchHelperCallBack.class.getSimpleName(), "onItemLongClick");
                int pos = vh.getAdapterPosition() - mRecyclerView.getHeaders_includingRefreshCount();
                if (pos < 0 || pos >= datas.size()) {
                    return;
                }
                onItemViewLongClick(vh, datas.get(pos));
                //判断被拖拽的是否是头部，如果不是则执行拖拽
                if (vh.getLayoutPosition() >= mRecyclerView.getHeaders_includingRefreshCount()) {
                    //长按拖动跟下拉刷新事件冲突
                    //  mRecyclerView.setPullRefreshEnabled(false);
                    if (setDrager()) {
                        mItemTouchHelper.startDrag(vh);

                        //获取系统震动服务
                        Vibrator vib = (Vibrator) getSystemService(Service.VIBRATOR_SERVICE);//震动70毫秒
                        vib.vibrate(70);
                    }
                }
            }
        });

        mItemTouchHelper = new ItemTouchHelper(buildItemTouchHelperCallBack());
        mRecyclerView.setPullRefreshEnabled(true);
        if (setDrager()) {
            mItemTouchHelper.attachToRecyclerView(mRecyclerView);
        }
        mRecyclerView.refresh();

    }

    protected boolean setDrager() {
        return false;
    }

    protected abstract void fetchListItems(@NonNull Map<String, Object> params);

    /**
     * 加载更多,需要加载更多，请重写这个方法
     *
     * @param params
     */
    protected void fetchMoreListItems(@NonNull Map<String, Object> params) {
        fetchListItems(params);
    }

    /**
     * 列表通用参数放到这里，比如分页
     *
     * @return
     */
    @NonNull
    protected Map<String, Object> getFetchListItemsParams() {
        Map<String, Object> params = new HashMap<>();
        params.put("pageNumber", pageNumber);
        params.put("pageSize", pageSize);
        return params;
    }

    /**
     * RecyclerView 的Adapter , 用于展示Item
     */
    protected abstract MultiItemTypeAdapter<T> createAdapter();

    public MultiItemTypeAdapter<T> getAdapter() {
        return mAdapter;
    }


    /**
     * 对外暴露创建itemtouchhelpercallback，业务层可以自定义这个拖拽效果.
     *
     * @return
     */
    protected ItemTouchHelper.Callback buildItemTouchHelperCallBack() {
        return new ItemTouchHelperCallBack(mRecyclerView, datas, getAdapter());
    }

    /**
     * 默认没有分割线，上层需要就重写这个方法。
     *
     * @return
     */
    protected List<? extends RecyclerView.ItemDecoration> buildItemDecorations() {
        return null;
    }

    /**
     * 默认recycle layoutmanager
     *
     * @return
     */
    protected RecyclerView.LayoutManager buildLayoutManager() {
        LinearLayoutManager layoutManager = new LinearLayoutManager(this);
        layoutManager.setOrientation(LinearLayoutManager.VERTICAL);
        return layoutManager;
    }

    /**
     * 局部刷新
     *
     * @param position
     */
    public void notifyItemChanged(int position) {
        mRecyclerView.notifyItemChanged(position);
    }

    /**
     * 局部刷新
     *
     * @param data
     * @param position
     */
    public void notifyItemChanged(int position, T data) {
        mRecyclerView.notifyItemChanged(position, data);
    }

    /**
     * 局部刷新
     *
     * @param position
     * @param newDatas
     */
    public void notifyItemChanged(List<T> newDatas, int position) {
        mRecyclerView.notifyItemInserted(newDatas, position);
    }

    /**
     * 加载成功,刷新页面
     *
     * @param items
     */
    public void loadSuccess(List<T> items) {
        hideLoadingDialog();
        mRecyclerView.refreshComplete();
        removeExceptionView();
        if (items == null || items.size() == 0) {
            datas.clear();
            mAdapter.notifyDataSetChanged();
            showEmptyView();
            return;
        }

        datas.clear();
        datas.addAll(items);
        pageNumber++;
        mAdapter.notifyDataSetChanged();
    }

    /**
     * 加载失败
     *
     * @param msg
     */
    public void loadFailed(String msg) {
        mRecyclerView.refreshComplete();
        hideLoadingDialog();
        showErrorView();
    }

    /**
     * 加载更多成功了
     *
     * @param moreDatas
     */
    public void loadMoreSuccess(List<T> moreDatas) {
        mRecyclerView.loadMoreComplete();
        mRecyclerView.refreshComplete();
        if (moreDatas == null || moreDatas.size() == 0) {
            mRecyclerView.setNoMore(true);
            return;
        }
        removeExceptionView();
        datas.addAll(moreDatas);
        mAdapter.notifyDataSetChanged();
        if (moreDatas.size() < pageSize) {
            mRecyclerView.setNoMore(true);
        }
        pageNumber++;
    }

    public void loadMoreFailed() {
        mRecyclerView.loadMoreComplete();
        showToast("加载失败");
    }

    /**
     * 数据为空
     */
    public void loadEmpty() {
        hideLoadingDialog();
        showEmptyView();
    }


    /**
     * 点击加载失败页面，重新去加载
     */
    protected void onErroViewClicked() {
        showLoadingDialog();
        fetchListItems(getFetchListItemsParams());
    }

    /**
     * 设置adapter
     */
    private void setAdapter(List<T> items) {
        datas.clear();
        datas.addAll(items);
        mRecyclerView.setAdapter(mAdapter);
        // getAdapter().notifyDataSetChanged();
    }

    @Override
    public void onRefresh() {
        Observable.timer(500, TimeUnit.MILLISECONDS).observeOn(AndroidSchedulers.mainThread()).subscribe(new Consumer<Long>() {
            @Override
            public void accept(Long aLong) throws Exception {
                pageNumber = 1;
                fetchListItems(getFetchListItemsParams());
            }
        });
    }

    @Override
    public void onLoadMore() {
        fetchMoreListItems(getFetchListItemsParams());
    }


    /**
     * 加一个顶部悬浮view
     *
     * @param view
     */
    public void addTopFloatView(View view) {
        topContaniner.addView(view);
    }

    /**
     * 加一个底部悬浮view
     *
     * @param view
     */
    public void addBottomFloatView(View view) {
        bottomContainer.addView(view);
    }

    /**
     * item的点击事件
     *
     * @param vh
     */
    protected void onItemViewClick(RecyclerView.ViewHolder vh, T entity) {

    }

    /**
     * item的长按点击事件
     *
     * @param vh
     */
    protected void onItemViewLongClick(RecyclerView.ViewHolder vh, T entity) {

    }

    @Override
    protected void onDestroy() {
        // any time,when you finish your activity or fragment,call this below
        if (mRecyclerView != null) {
            mRecyclerView.destroy(); // this will totally release XR's memory
            mRecyclerView = null;
        }
        //super.onDestroy();要最后调用，因为butternif unbind之后这些view就为null了，可能会引起内存泄露。
        super.onDestroy();
    }

    protected List<? extends RecyclerView.ItemDecoration> buildDefaultItemDecorations() {
        int color = getResources().getColor(R.color.divider_dark);
        return Collections.singletonList(new DividerGridItemDecoration(this, 3, color) {
            @Override
            public boolean[] getItemSidesIsHaveOffsets(int itemPosition) {
                //顺序:left, top, right, bottom
                boolean[] booleans = {false, false, false, false};
                if (itemPosition == 0) {
                    //因为给 RecyclerView 添加了 header，所以原本的 position 发生了变化
                    //position 为 0 的地方实际上是 header，真正的列表 position 从 1 开始
                } else {
                    switch (itemPosition % 3) {
                        case 0:
                            //每一行第三个只显示左边距和下边距
                            booleans[0] = true;
                            booleans[3] = true;
                            break;
                        case 1:
                            //每一行第一个显示右边距和下边距
                            booleans[2] = true;
                            booleans[3] = true;
                            break;
                        case 2:
                            //每一行第二个显示左右边距和下边距
                            booleans[0] = true;
                            booleans[2] = true;
                            booleans[3] = true;
                            break;
                    }
                }
                return booleans;
            }
        });
    }
}
