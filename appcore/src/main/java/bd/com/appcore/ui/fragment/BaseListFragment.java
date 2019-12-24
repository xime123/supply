package bd.com.appcore.ui.fragment;

import android.app.Service;
import android.os.Vibrator;
import android.support.annotation.NonNull;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.helper.ItemTouchHelper;
import android.util.Log;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.AnimationUtils;
import android.view.animation.LayoutAnimationController;
import android.widget.FrameLayout;

import com.jcodecraeer.xrecyclerview.ProgressStyle;
import com.jcodecraeer.xrecyclerview.XRecyclerView;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.TimeUnit;

import bd.com.appcore.R;
import bd.com.appcore.mvp.IBasePresenter;
import bd.com.appcore.mvp.IBaseView;
import bd.com.appcore.ui.adapter.CommonAdapter;
import bd.com.appcore.ui.view.ItemTouchHelperCallBack;
import bd.com.appcore.ui.view.OnRecyclerItemClickListener;
import io.reactivex.Observable;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.functions.Consumer;
import rx.schedulers.Schedulers;


public abstract class BaseListFragment<P extends IBasePresenter<V>, V extends IBaseView, T> extends BaseUiFragment<P, V> implements XRecyclerView.LoadingListener {
    private static final String TAG = BaseListFragment.class.getSimpleName();
    protected XRecyclerView mRecyclerView;
    protected CommonAdapter mAdapter;

    protected List<T> datas = new ArrayList<>();
    private ItemTouchHelper mItemTouchHelper;
    //这个暂时这么定，后台可能是根据offset limit这样分页的，到时候再改
    protected int pageNumber = 1;
    protected int pageSize=10;

    @Override
    protected int getLayoutId() {
        return R.layout.base_list_layout;
    }

    @Override
    protected void initContentView(ViewGroup contentView) {
        super.initContentView(contentView);
        mRecyclerView = contentView.findViewById(R.id.recycler_view);
        mAdapter = createAdapter();
        mRecyclerView.setAdapter(mAdapter);
        mRecyclerView.setLayoutManager(buildLayoutManager());
        List<? extends RecyclerView.ItemDecoration> decorations = buildItemDecorations();
        if (decorations != null && decorations.size() > 0) {
            for (RecyclerView.ItemDecoration itemDecoration : buildItemDecorations()) {
                mRecyclerView.addItemDecoration(itemDecoration);
            }
        }
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

                //判断被拖拽的是否是头部，如果不是则执行拖拽
                if (vh.getLayoutPosition() >= mRecyclerView.getHeaders_includingRefreshCount()) {
                    //长按拖动跟下拉刷新事件冲突
                    // mRecyclerView.setPullRefreshEnabled(false);
                    if (setDrager()) {
                        mItemTouchHelper.startDrag(vh);

                        //获取系统震动服务
                        Vibrator vib = (Vibrator) getActivity().getSystemService(Service.VIBRATOR_SERVICE);//震动70毫秒
                        vib.vibrate(70);
                    }
                }
            }
        });

        mItemTouchHelper = new ItemTouchHelper(buildItemTouchHelperCallBack());

        if (setDrager()) {
            mItemTouchHelper.attachToRecyclerView(mRecyclerView);
        }

        mRecyclerView.setPullRefreshEnabled(setCanRefresh());
        mRecyclerView.setLoadingMoreEnabled(setCanLoadMore());
        if (setCanRefresh()) {
            mRecyclerView.refresh();
        }

    }

    /**
     * 是否支持下拉刷新，默认是false，不支持
     *
     * @return
     */
    protected boolean setCanRefresh() {
        return false;
    }

    /**
     * 是否支持上拉加载更多，默认是false，不支持
     *
     * @return
     */
    protected boolean setCanLoadMore() {
        return false;
    }

    /**
     * 是否支持拖拽，默认是false，不支持
     *
     * @return
     */
    protected boolean setDrager() {
        return false;
    }

    /**
     * 加载更多,需要加载更多，请重写这个方法
     *
     * @param params
     */
    protected void fetchMoreListItems(@NonNull Map<String, Object> params) {
        fetchListItems(params);
    }

    /**
     * 首次或者刷新加载列表数据
     *
     * @param params
     */
    protected abstract void fetchListItems(@NonNull Map<String, Object> params);

    /**
     * 列表通用参数放到这里，比如分页
     *
     * @return
     */
    @NonNull
    protected Map<String, Object> getFetchListItemsParams() {
        Map<String, Object> params = new HashMap<>();
        params.put("pageNumber", pageNumber);
        params.put("pageSize",pageSize);
        return params;
    }

    /**
     * RecyclerView 的Adapter , 用于展示Item
     */
    protected abstract CommonAdapter<T> createAdapter();

    public CommonAdapter<T> getAdapter() {
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
        LinearLayoutManager layoutManager = new LinearLayoutManager(getActivity());
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
        if(items==null||items.size()==0){
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
     * 数据为空
     */
    public void loadEmpty() {
        hideLoadingDialog();
        showEmptyView();
    }

    /**
     * 加载更多成功了
     *
     * @param moreDatas
     */
    public void loadMoreSuccess(List<T> moreDatas) {
        mRecyclerView.loadMoreComplete();
        if(moreDatas==null||moreDatas.size()==0){
            mRecyclerView.setNoMore(true);
            return;
        }
        datas.addAll(moreDatas);
        mAdapter.notifyDataSetChanged();
        if (moreDatas.size() < pageSize) {
            mRecyclerView.setNoMore(true);
        }
        pageNumber++;
    }

    public void loadMoreFailed() {
        mRecyclerView.loadMoreComplete();
        safetyToast("加载失败");
    }

    /**
     * 点击加载失败页面，重新去加载
     */
    protected void onErroViewClicked() {
        //showLoadingDialog();
        removeExceptionView();
//        Observable.timer(500,TimeUnit.MILLISECONDS)
//                .observeOn(AndroidSchedulers.mainThread())
//                .subscribe(new Consumer<Long>() {
//                    @Override
//                    public void accept(Long aLong) throws Exception {
//                        mRecyclerView.refresh();
//                    }
//                });

        fetchListItems(getFetchListItemsParams());
    }


    @Override
    public void onRefresh() {
        pageNumber = 1;
        fetchListItems(getFetchListItemsParams());
        Log.e(TAG, "============================>onRefresh");
    }

    @Override
    public void onLoadMore() {
        fetchMoreListItems(getFetchListItemsParams());
        Log.e(TAG, "============================>onLoadMore");
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

}
