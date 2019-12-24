package bd.com.supply.module.news;

import android.content.Context;
import android.content.Intent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.bigkoo.convenientbanner.CBPageAdapter;
import com.bumptech.glide.Glide;

import bd.com.appcore.IntentKey;
import bd.com.supply.R;
import bd.com.supply.module.news.domain.News;
import bd.com.supply.module.news.domain.News;
import jnr.ffi.annotations.In;

/**
 * author:     xumin
 * date:       2018/8/24
 * email:      xumin2@evergrande.cn
 */
public class NewsTopView implements CBPageAdapter.Holder<News> {
    private ViewGroup bannerContent;
    private ImageView imageView;
    private TextView titleTv;

    @Override
    public View createView(final Context context) {
        //你可以通过layout文件来创建，也可以像我一样用代码创建，不一定是Image，任何控件都可以进行翻页
        bannerContent = (ViewGroup) View.inflate(context, R.layout.news_top_item_layout, null);
        imageView = bannerContent.findViewById(R.id.banner_iv);
        titleTv = bannerContent.findViewById(R.id.banner_title);

        return bannerContent;
    }

    @Override
    public void UpdateUI(final Context context, int position, final News data) {
        Glide.with(context).load(data.getBgUrl()).into(imageView);
        titleTv.setText(data.getTitle());
        imageView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent=new Intent(context,WebViewActivity.class);
                intent.putExtra(IntentKey.NEWS_URL,data.getTargetUrl());
                context.startActivity(intent);
            }
        });
    }
}
