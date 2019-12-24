package bd.com.supply.widget;

import android.content.Context;
import android.text.TextUtils;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.RelativeLayout;
import android.widget.TextView;

import bd.com.appcore.ui.view.CircleImageView;
import bd.com.appcore.util.AppSettings;
import bd.com.supply.R;
import bd.com.supply.module.wallet.bus.WalletChangeEvent;
import bd.com.supply.util.ResourceUtil;
import bd.com.walletdb.entity.WalletEntity;
import de.greenrobot.event.EventBus;


/**
 * author:     labixiaoxin
 * date:       2018/6/22
 * email:      labixiaoxin2@qq.cn
 */
public class WalletItemView extends RelativeLayout {
    private Context context;
    private TextView walletNameTv;
    private TextView walletAddrTv;
    private CircleImageView walletIv;
    private WalletEntity entity;
    public WalletItemView(Context context) {
        super(context);
        init(context);
    }

    public WalletItemView(Context context, AttributeSet attrs) {
        super(context, attrs);
        init(context);
    }

    public WalletItemView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init(context);
    }

    private void init(Context context){
        this.context=context;
        LayoutInflater.from(context).inflate(R.layout.item_menu_layout,this,true);
        walletNameTv=findViewById(R.id.wallet_name_tv);
        walletAddrTv=findViewById(R.id.wallet_name_addr);
        walletIv=findViewById(R.id.wallet_iv);
    }

    public void setContent(final WalletEntity entity){
        this.entity=entity;
        walletNameTv.setText(entity.getName());
        int resId=ResourceUtil.getDrawbleResIdByName(context,entity.getIconStr());
        walletIv.setImageResource(resId);
        String cuurentAddr=AppSettings.getAppSettings().getCurrentAddress();
//        if(TextUtils.equals(cuurentAddr,entity.getAddress())){
//            setBackgroundColor(context.getResources().getColor(R.color.divider));
//        }else {
//            setBackgroundColor(context.getResources().getColor(R.color.transparent));
//        }
        walletAddrTv.setText(entity.getAddress());
        invalidate();
        this.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                AppSettings.getAppSettings().setCurrentAddress(entity.getAddress());
                WalletChangeEvent event=new WalletChangeEvent();
                event.setEntity(entity);
                EventBus.getDefault().post(event);
            }
        });
    }

}
