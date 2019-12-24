package bd.com.supply.module.wallet.ui.fragment;

import android.graphics.Bitmap;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;

import bd.com.appcore.IntentKey;
import bd.com.appcore.qrcode.QrcodeGen;
import bd.com.appcore.ui.fragment.BaseFragment;
import bd.com.supply.R;
import bd.com.appcore.util.ConvertUtils;
import bd.com.walletdb.entity.WalletEntity;
import bd.com.walletdb.manager.WalletDBManager;


public class KeyStoreQrcodeFragment extends BaseFragment {

    private ImageView keystoreQrcodeIv;
    @Override
    protected int getLayoutId() {
        return R.layout.wallet_keystore_export_qrcode_layout;
    }

    @Override
    protected void initView(View content) {
        super.initView(content);
        keystoreQrcodeIv=content.findViewById(R.id.qrcode_iv);

    }

    public static KeyStoreQrcodeFragment newInstance(WalletEntity entity) {
        KeyStoreQrcodeFragment fragment = new KeyStoreQrcodeFragment();
        Bundle args = new Bundle();
        args.putParcelable(IntentKey.WALLET_ENTITY,entity);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    protected void initData() {
        super.initData();
        WalletEntity entity=getArguments().getParcelable(IntentKey.WALLET_ENTITY);
        Bitmap qrcodeBmp= QrcodeGen.genQrcodeBitmap(ConvertUtils.dp2px(getContext(),250f),entity.getKeystore());
        keystoreQrcodeIv.setImageBitmap(qrcodeBmp);
//        entity.setIsBacked(true);
//        WalletDBManager.getManager().updateWallet(entity);
    }
}
