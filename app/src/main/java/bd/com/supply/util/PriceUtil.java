package bd.com.supply.util;

import android.util.Log;

import java.math.BigDecimal;

import bd.com.appcore.util.AppSettings;
import bd.com.supply.module.common.Constant;
import bd.com.walletdb.entity.Price;
import bd.com.walletdb.manager.PriceManager;

public class PriceUtil {
    public static BigDecimal getMoney(String tokenAddr, String balance) {
        Log.i("PriceUtil", "tokenAddr=" + tokenAddr + "   balance=" + balance);
        BigDecimal totalValue = new BigDecimal("0");
        Price price = PriceManager.getManager().getPrice(tokenAddr, AppSettings.getAppSettings().getCurrentChainId());
        if (price != null) {
            BigDecimal decimalPrice = new BigDecimal(price.getAsks());
            BigDecimal decimalBalance = new BigDecimal(balance);
            totalValue = decimalPrice.multiply(decimalBalance);
        }
        return totalValue;
    }

}
