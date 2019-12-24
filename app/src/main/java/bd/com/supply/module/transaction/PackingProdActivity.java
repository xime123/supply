package bd.com.supply.module.transaction;

import android.content.Intent;
import android.support.annotation.NonNull;
import android.text.TextUtils;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import bd.com.appcore.IntentKey;
import bd.com.appcore.qrcode.QrCodePackProdEvent;
import bd.com.appcore.rx.RxTaskCallBack;
import bd.com.appcore.rx.RxTaskScheduler;
import bd.com.appcore.ui.activity.BaseListActivity;
import bd.com.appcore.ui.adapter.CommonAdapter;
import bd.com.appcore.ui.adapter.MultiItemTypeAdapter;
import bd.com.appcore.ui.adapter.base.ViewHolder;
import bd.com.appcore.util.AppSettings;
import bd.com.supply.R;
import bd.com.supply.module.transaction.model.domian.ProductOri;
import bd.com.supply.module.transaction.presenter.PackingProdPresenter;
import bd.com.supply.module.transaction.ui.ScanQrcodeActivity;
import bd.com.supply.web3.Web3Proxy;
import bd.com.supply.web3.contract.Authorized;
import bd.com.supply.web3.contract.Product;
import bd.com.supply.widget.VerifyPwdDialog;
import bd.com.supply.module.transaction.presenter.PackingProdPresenter;
import bd.com.supply.module.transaction.ui.ScanQrcodeActivity;
import bd.com.supply.widget.VerifyPwdDialog;
import bd.com.walletdb.entity.WalletEntity;
import bd.com.walletdb.manager.WalletDBManager;
import de.greenrobot.event.EventBus;

public class PackingProdActivity extends BaseListActivity<PackingProdPresenter, PackingProdPresenter.View, ProductOri> implements PackingProdPresenter.View {
    private String aaddr;
    private List<String> prodAddrs = new ArrayList<>();


    @Override
    protected void fetchListItems(@NonNull Map<String, Object> params) {
        if (prodAddrs.size() == 0) {
            showEmptyView();
        }
    }

    @Override
    protected void initTooBar() {
        super.initTooBar();
        EventBus.getDefault().register(this);

        setTitle("打包产品");
        actionBar.setRightTv("添加产品");
        actionBar.setOnRightTvClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(PackingProdActivity.this, ScanQrcodeActivity.class);
                intent.putExtra(IntentKey.QRCODE_TYPE, 9);
                startActivity(intent);
            }
        });
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        EventBus.getDefault().unregister(this);
    }

    @Override
    protected void initContentView(ViewGroup contentView) {
        super.initContentView(contentView);
        addBottomView();
        mRecyclerView.setPullRefreshEnabled(false);
    }

    @Override
    protected MultiItemTypeAdapter<ProductOri> createAdapter() {
        return new CommonAdapter<ProductOri>(this, R.layout.item_auth_layout, datas) {
            @Override
            protected void convert(ViewHolder holder, ProductOri productOri, int position) {
                //holder.setText(R.id.time_tv, DateKit.timeStamp2Date(productOri.getCreateTime() / 1000 + "", null));
                holder.setVisible(R.id.time_tv, false);
                holder.setVisible(R.id.auth_name_tv, false);
                holder.setVisible(R.id.add_auth_info_iv, false);
                holder.setText(R.id.auth_address_tv, productOri.getPaddr());
                //holder.setText(R.id.product_name_tv, productOri.getName());
                //holder.setVisible(R.id.add_prod_info_iv, false);
            }
        };
    }

    @Override
    protected void initData() {
        super.initData();
        aaddr = getIntent().getStringExtra(IntentKey.AUTH_ADDRESS);
        if (TextUtils.isEmpty(aaddr)) {
            showToast("鉴权地址为空");
            finish();
            return;
        }
    }

    @Override
    protected PackingProdPresenter initPresenter() {
        return new PackingProdPresenter();
    }

    @Override
    protected PackingProdPresenter.View initView() {
        return this;
    }

    private void addBottomView() {
        ViewGroup bottomView = (ViewGroup) View.inflate(this, R.layout.product_list_bottom_layout, null);
        TextView addSosoTv = bottomView.findViewById(R.id.add_soso_tv);
        addSosoTv.setText("扫盒子码打包");
        addSosoTv.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (prodAddrs.size() == 0) {
                    showToast("请添加产品");
                    return;
                }
                Intent intent = new Intent(PackingProdActivity.this, ScanQrcodeActivity.class);
                intent.putExtra(IntentKey.QRCODE_TYPE, 8);
                startActivity(intent);
            }
        });
        addBottomFloatView(bottomView);
    }

    public void onEventMainThread(final QrCodePackProdEvent event) {
        if (event.getType() == 8) {//扫码打包盒子
            if(SoSoConfig.NEED_PWD){
                showPwdDialog(event.getResult());
            }else {
                mPresenter.packProdList(event.getResult(), prodAddrs, aaddr);
            }

        } else if (event.getType() == 9) {//扫码添加产品
            if (prodAddrs.contains(event.getResult())) {
                showToast("重复产品");
                return;
            }
            RxTaskScheduler.postIoMainTask(new RxTaskCallBack<Boolean>() {
                @Override
                public void onSuccess(Boolean aBoolean) {
                    super.onSuccess(aBoolean);
                    if(aBoolean){
                        prodAddrs.add(event.getResult());
                        mPresenter.getProd(event.getResult());
                    }else {
                        showToast("鉴权地址不合法");
                    }
                }

                @Override
                public Boolean doWork() throws Exception {
                    String prodAddr=event.getResult();
                    Product product=Product.load(prodAddr, Web3Proxy.getWeb3Proxy().getWeb3j(), Web3Proxy.getWeb3Proxy().getPoolTransactionManager(), Web3Proxy.GAS_PRICE, Web3Proxy.PACK_SOSO_GAS_LIMIT);
                    String authAddr=product.authorizedIdentify().send();
                    return aaddr.equalsIgnoreCase(authAddr);
                }
            });

        }
    }

    private VerifyPwdDialog dialog;

    private void showPwdDialog(final String boxAddr) {
        if (dialog != null && dialog.isShowing()) {
            dialog.dismiss();
        }
        dialog = new VerifyPwdDialog(this);

        dialog.setOnOkClickListener(new VerifyPwdDialog.OnOkClickListener() {
            @Override
            public void onOkClick(String pwd) {
                dialog.dismiss();
                if (validAccount(pwd)) {
                    mPresenter.packProdList(boxAddr, prodAddrs, aaddr);
                    SoSoConfig.NEED_PWD = false;
                }
            }
        });
        dialog.show();
    }

    private boolean validAccount(String password) {
        if (TextUtils.isEmpty(password)) {
            Toast.makeText(this, "交易密码不能为空", Toast.LENGTH_LONG).show();
            return false;
        }
        String currentAddres = AppSettings.getAppSettings().getCurrentAddress();
        WalletEntity entity = WalletDBManager.getManager().getWalletEntity(currentAddres);
        if (!TextUtils.equals(password, entity.getPassword())) {
            Toast.makeText(this, "交易密码不正确", Toast.LENGTH_LONG).show();
            return false;
        }
        return true;
    }


    @Override
    public void packingFailed(String msg) {
        showToast(msg);
    }

    @Override
    public void packingSuccess() {
        showToast("打包成功");
        finish();
    }

    @Override
    public void reportFailed(String msg) {
        showToast("上报失败");
    }

    @Override
    public void reportSuccess() {
        showToast("上报成功");
        finish();
    }

    @Override
    public void unLegalForProd(String errorMsg) {
        showToast(errorMsg);
    }

    @Override
    public void remainProdOri(List<ProductOri> remainOriList) {
        datas.clear();
        datas.addAll(remainOriList);
        mAdapter.notifyDataSetChanged();
    }
}
