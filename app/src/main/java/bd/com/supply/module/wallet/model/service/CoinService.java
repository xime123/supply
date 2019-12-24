package bd.com.supply.module.wallet.model.service;


import android.text.TextUtils;
import android.util.Log;

import java.util.concurrent.TimeUnit;

import bd.com.supply.module.wallet.ApiConfig;
import bd.com.supply.module.wallet.model.CoinModel;
import bd.com.supply.module.wallet.ApiConfig;
import io.reactivex.Observable;
import io.reactivex.functions.Consumer;
import io.reactivex.schedulers.Schedulers;


public class CoinService {
    private static CoinService service=new CoinService();
    private CoinService(){}
    private String ethPrice="0";
    public static CoinService getService(){
        return service;
    }

    /**
     * 每个一分钟去拿一次价格
     */
    public void start(){
        Observable.interval(0,60*1000, TimeUnit.MILLISECONDS)
                  .subscribeOn(Schedulers.io())
                  .subscribe(new Consumer<Long>() {
                      @Override
                      public void accept(Long aLong) throws Exception {
                          String price=CoinModel.getWalletModel().getPrice(ApiConfig.getEthscanPrice());
                          Log.e("CoinService"," ether price="+price);
                          if(!TextUtils.isEmpty(price)){
                              setEthPrice(price);
                          }
                      }
                  });
    }


    public String getEthPrice() {
        return ethPrice;
    }

    public void setEthPrice(String ethPrice) {
        this.ethPrice = ethPrice;
    }
}
