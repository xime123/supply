package bd.com.supply.module.user.ui;

import android.content.Intent;
import android.support.v7.widget.SwitchCompat;
import android.text.TextUtils;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.CompoundButton;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.List;

import bd.com.appcore.ui.activity.BaseUiActivity;
import bd.com.appcore.ui.view.CommonLineTextView;
import bd.com.appcore.util.AppSettings;
import bd.com.appcore.util.TDString;
import bd.com.appupdate.util.UpdateAppUtils;
import bd.com.supply.R;
import bd.com.supply.module.common.UpdateInfo;
import bd.com.supply.module.user.presenter.SettingPresenter;
import bd.com.supply.module.wallet.ApiConfig;
import bd.com.supply.module.wallet.bus.TokenChangeEvent;
import bd.com.supply.util.APKVersionUtil;
import bd.com.supply.widget.SelectChainDialog;
import bd.com.supply.widget.VerifyPwdDialog;
import bd.com.supply.module.common.UpdateInfo;
import bd.com.supply.module.user.presenter.SettingPresenter;
import bd.com.supply.module.wallet.ApiConfig;
import bd.com.supply.widget.SelectChainDialog;
import bd.com.supply.widget.VerifyPwdDialog;
import bd.com.walletdb.entity.ChainEntity;
import bd.com.walletdb.entity.WalletEntity;
import bd.com.walletdb.manager.WalletDBManager;
import de.greenrobot.event.EventBus;

public class SettingActivity extends BaseUiActivity<SettingPresenter, SettingPresenter.View> implements SettingPresenter.View {
    private CommonLineTextView clearCacheCtv;
    private CommonLineTextView updateCtv;
    private CommonLineTextView configChainCtv;
    private CommonLineTextView protocoltv;
    private CommonLineTextView publishTokenSec;
    private CommonLineTextView shareCtv;
    private SwitchCompat logSwitch;
    private List<String> chainNameList = new ArrayList<>();
    private List<ChainEntity> chainEntityList = new ArrayList<>();

    @Override
    protected SettingPresenter initPresenter() {
        return new SettingPresenter();
    }

    @Override
    protected SettingPresenter.View initView() {
        return this;
    }

    @Override
    protected void initData() {
        super.initData();
        mPresenter.getCacheSize(this);
        mPresenter.getChainList();
        String versionName = APKVersionUtil.getVerName(this);
        updateCtv.setContent("V" + versionName);
        boolean enableBugly = AppSettings.getAppSettings().getEnableBugly();
        logSwitch.setChecked(enableBugly);
        logSwitch.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean b) {
                AppSettings.getAppSettings().setEnableBugly(b);
                showToast(b ? "日志已开启" : "日志已关闭");
            }
        });
    }


    @Override
    protected void initContentView(ViewGroup containerView) {
        super.initContentView(containerView);
        actionBar.setTitle("系统设置");
        logSwitch = findViewById(R.id.log_switch_lockpattern);
        clearCacheCtv = findViewById(R.id.clear_cache_ctv);
        configChainCtv = findViewById(R.id.config_chain_ctv);
        updateCtv = findViewById(R.id.app_update_ctv);
        protocoltv = findViewById(R.id.protocol_tv);
        publishTokenSec = findViewById(R.id.publish_token_list);
        shareCtv = findViewById(R.id.share_ctv);

        clearCacheCtv.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                mPresenter.doClearCache(SettingActivity.this);
            }
        });
        updateCtv.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                mPresenter.checkUpdate(SettingActivity.this);
            }
        });
        configChainCtv.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                configChain();
            }
        });
        protocoltv.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(SettingActivity.this, ProtocolActivity.class);
                startActivity(intent);
            }
        });
        publishTokenSec.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showPwdDialog();
            }
        });
        shareCtv.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                sendtext();
            }
        });
    }

    @Override
    protected int getLayoutId() {
        return R.layout.activity_setting;
    }


    @Override
    public void onGetCacheSize(String size) {
        //clearCacheCtv.setContent(size);
    }

    @Override
    public void onClearSuccess() {
        showToast("清理成功");
    }

    @Override
    public void checkedUpdate(UpdateInfo info) {
        OnUpdateDialogCancel onUpdateDialogCancel = new OnUpdateDialogCancel();
        mPresenter.realUpdate(this, info, onUpdateDialogCancel);

    }

    @Override
    public void checkedUpdateFailed(String info) {
        showToast("已经是最新版本");
    }

    public class OnUpdateDialogCancel implements UpdateAppUtils.OnCancelClicked {

        @Override
        public void canceled(boolean force) {
            if (force) {
                finish();
            }
        }
    }

    @Override
    public void onGetChainListSuccess(List<ChainEntity> chainEntityList) {
        if (chainEntityList == null) return;
        this.chainEntityList.clear();
        this.chainEntityList.addAll(chainEntityList);
        this.chainNameList.clear();
        for (ChainEntity entity : chainEntityList) {
            chainNameList.add(entity.getName());
        }
    }

    @Override
    public void onGetChainListFailed(String msg) {
        showToast(msg);
    }

    private void configChain() {
        if (chainNameList.size() == 0) return;
        SelectChainDialog dialog = new SelectChainDialog(this);
        String currentChainName = "当前选择的链为：";
        for (ChainEntity entity : chainEntityList) {
            if (ApiConfig.BASE_URL.equals(entity.getExplorUrl())) {
                currentChainName = currentChainName + entity.getName();
                break;
            }
        }
        dialog.setData(chainEntityList, TDString.getStr(R.string.choose_chain_name), currentChainName);
        dialog.setOnItemClickListener(new OnSelectOnlineItemClick());
        dialog.show();
    }

    /**
     * 链选择
     */
    class OnSelectOnlineItemClick implements AdapterView.OnItemClickListener {

        @Override
        public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
            String chainIp = chainEntityList.get(position).getExplorUrl();
            if (!TextUtils.isEmpty(chainIp)) {
                AppSettings.getAppSettings().setCurrentChainIp(chainIp);
                AppSettings.getAppSettings().setCurrentChinId(chainEntityList.get(position).getChainId());
                ApiConfig.BASE_URL = chainIp;
                EventBus.getDefault().post(new TokenChangeEvent());
                // AppDataInitor.getInitor().init();
            }
        }
    }

    private VerifyPwdDialog dialog;

    private void showPwdDialog() {
        if (dialog != null && dialog.isShowing()) {
            dialog.dismiss();
        }
        dialog = new VerifyPwdDialog(this);

        dialog.setOnOkClickListener(new VerifyPwdDialog.OnOkClickListener() {
            @Override
            public void onOkClick(String pwd) {
                dialog.dismiss();
                if (validAccount(pwd)) {
                    Intent intent = new Intent(SettingActivity.this, PublishTokenActivity.class);
                    startActivity(intent);
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

    //创建方法将输入的内容发出去
    public void sendtext() {
        String text = APKVersionUtil.getDownLoadUrl();
        if (TextUtils.isEmpty(text)) {
            return;
        }
        Intent intent = new Intent();
        /*设置action为发送分享，
         *并判断要发送分享的内容是否为空
         */
        intent.setAction(Intent.ACTION_SEND);
        if (text != null) {
            intent.putExtra(Intent.EXTRA_TEXT, text);
        } else {
            intent.putExtra(Intent.EXTRA_TEXT, "");
        }
        intent.setType("text/plain");//设置分享发送的数据类型
        //未指定选择器，部分定制系统首次选择后，后期将无法再次改变
        //指定选择器选择使用有发送文本功能的App
        startActivity(Intent.createChooser(intent, getResources().getText(R.string.app_name)));
    }
}
