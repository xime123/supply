package bd.com.supply;

import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.annotation.RequiresApi;
import android.support.v4.app.FragmentManager;
import android.text.TextUtils;
import android.util.Log;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ImageView;
import android.widget.TabHost;
import android.widget.TabWidget;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.List;

import bd.com.appcore.IntentKey;
import bd.com.appcore.rx.RxTaskCallBack;
import bd.com.appcore.rx.RxTaskScheduler;
import bd.com.appcore.ui.activity.BaseCoreActivity;
import bd.com.appcore.util.AppSettings;
import bd.com.appupdate.util.UpdateAppUtils;
import bd.com.supply.main.OnMenuClickedEvent;
import bd.com.supply.module.common.UpdateInfo;
import bd.com.supply.module.transaction.SoSoConfig;
import bd.com.supply.module.user.ui.UserFragment;
import bd.com.supply.module.wallet.bus.WalletChangeEvent;
import bd.com.supply.module.wallet.ui.fragment.WalletFragment;
import bd.com.supply.widget.MessageDialog;
import bd.com.supply.widget.MyFragmentTabHost;
import bd.com.supply.widget.SelectWalletDialog2;
import bd.com.walletdb.entity.WalletEntity;
import bd.com.walletdb.manager.WalletDBManager;
import de.greenrobot.event.EventBus;

public class MainActivity extends BaseCoreActivity implements TabHost.OnTabChangeListener, MainPresenter.View {
    // 定义一个变量，来标识是否退出
    private static boolean isExit = false;

    Handler mHandler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            isExit = false;
        }
    };

    private static final String TAG = "MainActivity";
    private static final String TAG_HOME = "tag_home";
    private static final String TAG_APP = "tag_app";
    private static final String TAG_PRICE = "tag_price";
    private static final String TAG_NEWS = "tag_news";
    private static final String TAG_USER = "tag_userCenter";


    public static final int MAIN_TAB_HOME = 0;
    public static final int MAIN_TAB_CATEGORY = 1;

    public static final int MAIN_TAB_CART = 2;
    private FragmentManager mFragmentManager;
    public static final int MAIN_TAB_USER = 3;

    private MyFragmentTabHost mTabHost;



    private List<WalletEntity> walletEntities = new ArrayList<>();

    private MessageDialog dialog;
    private MainPresenter mPresenter;

    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        Log.e("test", "onCreate");
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        mPresenter = new MainPresenter();
        mPresenter.onAttachView(this);
        EventBus.getDefault().register(this);
        initView();

        initData();
        immerse(true);
    }

    @Override
    protected void onNewIntent(Intent intent) {
        super.onNewIntent(intent);
        Log.e("MainActivity", "onNewIntent!!!!");
        String title = intent.getStringExtra(IntentKey.MESSAGE_TITLE);
        String content = intent.getStringExtra(IntentKey.MESSAGE_CONTENT);
        if (!TextUtils.isEmpty(title) && !TextUtils.isEmpty(content)) {
            this.showMessageDialog(title, content);
        }
    }

    private void initView() {

        // TODO Auto-generated method stub
        mTabHost = (MyFragmentTabHost) findViewById(android.R.id.tabhost);
        mTabHost.setup(this, getSupportFragmentManager(), R.id.realtabcontent);
        TabWidget tabWidget = (TabWidget) findViewById(android.R.id.tabs);
        tabWidget.setDividerDrawable(null);
        //首页
        mTabHost.addTab(mTabHost.newTabSpec(TAG_HOME).setIndicator(getIndicatorView(R.drawable.ic_index, "金融")), WalletFragment.class, null);
        //溯源
     //   mTabHost.addTab(mTabHost.newTabSpec(TAG_APP).setIndicator(getIndicatorView(R.drawable.ic_finance, "溯源")), TraceFragment.class, null);

//        //行情
//        mTabHost.addTab(mTabHost.newTabSpec(TAG_PRICE).setIndicator(
//                getIndicatorView(R.drawable.ic_discovery,  "行情")),
//                PriceFragment.class, null);

        //公告
//        mTabHost.addTab(mTabHost.newTabSpec(TAG_NEWS).setIndicator(getIndicatorView(R.drawable.ic_discovery, "公告")), WebViewFragment.class, null);
        //个人中心
        mTabHost.addTab(mTabHost.newTabSpec(TAG_USER).setIndicator(getIndicatorView(R.drawable.ic_mine, "我")), UserFragment.class, null);

        mTabHost.setOnTabChangedListener(this);
        // setTabHostClickable(false);

        mTabHost.setCurrentTabByTag(TAG_HOME);

    }




    private void showSelectWalletDialog() {
        // if(!AppSettings.getAppSettings().isLockPatternOpen())return;
        SelectWalletDialog2 dialog = new SelectWalletDialog2(this);
        dialog.setData(walletEntities );
        dialog.setOnItemClickListener(new OnDialogItemClick());
        dialog.show();

    }

    class OnDialogItemClick implements AdapterView.OnItemClickListener {

        @Override
        public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
            WalletEntity entity=walletEntities.get(position);
            AppSettings.getAppSettings().setCurrentAddress(entity.getAddress());
            WalletChangeEvent event=new WalletChangeEvent();
            event.setEntity(entity);
            EventBus.getDefault().post(event);
        }
    }

    private void initData() {
        RxTaskScheduler.postIoMainTask(new RxTaskCallBack<List<WalletEntity>>() {
            @Override
            public void onSuccess(List<WalletEntity> walletEntities) {
                super.onSuccess(walletEntities);
                if (walletEntities != null) {
                    MainActivity.this.walletEntities.clear();
                    MainActivity.this.walletEntities.addAll(walletEntities);
                }
            }

            @Override
            public List<WalletEntity> doWork() throws Exception {
                List<WalletEntity> entities = WalletDBManager.getManager().getAllWalletList();
                return entities;
            }
        });
        Intent intent = getIntent();
        String title = intent.getStringExtra(IntentKey.MESSAGE_TITLE);
        String content = intent.getStringExtra(IntentKey.MESSAGE_CONTENT);
        if (!TextUtils.isEmpty(title) && !TextUtils.isEmpty(content)) {
            //备份提醒
            this.showMessageDialog(title, content);
        }
        //检查版本更新
        //mPresenter.checkUpdate(this);
    }

    private View getIndicatorView(int tabIvRes, String labelResId) {
        LayoutInflater inflater = getLayoutInflater();
        ViewGroup rootView = (ViewGroup) inflater.inflate(R.layout.main_tab_indicator, null);
        ImageView tabIv = rootView.findViewById(R.id.tab_iv);
        TextView view = (TextView) rootView.findViewById(R.id.label);
        view.setText(labelResId);
        tabIv.setImageResource(tabIvRes);
        return rootView;
    }


    public void setTabHostClickable(boolean canClickable) {
        for (int i = 0; i < mTabHost.getTabWidget().getTabCount(); i++) {
            Log.e(TAG, i + "  " + canClickable);
            mTabHost.getTabWidget().getChildTabViewAt(i).setClickable(canClickable);
        }
    }

    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
    @Override
    public void onTabChanged(String tabId) {
        switch (tabId) {
            case TAG_HOME:
            case TAG_USER:
                immerse(true);
                break;
            default:
                immerse(false);
                break;
        }

    }

    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if (keyCode == KeyEvent.KEYCODE_BACK) {
            exit();
            return false;
        }
        return super.onKeyDown(keyCode, event);
    }



    private void exit() {
        if (!isExit) {
            isExit = true;
            Toast.makeText(getApplicationContext(), "双击退出", Toast.LENGTH_SHORT).show();
            // 利用handler延迟发送更改状态信息
            mHandler.sendEmptyMessageDelayed(0, 2000);
        } else {
            SoSoConfig.NEED_SWITCH_ENV = true;
            finish();
            //  moveTaskToBack(true);
            //       Process.killProcess(Process.myPid());
        }
    }

    /**
     * menu点击事件
     *
     * @param event
     */
    public void onEventMainThread(OnMenuClickedEvent event) {
       // drawerLayout.openDrawer(navigationView);
        showSelectWalletDialog();
        initData();
    }

    /**
     * menu点击事件
     *
     * @param event
     */
    public void onEventMainThread(WalletChangeEvent event) {
        SoSoConfig.NEED_PWD = true;
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (dialog != null && dialog.isShowing()) {
            dialog.dismiss();
            dialog = null;
        }
        EventBus.getDefault().unregister(this);
        mPresenter.onDetachView();
    }

    private void showMessageDialog(String title, String content) {
        if (dialog != null && dialog.isShowing()) {
            dialog.dismiss();
        }
        dialog = new MessageDialog(this);
        dialog.setDes(content);
        dialog.setTitle(title);
        dialog.show();
    }

    @Override
    public void checkedUpdate(UpdateInfo updateInfo) {
        OnUpdateDialogCancel onUpdateDialogCancel = new OnUpdateDialogCancel();
        mPresenter.realUpdate(this, updateInfo, onUpdateDialogCancel);
    }

    @Override
    public void checkeUpdateFailed() {
    }

    public class OnUpdateDialogCancel implements UpdateAppUtils.OnCancelClicked {

        @Override
        public void canceled(boolean force) {
            if (force) {
                finish();
            }
        }
    }


}
