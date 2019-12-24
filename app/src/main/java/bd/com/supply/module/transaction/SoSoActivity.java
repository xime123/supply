package bd.com.supply.module.transaction;

import android.content.Intent;
import android.os.Build;
import android.support.annotation.NonNull;
import android.support.annotation.RequiresApi;
import android.text.TextUtils;
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
import bd.com.supply.R;
import bd.com.supply.module.transaction.model.domian.ArchivesBean;
import bd.com.supply.module.transaction.model.domian.CategoryInfo;
import bd.com.supply.module.transaction.model.domian.CategoryInfoResp;
import bd.com.supply.module.transaction.model.domian.Operation;
import bd.com.supply.module.transaction.model.domian.ProductBean;
import bd.com.supply.module.transaction.model.domian.TechnologyInfo;
import bd.com.supply.module.transaction.presenter.SoSoPresenter;
import bd.com.supply.module.video.VideoPlayActivity;
import bd.com.supply.module.wallet.ApiConfig;
import bd.com.supply.util.DateKit;
import bd.com.supply.module.transaction.model.domian.ArchivesBean;
import bd.com.supply.module.transaction.model.domian.CategoryInfo;
import bd.com.supply.module.transaction.model.domian.Operation;
import bd.com.supply.module.transaction.model.domian.ProductBean;
import bd.com.supply.module.transaction.model.domian.TechnologyInfo;
import bd.com.supply.module.transaction.presenter.SoSoPresenter;

public class SoSoActivity extends BaseListActivity<SoSoPresenter, SoSoPresenter.View, Operation> implements SoSoPresenter.View {
    private String prodAddr;
    private String DEFAULT_ADDR = "0x4703f5735e54020e4fd55bb13ce3463b006a61d1";
    private TextView bnTv, plc_tv, spec_tv, sm_tv, nm_tv, pd_tv, mt_tv, ad_tv, tel_tv;
    private ImageView bgi;
    private final String IMG_PRE = ApiConfig.SOSO_SOURCE;

    //标题
    private List<String> mTitles = new ArrayList<>();

    @Override
    protected SoSoPresenter initPresenter() {
        return new SoSoPresenter();
    }

    @Override
    protected SoSoPresenter.View initView() {
        return this;
    }


    @Override
    protected MultiItemTypeAdapter<Operation> createAdapter() {
        return new CommonAdapter<Operation>(getApplicationContext(), R.layout.soso_new_item_layout, datas) {
            @Override
            protected void convert(ViewHolder holder, final Operation soSoBean, int position) {
                TextView platformTv = holder.getView(R.id.platform_tv);
                final ProductBean productBean = soSoBean.getProductBean();
                ArchivesBean archivesBean = soSoBean.getArchivesBean();
                if (productBean == null || archivesBean == null) return;
                setPlatform(platformTv, Integer.valueOf(productBean.getPerio()), productBean.getTitle());
                String time = productBean.getTime();
                if (!TextUtils.isEmpty(time)) {
                    if (time.length() == 13) {
                        holder.setText(R.id.time_tv, DateKit.timeStamp2Date(Long.valueOf(time) / 1000 + "", null));
                    } else if (time.length() == 10 && !time.contains("-")) {
                        holder.setText(R.id.time_tv, DateKit.timeStamp2Date(Long.valueOf(time) + "", null));
                    } else {
                        holder.setText(R.id.time_tv, productBean.getTime());
                    }
                }

                holder.setText(R.id.name_tv, archivesBean.getNm());
                holder.setText(R.id.addr_tv, archivesBean.getAddr());
                holder.setText(R.id.net_tv, archivesBean.getRp());
                holder.setText(R.id.tel_tv, archivesBean.getMp());

                setIcons(holder, archivesBean);
                holder.setOnClickListener(R.id.show_bc_info_tv, new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        showBcInfo(productBean.getUuid(), soSoBean.getBlockNum(), soSoBean.getOpAddress());
                    }
                });
                if ("0".equals(soSoBean.getPerio())) {
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
            icons.add(vdoabbr);
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
                    if (hashVideo) {
                        VideoPlayActivity.startActivity(SoSoActivity.this, video);
                    } else {
                        PreviewPhotoActivity.start(iv1, 0, imgUrls);
                    }
                }
            });
            iv2.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    PreviewPhotoActivity.start(iv1, hashVideo ? 0 : 1, imgUrls);
                }
            });
            iv3.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    PreviewPhotoActivity.start(iv1, hashVideo ? 1 : 2, imgUrls);
                }
            });

        }
    }

    private void setPlatform(TextView platformTv, int index, String platName) {
        int bgId = R.drawable.cycle_blue_bg;
        switch (index) {
            case 0:
                platName = "厂家";
                bgId = R.drawable.cycle_green_pressed_bg;
                break;
            case 1:
                platName = "省代理";
                bgId = R.drawable.cycle_green_normal_bg;
                break;
            case 2:
                platName = "市代理";
                bgId = R.drawable.cycle_green_tan_bg;
                break;
            case 3:
                platName = "商家";
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
        plc_tv = header.findViewById(R.id.plc_tv);
        spec_tv = header.findViewById(R.id.spec_tv);
        sm_tv = header.findViewById(R.id.sm_tv);
        nm_tv = header.findViewById(R.id.nm_tv);
        pd_tv = header.findViewById(R.id.pd_tv);
        mt_tv = header.findViewById(R.id.mt_tv);
        ad_tv = header.findViewById(R.id.ad_tv);
        tel_tv = header.findViewById(R.id.tel_tv);
        bgi = header.findViewById(R.id.bgi);
        mRecyclerView.addHeaderView(header);


        actionBar.setActionbarDividerVisiable(false);
        // mRecyclerView.setLoadingMoreEnabled(false);
    }

    @Override
    protected void fetchListItems(@NonNull Map params) {
        mTitles.clear();

        mPresenter.getOperationLog(prodAddr);
    }

    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
    @Override
    protected void initTooBar() {
        super.initTooBar();
        // actionBar.setBackgroundColor(getResources().getColor(R.color.green_press));
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
    public void getCategorySuccess(CategoryInfoResp resp) {
        //private TextView bnTv,plc_tv,spec_tv,sm_tv,nm_tv,pd_tv,mt_tv,ad_tv,tel_tv;
        List<CategoryInfo> categoryInfoList = resp.getCategoryInfoList();
        if (categoryInfoList != null && categoryInfoList.size() > 0) {
            CategoryInfo categoryInfo = categoryInfoList.get(0);
            TechnologyInfo technologyInfo = categoryInfo.getTechnologyInfo();
            if (technologyInfo != null) {
                bnTv.setText(technologyInfo.getBn());
                plc_tv.setText(technologyInfo.getPlc());
                spec_tv.setText(technologyInfo.getSpec());
                sm_tv.setText(technologyInfo.getSm());
                nm_tv.setText(technologyInfo.getNm());
                pd_tv.setText(technologyInfo.getPd());
                mt_tv.setText(categoryInfo.getMaterial());
                ad_tv.setText(technologyInfo.getAddr());
                tel_tv.setText(technologyInfo.getPh());
                Glide.with(this).load(IMG_PRE + technologyInfo.getBgi()).error(R.mipmap.soso_top_bg).into(bgi);
            }
        }

    }


    @Override
    public void getCategoryFailed(String msg) {
        showToast(msg);
    }

    @Override
    public void sosoFailed(String msg) {
        showErrorView(msg);
    }

    @Override
    public void onOperationLog(List<Operation> operationList) {

    }


}
