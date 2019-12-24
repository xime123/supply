package bd.com.supply.module.wallet.ui;

import android.content.Intent;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.SwitchCompat;
import android.text.TextUtils;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CompoundButton;

import java.util.List;
import java.util.Map;

import bd.com.appcore.ui.activity.BaseListActivity;
import bd.com.appcore.ui.adapter.CommonAdapter;
import bd.com.appcore.ui.adapter.base.ViewHolder;
import bd.com.appcore.ui.view.CircleImageView;
import bd.com.appcore.util.AppSettings;
import bd.com.supply.R;
import bd.com.supply.module.common.GlobConfig;
import bd.com.supply.module.transaction.ui.TokenSearchActivity;
import bd.com.supply.module.wallet.bus.TokenChangeEvent;
import bd.com.supply.module.wallet.presenter.TokenPresenter;
import bd.com.supply.util.ImageUtils;
import bd.com.supply.util.ResourceUtil;
import bd.com.supply.module.wallet.presenter.TokenPresenter;
import bd.com.walletdb.entity.TokenEntity;
import bd.com.walletdb.manager.TokenManager;
import de.greenrobot.event.EventBus;


public class TokenListActivity extends BaseListActivity<TokenPresenter, TokenPresenter.ITokenView, TokenEntity> implements TokenPresenter.ITokenView {
    private boolean isSelf = false;

    @Override
    protected void initTooBar() {
        super.initTooBar();
        actionBar.setMenu2Resource(R.mipmap.search_1);
        actionBar.setOnMenu2ClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(TokenListActivity.this, TokenSearchActivity.class);
                startActivity(intent);
            }
        });
        actionBar.setOnBackClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                sendEvent();
                finish();
            }
        });
    }

    @Override
    protected void fetchListItems(@NonNull Map<String, Object> params) {
        if (mPresenter != null) {
            mPresenter.getTokenList(getFetchListItemsParams());
        }
    }

    @Override
    protected CommonAdapter<TokenEntity> createAdapter() {
        return new CommonAdapter<TokenEntity>(this, R.layout.wallet_item_token_layout, datas) {
            @Override
            protected void convert(ViewHolder holder, final TokenEntity tokenEntity, final int position) {
                if (!TextUtils.isEmpty(tokenEntity.getIcon())) {
                    CircleImageView tokenIc = holder.getView(R.id.token_icon);
                    Bitmap bitmap = ImageUtils.decodeBase64ToBitmap(tokenEntity.getIcon());
                    tokenIc.setImageBitmap(bitmap);
                }
                holder.setText(R.id.token_name, tokenEntity.getSymbol());
                holder.setText(R.id.token_symbol, tokenEntity.getPublisher());
                holder.setText(R.id.token_address, tokenEntity.getAddress());
                holder.setChecked(R.id.switch_token_check, tokenEntity.getChecked());
                SwitchCompat switchCompat = holder.getView(R.id.switch_token_check);
                switchCompat.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
                    @Override
                    public void onCheckedChanged(CompoundButton compoundButton, boolean b) {
                        onTokenChecked(tokenEntity, b);
                    }
                });

                String currentChainId = AppSettings.getAppSettings().getCurrentChainId();
                if (!moreOneToken() && GlobConfig.SCT_02_CHAIN_ID.equals(currentChainId) && tokenEntity.getChecked()) {
                    switchCompat.setVisibility(View.GONE);
                } else {
                    switchCompat.setVisibility(View.VISIBLE);
                }
            }
        };
    }


    private void onTokenChecked(TokenEntity tokenEntity, boolean checked) {
        tokenEntity.setChecked(checked);
        //mAdapter.notifyDataSetChanged();
        mPresenter.updateToken(tokenEntity);
        isSelf = true;
    }

    private void sendEvent() {
        EventBus.getDefault().post(new TokenChangeEvent());
    }

    private boolean moreOneToken() {
        int count = 0;
        for (TokenEntity entity : datas) {
            if (entity.getChecked()) {
                count++;
            }
        }
        if (count > 1) {
            return true;
        }
        return false;
    }

    @Override
    protected TokenPresenter initPresenter() {
        return new TokenPresenter();
    }

    @Override
    protected void initContentView(ViewGroup contentView) {
        super.initContentView(contentView);
        actionBar.setTitle("添加新资产");
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EventBus.getDefault().register(this);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        EventBus.getDefault().unregister(this);
    }

    public void onEventMainThread(TokenChangeEvent event) {
        // mPresenter.getCurrentWallet();
        if (!isSelf) {
            mRecyclerView.refresh();
        }
        isSelf = false;
    }

    @Override
    protected TokenPresenter.ITokenView initView() {
        return this;
    }

    @Override
    protected void onItemViewLongClick(RecyclerView.ViewHolder vh, TokenEntity entity) {
        ResourceUtil.setPrimaryClip(this, entity.getAddress());
        showToast("复制成功");
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        sendEvent();
    }
}
