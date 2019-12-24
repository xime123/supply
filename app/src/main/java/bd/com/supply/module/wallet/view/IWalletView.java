package bd.com.supply.module.wallet.view;

import java.util.List;

import bd.com.appcore.mvp.IListView;
import bd.com.walletdb.entity.ChainEntity;
import bd.com.walletdb.entity.TokenEntity;
import bd.com.walletdb.entity.WalletEntity;


public interface IWalletView extends IListView<TokenEntity> {
    void setTotalMoney(String totalMoney);

    void setWalletInfo(WalletEntity entity);

    void onGetChainListSuccess(List<ChainEntity> chainEntityList);

    void onGetChainListFailed(String msg);

    void isCurrentWalletBacked(WalletEntity entity);

    void  onPassWordRight(int type);
    void onPassWordFailed();
}
