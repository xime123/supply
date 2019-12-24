package bd.com.supply.app;

import android.content.Context;
import android.support.multidex.MultiDex;
import android.text.TextUtils;

import com.tencent.bugly.crashreport.CrashReport;
import com.tencent.smtt.sdk.QbSdk;

import com.yanzhenjie.nohttp.Logger;
import com.yanzhenjie.nohttp.NoHttp;

import bd.com.appcore.CoreApp;
import bd.com.appcore.CustomerException;
import bd.com.appcore.util.AppSettings;
import bd.com.supply.Config;
import bd.com.supply.module.async.BlockNumberAsync;
import bd.com.supply.module.common.AppFilePath;
import bd.com.supply.module.common.lvadapter.AppDataInitor;
import bd.com.supply.module.push.manager.PushManager;
import bd.com.supply.module.transaction.model.EnvModel;
import bd.com.supply.module.user.lockpattern.LockPatternCallback;
import bd.com.supply.module.wallet.ApiConfig;
import bd.com.supply.util.AppCacheUitl;
import bd.com.supply.util.ProcessUtil;
import bd.com.supply.module.async.BlockNumberAsync;
import bd.com.supply.module.common.lvadapter.AppDataInitor;
import bd.com.supply.module.transaction.model.EnvModel;
import bd.com.supply.module.user.lockpattern.LockPatternCallback;
import bd.com.supply.module.wallet.ApiConfig;
import bd.com.walletdb.GreendaoFactory;
import cn.jpush.android.api.JPushInterface;

/**
 * author:     labixiaoxin
 * date:       2018/5/20
 * email:      labixiaoxin2@qq.cn
 */
public class BdApplication extends CoreApp {

    @Override
    public void onCreate() {
        super.onCreate();
        AppFilePath.init(this);
        CustomerException.getExceptionControl().init(context);
        AppSettings.getAppSettings().init(this);
        GreendaoFactory.make(this);
        GreendaoFactory.makeTx(this);
        GreendaoFactory.makeContact(this);
        NoHttp.initialize(this);
        Logger.setDebug(true);// 开启NoHttp的调试模式, 配置后可看到请求过程、日志和错误信息。
        Logger.setTag("Nohttp");
        resetDB();

        initChanin();
        //初始化jpush
        JPushInterface.setDebugMode(true);    // 设置开启日志,发布时请关闭日志
        JPushInterface.init(this);            // 初始化 JPush
        //JPUSH初始化完了才可以调用
        PushManager.init(this);
        initBugly();
        AppDataInitor.getInitor().init();
        // CoinService.getService().start();
        BlockNumberAsync.getAsync().init();
        EnvModel.getInstance().init();
        /**
         * 注册
         */
        registerActivityLifecycleCallbacks(new LockPatternCallback());
        initUmeng();
        QbSdk.initX5Environment(this,null);
    }

    private void initUmeng() {
        /**
         * 注意: 即使您已经在AndroidManifest.xml中配置过appkey和channel值，也需要在App代码中调
         * 用初始化接口（如需要使用AndroidManifest.xml中配置好的appkey和channel值，
         * UMConfigure.init调用中appkey和channel参数请置为null）。
         */
//        UMConfigure.init(this,   UMConfigure.DEVICE_TYPE_PHONE, null);
//        // 选用AUTO页面采集模式
//        MobclickAgent.setPageCollectionMode(MobclickAgent.PageMode.AUTO);
    }

    /**
     * 初始化bugly
     */
    private void initBugly() {
        Context context = getApplicationContext();
        // 获取当前包名
        String packageName = context.getPackageName();
        // 获取当前进程名
        String processName = ProcessUtil.getProcessName(android.os.Process.myPid());
        // 设置是否为上报进程
        CrashReport.UserStrategy strategy = new CrashReport.UserStrategy(context);
        strategy.setUploadProcess(processName == null || processName.equals(packageName));
        // 初始化Bugly
        boolean enableBugly = AppSettings.getAppSettings().getEnableBugly();
        CrashReport.initCrashReport(context, Config.BUGLY_APP_ID, true, strategy);
        // 如果通过“AndroidManifest.xml”来配置APP信息，初始化方法如下
        // CrashReport.initCrashReport(context, strategy

    }

    @Override
    public void attachBaseContext(Context base) {
        MultiDex.install(base);
        super.attachBaseContext(base);
    }

    private void resetDB() {
        boolean hasReset = AppSettings.getAppSettings().getDBHasReseted();
        if (!hasReset) {
            /**
             * 记得下个版本注释掉
             */
            AppCacheUitl.resetDB();
            AppSettings.getAppSettings().setDBHasReseted(true);
        }
    }

    private void initChanin() {
        String configedChainIp = AppSettings.getAppSettings().getCurrentChainIp();
        if (!TextUtils.isEmpty(configedChainIp)) {
            ApiConfig.BASE_URL = configedChainIp;
        }
    }
}
