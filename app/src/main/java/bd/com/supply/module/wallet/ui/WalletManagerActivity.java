package bd.com.supply.module.wallet.ui;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;

import java.util.Map;

import bd.com.appcore.IntentKey;
import bd.com.appcore.ui.activity.BaseListActivity;
import bd.com.appcore.ui.adapter.CommonAdapter;
import bd.com.appcore.ui.adapter.base.ViewHolder;
import bd.com.supply.R;
import bd.com.supply.module.common.GlobConfig;
import bd.com.supply.module.wallet.bus.WalletChangeEvent;
import bd.com.supply.module.wallet.presenter.WalletManagerPresenter;
import bd.com.supply.module.wallet.view.IWalletManagerView;
import bd.com.supply.util.ResourceUtil;
import bd.com.supply.module.wallet.view.IWalletManagerView;
import bd.com.supply.util.ResourceUtil;
import bd.com.walletdb.entity.WalletEntity;
import de.greenrobot.event.EventBus;


public class WalletManagerActivity extends BaseListActivity<WalletManagerPresenter,IWalletManagerView,WalletEntity> implements IWalletManagerView{
    @Override
    protected void fetchListItems(@NonNull Map<String, Object> params) {
        if(mPresenter!=null) {
            mPresenter.getDatas();
            mRecyclerView.setLoadingMoreEnabled(false);
            mRecyclerView.setNoMore(true);
        }
    }

    @Override
    protected CommonAdapter<WalletEntity> createAdapter() {
        return new CommonAdapter<WalletEntity>(this, R.layout.wallet_manager_item_layout,datas) {
            @Override
            protected void convert(ViewHolder holder, WalletEntity walletEntity, int position) {
                if(TextUtils.isEmpty(walletEntity.getIconStr())){
                    holder.setImageResource(R.id.wallet_iv,R.mipmap.ic_category_32);
                }else {
                    int resId= ResourceUtil.getDrawbleResIdByName(WalletManagerActivity.this,walletEntity.getIconStr());
                    holder.setImageResource(R.id.wallet_iv,resId);
                }
                holder.setText(R.id.token_type, GlobConfig.getMainTokenType());
                holder.setText(R.id.wallet_name_tv,walletEntity.getName());
                holder.setText(R.id.wallet_address_tv,walletEntity.getAddress());
                holder.setText(R.id.balance_tv,walletEntity.getBalance());
            }
        };
    }

    @Override
    protected void initTooBar() {
        super.initTooBar();
        setTitle("钱包管理");
    }

    @Override
    protected WalletManagerPresenter initPresenter() {
        return new WalletManagerPresenter();
    }

    @Override
    protected IWalletManagerView initView() {
        return this;
    }

    @Override
    protected void onItemViewClick(RecyclerView.ViewHolder vh, WalletEntity entity) {
        super.onItemViewClick(vh, entity);
        Intent intent=new Intent(this,WalletDetailActivity.class);
        intent.putExtra(IntentKey.WALLET_ADDRESS,entity.getAddress());
        startActivity(intent);

    }

    public void onEventMainThread(WalletChangeEvent event) {
        onRefresh();
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

}
