package bd.com.supply.module.transaction.view;

import java.util.List;

import bd.com.appcore.mvp.IListView;
import bd.com.walletdb.entity.TxHistory;

/**
 * author:     labixiaoxin
 * date:       2018/6/30
 * email:      labixiaoxin2@qq.cn
 */
public interface ITransactionHisActView extends IListView<TxHistory> {
    void onGetReceiptSuccess();

    void onGetReceiptFailed(TxHistory txHistory);

    void onGetBalanceSuccess(String balance);

    void onGetBalanceFailed(String msg);

    //void needNotifyTxHistory(List<TxHistory> txHistoryList);

    void onBlockNumSuccess(int num);
}
