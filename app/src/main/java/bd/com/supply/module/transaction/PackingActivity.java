package bd.com.supply.module.transaction;

import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.view.ViewGroup;

import java.util.Map;

import bd.com.appcore.IntentKey;
import bd.com.appcore.ui.activity.BaseListActivity;
import bd.com.appcore.ui.adapter.CommonAdapter;
import bd.com.appcore.ui.adapter.MultiItemTypeAdapter;
import bd.com.appcore.ui.adapter.base.ViewHolder;
import bd.com.supply.R;
import bd.com.supply.module.transaction.model.domian.AuthBean;
import bd.com.supply.module.transaction.presenter.PackingPresenter;
import bd.com.supply.widget.ChooseOpDialog;
import bd.com.supply.module.transaction.model.domian.AuthBean;
import bd.com.supply.module.transaction.presenter.PackingPresenter;
import bd.com.supply.widget.ChooseOpDialog;
import de.greenrobot.event.EventBus;

public class PackingActivity extends BaseListActivity<PackingPresenter, PackingPresenter.View, AuthBean> implements PackingPresenter.View {
    private ChooseOpDialog opDialog;

    @Override
    protected void fetchListItems(@NonNull Map<String, Object> params) {
        mPresenter.getAuthList(params);
    }

    @Override
    protected void initContentView(ViewGroup contentView) {
        super.initContentView(contentView);
        setTitle("选择鉴权");
    }

    @Override
    protected MultiItemTypeAdapter<AuthBean> createAdapter() {
        return new CommonAdapter<AuthBean>(this, R.layout.item_auth_layout, datas) {
            @Override
            protected void convert(ViewHolder holder, AuthBean authBean, int position) {
                holder.setText(R.id.auth_name_tv, authBean.alias + "");
                holder.setText(R.id.auth_address_tv, authBean.aaddr + "");
                holder.setText(R.id.time_tv, authBean.createTime + "");
            }
        };
    }


    @Override
    protected void onItemViewClick(RecyclerView.ViewHolder vh, AuthBean entity) {
        super.onItemViewClick(vh, entity);
//        Intent intent = new Intent(this, PackingProdActivity.class);
//        intent.putExtra(IntentKey.AUTH_ADDRESS, entity.aaddr);
//        startActivity(intent);
        showChooseDialog(entity.aaddr);
    }

    @Override
    protected PackingPresenter initPresenter() {
        return new PackingPresenter();
    }

    @Override
    protected PackingPresenter.View initView() {
        return this;
    }

    private void showChooseDialog(final String aaddr) {
        if (opDialog != null && opDialog.isShowing()) {
            opDialog.dismiss();
        }
        opDialog = new ChooseOpDialog(this);
        opDialog.setDes("请选择打包内容")
                .setOk("产品打包")
                .setCancel("盒子打包");
        opDialog.setOnOkClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                gotoPacking(PackingProdActivity.class, aaddr);
                opDialog.dismiss();
            }
        });

        opDialog.setOnCancelClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                gotoPacking(PackingBoxActivity.class, aaddr);
                opDialog.dismiss();
            }
        });
        opDialog.show();
    }

    private void gotoPacking(Class clazz, String aaddr) {
        Intent intent = new Intent(this, clazz);
        intent.putExtra(IntentKey.AUTH_ADDRESS, aaddr);
        startActivity(intent);
    }
}
