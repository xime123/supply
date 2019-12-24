package bd.com.supply.module.transaction;

import android.content.Context;
import android.content.Intent;
import android.text.TextUtils;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import com.tencent.bugly.crashreport.BuglyLog;

import java.util.HashMap;

import bd.com.appcore.IntentKey;
import bd.com.appcore.qrcode.QrCodeImportEvent;
import bd.com.appcore.ui.fragment.BaseUiFragment;
import bd.com.appcore.ui.view.CircleImageView;
import bd.com.appcore.util.AppSettings;
import bd.com.supply.R;
import bd.com.supply.main.TransferSuccessEvent;
import bd.com.supply.module.common.SosoHookClickListener;
import bd.com.supply.module.transaction.model.EnvModel;
import bd.com.supply.module.transaction.presenter.TracePresenter;
import bd.com.supply.module.transaction.ui.NewTxHistoryActivity;
import bd.com.supply.module.transaction.ui.ScanQrcodeActivity;
import bd.com.supply.module.wallet.bus.TokenChangeEvent;
import bd.com.supply.module.wallet.bus.WalletChangeEvent;
import bd.com.supply.widget.AppDialog;
import bd.com.supply.widget.ChooseOpDialog;
import bd.com.supply.widget.SosoLoadingDialog;
import bd.com.supply.widget.VerifyPwdDialog;
import bd.com.supply.widget.marqueeview.MarqueTextView;
import bd.com.supply.module.transaction.model.EnvModel;
import bd.com.supply.module.transaction.presenter.TracePresenter;
import bd.com.supply.module.transaction.ui.NewTxHistoryActivity;
import bd.com.supply.module.transaction.ui.ScanQrcodeActivity;
import bd.com.supply.widget.AppDialog;
import bd.com.supply.widget.ChooseOpDialog;
import bd.com.supply.widget.VerifyPwdDialog;
import bd.com.supply.widget.marqueeview.MarqueTextView;
import bd.com.walletdb.entity.TokenEntity;
import bd.com.walletdb.entity.WalletEntity;
import bd.com.walletdb.manager.TokenManager;
import bd.com.walletdb.manager.WalletDBManager;
import de.greenrobot.event.EventBus;


public class TraceFragment extends BaseUiFragment<TracePresenter, TracePresenter.View> implements TracePresenter.View, View.OnClickListener {
    AppDialog appDialog;
    private CircleImageView scanIv;
    private TextView balanceTv;
    private TextView sosoBalanceTv;
    private TextView chargeTv;
    private TextView sosoCountTv;
    private TextView sosoTimeTv;
    private MarqueTextView envTv;
    private TextView categoryListTv;
    private TextView productListTv;
    private TextView chargeMoneyTv;
    private TextView cateCountTv;
    private TextView prodCountTv;
    private boolean needPwd = true;

    @Override
    protected TracePresenter initPresenter() {
        return new TracePresenter();
    }

    @Override
    protected TracePresenter.View initView() {
        return this;
    }

    @Override
    protected void initTooBar(ViewGroup rootView) {
        super.initTooBar(rootView);
        actionBar.setVisibility(View.GONE);
    }

    @Override
    protected int getLayoutId() {
        return R.layout.fragment_trace_home;
    }

    @Override
    protected void initContentView(ViewGroup contentView) {
        super.initContentView(contentView);
        categoryListTv = contentView.findViewById(R.id.category_list_tv);
        productListTv = contentView.findViewById(R.id.product_list_tv);
        sosoCountTv = contentView.findViewById(R.id.account_total_kwh);
        sosoTimeTv = contentView.findViewById(R.id.account_total_time);
        envTv = contentView.findViewById(R.id.tv_env);
        scanIv = contentView.findViewById(R.id.charging_scann);
        balanceTv = contentView.findViewById(R.id.charging_home_bill);
        sosoBalanceTv = contentView.findViewById(R.id.account_banace);
        chargeMoneyTv = contentView.findViewById(R.id.charg_money_tv);
        cateCountTv = contentView.findViewById(R.id.account_total_kwh);
        prodCountTv = contentView.findViewById(R.id.account_total_time);
        cateCountTv.setOnClickListener(this);
        prodCountTv.setOnClickListener(this);
        sosoBalanceTv.setOnClickListener(this);
        changeEnv();
        envTv.setOnClickListener(new SosoHookClickListener(getActivity()) {
            @Override
            public void onHookClick() {
                changeEnv();
            }
        });
        scanIv.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getContext(), ScanQrcodeActivity.class);
                intent.putExtra(IntentKey.QRCODE_TYPE, 4);
                startActivity(intent);
//                AppDialog appDialog=AppDialog.loadingCreate(getActivity(),"");
//                appDialog.showLoadingDialog("正在处理...");
            }
        });

        chargeTv = contentView.findViewById(R.id.charging_recharge);
        chargeTv.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getContext(), ScanQrcodeActivity.class);
                intent.putExtra(IntentKey.QRCODE_TYPE, 5);
                startActivity(intent);
            }
        });
        categoryListTv.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                gotoCategoryList();
            }
        });


        productListTv.setOnClickListener(this);
        chargeMoneyTv.setOnClickListener(this);
    }

    private void changeEnv() {
        String currentEnv = EnvModel.getInstance().getCurrentEnv();
        if (!TextUtils.isEmpty(currentEnv)) {
            envTv.setText("当前溯源环境为：" + currentEnv + "《点此切换》");
        }
    }

    private VerifyPwdDialog dialog;
    private ChooseOpDialog opDialog;

    private void showPwdDialog(final String prodAddr, final int type) {
        if (dialog != null && dialog.isShowing()) {
            dialog.dismiss();
        }
        dialog = new VerifyPwdDialog(getActivity());

        dialog.setOnOkClickListener(new VerifyPwdDialog.OnOkClickListener() {
            @Override
            public void onOkClick(String pwd) {
                dialog.dismiss();
                if (validAccount(pwd)) {
                    SoSoConfig.NEED_PWD = false;
                    mPresenter.autoSoso(prodAddr, type);
                }
            }
        });
        dialog.show();
    }

    private void showChooseDialog(final String result, final int type) {
        if (opDialog != null && opDialog.isShowing()) {
            opDialog.dismiss();
        }
        opDialog = new ChooseOpDialog(getActivity());

        opDialog.setOnOkClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                gotoSoso(result, type);
                opDialog.dismiss();
            }
        });

        opDialog.setOnCancelClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (SoSoConfig.NEED_PWD) {
                    showPwdDialog(result, type);
                } else {
                    mPresenter.autoSoso(result, type);
                }
                opDialog.dismiss();
            }
        });
        opDialog.show();
    }

    private void gotoSoso(String result, int type) {
        Intent intent;
        if (type == 6) {
            intent = new Intent(getContext(), ProductListActivity.class);
            intent.putExtra(IntentKey.BOX_ADDRESS, result);
        } else if (type == 10) {//大盒子码溯源记录，跳转盒子列表页面
            intent = new Intent(getContext(), BoxListActivity.class);
            intent.putExtra(IntentKey.BIG_BOX_ADDRESS, result);
        } else {
            intent = new Intent(getContext(), NewSoSoActivity.class);
            intent.putExtra(IntentKey.PRODUCT_ADDRESS, result);
        }
        startActivity(intent);
    }

    private boolean validAccount(String password) {
        if (TextUtils.isEmpty(password)) {
            Toast.makeText(getActivity(), "交易密码不能为空", Toast.LENGTH_LONG).show();
            return false;
        }
        String currentAddres = AppSettings.getAppSettings().getCurrentAddress();
        WalletEntity entity = WalletDBManager.getManager().getWalletEntity(currentAddres);
        if (!TextUtils.equals(password, entity.getPassword())) {
            Toast.makeText(getActivity(), "交易密码不正确", Toast.LENGTH_LONG).show();
            return false;
        }
        return true;
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        EventBus.getDefault().register(this);
    }

    @Override
    public void onDetach() {
        super.onDetach();
        if(appDialog!=null){
            appDialog.dismiss();
        }
        EventBus.getDefault().unregister(this);
    }

    /**
     * menu点击事件
     *
     * @param event
     */
    public void onEventMainThread(QrCodeImportEvent event) {
        BuglyLog.i("QrCodeImportEvent", "event=" + event.getResult());

        if (TextUtils.isEmpty(event.getResult())) {
            showToast("无效二维码");
            return;
        }

        if (!event.getResult().contains("0x")) {
            showToast("无效二维码");
            return;
        }

        switch (event.getType()) {
            case 4:
                String result = event.getResult();
                String res[] = result.split("0x");
                String addr = "0x" + res[1];
                showChooseDialog(addr, event.getType());
                break;
            case 5:
                mPresenter.chargeCoin(event.getResult());
                break;
            case 6:
                String result1 = event.getResult();
                String res1[] = result1.split("0x");
                String addr1 = "0x" + res1[1];
                showChooseDialog(addr1, event.getType());
                break;
            case 10:
                result = event.getResult();
                res = result.split("0x");
                addr = "0x" + res[1];
                showChooseDialog(addr, event.getType());
                break;

        }
    }

    public void onEventMainThread(WalletChangeEvent event) {
        refresh();
    }

    public void onEventMainThread(TransferSuccessEvent event) {
        // mPresenter.getCurrentWallet();
        refresh();
    }

    public void onEventMainThread(TokenChangeEvent event) {
        refresh();
    }


    private void refresh() {
        mPresenter.getBalance(AppSettings.getAppSettings().getLastSosoTokenAddr());
    }

    public void setNeedPwd(boolean needPwd) {
        this.needPwd = needPwd;
    }

    /**
     * 这个生命周期依赖于挂载的activity，tab切换，activity onresume并不会执行,导致fragment也不会执行
     */
    @Override
    public void onResume() {
        super.onResume();
        mPresenter.getBalance(AppSettings.getAppSettings().getLastSosoTokenAddr());
        mPresenter.getBatchList(new HashMap<String, Object>());
        mPresenter.getProductList(new HashMap<String, Object>());
        showPwdDialog(null, -1);
        if (dialog != null) {
            dialog.dismiss();
            dialog = null;
        }
    }

    /**
     * 在使用FragmentPagerAdapter时才会调用
     * @param isVisibleToUser
     */
    @Override
    public void setUserVisibleHint(boolean isVisibleToUser) {
        super.setUserVisibleHint(isVisibleToUser);
    }

    /**
     * 普通activity调用fragment时通过这个函数来监听是否显示（隐藏）
     * @param hidden
     */
    @Override
    public void onHiddenChanged(boolean hidden) {
        super.onHiddenChanged(hidden);
        if(!hidden){
            initMarque();
        }
    }

    @Override
    public void onGetBalanceSuccess(String balance) {
        balanceTv.setText("当前账户余额:" + balance);
        sosoBalanceTv.setText(balance);
    }

    @Override
    public void onReportRecordSuccess(String msg) {
        showToast(msg);
    }

    @Override
    public void onGetBalanceFailed(String msg) {
        balanceTv.setText("当前账户余额:" + msg);
    }

    @Override
    public void onChargeSuccess() {
        showToast("提币成功");
        EventBus.getDefault().post(new TransferSuccessEvent());
        initData();
    }

    @Override
    protected void initData() {
        super.initData();

    }

    private void initMarque() {
        envTv.setSelected(true);
        envTv.setEllipsize(TextUtils.TruncateAt.MARQUEE);
        envTv.setMarqueeRepeatLimit(-1);
        envTv.setFocusable(true);
        envTv.setFocusableInTouchMode(true);
        envTv.requestFocus();
    }

    @Override
    public void onChargeFailed(String msg) {
        safetyToast(msg);
    }

    @Override
    public void productListCount(int count) {
        sosoTimeTv.setText(count + "个产品");
    }

    @Override
    public void categoryListCount(int count) {
        sosoCountTv.setText("打包产品");
    }

    @Override
    public void onSosoSuccess() {
        showToast("自动溯源成功");
    }

    @Override
    public void onSosoFailed(String msg) {
        showToast(msg);
    }

    @Override
    public void onBindResutl(int type) {
        showToast(type == 0 ? "绑定成功" : "绑定失败");
    }


    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.account_total_kwh:
            case R.id.category_list_tv:
                //gotoCategoryList();放到hook里面去
                break;
            case R.id.product_list_tv:
            case R.id.account_total_time:
                gotoProductList();
                break;
            case R.id.charg_money_tv:
            case R.id.account_banace:
                gotoTxHis();
                break;
        }
    }

    private void gotoTxHis() {
        String tokenAddr = AppSettings.getAppSettings().getLastSosoTokenAddr();
        if (TextUtils.isEmpty(tokenAddr)) return;

        TokenEntity tokenEntity = TokenManager.getManager().getTokenByAddress(tokenAddr);
        if (tokenEntity != null) {
            // Intent intent = new Intent(getActivity(), TransactionHisActivity.class);
            Intent intent = new Intent(getActivity(), NewTxHistoryActivity.class);
            intent.putExtra(IntentKey.WALLET_ENTITY, tokenEntity);
            startActivity(intent);
        }
    }

    private void gotoCategoryList() {
//        Intent intent = new Intent(getContext(), CategoryListActivity.class);
//        startActivity(intent);
        //打包
        Intent intent = new Intent(getContext(), PackingActivity.class);
        startActivity(intent);
    }

    private void gotoProductList() {
        Intent intent = new Intent(getContext(), ProductListActivity.class);
        startActivity(intent);
    }

    @Override
    public void showSosoDialog(String text){
        if(appDialog!=null){
            if(TextUtils.isEmpty(text)){
                appDialog.showLoadingDialog();
            }else {
                appDialog.showLoadingDialog(text);
            }
        }else {
            appDialog=AppDialog.loadingCreate(getActivity(),text);
            appDialog.show();
        }
    }

    @Override
    public void hideSosoDialog() {
        if(appDialog!=null){
            appDialog.dismiss();
        }
    }
}
