package bd.com.supply.module.wallet.ui.fragment;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;

import java.util.ArrayList;

import bd.com.appcore.IntentKey;
import bd.com.appcore.qrcode.QrCodeImportEvent;
import bd.com.appcore.rx.RxTaskCallBack;
import bd.com.appcore.rx.RxTaskScheduler;
import bd.com.appcore.ui.fragment.BaseFragment;
import bd.com.appcore.util.AppSettings;
import bd.com.supply.MainActivity;
import bd.com.supply.R;
import bd.com.supply.module.wallet.ui.MyWalletUtil;
import bd.com.supply.util.ResourceUtil;
import bd.com.supply.util.ResourceUtil;
import bd.com.walletdb.entity.WalletEntity;
import bd.com.walletdb.manager.WalletDBManager;
import de.greenrobot.event.EventBus;
import io.reactivex.disposables.Disposable;


public class KeyStoreFileFragment extends BaseFragment {
    private TextView copyTv;
    private TextView keyStoreTv;
    private WalletEntity entity;

    @Override
    protected int getLayoutId() {
        return R.layout.wallet_keystore_export_file_layout;
    }

    @Override
    protected void initView(View content) {
        super.initView(content);
        copyTv = content.findViewById(R.id.copy_tv);
        keyStoreTv = content.findViewById(R.id.keystore_tv);
        copyTv.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startCopy(keyStoreTv.getText().toString().trim());
            }
        });
    }

    public static KeyStoreFileFragment newInstance(WalletEntity entity) {
        KeyStoreFileFragment fragment = new KeyStoreFileFragment();
        Bundle args = new Bundle();
        args.putParcelable(IntentKey.WALLET_ENTITY, entity);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    protected void initData() {
        super.initData();
        entity = getArguments().getParcelable(IntentKey.WALLET_ENTITY);
        keyStoreTv.setText(entity.getKeystore());
    }


    private void startCopy(String content) {
        ResourceUtil.setPrimaryClip(getContext(), content);
        safetyToast("复制成功");
        AppSettings.getAppSettings().setWalletIsBacked(entity.getAddress(),true);
        WalletDBManager.getManager().updateWallet(entity);
    }
}
