package bd.com.walletdb.action;

import org.greenrobot.greendao.query.QueryBuilder;

import bd.com.walletdb.GreendaoFactory;
import bd.com.walletdb.entity.ChainEntity;
import bd.com.walletdb.greendao.ChainEntityDao;
import bd.com.walletdb.greendao.DaoSession;

public class ChainAction extends BaseDaoAction<ChainEntity, ChainEntityDao> {
    @Override
    protected QueryBuilder<ChainEntity> getQueryBuilder() {
        return getEntityDao().queryBuilder();
    }

    @Override
    protected ChainEntityDao getEntityDao() {
        DaoSession daoSession = getDaoSession();
        if (daoSession != null) {
            return daoSession.getChainEntityDao();
        } else {
            return null;
        }
    }

    @Override
    protected DaoSession getDaoSession() {
        return GreendaoFactory.getDaoSession();
    }
}
