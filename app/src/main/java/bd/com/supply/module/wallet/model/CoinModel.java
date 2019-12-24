package bd.com.supply.module.wallet.model;

import android.util.Log;

import com.yanzhenjie.nohttp.NoHttp;
import com.yanzhenjie.nohttp.RequestMethod;
import com.yanzhenjie.nohttp.rest.Request;
import com.yanzhenjie.nohttp.rest.Response;
import com.yanzhenjie.nohttp.rest.SimpleResponseListener;
import com.yanzhenjie.nohttp.rest.StringRequest;
import com.yanzhenjie.nohttp.rest.SyncRequestExecutor;

import org.json.JSONObject;
import org.web3j.crypto.Credentials;
import org.web3j.protocol.Web3j;
import org.web3j.protocol.Web3jFactory;
import org.web3j.protocol.core.DefaultBlockParameter;
import org.web3j.protocol.core.methods.response.EthGetBalance;
import org.web3j.protocol.http.HttpService;
import org.web3j.utils.Convert;

import java.math.BigDecimal;
import java.math.BigInteger;
import java.util.ArrayList;
import java.util.List;

import bd.com.appcore.base.ModelCallBack;
import bd.com.appcore.network.HttpServer;
import bd.com.appcore.util.AppSettings;
import bd.com.appcore.util.GsonUtil;
import bd.com.supply.module.common.GlobConfig;
import bd.com.supply.module.wallet.ApiConfig;
import bd.com.supply.module.wallet.model.domain.SugGasResp;
import bd.com.supply.util.FakeDataHelper;
import bd.com.supply.web3.Web3Proxy;
import bd.com.supply.module.wallet.ApiConfig;
import bd.com.supply.module.wallet.model.domain.SugGasResp;
import bd.com.supply.util.FakeDataHelper;
import bd.com.supply.web3.Web3Proxy;
import bd.com.walletdb.entity.TokenEntity;
import bd.com.walletdb.entity.WalletEntity;
import bd.com.walletdb.manager.TokenManager;
import bd.com.walletdb.manager.WalletDBManager;


public class CoinModel {
    private static final String TAG = CoinModel.class.getSimpleName();
    private static CoinModel walletModel = new CoinModel();

    private CoinModel() {
    }

    public static CoinModel getWalletModel() {
        return walletModel;
    }


    public List<TokenEntity> getCoinList() {
        String chainId = AppSettings.getAppSettings().getCurrentChainId();
        String walletAddr = AppSettings.getAppSettings().getCurrentAddress();
        List<TokenEntity> dbCoins = TokenManager.getManager().getCheckedTokens(walletAddr, chainId);
        List<TokenEntity> beans = FakeDataHelper.getCoins();
        try {
            //由后台来配置来
            //CoinBean coinBean = Web3Proxy.getWeb3Proxy().getNulsCoinInfo(credentials);
            //beans.add(coinBean);
            if (dbCoins != null && dbCoins.size() > 0) {
                for (TokenEntity fakeEntity : beans) {
                    if (!dbCoins.contains(fakeEntity)) {
                        dbCoins.add(fakeEntity);
                    }
                }
                //sct要方到最上面，一下逻辑是处理sct排序问题
                TokenEntity sctEntity = null;
                for (TokenEntity dbEntity : dbCoins) {
                    if (FakeDataHelper.MAIN_SCT_SYMBOL.equals(dbEntity.getSymbol()) || FakeDataHelper.SCT_SYMBOL.equals(dbEntity.getSymbol())) {
                        sctEntity = dbEntity;
                        break;
                    }
                }
                if (sctEntity != null) {
                    dbCoins.remove(sctEntity);
                    dbCoins.add(0, sctEntity);
                }
                return dbCoins;
            } else {
                return beans;
            }
        } catch (Exception e) {
            e.printStackTrace();
            Log.e(TAG, "获取bodao 币发生错误 e=" + e.getMessage());
        }
        return null;
    }


    /**
     * 查余额,主
     *
     * @param address       钱包地址
     * @param endPoint:节点地址
     * @return
     */
    public String getBalance(final String endPoint, final String address) {
        try {
            Web3j web3j = Web3jFactory.build(new HttpService(endPoint));  // defaults to http://localhost:8545/
            EthGetBalance ethGetBalance = web3j.ethGetBalance(address, new DefaultBlockParameter() {
                @Override
                public String getValue() {
                    return "latest";
                }
            }).sendAsync().get();
            BigInteger bigInteger = ethGetBalance.getBalance();
            if (bigInteger.intValue() != 0) {
                String valueStr = bigInteger.toString();
                BigDecimal valueBD = Convert.fromWei(valueStr, Convert.Unit.ETHER);
                BigDecimal setScale = valueBD.setScale(4, BigDecimal.ROUND_HALF_DOWN);
                return setScale.toPlainString();
            } else {
                return "0";
            }
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }

    }


    /**
     * ERC20余额
     *
     * @param contractAddress
     */
    public String getERC20Balance(String contractAddress) {
        try {
            String address = AppSettings.getAppSettings().getCurrentAddress();
            WalletEntity walletEntity = WalletDBManager.getManager().getWalletEntity(address);
            Credentials credentials = Credentials.create(walletEntity.getPrivateKey());
            TokenEntity tokenEntity = TokenManager.getManager().getTokenByAddress(contractAddress);
            return Web3Proxy.getWeb3Proxy().getERC20Balance(credentials, tokenEntity);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    /**
     * 查币的价格
     *
     * @param endPoint
     * @return
     */
    public String getPrice(String endPoint) {
        StringRequest req = new StringRequest(endPoint, RequestMethod.POST);
        Response<String> response = SyncRequestExecutor.INSTANCE.execute(req);
        if (response.isSucceed()) {
            try {
                String result = response.get();
                JSONObject jsonObject = new JSONObject(result);
                JSONObject resultObject = (JSONObject) jsonObject.optJSONObject("result");
                String price = resultObject.optString("ethusd");
                return price;
            } catch (Exception e) {
                e.printStackTrace();
                return null;
            }
            // 请求成功。
        } else {
            // 请求失败，拿到错误：
            return null;
        }
    }

    public void getSugGas(final ModelCallBack<SugGasResp> callBack) {
        final Request<JSONObject> request = NoHttp.createJsonObjectRequest(ApiConfig.getSugGasUrl(), RequestMethod.POST);
        final String json = new JSONObject().toString();
        Log.i(TAG, "getSugGas params json====>" + json);
        request.setDefineRequestBodyForJson(json);
        HttpServer.getInstance().request(0, request, new SimpleResponseListener<JSONObject>() {

            @Override
            public void onSucceed(int what, Response<JSONObject> response) {
                Log.i(TAG, "getSugGas onSucceed====>" + response.get().toString());
                super.onSucceed(what, response);
                if (response.isSucceed()) {
                    JSONObject jsonObject = response.get();
                    int status = jsonObject.optInt("status");
                    String msg = jsonObject.optString("msg");
                    if (status == 0) {
                        SugGasResp resp = GsonUtil.jsonToObject(jsonObject.toString(), SugGasResp.class);
                        callBack.onResponseSuccess(resp);
                    } else {
                        callBack.onResponseFailed(status, msg);
                    }
                }
            }

            @Override
            public void onFailed(int what, Response<JSONObject> response) {
                super.onFailed(what, response);
                // Log.i(TAG, "getTransactionList onFailed====>" + response.get().toString());
                callBack.onResponseFailed(response.responseCode(), response.getException().getMessage());
            }
        });
    }
}
