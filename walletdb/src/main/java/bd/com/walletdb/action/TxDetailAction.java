package bd.com.walletdb.action;

import org.greenrobot.greendao.query.QueryBuilder;

import bd.com.walletdb.GreendaoFactory;
import bd.com.walletdb.entity.TransactionDetail;
import bd.com.walletdb.entity.TxHistory;
import bd.com.walletdb.greendao.DaoSession;
import bd.com.walletdb.greendao.TransactionDetailDao;
import bd.com.walletdb.greendao.TxHistoryDao;


public class TxDetailAction extends BaseDaoAction<TransactionDetail,TransactionDetailDao> {
    @Override
    protected QueryBuilder<TransactionDetail> getQueryBuilder() {
        return getEntityDao().queryBuilder();
    }

    @Override
    protected TransactionDetailDao getEntityDao() {
        return getDaoSession().getTransactionDetailDao();
    }

    @Override
    protected DaoSession getDaoSession() {
        return GreendaoFactory.getsTxDaoSession();
    }
}
