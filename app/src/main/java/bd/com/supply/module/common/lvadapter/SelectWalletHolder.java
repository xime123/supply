package bd.com.supply.module.common.lvadapter;

import android.content.Context;
import android.view.View;
import android.widget.TextView;

import bd.com.supply.R;


public class SelectWalletHolder extends BaseHolder {



    private TextView nameTv;



    public SelectWalletHolder(Context context) {

        super(context);

    }

    @Override
    public View initHolderView() {
        mHolderView = View.inflate(mContext, R.layout.item_select_wallet_layout, null);
        nameTv = (TextView) mHolderView.findViewById(R.id.wallet_name_tv);
        return mHolderView;
    }

    @Override
    public void refreshHolderView(Object objectBean, int position) {
       String nameStr = (String) objectBean;
        nameTv.setText(nameStr);

    }


}
