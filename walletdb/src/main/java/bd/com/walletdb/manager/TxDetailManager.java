package bd.com.walletdb.manager;

import java.util.List;

import bd.com.walletdb.action.TxDetailAction;
import bd.com.walletdb.entity.TransactionDetail;
import bd.com.walletdb.greendao.TransactionDetailDao;

public class TxDetailManager {
    private static TxDetailManager instance = new TxDetailManager();

    private TxDetailManager() {
    }

    public static TxDetailManager getInstance() {
        return instance;
    }

    public TransactionDetail findTxDetailByHash(String pkHash) {
        TxDetailAction action = new TxDetailAction();
        List<TransactionDetail> detailList = action.eq(TransactionDetailDao.Properties.PkHash, pkHash).queryAnd();
        if (detailList != null && detailList.size() > 0) {
            return detailList.get(0);
        }
        return null;
    }

    public void inputTxDetail(TransactionDetail detail) {
        TxDetailAction action = new TxDetailAction();
        action.insertOrReplace(detail);
    }
}
