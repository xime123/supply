package bd.com.supply.main;

import bd.com.walletdb.entity.TxHistory;


public class TransferStartEvent {
    private TxHistory history;

    public TxHistory getHistory() {
        return history;
    }

    public void setHistory(TxHistory history) {
        this.history = history;
    }
}
