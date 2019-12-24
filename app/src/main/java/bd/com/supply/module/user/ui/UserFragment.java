package bd.com.supply.module.user.ui;

import android.content.Intent;
import android.text.TextUtils;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import com.tencent.bugly.crashreport.CrashReport;

import bd.com.appcore.IntentKey;
import bd.com.appcore.ui.fragment.BaseFragment;
import bd.com.appcore.ui.view.CommonLineTextView;
import bd.com.appcore.util.AppSettings;
import bd.com.supply.R;
import bd.com.supply.module.wallet.ui.WalletDetailActivity;
import bd.com.supply.module.wallet.ui.WalletManagerActivity;
import bd.com.supply.util.APKVersionUtil;
import bd.com.supply.widget.CommonItemView;
import bd.com.supply.widget.VerifyPwdDialog;
import bd.com.supply.widget.CommonItemView;
import bd.com.walletdb.entity.WalletEntity;
import bd.com.walletdb.manager.WalletDBManager;


public class UserFragment extends BaseFragment {
    private CommonItemView addressList;
    private CommonItemView cltWallet;
    private CommonItemView setCtv;
    private CommonItemView sec;
    private TextView walletName;
    private TextView editWallet;

    @Override
    protected int getLayoutId() {
        return R.layout.mine_fram_layout;
    }

    @Override
    protected void initView(View content) {
        super.initView(content);
        addressList = content.findViewById(R.id.address_list);
        walletName = content.findViewById(R.id.tv_mine_name);
        cltWallet = content.findViewById(R.id.info_ctv);
        setCtv = content.findViewById(R.id.setting_ctv);
        sec = content.findViewById(R.id.account_ctv);
        editWallet = content.findViewById(R.id.tv_mine_edit);
        setListener();
    }

    private void setListener() {

        addressList.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getActivity(), ContactListActivity.class);
                startActivity(intent);
            }
        });
        sec.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getActivity(), AccountSecurityManageActivity.class);
                startActivity(intent);
            }
        });
        cltWallet.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getActivity(), WalletManagerActivity.class);
                startActivity(intent);
            }
        });

        setCtv.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getActivity(), SettingActivity.class);
                startActivity(intent);
            }
        });

    }

    @Override
    protected void initData() {
        final String currentAddress = AppSettings.getAppSettings().getCurrentAddress();
        WalletEntity entity = WalletDBManager.getManager().getWalletEntity(currentAddress);
        if (entity != null) {
            walletName.setText(entity.getName());
            editWallet.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent intent = new Intent(getContext(), WalletDetailActivity.class);
                    intent.putExtra(IntentKey.WALLET_ADDRESS, currentAddress);
                    startActivity(intent);
                }
            });
        }
    }
}
