package bd.com.walletdb.manager;

import android.text.TextUtils;

import java.util.List;

import bd.com.walletdb.action.DouTxAction;
import bd.com.walletdb.entity.DoubleTxEntity;
import bd.com.walletdb.greendao.DoubleTxEntityDao;

public class DoubleTxManager {
    private DoubleTxManager() {
        if (Singleton.manager != null) {
            throw new IllegalStateException();
        }
    }

    private static class Singleton {
        private static DoubleTxManager manager = new DoubleTxManager();
    }

    public static DoubleTxManager getManager() {
        return Singleton.manager;
    }

    public void inputDouTx(DoubleTxEntity e) {
        DouTxAction action = new DouTxAction();
        action.insertOrReplace(e);
    }

    public DoubleTxEntity getDouTxByTo(String to) {
        if (TextUtils.isEmpty(to)) {
            return null;
        }
        DouTxAction action = new DouTxAction();
        List<DoubleTxEntity> doubleTxEntityList = action.eq(DoubleTxEntityDao.Properties.To, to).queryAnd();
        if (doubleTxEntityList != null && doubleTxEntityList.size() > 0) {
            return doubleTxEntityList.get(0);
        }
        return null;
    }
}
