package bd.com.walletdb.action;

import org.greenrobot.greendao.query.QueryBuilder;

import bd.com.walletdb.GreendaoFactory;
import bd.com.walletdb.entity.TransactionEntity;
import bd.com.walletdb.entity.TxHistory;
import bd.com.walletdb.greendao.DaoSession;
import bd.com.walletdb.greendao.TransactionEntityDao;
import bd.com.walletdb.greendao.TxHistoryDao;


public class TxHistoryAction extends BaseDaoAction<TxHistory,TxHistoryDao> {
    @Override
    protected QueryBuilder<TxHistory> getQueryBuilder() {
        return getEntityDao().queryBuilder();
    }

    @Override
    protected TxHistoryDao getEntityDao() {
        return getDaoSession().getTxHistoryDao();
    }

    @Override
    protected DaoSession getDaoSession() {
        return GreendaoFactory.getsTxDaoSession();
    }
}
