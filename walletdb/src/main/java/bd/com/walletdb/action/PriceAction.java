package bd.com.walletdb.action;

import org.greenrobot.greendao.query.QueryBuilder;

import bd.com.walletdb.GreendaoFactory;
import bd.com.walletdb.entity.Price;
import bd.com.walletdb.greendao.DaoSession;
import bd.com.walletdb.greendao.PriceDao;

public class PriceAction extends BaseDaoAction<Price, PriceDao> {
    @Override
    protected QueryBuilder<Price> getQueryBuilder() {
        return getEntityDao().queryBuilder();
    }

    @Override
    protected PriceDao getEntityDao() {
        DaoSession daoSession = getDaoSession();
        if (daoSession != null) {
            return daoSession.getPriceDao();
        } else {
            return null;
        }
    }

    @Override
    protected DaoSession getDaoSession() {
        return GreendaoFactory.getDaoSession();
    }
}
