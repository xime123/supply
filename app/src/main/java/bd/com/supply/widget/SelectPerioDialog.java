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
import bd.com.supply.module.common.lvadapter.SelectChainHolder;
import bd.com.supply.module.common.lvadapter.SelectPeriosHolder;
import bd.com.supply.module.common.lvadapter.SuperAdapter;
import bd.com.supply.module.common.TextWatcherProxy;
import bd.com.supply.module.common.lvadapter.BaseHolder;
import bd.com.supply.module.common.lvadapter.SuperAdapter;
import bd.com.walletdb.entity.ChainEntity;


/**
 * Created by 徐敏 on 2017/8/15.
 */

public class SelectPerioDialog extends Dialog {
    private ListView listView;
    private TextView titleTv;
    private TextView titleDesTv;
    private TextView cancelTv;
    private TextView sureTv;
    private EditText searchEt;
    private List<Perios> mwalletNames = new ArrayList<>();
    private List<Perios> originalData = new ArrayList<>();
    private Context mContext;
    private String title;
    private String titleDes;
    private MyAdapter adapter;
    private AdapterView.OnItemClickListener listener;
    //tech
    AdapterView<?> parent;
    View view;
    int position;
    long id;

    public SelectPerioDialog(@NonNull Context context) {
        super(context, R.style.DialogTranslucentNoTitle);
        mContext = context;
        setContentView(R.layout.dialog_select_wallet_layout);
        listView = (ListView) findViewById(R.id.user_lv);
        titleTv = (TextView) findViewById(R.id.title_tv);
        titleDesTv = (TextView) findViewById(R.id.title_des_tv);
        searchEt = (EditText) findViewById(R.id.search_et);
        cancelTv = (TextView) findViewById(R.id.cancel_tv);
        sureTv = findViewById(R.id.sure_tv);
        setCanceledOnTouchOutside(false);
    }


    public void setData(List<Perios> mwalletNames, String title, String titleDes) {
        this.mwalletNames.clear();
        this.mwalletNames.addAll(mwalletNames);
        originalData.addAll(mwalletNames);
        this.title = title;
        this.titleDes = titleDes;
        titleTv.setText(title);
        titleDesTv.setText(titleDes);
        adapter = new MyAdapter(mwalletNames);
        listView.setAdapter(adapter);
        setListener();
    }

    public void setEditTvVisiable() {
        searchEt.setVisibility(View.VISIBLE);
        titleDesTv.setVisibility(View.GONE);
    }

    public void setOnItemClickListener(final AdapterView.OnItemClickListener listener) {
        if (listView != null) {
            SelectPerioDialog.this.listener = listener;
            listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                @Override
                public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                    listener.onItemClick(parent, view, position, id);
                    // dismiss();
                    for(int i=0;i<mwalletNames.size();i++){
                        mwalletNames.get(i).setSelected(false);
                    }
                    mwalletNames.get(position).setSelected(true);
                    SelectPerioDialog.this.parent = parent;
                    SelectPerioDialog.this.view = view;
                    SelectPerioDialog.this.position = position;
                    SelectPerioDialog.this.id = id;
                    adapter.notifyDataSetChanged();
                }
            });
        }
    }


    public SelectPerioDialog(@NonNull Context context, @StyleRes int themeResId) {
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
            return new SelectPeriosHolder(mContext);
        }
    }

    private void setListener() {
        sureTv.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (listener != null && parent != null) {
                    listener.onItemClick(parent, view, position, id);
                }
                dismiss();
            }
        });
        cancelTv.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dismiss();
            }
        });
        searchEt.addTextChangedListener(new TextWatcherProxy(new TextWatcherProxy.OnEdit() {
            @Override
            public void afterTextChanged(Editable s) {
                String result = s.toString();
                mwalletNames.clear();
                if (TextUtils.isEmpty(result)) {
                    mwalletNames.addAll(originalData);
                } else {
                    for (Perios entity : originalData) {
                        if (entity.getName().contains(result)) {
                            mwalletNames.add(entity);
                        }
                    }
                }
                adapter.bindtems(mwalletNames);
                adapter.notifyDataSetChanged();
            }
        }) {
        });
    }

    public static class Perios{
        private String name;
        private boolean selected;

        public String getName() {
            return name;
        }

        public void setName(String name) {
            this.name = name;
        }

        public boolean isSelected() {
            return selected;
        }

        public void setSelected(boolean selected) {
            this.selected = selected;
        }
    }

}
