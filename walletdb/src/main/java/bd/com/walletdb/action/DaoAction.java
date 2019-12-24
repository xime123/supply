package bd.com.walletdb.action;

import org.greenrobot.greendao.Property;

import java.util.List;

/**
 * Created by hef on 2017/4/1.
 */

public interface DaoAction<T> {

    /* 同步方法 */
    /**
     *  Function: insert()
     *  NOTE: 插入的数据的主键值，不可以跟数据库中已有数据的主键值相同，否则会导致报异常
     *      android.database.sqlite.SQLiteConstraintException
     *
     **/
    public long insert(T t);

    public void update(T t);

    /**
     *  Function: insertOrReplace()
     *  NOTE: 插入的数据的主键值，如果跟数据库中已有数据的主键值相同，会更新数据库中的数据；不同才会写
     *      入新的数据项
     *
     **/
    public long insertOrReplace(T t);

    /**
     *  func: 数据表中数据项的数量
     *
     **/
    public long count();

    /**
     *  func: 获取数据表中所有的数据
     *
     **/
    public List<T> loadAll();

    public void deleteAll();

//    public void delete(T t);

//    public void update(T t);

//    public void delete(T t);
//
//    public void query(String str);

    /**
     *  Function: deleteByKey()
     *      按键值来删除表项
     *  Params:
     *      Object key, 要删除项的键值
     *
     **/
    public void deleteByKey(Object key);

    /**
     *  Function: loadAllAsc()
     *      加载所有数据库数据，按传入的表项属性的升序排列
     *  Params:
     *      Property... properties, 表项属性/表项字段，参考ReportEntityDao.Properties.Id获得该值传入
     *
     **/
    public List<T> loadAllAsc(Property... properties);  // 可以不带参数

    /**
     *  Function: loadAllDesc()
     *      加载所有数据库数据，按传入的表项属性的降序排列
     *  Params:
     *      Property... properties, 表项属性/表项字段，参考ReportEntityDao.Properties.Id获得该值传入
     *
     **/
    public List<T> loadAllDesc(Property... properties);


    /* 添加查询条件方法
       NOTE: 下面的方法，如果填的表项值（Object value）与表项值的实际类型不一致，不会导致程序crash。
       查询条件也会生成，并存储在BaseDaoAction中，只是该查询条件是找不到匹配项的。
     */
    public DaoAction<T> eq(Property property, Object value);
    public DaoAction<T> notEq(Property property, Object value);
    public DaoAction<T> like(Property property, String value);
    public DaoAction<T> between(Property property, Object value1, Object value2);
    public DaoAction<T> gt(Property property, Object value);
    public DaoAction<T> lt(Property property, Object value);
    public DaoAction<T> ge(Property property, Object value);
    public DaoAction<T> le(Property property, Object value);

    /**
     *  Function: limit()
     *      添加查询返回结果数量的条件。
     *  Params:
     *      int limit，查询返回的结果条数
     **/
    public DaoAction<T> limit(int limit);

    /**
     *  Function: offset()
     *      添加查询返回结果起始位置的条件。
     *  Params:
     *      int offset，起始位置，从0开始编号。只返回offset之后的数据
     **/
    public DaoAction<T> offset(int offset);

    /**
     *  Function: dump()
     *      清空查询条件，每次查询前，稳妥起见，可以先调用该方法，把之前的查询条件清除。
     *
     **/
    public DaoAction<T> dump();

    /**
     *  Function: queryAnd()
     *      查询条件通过前面的添加查询条件方法已经预置到DaoAction的实体对象中，该方法将预置的查询条件按
     *      逻辑与的方式进行查询。查询结束后，清空之前预设的查询条件。
     *
     **/
    public List<T> queryAnd();

    /**
     *  Function: queryOr()
     *      查询条件通过前面的添加查询条件方法已经预置到DaoAction的实体对象中，该方法将预置的查询条件按
     *      逻辑或的方式进行查询。查询结束后，清空之前预设的查询条件。
     *
     **/
    public List<T> queryOr();

    /**
     *  Function: queryAndWithAsc()
     *      按逻辑与的方式查询，查询结果按传入的表项属性值的升序排序返回。
     *
     *  Params:
     *      Property... properties, 表项属性/表项字段，参考ReportEntityDao.Properties.Id获得该值传入
     *
     **/
    public List<T> queryAndWithAsc(Property... properties);

    /**
     *  Function: queryAndWithAsc()
     *      按逻辑与的方式查询，查询结果按传入的表项属性值的降序排序返回。
     *
     *  Params:
     *      Property... properties, 表项属性/表项字段，参考ReportEntityDao.Properties.Id获得该值传入
     *
     **/
    public List<T> queryAndWithDesc(Property... properties);

    public List<T> queryOrWithAsc(Property... properties);
    public List<T> queryOrWithDesc(Property... properties);

    public long getQueryAndCount();
    public long getQueryOrCount();

    /* 异步方法 */
    public void deleteAllAsync();

    public void updateAllAsync(List<T> list);

    public void insertAsync(T t);

    boolean insertOrReplaceInTx(Iterable<T> list);

    public void insertOrReplaceAsync(T t);

    public void deleteByKeyAsync(Object key);

//    public void queryAndWithAscAsync();
//    public void queryOrWithDescAsync();

//    public void queryAndAsync(Map<String, String> map, AsyncDaoCallback<T> callback);
//
//    public void queryOrAsync(final Map<String, String> map, final AsyncDaoCallback<T> callback);

    public interface AsyncDaoCallback<T>{
        public void onResult(List<T> list);
    }
}
