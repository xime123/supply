package bd.com.supply.module.wallet.model.domain;

import java.math.BigInteger;


public class WalletBean {
    private String walletName;
    private String address;
    private String iconStr;
    private BigInteger balance;

    public String getWalletName() {
        return walletName;
    }

    public void setWalletName(String walletName) {
        this.walletName = walletName;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public String getIconStr() {
        return iconStr;
    }

    public void setIconStr(String iconStr) {
        this.iconStr = iconStr;
    }

    public BigInteger getBalance() {
        return balance;
    }

    public void setBalance(BigInteger balance) {
        this.balance = balance;
    }
}
