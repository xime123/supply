package bd.com.walletdb.action;

import org.greenrobot.greendao.query.QueryBuilder;

import bd.com.walletdb.GreendaoFactory;
import bd.com.walletdb.entity.TransactionEntity;
import bd.com.walletdb.greendao.DaoSession;
import bd.com.walletdb.greendao.TransactionEntityDao;


public class TxAction extends BaseDaoAction<TransactionEntity,TransactionEntityDao> {
    @Override
    protected QueryBuilder<TransactionEntity> getQueryBuilder() {
        return getEntityDao().queryBuilder();
    }

    @Override
    protected TransactionEntityDao getEntityDao() {
        return getDaoSession().getTransactionEntityDao();
    }

    @Override
    protected DaoSession getDaoSession() {
        return GreendaoFactory.getsTxDaoSession();
    }
}
