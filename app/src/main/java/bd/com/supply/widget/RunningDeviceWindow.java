package bd.com.supply.widget;

import android.content.Context;
import android.content.Intent;
import android.graphics.drawable.ColorDrawable;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.PopupWindow;
import android.widget.RelativeLayout;

import java.util.List;

import bd.com.appcore.IntentKey;
import bd.com.appcore.ui.adapter.CommonAdapter;
import bd.com.appcore.ui.adapter.MultiItemTypeAdapter;
import bd.com.appcore.ui.adapter.base.ViewHolder;
import bd.com.appcore.util.AppSettings;
import bd.com.supply.MainActivity;
import bd.com.supply.R;
import bd.com.supply.module.common.GlobConfig;
import bd.com.supply.module.transaction.ui.QrcodeActivity;
import bd.com.supply.module.transaction.ui.ScanQrcodeActivity;
import bd.com.supply.module.user.domian.FunItemBean;
import bd.com.supply.module.wallet.ui.CreateWalletActivity;
import bd.com.supply.module.wallet.ui.TokenListActivity;
import bd.com.supply.util.FakeDataHelper;


public class RunningDeviceWindow extends PopupWindow {
    public static final String TAG = RunningDeviceWindow.class.getSimpleName();

    private Context context;
    private RecyclerView running_dev_rv;
    private View mViewTriangle;
    private CommonAdapter<FunItemBean> mAdapter;
    private List<FunItemBean> runningDevs;

    public RunningDeviceWindow(Context context, List<FunItemBean> runningDevs) {
        this.context = context;
        View view = View.inflate(context, R.layout.running_device_window, null);
        this.runningDevs = runningDevs;
        setContentView(view);

        initView(view);
        initPopupWindow();
    }

    private void initView(View view) {
        running_dev_rv = view.findViewById(R.id.running_dev_rv);
        mViewTriangle = view.findViewById(R.id.view_triangle);
        running_dev_rv.setLayoutManager(new LinearLayoutManager(context));

        initAdapter();
        running_dev_rv.setAdapter(mAdapter);
    }

    private void initAdapter() {
        mAdapter = new CommonAdapter<FunItemBean>(context, R.layout.item_running_dev, runningDevs) {
            @Override
            protected void convert(ViewHolder holder, FunItemBean runningDev, int position) {
                holder.setText(R.id.tv_room_name, runningDev.getName());
            }
        };

        mAdapter.setOnItemClickListener(new MultiItemTypeAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(View view, RecyclerView.ViewHolder holder, int position) {
                if ( runningDevs != null && position < runningDevs.size()) {
                    FunItemBean runningDev = runningDevs.get(position);
                    gotoFucn(runningDev);
                    dismiss();
                }
            }

            @Override
            public boolean onItemLongClick(View view, RecyclerView.ViewHolder holder, int position) {
                return false;
            }
        });
    }

    private void gotoFucn(FunItemBean runningDev) {
        switch (runningDev.getType()){
            case FunItemBean.OPEN_SCAN:
                openScanQrcode();
                break;
            case FunItemBean.MOENY:
                gotoToken();
                break;
            case FunItemBean.CREATE_WALLET:
                createWallet();
                break;
        }
    }

    private void gotoToken() {
        Intent intent = new Intent(context, QrcodeActivity.class);
        String currentChainID = AppSettings.getAppSettings().getCurrentChainId();
        if (GlobConfig.ETH_CHAIN_ID.equals(currentChainID)) {
            intent.putExtra(IntentKey.TRANSLATE_COIN_TYPE_SYMBLE, FakeDataHelper.MAIN_ETH_SYMBOL);
        } else {
            intent.putExtra(IntentKey.TRANSLATE_COIN_TYPE_SYMBLE, FakeDataHelper.MAIN_ETH_SYMBOL);
        }

        context.startActivity(intent);
    }


    //初始化popwindow
    private void initPopupWindow() {
        setWidth(ViewGroup.LayoutParams.WRAP_CONTENT);
        setHeight(ViewGroup.LayoutParams.WRAP_CONTENT);
        setBackgroundDrawable(new ColorDrawable());
        setFocusable(true);
        setOutsideTouchable(true);
        /**
         *设置可以触摸
         */
        setTouchable(true);


        /**
         * 设置点击外部可以消失
         */

        setTouchInterceptor(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View view, MotionEvent motionEvent) {
                /**
                 * 判断是不是点击了外部
                 */
                if (motionEvent.getAction() == MotionEvent.ACTION_OUTSIDE) {
                    return true;
                }
                //不是点击外部
                return false;
            }
        });
    }

    /**
     * 显示popupWindow
     */
    public void showPopupWindow(View parent) {
        if (!isShowing()) {
            RelativeLayout.LayoutParams params = (RelativeLayout.LayoutParams) parent.
                    getLayoutParams();
            setTriangleMargin(params.rightMargin + parent.getWidth() / 2);
            // 以下拉方式显示popupwindow
            showAsDropDown(parent);
        }
    }

    //确保小三角显示在parent正下方
    private void setTriangleMargin(int right) {
        RelativeLayout.LayoutParams params = (RelativeLayout.LayoutParams) mViewTriangle.
                getLayoutParams();
        params.setMargins(0, 0, right - params.width / 2, 0);
    }

    public interface IRoomClick {
        void onClickRoom(long roomId, String roomName);
    }

    private IRoomClick iRoomClick;

    public void setiRoomClick(IRoomClick iRoomClick) {
        this.iRoomClick = iRoomClick;
    }

    private void openScanQrcode(){
        Intent intent = new Intent(context, ScanQrcodeActivity.class);
        intent.putExtra(IntentKey.QRCODE_TYPE, 3);
        context.startActivity(intent);
    }

    private void createWallet(){
        Intent intent = new Intent(context, CreateWalletActivity.class);
        context. startActivity(intent);
    }
}
