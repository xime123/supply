package bd.com.supply.module.wallet.ui.fragment;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.os.Build;
import android.support.annotation.NonNull;
import android.support.annotation.RequiresApi;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import bd.com.appcore.IntentKey;
import bd.com.appcore.ui.adapter.CommonAdapter;
import bd.com.appcore.ui.adapter.base.ViewHolder;
import bd.com.appcore.ui.fragment.BaseListFragment;
import bd.com.appcore.util.AppSettings;
import bd.com.appcore.util.TDString;
import bd.com.supply.R;
import bd.com.supply.main.OnMenuClickedEvent;
import bd.com.supply.main.TransferSuccessEvent;
import bd.com.supply.module.async.BlockNumberAsync;
import bd.com.supply.module.common.GlobConfig;
import bd.com.supply.module.transaction.model.EnvModel;
import bd.com.supply.module.transaction.ui.NewTxHistoryActivity;
import bd.com.supply.module.transaction.ui.QrcodeActivity;
import bd.com.supply.module.transaction.ui.ScanQrcodeActivity;
import bd.com.supply.module.wallet.ApiConfig;
import bd.com.supply.module.wallet.bus.TokenChangeEvent;
import bd.com.supply.module.wallet.bus.WalletChangeEvent;
import bd.com.supply.module.wallet.presenter.WalletPresenter;
import bd.com.supply.module.wallet.ui.ExportKeyStoreActivity;
import bd.com.supply.module.wallet.ui.TokenListActivity;
import bd.com.supply.module.wallet.view.IWalletView;
import bd.com.supply.util.FakeDataHelper;
import bd.com.supply.util.ImageUtils;
import bd.com.supply.util.ResourceUtil;
import bd.com.supply.widget.MessageDialog;
import bd.com.supply.widget.RunningDeviceWindow;
import bd.com.supply.widget.SelectWalletDialog;
import bd.com.supply.widget.VerifyPwdDialog;
import bd.com.supply.widget.marqueeview.MarqueeView;
import bd.com.supply.module.transaction.model.EnvModel;
import bd.com.supply.module.transaction.ui.NewTxHistoryActivity;
import bd.com.supply.module.transaction.ui.QrcodeActivity;
import bd.com.supply.module.transaction.ui.ScanQrcodeActivity;
import bd.com.supply.module.wallet.ApiConfig;
import bd.com.supply.module.wallet.presenter.WalletPresenter;
import bd.com.supply.module.wallet.ui.TokenListActivity;
import bd.com.supply.widget.SelectWalletDialog;
import bd.com.supply.widget.VerifyPwdDialog;
import bd.com.supply.widget.marqueeview.MarqueeView;
import bd.com.walletdb.entity.ChainEntity;
import bd.com.walletdb.entity.TokenEntity;
import bd.com.walletdb.entity.WalletEntity;
import bd.com.walletdb.manager.WalletDBManager;
import de.greenrobot.event.EventBus;


public class WalletFragment extends BaseListFragment<WalletPresenter, IWalletView, TokenEntity> implements IWalletView {
    private static String TAG = WalletFragment.class.getSimpleName();
    private TextView userNameTv;
    private TextView addressTv;
    private TextView totalMoneyTv;
    private ImageView addFun;
    private TextView chainNameTv;
    private ImageView coinIcon;
    private List<String> chainNameList = new ArrayList<>();
    private List<ChainEntity> chainEntityList = new ArrayList<>();
    private MessageDialog dialog;
    private VerifyPwdDialog verifyPwdDialog;
    private MarqueeView tipsSv;
    private RunningDeviceWindow runningDeviceWindow;
    @Override
    protected boolean setCanRefresh() {
        return true;
    }


    @Override
    protected WalletPresenter initPresenter() {
        return new WalletPresenter();
    }

    @Override
    protected IWalletView initView() {
        ViewGroup header = (ViewGroup) View.inflate(getContext(), R.layout.wallet_main_header_layout, null);
        header.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getActivity(), QrcodeActivity.class);
                String currentChainID = AppSettings.getAppSettings().getCurrentChainId();
                if (GlobConfig.ETH_CHAIN_ID.equals(currentChainID)) {
                    intent.putExtra(IntentKey.TRANSLATE_COIN_TYPE_SYMBLE, FakeDataHelper.MAIN_ETH_SYMBOL);
                } else {
                    intent.putExtra(IntentKey.TRANSLATE_COIN_TYPE_SYMBLE, FakeDataHelper.MAIN_ETH_SYMBOL);
                }

                startActivity(intent);

            }
        });
        tipsSv = header.findViewById(R.id.marqueeView);
        mRecyclerView.addHeaderView(header);
        userNameTv = header.findViewById(R.id.username_tv);
        addressTv = header.findViewById(R.id.address_tv);
        totalMoneyTv = header.findViewById(R.id.total_money_tv);
        addFun = header.findViewById(R.id.add_fundation_iv);
        chainNameTv = header.findViewById(R.id.chain_name_tv);
        addFun.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getActivity(), TokenListActivity.class);
                startActivity(intent);
                //               test();
            }
        });
        actionBar.setOnBackClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                EventBus.getDefault().post(new OnMenuClickedEvent());
            }
        });
        actionBar.setOnMenu2ClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                popWindowRight();
            }
        });

        actionBar.setTitle("钱包");

        coinIcon = header.findViewById(R.id.user_center_head_iv);
        return this;
    }

    private void popWindowRight() {
        initRunningDevWindow();

        if (runningDeviceWindow != null && !runningDeviceWindow.isShowing()) {
            runningDeviceWindow.showPopupWindow(actionBar.getmMenu2Iv());
        }
    }


    @Override
    protected void fetchListItems(@NonNull Map<String, Object> params) {
        if (mPresenter != null) {
            mPresenter.startAsyncToken();
            mPresenter.getChainList();
        }
    }

    @Override
    protected CommonAdapter<TokenEntity> createAdapter() {
        return new CommonAdapter<TokenEntity>(getContext(), R.layout.wallet_item_coin_layout, datas) {
            @Override
            protected void convert(ViewHolder holder, TokenEntity coinBean, int position) {
                holder.setText(R.id.coin_name_tv, coinBean.getSymbol());
                holder.setText(R.id.eth_tv, coinBean.getBalance());
                holder.setText(R.id.eth_money_tv, "¥ " + coinBean.getPrice());
                if (!TextUtils.isEmpty(coinBean.getIcon())) {
                    if (FakeDataHelper.getSctTokenAddr().equals(coinBean.getAddress())) {
                        holder.setImageResource(R.id.coin_iv, R.mipmap.real_app_icon);
                    } else if (FakeDataHelper.getEthTokenAddr().equals(coinBean.getAddress())) {
                        holder.setImageResource(R.id.coin_iv, ResourceUtil.getDrawbleResIdByName(getContext(), coinBean.getIcon()));
                    } else {
                        if (coinBean.getIcon().startsWith("ic_category")) {
                            holder.setImageResource(R.id.coin_iv, ResourceUtil.getDrawbleResIdByName(getContext(), coinBean.getIcon()));
                        } else {
                            ImageView imageView = holder.getView(R.id.coin_iv);
                            imageView.setImageBitmap(ImageUtils.decodeBase64ToBitmap(coinBean.getIcon()));
                        }
                    }

                }

            }
        };
    }

    @SuppressLint("ResourceType")
    @RequiresApi(api = Build.VERSION_CODES.M)
    @Override
    protected void initTooBar(ViewGroup rootView) {
        super.initTooBar(rootView);
        actionBar.setBackResource(R.mipmap.main_menu);
        actionBar.setMenu2Resource(R.mipmap.shouye_btn_add);
        actionBar.setBackgroundColor(getResources().getColor(R.color.white));
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            actionBar.setTitleStyle(R.style.index_title);
        }
        actionBar.getTitleTv().setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                configChain();
            }
        });
        // actionBar.getTitleTv().setBackgroundResource(R.drawable.shape_title_bg);
    }

    List<String> info = new ArrayList<>();

    @Override
    protected void initData() {
        super.initData();
        mPresenter.checkWalletBacked();
        mPresenter.getCurrentWallet();
        info.add("当前块高：");
        info.add("当前块高：");
        tipsSv.startWithList(info);

        BlockNumberAsync.getAsync().addListener(new BlockNumberAsync.OnBlockNumberListener() {
            @Override
            public void onBlockNumber(long blockNum) {
                info.clear();
                info.add("当前块高：" + blockNum);

            }
        });
    }

    @Override
    public void onStart() {
        super.onStart();
        tipsSv.startFlipping();
    }

    private void initRunningDevWindow() {
        if (runningDeviceWindow != null) {
            runningDeviceWindow.dismiss();
        }

        runningDeviceWindow = new RunningDeviceWindow(getActivity(), mPresenter.getFunctions());

    }

    @Override
    public void onStop() {
        super.onStop();
        tipsSv.stopFlipping();
    }

    /**
     * 填充页面
     *
     * @param walletEntity
     */
    public void setWalletInfo(WalletEntity walletEntity) {
        Log.i(TAG, "setWalletInfo  walletEntity=" + walletEntity.toString());
        userNameTv.setText(walletEntity.getName());
        addressTv.setText(walletEntity.getAddress());
        coinIcon.setImageResource(ResourceUtil.getDrawbleResIdByName(getContext(), walletEntity.getIconStr()));
    }


    @Override
    public void setTotalMoney(String totalMoney) {
        totalMoneyTv.setText(totalMoney);
    }

    @Override
    protected void onItemViewClick(RecyclerView.ViewHolder vh, TokenEntity entity) {
        super.onItemViewClick(vh, entity);
        // Intent intent = new Intent(getActivity(), TransactionHisActivity.class);
        Intent intent = new Intent(getActivity(), NewTxHistoryActivity.class);
        intent.putExtra(IntentKey.WALLET_ENTITY, entity);
        startActivity(intent);
    }

    public void onEventMainThread(WalletChangeEvent event) {
        mPresenter.getCurrentWallet();
        mPresenter.checkWalletBacked();
        mRecyclerView.refresh();
        EnvModel.getInstance().init();
    }

    public void onEventMainThread(TransferSuccessEvent event) {
        // mPresenter.getCurrentWallet();
        mRecyclerView.refresh();
    }

    public void onEventMainThread(TokenChangeEvent event) {
        // mPresenter.getCurrentWallet();
        mRecyclerView.refresh();
    }

    @Override
    public void onAttach(Context context) {
        Log.e("test", "onAttach");
        super.onAttach(context);

        EventBus.getDefault().register(this);
    }

    @Override
    public void onDetach() {
        super.onDetach();
        if (dialog != null) {
            dialog.dismiss();
            dialog = null;
        }
        EventBus.getDefault().unregister(this);
    }


    @Override
    public void onGetChainListSuccess(List<ChainEntity> chainEntityList) {
        if (chainEntityList == null) {
            return;
        }
        String currentChain = AppSettings.getAppSettings().getCurrentChainId();
        this.chainEntityList.clear();
        this.chainEntityList.addAll(chainEntityList);
        this.chainNameList.clear();
        for (ChainEntity entity : chainEntityList) {
            if (entity.getChainId().equals(currentChain)) {
                String chainName = entity.getName();
                if (TextUtils.isEmpty(chainName)) {
                    chainName = "链盟宝（内测)";
                }
               // actionBar.setTitle(chainName);
            }
            chainNameList.add(entity.getName());
        }

        // configChain();
    }

    private void setChainNameTv() {

    }

    @Override
    public void onGetChainListFailed(String msg) {
        showErrorToast("无网络连接","请检测网络设置",true);
      //  actionBar.setTitle("无网络连接");
    }

    @Override
    public void isCurrentWalletBacked(WalletEntity entity) {
        if (entity != null && !AppSettings.getAppSettings().getWalletIsBacked(entity.getAddress())) {
            showNoticeDialog();
        }
    }

    @Override
    public void onPassWordRight(int type) {
        gotoBackUpWallet();
    }

    @Override
    public void onPassWordFailed() {
        showToast("密码错误");
        // showNoticeDialog();
    }

    private void showNoticeDialog() {
        dialog = new MessageDialog(getActivity());
        dialog.setTitle("安全提示").setDes("您还没有备份钱包");
        dialog.setSureText("立马备份");
        dialog.setOnOkClickListener(new MessageDialog.OnOkClickListener() {
            @Override
            public void onOkClick(String pwd) {
                dialog.dismiss();
                showVerifyDialog(1);
            }
        });
        dialog.show();
    }

    private void gotoBackUpWallet() {
        Intent intent = new Intent(getActivity(), ExportKeyStoreActivity.class);
        String address = AppSettings.getAppSettings().getCurrentAddress();
        WalletEntity entity = WalletDBManager.getManager().getWalletEntity(address);
        intent.putExtra(IntentKey.WALLET_ENTITY, entity);
        startActivity(intent);
    }

    private void configChain() {
        if (chainNameList.size() == 0) {
            mPresenter.getChainList();
            return;
        }
        SelectWalletDialog dialog = new SelectWalletDialog(getActivity());
        String currentChainName = "当前选择的链为：";
        for (ChainEntity entity : chainEntityList) {
            if (ApiConfig.BASE_URL.equals(entity.getExplorUrl())) {
                currentChainName = currentChainName + entity.getName();
                break;
            }
        }
        dialog.setData(chainNameList, TDString.getStr(R.string.choose_chain_name), currentChainName);
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
            }
        }
    }

    private void showVerifyDialog(final int type) {
        if (verifyPwdDialog != null && verifyPwdDialog.isShowing()) {
            verifyPwdDialog.dismiss();
        }
        verifyPwdDialog = new VerifyPwdDialog(getContext());

        verifyPwdDialog.setOnOkClickListener(new VerifyPwdDialog.OnOkClickListener() {
            @Override
            public void onOkClick(String pwd) {
                verifyPwdDialog.dismiss();
                String address = AppSettings.getAppSettings().getCurrentAddress();
                WalletEntity entity = WalletDBManager.getManager().getWalletEntity(address);
                mPresenter.validAccount(pwd, entity, type);
            }
        });
        verifyPwdDialog.show();
    }


}
