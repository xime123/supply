package bd.com.supply.module.news;

import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.bigkoo.convenientbanner.CBViewHolderCreator;
import com.bigkoo.convenientbanner.ConvenientBanner;
import com.bumptech.glide.Glide;

import java.util.List;
import java.util.Map;

import bd.com.appcore.IntentKey;
import bd.com.appcore.ui.adapter.CommonAdapter;
import bd.com.appcore.ui.adapter.base.ViewHolder;
import bd.com.appcore.ui.fragment.BaseListFragment;
import bd.com.supply.R;
import bd.com.supply.module.news.domain.News;
import bd.com.supply.module.news.presenter.NewsPresenter;


public class NewsFragment extends BaseListFragment<NewsPresenter, NewsPresenter.View, News> implements NewsPresenter.View {
    private ConvenientBanner convenientBanner;

    @Override
    protected void fetchListItems(@NonNull Map<String, Object> params) {
        mPresenter.getNewsList();
        mPresenter.getBannerList();
    }


    @Override
    protected void initData() {
        super.initData();
        fetchListItems(null);
    }

    @Override
    protected boolean setCanRefresh() {
        return true;
    }

    @Override
    protected void initContentView(ViewGroup contentView) {
        super.initContentView(contentView);
        actionBar.setVisibility(View.GONE);
        ViewGroup topView = (ViewGroup) View.inflate(getContext(), R.layout.news_top_layout, null);
        convenientBanner = topView.findViewById(R.id.convenientBanner);
        //addTopFloatView(topView);
        mRecyclerView.addHeaderView(topView);
        initBanner();
    }

    @Override
    protected boolean setCanLoadMore() {
        return true;
    }

    private void initBanner() {
        //开始自动翻页
        convenientBanner.setPageIndicator(new int[]{R.drawable.shape_index_white_line, R.drawable.shape_index_white_line2})
                .setPageTransformer(ConvenientBanner.Transformer.DefaultTransformer);
    }

    @Override
    protected CommonAdapter<News> createAdapter() {
        return new CommonAdapter<News>(getContext(),R.layout.news_item_layout,datas) {
            @Override
            protected void convert(ViewHolder holder, News news, int position) {
                ImageView imageView=holder.getView(R.id.news_item_iv);
                Glide.with(getContext()).load(news.getBgUrl()).into(imageView);
                holder.setText(R.id.wallet_name_tv,news.getTitle());
                holder.setText(R.id.wallet_address_tv,news.getDes());
                holder.setText(R.id.time_tv,news.getTime());
            }
        };
    }

    @Override
    protected void onItemViewClick(RecyclerView.ViewHolder vh, News entity) {
        super.onItemViewClick(vh, entity);
        Intent intent=new Intent(getActivity(),WebViewActivity.class);
        intent.putExtra(IntentKey.NEWS_URL,entity.getTargetUrl());
        startActivity(intent);
    }

    @Override
    protected void initView(View content) {
        super.initView(content);
    }

    @Override
    protected NewsPresenter initPresenter() {
        return new NewsPresenter();
    }

    @Override
    protected NewsPresenter.View initView() {
        return this;
    }


    @Override
    public void onGetBannerListSuccess(List<News> banners) {
        convenientBanner.setPages(new CBViewHolderCreator<NewsTopView>() {
            @Override
            public NewsTopView createHolder() {
                return new NewsTopView();
            }
        }, banners);
        convenientBanner.setManualPageable((banners == null || banners.size() <= 1) ? false : true);
        if (convenientBanner.isManualPageable())
            convenientBanner.startTurning(2500);
    }

    @Override
    public void onGetBannerListFailed() {

    }

    @Override
    public void loadSuccess(List<News> items) {
        super.loadSuccess(items);
        mRecyclerView.setNoMore(true);
    }
}
