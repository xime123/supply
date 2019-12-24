package bd.com.supply.module.async;

import java.util.List;

import bd.com.walletdb.entity.TokenEntity;

/**
 *
 */
public class AsyncHelper {
    /**
     * 同步token列表然后再同步价格
     *
     * @param tokenAsync
     */
    public static void asyncTokenAndPrice(final TokenAsync tokenAsync, final PriceAsync.AsyncPriceListener listener) {
        tokenAsync.addListener(new TokenAsync.AsyncTokenListener() {
            @Override
            public void onFinished(List<TokenEntity> entityList) {

                PriceAsync priceAsync = new PriceAsync(tokenAsync.getChainId(), entityList);
                priceAsync.addListener(listener);
                priceAsync.asyncPrice();
            }
        });
        tokenAsync.asyncTokenList();
    }

}
