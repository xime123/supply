package bd.com.supply.module.wallet.view;

import bd.com.appcore.mvp.IBaseView;


public interface IChangePwdView extends IBaseView {
    void onPwdRight();

    void onPwdError();
}
