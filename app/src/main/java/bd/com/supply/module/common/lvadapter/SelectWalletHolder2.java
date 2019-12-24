package bd.com.supply.module.common.lvadapter;

import android.content.Context;
import android.view.View;
import android.widget.TextView;

import bd.com.supply.R;
import bd.com.walletdb.entity.WalletEntity;


public class SelectWalletHolder2 extends BaseHolder {



    private TextView nameTv;
    private TextView addrTv;


    public SelectWalletHolder2(Context context) {

        super(context);

    }

    @Override
    public View initHolderView() {
        mHolderView = View.inflate(mContext, R.layout.item_menu_layout, null);
        nameTv = (TextView) mHolderView.findViewById(R.id.wallet_name_tv);
        addrTv = (TextView) mHolderView.findViewById(R.id.wallet_name_addr);
        return mHolderView;
    }

    @Override
    public void refreshHolderView(Object objectBean, int position) {
       WalletEntity walletEntity = (WalletEntity) objectBean;
        nameTv.setText(walletEntity.getName());
        addrTv.setText(walletEntity.getAddress());
    }


}
