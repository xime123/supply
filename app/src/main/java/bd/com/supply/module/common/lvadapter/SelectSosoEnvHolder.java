package bd.com.supply.module.common.lvadapter;

import android.content.Context;
import android.view.View;
import android.widget.TextView;

import bd.com.supply.R;
import bd.com.supply.module.transaction.model.domian.SosoEnv;
import bd.com.supply.module.transaction.model.domian.SosoEnv;


public class SelectSosoEnvHolder extends BaseHolder {



    private TextView nameTv;



    public SelectSosoEnvHolder(Context context) {

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
        SosoEnv entity = (SosoEnv) objectBean;
        nameTv.setText(entity.getAlias());
        if(entity.isSelected()){
            nameTv.setSelected(true);
        }else {
            nameTv.setSelected(false);
        }
    }


}
