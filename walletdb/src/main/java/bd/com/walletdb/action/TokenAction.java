package bd.com.walletdb.action;

import org.greenrobot.greendao.query.QueryBuilder;

import bd.com.walletdb.GreendaoFactory;
import bd.com.walletdb.entity.TokenEntity;
import bd.com.walletdb.greendao.DaoSession;
import bd.com.walletdb.greendao.TokenEntityDao;


public class TokenAction extends BaseDaoAction<TokenEntity, TokenEntityDao> {
    @Override
    protected QueryBuilder<TokenEntity> getQueryBuilder() {
        return getEntityDao().queryBuilder();
    }

    @Override
    protected TokenEntityDao getEntityDao() {
        DaoSession daoSession = getDaoSession();
        if (daoSession != null) {
            return daoSession.getTokenEntityDao();
        } else {
            return null;
        }
    }

    @Override
    protected DaoSession getDaoSession() {
        return GreendaoFactory.getDaoSession();
    }
}
