package bd.com.walletdb.action;

import org.greenrobot.greendao.query.QueryBuilder;

import bd.com.walletdb.GreendaoFactory;
import bd.com.walletdb.entity.ContactEntity;
import bd.com.walletdb.greendao.ContactEntityDao;
import bd.com.walletdb.greendao.DaoSession;

public class ContactAction extends BaseDaoAction<ContactEntity,ContactEntityDao> {
    @Override
    protected QueryBuilder<ContactEntity> getQueryBuilder() {
        return getEntityDao().queryBuilder();
    }

    @Override
    protected ContactEntityDao getEntityDao() {
        DaoSession daoSession = getDaoSession();
        if (daoSession != null) {
            return daoSession.getContactEntityDao();
        } else {
            return null;
        }
    }

    @Override
    protected DaoSession getDaoSession() {
        return GreendaoFactory.getsContactDaoSession();
    }
}
