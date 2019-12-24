package bd.com.walletdb.action;

import org.greenrobot.greendao.AbstractDao;
import org.greenrobot.greendao.Property;
import org.greenrobot.greendao.query.QueryBuilder;
import org.greenrobot.greendao.query.WhereCondition;

import java.util.ArrayList;
import java.util.List;

import bd.com.walletdb.greendao.DaoSession;


/**
 * Created by hef on 2018/1/11.
 *
 */

public abstract class BaseDaoAction<T, G extends AbstractDao> implements DaoAction<T> {

    private static final String TAG = "BaseDaoAction";

    private static int LOGIC_AND  = 1;
    private static int LOGIC_OR = 2;

//    private static void setupDatabase(Context context){
//        if(sDaoSession != null){
//            return;
//        }
//
//        synchronized (BaseDaoAction.class) {
//            if(sDaoSession == null) {
//                Log.d(TAG, "setupDatabase, begin = " + System.currentTimeMillis());
//                BHLog.i(TAG, "setupDatabase, begin = " + System.currentTimeMillis());
//                DaoMaster.DevOpenHelper helper = new DaoMaster.DevOpenHelper(context,
//                        DB_NAME, null);
//                SQLiteDatabase db = helper.getWritableDatabase();
//                // 注意：该数据库连接属于 DaoMaster，所以多个 Session 指的是相同的数据库连接。
//                sMaster = new DaoMaster(db);
//                sDaoSession = sMaster.newSession();
//                Log.d(TAG, "setupDatabase, end = " + System.currentTimeMillis());
//                BHLog.i(TAG, "setupDatabase, end = " + System.currentTimeMillis());
//            }
//        }
//    }

    protected abstract QueryBuilder<T> getQueryBuilder();

    protected abstract G getEntityDao();

    protected abstract DaoSession getDaoSession();

    /**
     *  Function: insert()
     *  NOTE: 插入的数据的主键值，不可以跟数据库中已有数据的主键值相同，否则会导致报异常
     *      android.database.sqlite.SQLiteConstraintException
     *
     **/
    @Override
    public final long insert(T t){
        G entityDao = getEntityDao();
        if(entityDao == null){
            return 0;
        }

        return entityDao.insert(t);
    }

    @Override
    public void update(T t){
        G entityDao = getEntityDao();
        if(entityDao != null){
            entityDao.update(t);
        }
    }

    /**
     *  Function: insertOrReplace()
     *  NOTE: 插入的数据的主键值，如果跟数据库中已有数据的主键值相同，会更新数据库中的数据；不同才会写
     *      入新的数据项
     *
     **/
    @Override
    public long insertOrReplace(T t){
        G entityDao = getEntityDao();
        if(entityDao == null){
            return 0;
        }

        return entityDao.insertOrReplace(t);
    }

    @Override
    public final long count(){
        G entityDao = getEntityDao();
        if(entityDao == null){
            return 0;
        }

        return entityDao.count();
    }

    @Override
    public final List<T> loadAll(){
        G entityDao = getEntityDao();
        if(entityDao == null){
            return null;
        }

        return entityDao.loadAll();
    }

    @Override
    public final void deleteAll(){
        G entityDao = getEntityDao();
        if(entityDao != null){
            entityDao.deleteAll();
        }
    }

    public final void deleteInTx(List<T> tList){
        G entityDao = getEntityDao();
        if(entityDao != null){
            entityDao.deleteInTx(tList);
        }
    }

    /**
     *  Function: deleteByKey()
     *      按键值来删除表项
     *  Params:
     *      Object key, 要删除项的键值
     *
     **/
    @Override
    public final void deleteByKey(Object key){
        G entityDao = getEntityDao();
        if(entityDao != null){
            entityDao.deleteByKey(key);
        }
    }

    @Override
    public final void deleteByKeyAsync(final Object key){
        new Thread(new Runnable() {
            @Override
            public void run() {
                deleteByKey(key);
            }
        }).start();
    }

    @Override
    public final void deleteAllAsync(){
        new Thread(new Runnable() {
            @Override
            public void run() {
                deleteAll();
            }
        }).start();
    }

    @Override
    public final void insertAsync(final T t){
        new Thread(new Runnable() {
            @Override
            public void run() {
                insert(t);
            }
        }).start();
    }

    @Override
    public final void insertOrReplaceAsync(final T t){
        new Thread(new Runnable() {
            @Override
            public void run() {
                insertOrReplace(t);
            }
        }).start();
    }

    @Override
    public boolean insertOrReplaceInTx(Iterable<T> list) {
        G entityDao = getEntityDao();
        if(entityDao == null){
            return false;
        }
        entityDao.insertOrReplaceInTx(list);
        return true;
    }

    //    @Override
//    public final void queryAndAsync(final Map<String, String> map, final AsyncDaoCallback<T> callback){
//        new Thread(new Runnable() {
//            @Override
//            public void run() {
//                if(map != null){
//                    mList = query(map, LOGIC_AND);
//                } else {
//                    mList = loadAll();
//                }
//
//                Handler handler = new Handler(Looper.getMainLooper());
//                handler.post(new Runnable(){
//                    @Override
//                    public void run() {
//                        callback.onResult(mList);
//                        mList = null;
//                    }
//                });
//            }
//        }).start();
//    }
//
//    @Override
//    public final void queryOrAsync(final Map<String, String> map, final AsyncDaoCallback<T> callback){
//        new Thread(new Runnable() {
//            @Override
//            public void run() {
//                final List<T> list = query(map, LOGIC_OR);
//
//                Handler handler = new Handler(Looper.getMainLooper());
//                handler.post(new Runnable(){
//                    @Override
//                    public void run() {
//                        callback.onResult(list);
//                    }
//                });
//            }
//        }).start();
//    }

//    private QueryBuilder<T> takeQueryBuilder(Map<String, String> map, int logicType){
//        QueryBuilder<T> queryBuilder = getQueryBuilder();
//        QueryBuilder<T> result = null;
//        WhereCondition condition = null;
//        Property matchedPro = null;
//
//        try {
//            for (String key : map.keySet()) {
//                BHLog.i(TAG, "key = " + key + ", keyset = " + map.get(key));
//                if(map.get(key) == null){
//                    continue;
//                }
//                matchedPro = getProperty(key);
//                if (matchedPro == null) {
//                    continue;
//                }
//                if (condition == null) {
//                    condition = matchedPro.like(map.get(key));
//                } else {
//                    if(logicType == LOGIC_OR){
//                        condition = queryBuilder.or(condition, matchedPro.like(map.get(key)));
//                    } else{
//                        condition = queryBuilder.and(condition, matchedPro.like(map.get(key)));
//                    }
//                }
//            }
//
//            result = queryBuilder.where(condition);
//        } catch (Exception ex){
//            ex.printStackTrace();
//            result = null;
//        }
//
//        return result;
//    }

    private Property getProperty(String column){
        if(column == null || column.isEmpty()){
            return null;
        }

        G entityDao = getEntityDao();
        Property[] allProperties = entityDao.getProperties();
        for(Property pro : allProperties){
            if(column.toUpperCase().equals(pro.columnName) || column.toLowerCase().equals(pro.columnName)){
                return pro;
            }
        }
        return null;
    }

    public void updateAllAsync(final List<T> list){
        new Thread(new Runnable() {
            @Override
            public void run() {
                deleteAll();

                for(T t : list){
                    insert(t);
                }
            }
        }).start();
    }

    @Override
    public List<T> loadAllAsc(Property... properties){
        QueryBuilder<T> queryBuilder = getQueryBuilder();
        loadOrderAsc(queryBuilder);
        return getResult(queryBuilder);
    }

    @Override
    public List<T> loadAllDesc(Property... properties){
        QueryBuilder<T> queryBuilder = getQueryBuilder();
        loadOrderDesc(queryBuilder);
        return getResult(queryBuilder);
    }

    /* 添加查询条件方法 */
    @Override
    public DaoAction<T> eq(Property property, Object value){
        if(property != null){
            WhereCondition condition = property.eq(value);
            addCondition(condition);
        }

        return this;
    }

    @Override
    public DaoAction<T> notEq(Property property, Object value){
        if(property != null){
            WhereCondition condition = property.notEq(value);
            addCondition(condition);
        }

        return this;
    }

    @Override
    public DaoAction<T> like(Property property, String value){
        if(property != null){
            WhereCondition condition = property.like(value);
            addCondition(condition);
        }

        return this;
    }

    @Override
    public DaoAction<T> between(Property property, Object value1, Object value2){
        if(property != null){
            WhereCondition condition = property.between(value1, value2);
            addCondition(condition);
        }

        return this;
    }

    @Override
    public DaoAction<T> gt(Property property, Object value){
        if(property != null){
            WhereCondition condition = property.gt(value);
            addCondition(condition);
        }

        return this;
    }

    @Override
    public DaoAction<T> lt(Property property, Object value){
        if(property != null){
            WhereCondition condition = property.lt(value);
            addCondition(condition);
        }

        return this;
    }

    @Override
    public DaoAction<T> ge(Property property, Object value){
        if(property != null){
            WhereCondition condition = property.ge(value);
            addCondition(condition);
        }

        return this;
    }

    @Override
    public DaoAction<T> le(Property property, Object value){
        if(property != null){
            WhereCondition condition = property.le(value);
            addCondition(condition);
        }

        return this;
    }

    private ArrayList<WhereCondition> mConditions;

    private void addCondition(WhereCondition condition){
        if(mConditions == null){
            mConditions = new ArrayList<>();
        }

        if(condition != null){
            mConditions.add(condition);
        }
    }

    @Override
    public DaoAction<T> dump(){
        clearCondition();
        clearPosition();
        return this;
    }

    private void clearCondition(){
        mConditions = null;
    }

    private void clearPosition(){
        mLimit = INVALID_VALUE;
        mOffset = INVALID_VALUE;
    }

    public List<T> queryAnd(){
        QueryBuilder<T> queryBuilder = getQueryBuilder();
        loadCondition(queryBuilder, LOGIC_AND);
        loadPosition(queryBuilder);
        return getResult(queryBuilder);
    }
    public List<T> queryOr(){
        QueryBuilder<T> queryBuilder = getQueryBuilder();
        loadCondition(queryBuilder, LOGIC_OR);
        loadPosition(queryBuilder);
        return getResult(queryBuilder);
    }

    private List<T> getResult(QueryBuilder<T> queryBuilder){
        if(queryBuilder != null){
            return queryBuilder.list();
        } else {
            return null;
        }
    }

    @Override
    public List<T> queryAndWithAsc(Property... properties) {
        QueryBuilder<T> queryBuilder = getQueryBuilder();
        loadCondition(queryBuilder, LOGIC_AND);
        loadPosition(queryBuilder);
        loadOrderAsc(queryBuilder, properties);
        return getResult(queryBuilder);
    }

    @Override
    public List<T> queryAndWithDesc(Property... properties) {
        QueryBuilder<T> queryBuilder = getQueryBuilder();
        loadCondition(queryBuilder, LOGIC_AND);
        loadPosition(queryBuilder);
        loadOrderDesc(queryBuilder, properties);
        return getResult(queryBuilder);
    }

    @Override
    public List<T> queryOrWithAsc(Property... properties) {
        QueryBuilder<T> queryBuilder = getQueryBuilder();
        loadCondition(queryBuilder, LOGIC_OR);
        loadPosition(queryBuilder);
        loadOrderAsc(queryBuilder, properties);
        return getResult(queryBuilder);
    }

    @Override
    public List<T> queryOrWithDesc(Property... properties) {
        QueryBuilder<T> queryBuilder = getQueryBuilder();
        loadCondition(queryBuilder, LOGIC_OR);
        loadPosition(queryBuilder);
        loadOrderDesc(queryBuilder, properties);
        return getResult(queryBuilder);
    }

    private void loadOrderAsc(QueryBuilder<T> queryBuilder, Property... properties){
        if(queryBuilder != null){
            queryBuilder.orderAsc(properties);
        }
    }

    private void loadOrderDesc(QueryBuilder<T> queryBuilder, Property... properties){
        if(queryBuilder != null){
            queryBuilder.orderDesc(properties);
        }
    }

    /* 不加任何查询条件的情况下，getQueryBuilder()方法返回的QueryBuilder<T> queryBuilder，queryBuilder.list()
        获取的是所有的数据 */
    private void loadCondition(QueryBuilder<T> queryBuilder, int logicType){
        if(queryBuilder == null){
            clearCondition();
            return;
        }

        if(mConditions == null || mConditions.isEmpty()){
            return;
        }

        WhereCondition compositionCondition = null;

        for (WhereCondition condition : mConditions){
            if (compositionCondition == null) {
                compositionCondition = condition;
            } else {
                if(logicType == LOGIC_OR){
                    compositionCondition = queryBuilder.or(compositionCondition, condition);
                } else{
                    compositionCondition = queryBuilder.and(compositionCondition, condition);
                }
            }
        }

        queryBuilder.where(compositionCondition);
        clearCondition();
    }

    private void loadPosition(QueryBuilder<T> queryBuilder){
        if(queryBuilder != null) {
            if(mLimit > INVALID_VALUE ){
                queryBuilder.limit(mLimit);
            }

            if(mOffset > INVALID_VALUE){
                queryBuilder.offset(mOffset);
            }
        }

        clearPosition();
    }

//    private QueryBuilder<T> takeQueryBuilder(int logicType){
//        QueryBuilder<T> queryBuilder = getQueryBuilder();
//        QueryBuilder<T> result = null;
//        WhereCondition compositionCondition = null;
//
//        if(mConditions == null || mConditions.isEmpty()){
//            loadPosition(queryBuilder);
//            dump();
//            return queryBuilder;
//        }
//
//        for (WhereCondition condition : mConditions){
//            if (compositionCondition == null) {
//                compositionCondition = condition;
//            } else {
//                if(logicType == LOGIC_OR){
//                    compositionCondition = queryBuilder.or(compositionCondition, condition);
//                } else{
//                    compositionCondition = queryBuilder.and(compositionCondition, condition);
//                }
//            }
//        }
//        result = queryBuilder.where(compositionCondition);
//        loadPosition(result);
//        dump();
//        return result;
//    }


    @Override
    public long getQueryAndCount() {
        QueryBuilder<T> queryBuilder = getQueryBuilder();
        loadCondition(queryBuilder, LOGIC_AND);
        if(queryBuilder != null){
            return queryBuilder.count();
        } else {
            return 0;
        }
    }

    public long getQueryOrCount(){
        QueryBuilder<T> queryBuilder = getQueryBuilder();
        loadCondition(queryBuilder, LOGIC_OR);
        if(queryBuilder != null){
            return queryBuilder.count();
        } else {
            return 0;
        }
    }

    private static final int INVALID_VALUE = -1;

    private int mLimit = INVALID_VALUE;
    private int mOffset = INVALID_VALUE;

    @Override
    public DaoAction<T> limit(int limit) {
        mLimit = limit;
        return this;
    }

    @Override
    public DaoAction<T> offset(int offset) {
        mOffset = offset;
        return this;
    }
}
