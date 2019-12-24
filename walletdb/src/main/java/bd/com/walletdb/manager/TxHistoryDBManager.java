package bd.com.walletdb.manager;

import android.text.TextUtils;

import java.util.ArrayList;
import java.util.List;

import bd.com.walletdb.action.TokenAction;
import bd.com.walletdb.action.TxHistoryAction;
import bd.com.walletdb.entity.TxHistory;
import bd.com.walletdb.greendao.TxHistoryDao;


public class TxHistoryDBManager {
    private static TxHistoryDBManager manager = new TxHistoryDBManager();

    private TxHistoryDBManager() {
    }

    public static TxHistoryDBManager getManager() {
        return manager;
    }

    public void updateTxHistory(TxHistory history) {
        TxHistoryAction action = new TxHistoryAction();
        action.insertOrReplace(history);
    }

    public void insertTxHistory(TxHistory txHistory) {
        TxHistoryAction action = new TxHistoryAction();
        action.insertOrReplace(txHistory);
    }

    public void deleteTxHistoryByHash(String hash) {
        TxHistoryAction action = new TxHistoryAction();
        action.deleteByKey(hash);
    }

    public void insertTxHistoryList(List<TxHistory> histories) {
        TxHistoryAction action = new TxHistoryAction();
        action.insertOrReplaceInTx(histories);
    }

    public void insertTxHistoryListAsync(final List<TxHistory> histories) {
        if (histories == null) {
            return;
        }
        new Thread(new Runnable() {
            @Override
            public void run() {
                TxHistoryAction action = new TxHistoryAction();
                action.insertOrReplaceInTx(histories);
            }
        }).start();

    }

    /**
     * @param address
     */
    public List<TxHistory> findTxHistoryList(String address) {
        if (TextUtils.isEmpty(address)) {
            return null;
        }
        TxHistoryAction action = new TxHistoryAction();
        return action.eq(TxHistoryDao.Properties.Address, address).queryAnd();
    }


    public TxHistory findTxHistoryByHash(String hash) {
        if (TextUtils.isEmpty(hash)) {
            return null;
        }
        TxHistoryAction action = new TxHistoryAction();
        List<TxHistory> txHistories = action.eq(TxHistoryDao.Properties.PkHash, hash).queryAnd();
        if (txHistories != null && txHistories.size() > 0) {
            return txHistories.get(0);
        }
        return null;
    }

    /**
     * 确定一个币交易历史，需要以下几个纬度
     *
     * @param tokenAddress 哪个一币的交易，币的地址
     * @param walletAddr   哪个一个钱包的交易
     * @param chainId      哪一条链发起的交易
     * @return
     */
    public List<TxHistory> findTxHistory(String tokenAddress, String walletAddr, String chainId) {
        TxHistoryAction action = new TxHistoryAction();
        return action.eq(TxHistoryDao.Properties.Address, tokenAddress).eq(TxHistoryDao.Properties.WalletAddr, walletAddr).eq(TxHistoryDao.Properties.ChainId, chainId).queryAnd();
    }

    /**
     * 确定一个币交易历史，需要以下几个纬度
     *
     * @param tokenAddress 哪个一币的交易，币的地址
     * @param walletAddr   哪个一个钱包的交易
     * @param chainId      哪一条链发起的交易
     * @return
     */
    public List<TxHistory> findPendingTxHistory(String tokenAddress, String walletAddr, String chainId) {
        TxHistoryAction action = new TxHistoryAction();
        List<TxHistory> txHistoryList = action.loadAll();
        List<TxHistory> needList = new ArrayList<>();
        if (txHistoryList != null) {
            for (TxHistory txHistory : txHistoryList) {
                if (tokenAddress.equals(txHistory.getAddress()) && walletAddr.equals(txHistory.getWalletAddr()) && chainId.equals(txHistory.getChainId()) && txHistory.getState() != 0) {
                    needList.add(txHistory);
                }
            }
        }

        return needList;
    }

    public void reset() {
        TxHistoryAction action = new TxHistoryAction();
        action.deleteAll();
    }
}
