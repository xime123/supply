package bd.com.walletdb;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.util.Log;

import org.greenrobot.greendao.database.Database;

import bd.com.walletdb.config.Config;
import bd.com.walletdb.greendao.DaoMaster;
import bd.com.walletdb.greendao.DaoSession;
import bd.com.walletdb.helper.DbHelper;

import static bd.com.walletdb.config.Config.BD_WALLET;


/**
 * Created by Administrator on 2018/1/12.
 * greendao管理了两种数据库：
 * 通用数据库，跟用户无关的数据存放在该数据库中。
 * 用户数据库，每个用户一个数据库，存放用户的私有数据，比如用户信息。
 * <p>
 * 使用greendao数据库，必须在应用的Application的onCreate()中调用GreendaoFactory.make()，创建通用数据
 * 管理对象。
 * 根据登录的用户不同，创建对应的用户数据库管理对象，调用GreendaoFactory.makeUser()。
 */

public class GreendaoFactory {
    private static final String TAG = "GreendaoFactory";

    private static DaoMaster sMaster;
    private static DaoSession sDaoSession;

    private static DaoMaster sTxMaster;
    private static DaoSession sTxDaoSession;

    private static DaoMaster sContactMaster;
    private static DaoSession sContactDaoSession;


    public static void make(Context context) {
        Log.i(TAG, "setupDatabase, begin = " + System.currentTimeMillis());
        //增量更新

        DbHelper helper = new DbHelper(context, BD_WALLET);
        Database db = helper.getEncryptedWritableDb("supply123");
        // 注意：该数据库连接属于 DaoMaster，所以多个 Session 指的是相同的数据库连接。
//        daoSession = new DaoMaster(a.getEncryptedWritableDb(MY_PWD)).newSession();
//        daoSession.getUserDao().insert(man1);
        sMaster = new DaoMaster(db);
        sDaoSession = sMaster.newSession();
        Log.i(TAG, "setupDatabase, end = " + System.currentTimeMillis());
    }

    public static void makeTx(Context context) {
        //增量更行
        DbHelper helper = new DbHelper(context, Config.BD_TRANSACTION);
        SQLiteDatabase db = helper.getWritableDatabase();
        // 注意：该数据库连接属于 DaoMaster，所以多个 Session 指的是相同的数据库连接。
        sTxMaster = new DaoMaster(db);
        sTxDaoSession = sTxMaster.newSession();
        Log.i(TAG, "setupDatabase, end = " + System.currentTimeMillis());
    }

    public static void makeContact(Context context) {
        //增量更行
        DbHelper helper = new DbHelper(context, Config.BD_CONTACT);
        SQLiteDatabase db = helper.getWritableDatabase();
        // 注意：该数据库连接属于 DaoMaster，所以多个 Session 指的是相同的数据库连接。
        sContactMaster = new DaoMaster(db);
        sContactDaoSession = sContactMaster.newSession();
        Log.i(TAG, "setupDatabase, end = " + System.currentTimeMillis());
    }

    public static void clearUser() {
        sMaster = null;
        sDaoSession = null;
    }

    public static DaoSession getDaoSession() {
        return sDaoSession;
    }

    public static DaoSession getsTxDaoSession() {
        return sTxDaoSession;
    }

    public static DaoSession getsContactDaoSession() {
        return sContactDaoSession;
    }

}

