package bd.com.supply.module.wallet.presenter;

import java.util.List;

import bd.com.appcore.mvp.BasePresenterImpl;
import bd.com.appcore.rx.RxTaskCallBack;
import bd.com.appcore.rx.RxTaskScheduler;
import bd.com.supply.module.wallet.ApiConfig;
import bd.com.supply.module.wallet.model.CoinModel;
import bd.com.supply.module.wallet.view.IWalletManagerView;
import bd.com.supply.module.wallet.ApiConfig;
import bd.com.supply.module.wallet.view.IWalletManagerView;
import bd.com.walletdb.manager.WalletDBManager;
import bd.com.walletdb.entity.WalletEntity;


public class WalletManagerPresenter extends BasePresenterImpl<IWalletManagerView> {
    public void getDatas(){
        RxTaskScheduler.postIoMainTask(new RxTaskCallBack<List<WalletEntity>>() {
            @Override
            public void onSuccess(List<WalletEntity> walletEntities) {
                super.onSuccess(walletEntities);
                getBalance(walletEntities);
            }

            @Override
            public List<WalletEntity> doWork() throws Exception {
                List<WalletEntity>entities= WalletDBManager.getManager().getAllWalletList();
                return entities;
            }
        });

    }

    private void getBalance(final List<WalletEntity> walletEntities){
        RxTaskScheduler.postIoMainTask(new RxTaskCallBack<List<WalletEntity> >() {
            @Override
            public void onSuccess(List<WalletEntity> walletEntities) {
                super.onSuccess(walletEntities);
                if(mView!=null){
                    if(walletEntities!=null&&walletEntities.size()>0){

                        mView.loadSuccess(walletEntities);
                    }else {
                        mView.loadEmpty();
                    }
                }
            }

            @Override
            public List<WalletEntity> doWork() throws Exception {
                for(WalletEntity entity:walletEntities){
                  //  String address = AppSettings.getAppSettings().getCurrentAddress();
                    String balance = CoinModel.getWalletModel().getBalance(ApiConfig.getWeb3jUrlProxy(), entity.getAddress());
                    entity.setBalance(balance+"");
                }

                return walletEntities;
            }
        });


    }
}
