package bd.com.supply.module.wallet.view;

import bd.com.appcore.mvp.IBaseView;


public interface IWalletDetailView extends IBaseView {
    void  onPassWordRight(int type);
    void onPassWordFailed();
    void onGetBalanceSuccess(String balance);
}
