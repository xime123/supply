package bd.com.walletdb.action;

import org.greenrobot.greendao.query.QueryBuilder;

import bd.com.walletdb.GreendaoFactory;
import bd.com.walletdb.entity.WalletEntity;
import bd.com.walletdb.greendao.DaoSession;
import bd.com.walletdb.greendao.WalletEntityDao;


public class WalletAction extends BaseDaoAction<WalletEntity,WalletEntityDao> {
    @Override
    protected QueryBuilder<WalletEntity> getQueryBuilder() {
        return getEntityDao().queryBuilder();
    }

    @Override
    protected WalletEntityDao getEntityDao() {
        DaoSession daoSession = getDaoSession();
        if(daoSession != null){
            return daoSession.getWalletEntityDao();
        } else {
            return null;
        }
    }

    @Override
    protected DaoSession getDaoSession() {
        return GreendaoFactory.getDaoSession();
    }
}
