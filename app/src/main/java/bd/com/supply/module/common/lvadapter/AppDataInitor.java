package bd.com.supply.module.common.lvadapter;

import java.util.List;

import bd.com.appcore.util.AppSettings;
import bd.com.supply.module.async.AsyncHelper;
import bd.com.supply.module.async.PriceAsync;
import bd.com.supply.module.async.TokenAsync;
import bd.com.walletdb.entity.Price;

public class AppDataInitor {
    private static AppDataInitor initor = new AppDataInitor();
    private AppDataInitor() {
    }

    public static AppDataInitor getInitor() {
        return initor;
    }

    public void init() {
        String walletAddr = AppSettings.getAppSettings().getCurrentAddress();
        String chainId = AppSettings.getAppSettings().getCurrentChainId();
        TokenAsync tokenAsync = new TokenAsync(walletAddr, chainId);
        AsyncHelper.asyncTokenAndPrice(tokenAsync, new PriceAsync.AsyncPriceListener() {
            @Override
            public void onFinished(List<Price> priceList) {

            }
        });
    }

}
