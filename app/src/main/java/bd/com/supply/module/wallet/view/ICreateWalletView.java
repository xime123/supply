package bd.com.supply.module.wallet.view;

import bd.com.appcore.mvp.IBaseView;


public interface ICreateWalletView extends IBaseView {
    void onCreateWalletSuccess();
    void onCreateWalletFailed(String msg);
    void showTips(String tips);
}
