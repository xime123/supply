package bd.com.supply.widget;

import android.app.Dialog;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.support.annotation.NonNull;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.jcodecraeer.xrecyclerview.XRecyclerView;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import bd.com.appcore.ui.adapter.CommonAdapter;
import bd.com.appcore.ui.adapter.base.ViewHolder;
import bd.com.appcore.ui.view.DividerGridItemDecoration;
import bd.com.appcore.util.AppSettings;
import bd.com.supply.R;
import bd.com.supply.util.ImageUtils;
import bd.com.supply.util.ResourceUtil;
import bd.com.supply.util.ImageUtils;
import bd.com.walletdb.entity.WalletEntity;
import bd.com.walletdb.manager.WalletDBManager;

/**
 * author:     labixiaoxin
 * date:       2018/6/19
 * email:      labixiaoxin2@qq.cn
 */
public class SelectIconDialog extends Dialog {
    private Context mContext;
    private XRecyclerView mRecyclerView;
    private CommonAdapter<String> adapter;
    private List<String> datas = new ArrayList<>();
    private OnIconSelectedListener listener;
    private ImageView cancelIv;

    public SelectIconDialog(@NonNull Context context, int themeResId) {
        super(context, themeResId);
        this.mContext = context;
        setContentView(R.layout.select_icon_dialog_layout);
        initAttr();
        initAdapter();
        initView();
    }

    private void initAdapter() {
        for (int i = 0; i < 33; i++) {
            datas.add("ic_category_" + i);
        }
        adapter = new CommonAdapter<String>(mContext, R.layout.item_icon_layout, datas) {
            @Override
            protected void convert(ViewHolder holder, final String s, int position) {
                final int resId = ResourceUtil.getDrawbleResIdByName(mContext, s);
                holder.setImageResource(R.id.icon_iv, resId);
                holder.itemView.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        if (listener != null) {
                            Bitmap bitmap = BitmapFactory.decodeResource(mContext.getResources(), resId);
                            String base64Icon = ImageUtils.encode2Base64ByBitmap(bitmap);
                            listener.onIconSelectd(base64Icon);
                        }
                    }
                });
            }
        };
    }

    private void initView() {
        cancelIv = findViewById(R.id.cancel_iv);
        mRecyclerView = findViewById(R.id.recycler_view);
        mRecyclerView.setPullRefreshEnabled(false);
        mRecyclerView.setLoadingMoreEnabled(false);
        mRecyclerView.setLayoutManager(new GridLayoutManager(mContext, 3));
        mRecyclerView.setAdapter(adapter);
        mRecyclerView.setItemAnimator(new DefaultItemAnimator());
        cancelIv.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dismiss();
            }
        });
    }

    private void initAttr() {
        //获取当前Activity所在的窗体
        Window dialogWindow = getWindow();
        //设置Dialog从窗体底部弹出
        dialogWindow.setGravity(Gravity.BOTTOM);
        //获得窗体的属性
        WindowManager.LayoutParams lp = dialogWindow.getAttributes();
        lp.width = ViewGroup.LayoutParams.MATCH_PARENT;
        lp.x = 0;
        lp.y = 0;//设置Dialog距离底部的距离
//       将属性设置给窗体
        dialogWindow.setAttributes(lp);
    }

    public void setListener(OnIconSelectedListener listener) {
        this.listener = listener;
    }

    public interface OnIconSelectedListener {
        void onIconSelectd(String iconBase64);
    }

    protected List<? extends RecyclerView.ItemDecoration> buildDefaultItemDecorations() {
        int color = mContext.getResources().getColor(R.color.bg_layout);
        return Collections.singletonList(new DividerGridItemDecoration(mContext, 3, color) {
            @Override
            public boolean[] getItemSidesIsHaveOffsets(int itemPosition) {
                //顺序:left, top, right, bottom
                boolean[] booleans = {false, false, false, false};
                if (itemPosition == 0) {
                    //因为给 RecyclerView 添加了 header，所以原本的 position 发生了变化
                    //position 为 0 的地方实际上是 header，真正的列表 position 从 1 开始
                } else {
                    switch (itemPosition % 3) {
                        case 0:
                            //每一行第三个只显示左边距和下边距
                            booleans[0] = true;
                            booleans[3] = true;
                            break;
                        case 1:
                            //每一行第一个显示右边距和下边距
                            booleans[2] = true;
                            booleans[3] = true;
                            break;
                        case 2:
                            //每一行第二个显示左右边距和下边距
                            booleans[0] = true;
                            booleans[2] = true;
                            booleans[3] = true;
                            break;
                    }
                }
                return booleans;
            }
        });
    }
}
