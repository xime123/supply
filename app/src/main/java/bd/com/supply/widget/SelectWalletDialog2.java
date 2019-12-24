package bd.com.supply.widget;

import android.app.Dialog;
import android.content.Context;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.StyleRes;
import android.text.Editable;
import android.text.TextUtils;
import android.view.View;
import android.widget.AdapterView;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;

import bd.com.supply.R;
import bd.com.supply.module.common.TextWatcherProxy;
import bd.com.supply.module.common.lvadapter.BaseHolder;
import bd.com.supply.module.common.lvadapter.SelectWalletHolder;
import bd.com.supply.module.common.lvadapter.SelectWalletHolder2;
import bd.com.supply.module.common.lvadapter.SuperAdapter;
import bd.com.walletdb.entity.WalletEntity;


public class SelectWalletDialog2 extends Dialog {
    private ListView listView;

    private List<WalletEntity>mwalletNames=new ArrayList<>();
    private List<WalletEntity>originalData=new ArrayList<>();
    private Context mContext;

    private MyAdapter adapter;

    public SelectWalletDialog2(@NonNull Context context) {
        super(context, R.style.DialogTranslucentNoTitle2);
        mContext = context;
        setContentView(R.layout.dialog_select_wallet_layout2);
        listView=(ListView) findViewById(R.id.user_lv);

        setCanceledOnTouchOutside(true);
    }


    public void setData(List<WalletEntity>mwalletNames){
        this.mwalletNames.clear();
        this. mwalletNames.addAll(mwalletNames);
        originalData.addAll(mwalletNames);

        adapter=new MyAdapter(mwalletNames);
        listView.setAdapter(adapter);
    }


    public void setOnItemClickListener(final AdapterView.OnItemClickListener listener){
        if(listView!=null) {
            listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                @Override
                public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                    listener.onItemClick(parent,view,position,id);
                    dismiss();
                }
            });
        }
    }



    public SelectWalletDialog2(@NonNull Context context, @StyleRes int themeResId) {
        super(context, themeResId);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }

    class MyAdapter extends SuperAdapter {

        public MyAdapter(List dates) {
            super(dates);
        }

        @Override
        public BaseHolder getSpecialHolder() {
            return new SelectWalletHolder2(mContext);
        }
    }


}
