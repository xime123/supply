package bd.com.supply.module.transaction.view;

import bd.com.appcore.mvp.IBaseView;
import bd.com.walletdb.entity.TxHistory;

/**
 * author:     labixiaoxin
 * date:       2018/5/25
 * email:      labixiaoxin2@qq.cn
 */
public interface ITxView extends IBaseView {
    void onTranslateSuccess(TxHistory history);

    void onTranslateFailed(String msg);

    void onGetBalanceSuccess(String balance);

    void onGetBalanceFailed(String msg);

    void onGetSugGasSuccess(int progess);
}
