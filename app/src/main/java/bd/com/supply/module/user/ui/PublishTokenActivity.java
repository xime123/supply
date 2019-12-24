package bd.com.supply.module.user.ui;

import android.text.TextUtils;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.EditText;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;

import bd.com.appcore.ui.activity.BaseUiActivity;
import bd.com.appcore.util.AppSettings;
import bd.com.appcore.util.TDString;
import bd.com.supply.R;
import bd.com.supply.module.user.presenter.PublishTokenPresenter;
import bd.com.supply.module.wallet.ApiConfig;
import bd.com.supply.module.wallet.bus.TokenChangeEvent;
import bd.com.supply.widget.SelectChainDialog;
import bd.com.supply.module.user.presenter.PublishTokenPresenter;
import bd.com.supply.module.wallet.ApiConfig;
import bd.com.supply.module.wallet.bus.TokenChangeEvent;
import bd.com.supply.widget.SelectChainDialog;
import bd.com.supply.widget.SelectIconDialog;
import bd.com.walletdb.entity.ChainEntity;
import bd.com.supply.widget.SelectIconDialog;
import bd.com.supply.widget.SelectWalletDialog;
import de.greenrobot.event.EventBus;

public class PublishTokenActivity extends BaseUiActivity<PublishTokenPresenter, PublishTokenPresenter.View> implements PublishTokenPresenter.View {
    private EditText tokenNameEt;
    private EditText tokenSymbolEt;
    private EditText tokenInitAmountEt;
    private EditText tokenPublisherEt;
    private TextView suerTv;

    private String tokenName;
    private String tokenSymbol;
    private String tokenInitAmount;
    private String tokenPublisher;
    private SelectIconDialog dialog;

    private List<String> chainNameList = new ArrayList<>();
    private List<ChainEntity> chainEntityList = new ArrayList<>();
    @Override
    protected PublishTokenPresenter initPresenter() {
        return new PublishTokenPresenter();
    }

    @Override
    protected void initContentView(ViewGroup containerView) {
        super.initContentView(containerView);
        actionBar.setTitle("一键发币");
        tokenNameEt = findViewById(R.id.token_name_et);
        tokenSymbolEt = findViewById(R.id.token_symbol_et);
        tokenInitAmountEt = findViewById(R.id.token_init_mount_et);
        tokenPublisherEt = findViewById(R.id.token_publisher_et);
        suerTv = findViewById(R.id.sure_tv);
        suerTv.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (isValidate()) {
                    showIconDialog();
                }
            }
        });
    }

    private void showIconDialog() {
       dialog=new SelectIconDialog(this,R.style.ActionSheetDialogStyle);
       dialog.setListener(new SelectIconDialog.OnIconSelectedListener() {
           @Override
           public void onIconSelectd(String iconBase64) {
               dialog.dismiss();
               mPresenter.publishToken(tokenName,tokenSymbol,tokenInitAmount,tokenPublisher,iconBase64);
           }
       });
        dialog.show();
    }

    @Override
    protected void initData() {
        super.initData();
        mPresenter.getChainList();
    }

    private boolean isValidate() {
        tokenName = tokenNameEt.getText().toString().trim();
        tokenSymbol = tokenSymbolEt.getText().toString().trim();
        tokenInitAmount = tokenInitAmountEt.getText().toString().trim();
        tokenPublisher = tokenPublisherEt.getText().toString().trim();
        if(TextUtils.isEmpty(tokenName)){
            showToast("代币名称不能为空");
            return false;
        }
        if(TextUtils.isEmpty(tokenSymbol)){
            showToast("代币简称不能为空");
            return false;
        }
        if(TextUtils.isEmpty(tokenInitAmount)){
            showToast("代币总供应量不能为空");
            return false;
        }

        if(TextUtils.isEmpty(tokenPublisher)){
            showToast("代币发行商不能为空");
            return false;
        }

        if(tokenInitAmount.startsWith("0")){
            showToast("代币格式不正确，请输入正确的数字");
            return false;
        }
        return true;
    }

    @Override
    protected PublishTokenPresenter.View initView() {
        return this;
    }

    @Override
    protected int getLayoutId() {
        return R.layout.activity_publish_token_layout;
    }


    @Override
    public void onPublishSuccess() {
        showToast("发布成功");
        EventBus.getDefault().post(new TokenChangeEvent());
        finish();
    }

    @Override
    public void onPublishFailed() {
        showToast("发布失败");
    }

    @Override
    public void onGetChainListSuccess(List<ChainEntity> chainEntityList) {
        this.chainEntityList.clear();
        this.chainEntityList.addAll(chainEntityList);
        this.chainNameList.clear();
        for (ChainEntity entity : chainEntityList) {
            chainNameList.add(entity.getName());
        }
        configChain();
    }

    private void configChain() {
        if(chainNameList.size()==0)return;
        SelectChainDialog dialog = new SelectChainDialog(this);
        String currentChainName = "当前选择的链为：";
        for (ChainEntity entity : chainEntityList) {
            if (ApiConfig.BASE_URL.equals(entity.getExplorUrl())) {
                currentChainName = currentChainName + entity.getName();
                break;
            }
        }
        dialog.setData(chainEntityList, TDString.getStr(R.string.choose_chain_name), currentChainName);
        dialog.setOnItemClickListener(new PublishTokenActivity.OnSelectOnlineItemClick());
        dialog.show();
    }

    @Override
    public void onGetChainListFailed(String msg) {
        showToast(msg);
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
            }
        }
    }
}
