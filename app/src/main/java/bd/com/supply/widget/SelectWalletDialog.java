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
import bd.com.supply.module.common.lvadapter.SuperAdapter;




public class SelectWalletDialog extends Dialog {
    private ListView listView;
    private TextView titleTv;
    private TextView titleDesTv;
    private TextView cancelTv;
    private EditText searchEt;
    private List<String>mwalletNames=new ArrayList<>();
    private List<String>originalData=new ArrayList<>();
    private Context mContext;
    private String title;
    private String titleDes;
    private MyAdapter adapter;

    public SelectWalletDialog(@NonNull Context context) {
        super(context, R.style.DialogTranslucentNoTitle);
        mContext = context;
        setContentView(R.layout.dialog_select_wallet_layout);
        listView=(ListView) findViewById(R.id.user_lv);
        titleTv=(TextView)findViewById(R.id.title_tv);
        titleDesTv=(TextView)findViewById(R.id.title_des_tv);
        searchEt=(EditText)findViewById(R.id.search_et);
        cancelTv=(TextView)findViewById(R.id.cancel_tv);
        setCanceledOnTouchOutside(false);
    }


    public void setData(List<String>mwalletNames,String title,String titleDes){
        this.mwalletNames.clear();
        this. mwalletNames.addAll(mwalletNames);
        originalData.addAll(mwalletNames);
        this.title=title;
        this.titleDes=titleDes;
        titleTv.setText(title);
        titleDesTv.setText(titleDes);
        adapter=new MyAdapter(mwalletNames);
        listView.setAdapter(adapter);
        setListener();
    }

    public void setEditTvVisiable(){
        searchEt.setVisibility(View.VISIBLE);
        titleDesTv.setVisibility(View.GONE);
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



    public SelectWalletDialog(@NonNull Context context, @StyleRes int themeResId) {
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
            return new SelectWalletHolder(mContext);
        }
    }

    private void setListener(){
        cancelTv.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dismiss();
            }
        });
        searchEt.addTextChangedListener(new TextWatcherProxy(new TextWatcherProxy.OnEdit() {
            @Override
            public void afterTextChanged(Editable s) {
                String result=s.toString();
                mwalletNames.clear();
                if(TextUtils.isEmpty(result)){
                    mwalletNames.addAll(originalData);
                }else {
                    for(String name:originalData){
                        if(name.contains(result)){
                            mwalletNames.add(name);
                        }
                    }
                }
                adapter.bindtems(mwalletNames);
                adapter.notifyDataSetChanged();
            }
        }) {
        });
    }

}
