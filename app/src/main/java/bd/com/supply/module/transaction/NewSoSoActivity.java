package bd.com.supply.module.transaction;

import android.content.Intent;
import android.os.Build;
import android.support.annotation.NonNull;
import android.support.annotation.RequiresApi;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import bd.com.appcore.IntentKey;
import bd.com.appcore.ui.activity.BaseListActivity;
import bd.com.appcore.ui.adapter.CommonAdapter;
import bd.com.appcore.ui.adapter.MultiItemTypeAdapter;
import bd.com.appcore.ui.adapter.base.ViewHolder;
import bd.com.appcore.util.AppSettings;
import bd.com.supply.R;
import bd.com.supply.module.transaction.model.domian.ArchivesBean;
import bd.com.supply.module.transaction.model.domian.Produce;
import bd.com.supply.module.transaction.model.domian.SoSoAd;
import bd.com.supply.module.transaction.model.domian.TechnologyInfo;
import bd.com.supply.module.transaction.presenter.NewSoSoPresenter;
import bd.com.supply.module.video.VideoPlayActivity;
import bd.com.supply.module.wallet.ApiConfig;
import bd.com.supply.widget.ActivityDialog;
import bd.com.supply.widget.marqueeview.MarqueeView;
import bd.com.supply.module.transaction.model.domian.ArchivesBean;
import bd.com.supply.module.transaction.model.domian.Produce;
import bd.com.supply.module.transaction.model.domian.SoSoAd;
import bd.com.supply.module.transaction.model.domian.TechnologyInfo;
import bd.com.supply.module.transaction.presenter.NewSoSoPresenter;
import bd.com.supply.widget.ActivityDialog;
import bd.com.supply.widget.marqueeview.MarqueeView;

public class NewSoSoActivity extends BaseListActivity<NewSoSoPresenter, NewSoSoPresenter.View, Produce> implements NewSoSoPresenter.View {
    private String prodAddr;
    private String DEFAULT_ADDR = "0x4703f5735e54020e4fd55bb13ce3463b006a61d1";
    private TextView bnTv, plc_tv, spec_tv, sm_tv, nm_tv, pd_tv, mt_tv, ad_tv, tel_tv;
    private MarqueeView ad_marqueeView;
    private ImageView iv_material_sale_out;
    private ImageView bgi;
    private final String IMG_PRE = ApiConfig.SOSO_SOURCE;
    List<String> adContentLists = new ArrayList<>();
    List<SoSoAd> soSoAdList = new ArrayList<>();
    private ActivityDialog activityDialog;
    //标题
    private List<String> mTitles = new ArrayList<>();

    @Override
    protected NewSoSoPresenter initPresenter() {
        return new NewSoSoPresenter();
    }

    @Override
    protected NewSoSoPresenter.View initView() {
        return this;
    }


    @Override
    protected MultiItemTypeAdapter<Produce> createAdapter() {
        return new CommonAdapter<Produce>(getApplicationContext(), R.layout.soso_new_item_layout, datas) {
            @Override
            protected void convert(ViewHolder holder, final Produce soSoBean, int position) {
                TextView platformTv = holder.getView(R.id.platform_tv);

                ArchivesBean archivesBean = soSoBean.archives;
                if (archivesBean == null) return;
                if(TextUtils.isEmpty(soSoBean.perio)){
                    setPlatform(platformTv, 0, soSoBean.title);
                }else {
                    setPlatform(platformTv, Integer.valueOf(soSoBean.perio), soSoBean.title);
                }
                holder.setText(R.id.time_tv, soSoBean.time);

                holder.setText(R.id.name_tv, archivesBean.getNm());
                holder.setText(R.id.addr_tv, archivesBean.getAddr());
                holder.setText(R.id.net_tv, archivesBean.getRp());
                String phone = archivesBean.getMp();
                if (!TextUtils.isEmpty(phone) && phone.length() > 4) {
                    String sub = phone.substring(3, phone.length() - 2);
                    String rep = "";
                    for (int i = 0; i < sub.length(); i++) {
                        rep = rep + "*";
                    }
                    if (rep.length() < 4) {
                        rep = "****";
                    }
                    phone = phone.replace(sub, " " + rep + " ");
                }
                holder.setText(R.id.tel_tv, phone);
                //  holder.setText(R.id.fix_tel_tv, archivesBean.getFp());
                setIcons(holder, archivesBean);
                holder.setOnClickListener(R.id.show_bc_info_tv, new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        showBcInfo(soSoBean.uuid, soSoBean.blockNumber, soSoBean.address);
                    }
                });
                if ("0".equals(soSoBean.perio)) {
                    holder.setVisible(R.id.net_ll, true);
                } else {
                    holder.setVisible(R.id.net_ll, false);
                }
            }
        };
    }

    private void showBcInfo(String uuid, String bn, String opAddress) {
        Intent intent = new Intent(this, BcInfoActivity.class);
        intent.putExtra(IntentKey.PRODUCT_ADDRESS, prodAddr);
        intent.putExtra(IntentKey.PRODUCT_LOG_BN, bn);
        intent.putExtra(IntentKey.PRODUCT_OP_ADDR, opAddress);
        intent.putExtra(IntentKey.PRODUCT_UUID, uuid);
        startActivity(intent);
    }

    boolean hashVideo = false;

    private void setIcons(ViewHolder holder, ArchivesBean archivesBean) {
        List<String> icons = archivesBean.getIcon();
        final String video = archivesBean.getVideo();
        String vdoabbr = archivesBean.getVdoabbr();

        if (!TextUtils.isEmpty(vdoabbr) && !TextUtils.isEmpty(video)) {
            hashVideo = true;
        }
        if (hashVideo) {
            icons.add(0, vdoabbr);
            holder.setVisible(R.id.iv_video_tips, true);
        } else {
            holder.setVisible(R.id.iv_video_tips, false);
        }
        if (icons == null || icons.size() == 0) {
            holder.setVisible(R.id.icons_ll, false);
        } else {
            holder.setVisible(R.id.icons_ll, true);
            final ImageView iv1 = holder.getView(R.id.iv_1);
            ImageView iv2 = holder.getView(R.id.iv_2);
            ImageView iv3 = holder.getView(R.id.iv_3);
            List<ImageView> ivs = new ArrayList<>();
            ivs.add(iv1);
            ivs.add(iv2);
            ivs.add(iv3);

            final List<String> imgUrls = new ArrayList<>();

            for (int i = 0; i < icons.size() && i < 3; i++) {
                imgUrls.add(IMG_PRE + icons.get(i));
                Glide.with(this).load(IMG_PRE + icons.get(i)).into(ivs.get(i));
            }
            iv1.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
//                    String url="http://sctim.cn/group1/M00/00/02/rBDTR10VgDuARbpxABHAzT9T_IU538.mp4";
//                    VideoPlayActivity.startActivity(NewSoSoActivity.this, url);
                    if (hashVideo) {
                        VideoPlayActivity.startActivity(NewSoSoActivity.this, IMG_PRE + video);
                    } else {
                        PreviewPhotoActivity.start(iv1, 0, imgUrls);
                    }

                }
            });
            iv2.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    PreviewPhotoActivity.start(iv1, 1, imgUrls);
                }
            });
            iv3.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    PreviewPhotoActivity.start(iv1, 2, imgUrls);
                }
            });

        }
    }

    private void showPrivateKeyDialog(String content) {

        if (activityDialog != null && activityDialog.isShowing()) {
            activityDialog.dismiss();
        }
        activityDialog = new ActivityDialog(this);
        activityDialog.setPrivateKey(content);
        activityDialog.show();

    }

    private void setPlatform(TextView platformTv, int index, String platName) {
        int bgId = R.drawable.cycle_blue_bg;
        switch (index) {
            case 0:
                platName = "壹";
                bgId = R.drawable.cycle_green_pressed_bg;
                break;
            case 1:
                platName = "贰";
                bgId = R.drawable.cycle_green_normal_bg;
                break;
            case 2:
                platName = "叁";
                bgId = R.drawable.cycle_green_tan_bg;
                break;
            case 3:
                platName = "肆";
                bgId = R.drawable.cycle_blue_bg;
                break;
            case 4:
                platName = "伍";
                bgId = R.drawable.cycle_blue_bg;
                break;
            case 5:
                platName = "陆";
                bgId = R.drawable.cycle_blue_bg;
                break;
        }
        platformTv.setBackgroundResource(bgId);
        platformTv.setText(platName);
    }


    @Override
    protected void initContentView(ViewGroup contentView) {
        super.initContentView(contentView);
        ViewGroup header = (ViewGroup) View.inflate(this, R.layout.soso_header_layout, null);
        //private TextView bnTv,plc_tv,spec_tv,sm_tv,nm_tv,pd_tv,mt_tv,ad_tv,tel_tv;
        bnTv = header.findViewById(R.id.bn_tv);
        iv_material_sale_out = header.findViewById(R.id.iv_material_sale_out);
        plc_tv = header.findViewById(R.id.plc_tv);
        spec_tv = header.findViewById(R.id.spec_tv);
        sm_tv = header.findViewById(R.id.sm_tv);
        nm_tv = header.findViewById(R.id.nm_tv);
        pd_tv = header.findViewById(R.id.pd_tv);
        mt_tv = header.findViewById(R.id.mt_tv);
        ad_tv = header.findViewById(R.id.ad_tv);
        tel_tv = header.findViewById(R.id.tel_tv);
        bgi = header.findViewById(R.id.bgi);
        ad_marqueeView = header.findViewById(R.id.ad_marqueeView);
        mRecyclerView.addHeaderView(header);
        mRecyclerView.setLoadingMoreEnabled(false);

        actionBar.setActionbarDividerVisiable(false);
        ad_marqueeView.setOnItemClickListener(new MarqueeView.OnItemClickListener() {
            @Override
            public void onItemClick(int position, TextView textView) {
                onMarqueeViewItemClick(position);
            }
        });
        // mRecyclerView.setLoadingMoreEnabled(false);
    }

    private void onMarqueeViewItemClick(int position) {
        if (soSoAdList.size() > 0) {
            SoSoAd soSoAd = soSoAdList.get(position);
            Log.e("onMarqueeViewItemClick", "soSoAd is:" + soSoAd);
//            Intent intent = new Intent(this, WebViewActivity.class);
//            intent.putExtra(IntentKey.NEWS_URL, soSoAd.getLink());
//            startActivity(intent);
            showPrivateKeyDialog(soSoAd.getContent());
        }
    }

    @Override
    protected void fetchListItems(@NonNull Map params) {
        mTitles.clear();

        mPresenter.getSosoInfo(prodAddr, AppSettings.getAppSettings().getCurrentAddress());
        mPresenter.getAdList();
    }

    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
    @Override
    protected void initTooBar() {
        super.initTooBar();
        actionBar.setTitle("食品安全溯源");
    }


    @Override
    protected void initData() {
        super.initData();
        prodAddr = getIntent().getStringExtra(IntentKey.PRODUCT_ADDRESS);
        if (TextUtils.isEmpty(prodAddr)) {
            prodAddr = DEFAULT_ADDR;
            showToast("产品地址为空");
        }
    }


    @Override
    public void getCategorySuccess(final TechnologyInfo technologyInfo) {
        if (technologyInfo != null) {
            bnTv.setText(technologyInfo.getBn());
            plc_tv.setText(technologyInfo.getPlc());
            spec_tv.setText(technologyInfo.getSpec());
            sm_tv.setText(technologyInfo.getSm());
            nm_tv.setText(technologyInfo.getNm());
            pd_tv.setText(technologyInfo.getPd());
            mt_tv.setText(technologyInfo.getMat());
            ad_tv.setText(technologyInfo.getAddr());
            tel_tv.setText(technologyInfo.getPh());
            Glide.with(this).load(IMG_PRE + technologyInfo.getBgi()).error(R.mipmap.soso_top_bg).into(bgi);
            final List<String> imgs = new ArrayList<>();
            imgs.add(IMG_PRE + technologyInfo.getBgi());
            bgi.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    PreviewPhotoActivity.start(bgi, 0, imgs);
                }
            });
        }

    }

    @Override
    public void sosoFailed(String msg) {
        showErrorView(msg);
    }

    @Override
    public void sosoAdFailed(String msg) {
        adContentLists.clear();
        this.soSoAdList.clear();

        adContentLists.add(msg);
        ad_marqueeView.startWithList(adContentLists);

    }

    @Override
    public void sosoAdEmpty(String msg) {
        adContentLists.clear();
        this.soSoAdList.clear();

        adContentLists.add(msg);
        ad_marqueeView.startWithList(adContentLists);

    }

    @Override
    public void sosoAdSuccess(List<SoSoAd> soSoAdList) {
        adContentLists.clear();
        this.soSoAdList.clear();

        this.soSoAdList.addAll(soSoAdList);
        for (SoSoAd soSoAd : soSoAdList) {
            adContentLists.add(soSoAd.getName());
        }
        ad_marqueeView.startWithList(adContentLists);


    }

    @Override
    public void isDone(boolean isDone) {
        iv_material_sale_out.setVisibility(isDone ? View.VISIBLE : View.GONE);
    }


}
