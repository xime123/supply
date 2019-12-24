package bd.com.supply.module.wallet.bus;

import bd.com.walletdb.entity.WalletEntity;


public class WalletChangeEvent {
    private WalletEntity entity;

    public WalletEntity getEntity() {
        return entity;
    }

    public void setEntity(WalletEntity entity) {
        this.entity = entity;
    }
}
