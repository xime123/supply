package bd.com.supply.module.transaction.model;

import android.text.TextUtils;
import android.util.Log;

import com.tencent.bugly.crashreport.BuglyLog;
import com.tencent.bugly.crashreport.CrashReport;
import com.yanzhenjie.nohttp.NoHttp;
import com.yanzhenjie.nohttp.RequestMethod;
import com.yanzhenjie.nohttp.rest.Request;
import com.yanzhenjie.nohttp.rest.Response;
import com.yanzhenjie.nohttp.rest.SimpleResponseListener;

import org.json.JSONObject;
import org.spongycastle.util.encoders.Hex;
import org.web3j.crypto.Credentials;
import org.web3j.crypto.RawTransaction;
import org.web3j.crypto.TransactionEncoder;
import org.web3j.protocol.Web3j;
import org.web3j.protocol.Web3jFactory;
import org.web3j.protocol.core.DefaultBlockParameterName;
import org.web3j.protocol.core.methods.response.EthGetTransactionCount;
import org.web3j.protocol.core.methods.response.EthGetTransactionReceipt;
import org.web3j.protocol.core.methods.response.EthSendTransaction;
import org.web3j.protocol.core.methods.response.TransactionReceipt;
import org.web3j.protocol.http.HttpService;
import org.web3j.tx.ChainId;
import org.web3j.tx.RawTransactionManager;
import org.web3j.tx.TransactionManager;
import org.web3j.tx.response.NoOpProcessor;
import org.web3j.tx.response.TransactionReceiptProcessor;
import org.web3j.utils.Convert;

import java.math.BigDecimal;
import java.math.BigInteger;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ExecutionException;

import bd.com.appcore.base.ModelCallBack;
import bd.com.appcore.network.HttpServer;
import bd.com.appcore.network.RequestParam;
import bd.com.appcore.util.AppSettings;
import bd.com.appcore.util.GsonUtil;
import bd.com.supply.app.ThreadManager;
import bd.com.supply.module.common.Constant;
import bd.com.supply.module.common.GlobConfig;
import bd.com.supply.module.transaction.model.domian.TxHistoryListResp;
import bd.com.supply.module.wallet.ApiConfig;
import bd.com.supply.module.wallet.model.CoinModel;
import bd.com.supply.util.DateKit;
import bd.com.supply.util.HttpUtils;
import bd.com.supply.util.TransactionLog;
import bd.com.supply.web3.Web3Proxy;
import bd.com.supply.web3.contract.NulsStandardToken;
import bd.com.supply.module.transaction.model.domian.TxHistoryListResp;
import bd.com.walletdb.entity.DoubleTxEntity;
import bd.com.walletdb.entity.TransactionDetail;
import bd.com.walletdb.entity.TxHistory;
import bd.com.walletdb.entity.WalletEntity;
import bd.com.walletdb.manager.DoubleTxManager;
import bd.com.walletdb.manager.WalletDBManager;


/**
 * author:     labixiaoxin
 * date:       2018/6/29
 * email:      labixiaoxin2@qq.cn
 */
public class TransactionModel {
    private static final String TAG = TransactionModel.class.getSimpleName();
    private static TransactionModel transactionModel = new TransactionModel();

    private TransactionModel() {
    }

    public static TransactionModel getTransactionModel() {
        return transactionModel;
    }

    private int listCount = 500;

    /**
     * 获取某个帐号的交易历史
     *
     * @param params 钱包地址
     * @return
     */
    public void getTransactionList(Map<String, Object> params, final ModelCallBack<TxHistoryListResp> callBack) {
        listCount++;
        Request<JSONObject> request = NoHttp.createJsonObjectRequest(ApiConfig.getTransactionHistory(), RequestMethod.POST);
        final String json = GsonUtil.mapToJson(params);
        request.setDefineRequestBodyForJson(json);
        HttpServer.getInstance().request(listCount, request, new SimpleResponseListener<JSONObject>() {

            @Override
            public void onSucceed(int what, Response<JSONObject> response) {
                BuglyLog.i(TAG, "getTransactionList onSucceed====>" + response.get().toString());
                super.onSucceed(what, response);
                if (response.isSucceed()) {
                    JSONObject jsonObject = response.get();
                    int status = jsonObject.optInt("status");
                    String msg = jsonObject.optString("msg");
                    if (status == 0) {
                        TxHistoryListResp resp = GsonUtil.jsonToObject(jsonObject.toString(), TxHistoryListResp.class);
                        List<TxHistory> txHistoryList = resp.getList();
                        if (txHistoryList == null) {
                            resp.setList(new ArrayList<TxHistory>());
                            callBack.onResponseSuccess(resp);
                        } else {
                            callBack.onResponseSuccess(resp);
                        }

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

    private int detailCount = 100;

    public void getTransactionDetail(String pkHash, final ModelCallBack<TransactionDetail> callBack) {
        detailCount++;
        Request<JSONObject> request = NoHttp.createJsonObjectRequest(ApiConfig.getTxDetail(), RequestMethod.POST);
        Map<String, Object> params = new HashMap<>();
        params.put(RequestParam.PKHASH, pkHash);
        String json = GsonUtil.mapToJson(params);
        request.setDefineRequestBodyForJson(json);
        HttpServer.getInstance().request(detailCount, request, new SimpleResponseListener<JSONObject>() {

            @Override
            public void onSucceed(int what, Response<JSONObject> response) {
                BuglyLog.i(TAG, "getTransactionDetail onSucceed====>" + response.get().toString());
                super.onSucceed(what, response);
                if (response.isSucceed()) {
                    JSONObject jsonObject = response.get();
                    int status = jsonObject.optInt("status");
                    String msg = jsonObject.optString("msg");
                    String dataJson = jsonObject.optString("data");
                    if (status == 0 && !TextUtils.isEmpty(dataJson)) {
                        TransactionDetail resp = GsonUtil.jsonToObject(dataJson, TransactionDetail.class);
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

    /**
     * 转账
     *
     * @param tokenAddr
     * @param from
     * @param to
     * @param value
     * @param password
     */
    public void transfer(final String tokenAddr, final String from, final String to, final String value, String password, final ModelCallBack<TxHistory> callBack) {
        ThreadManager.getInstance().postLogicTask(new Runnable() {
            @Override
            public void run() {
                try {
                    BuglyLog.e("transaction", "出发了一次交易发起 时间：" + System.currentTimeMillis());
                    TxHistory history = null;
                    WalletEntity walletEntity = WalletDBManager.getManager().getWalletEntity(from);
                    Credentials credentials = Credentials.create(walletEntity.getPrivateKey());
                    HttpService httpService = new HttpService(ApiConfig.getWeb3jUrlProxy(), HttpUtils.createOkHttpClient(), false);
                    httpService.addHeader(Constant.METHOD_KEY, Constant.METHOD_VALUE);
                    Web3j web3j = Web3jFactory.build(httpService);  // defaults to http://localhost:8545/
                    if (walletEntity == null) {
                        onTransactionFailed(-1, "交易失败", callBack);
                        return;
                    }
                    TransactionReceipt transactionReceipt;
                    //sct余额
                    String sctBalance = CoinModel.getWalletModel().getBalance(ApiConfig.getWeb3jUrlProxy(), from);
                    BigDecimal sctBalanceBD = Convert.toWei(sctBalance, Convert.Unit.ETHER);
                    BigDecimal kgBD = new BigDecimal(Web3Proxy.GAS_LIMIT);//矿工费用
                    if (GlobConfig.isMianTokenTransaction(tokenAddr)) {
                        Log.i(TAG,"主币转账 start");
                        // get the next available nonce
                        //转帐金额
                        BigDecimal weiValue = Convert.toWei(value, Convert.Unit.ETHER);
                        //转帐金额加矿工费用总额
                        BigDecimal totalValueBD = weiValue.add(kgBD);
                        if (sctBalanceBD.compareTo(totalValueBD) != 1) {
                            onTransactionFailed(-100, "余额不足", callBack);
                            return;
                        }
                        EthGetTransactionCount ethGetTransactionCount = web3j.ethGetTransactionCount(from, DefaultBlockParameterName.LATEST).sendAsync().get();
                        BigInteger nonce = ethGetTransactionCount.getTransactionCount();

                        RawTransaction rawTransaction = RawTransaction.createEtherTransaction(nonce, Web3Proxy.GAS_PRICE, Web3Proxy.GAS_LIMIT, to, weiValue.toBigInteger());

                        byte[] signedMessage = TransactionEncoder.signMessage(rawTransaction, credentials);
                        String hexValue = Hex.toHexString(signedMessage);
                        if (!TextUtils.isEmpty(hexValue) && !hexValue.startsWith("0x")) {
                            hexValue = "0x" + hexValue;
                        }
                        EthSendTransaction ethSendTransaction = web3j.ethSendRawTransaction(hexValue).send();
                        String hash = ethSendTransaction.getTransactionHash();

                        long timeStamp = System.currentTimeMillis();
                        String time = DateKit.timeStamp2Date(timeStamp / 1000 + "", null);
                        CrashReport.postCatchedException(new TransactionLog("from=" + from + "====>发起了一次主币交易  \nto=" + to + " \ntxHash=" + hash + "\n 时间：" + time));
                        inputDouTx(from,to,timeStamp);
                        BuglyLog.i(TAG, "hash=" + hash);
                        if (!hash.startsWith("0x000000")) {
                            history = new TxHistory();
                            history.setBlockGasLimit(Web3Proxy.GAS_LIMIT.toString());
                            history.setTransactionFrom(from);
                            history.setTransactionTo(to);
                            history.setCumulativeGas("0");
                            history.setPkHash(hash);
                            history.setAddress(tokenAddr);
                            history.setBlockNumber(-1);
                            history.setWalletAddr(walletEntity.getAddress());
                            history.setType(1);
                            history.setChainId(AppSettings.getAppSettings().getCurrentChainId());
                            history.setValue(value);
                            history.setState(2);
                            SimpleDateFormat dateformat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
                            String dateStr = dateformat.format(System.currentTimeMillis());
                            history.setBlockTimesStr(dateStr);
                        } else {
                            CrashReport.postCatchedException(new Exception(from + "主币交易失败 txHash:" + hash));
                            onTransactionFailed(-1,"交易失败",callBack);
                            return;
                        }
                        onTransactionSuccess(history, callBack);

                    } else {
                        Log.i(TAG,"代币转账 start");
                        TransactionReceiptProcessor processor = new NoOpProcessor(web3j);
                        TransactionManager transactionManager = new RawTransactionManager(web3j, credentials, ChainId.NONE, processor);
                        NulsStandardToken nulsStandardToken = NulsStandardToken.load(tokenAddr, web3j, transactionManager, Web3Proxy.GAS_PRICE, Web3Proxy.GAS_LIMIT);

                        BigInteger balance = nulsStandardToken.balanceOf(from).send();
                        BigDecimal balanceBD = new BigDecimal(balance);
                        BigDecimal valueBD = Convert.toWei(value, Convert.Unit.ETHER);

                        if (balanceBD.compareTo(valueBD) != 1) {
                            onTransactionFailed(-100, "余额不足", callBack);
                            return;
                        }
                        transactionReceipt = nulsStandardToken.transfer(to, valueBD.toBigInteger()).send();
                        long timeStamp = System.currentTimeMillis();
                        String time = DateKit.timeStamp2Date(timeStamp / 1000 + "", null);
                        String hash = transactionReceipt.getTransactionHash();
                        CrashReport.postCatchedException(new TransactionLog("from=" + from + "====>发起了一次代币交易  \nto=" + to + "\ntxHash=" + hash + " \n时间：" + time));
                        inputDouTx(from,to,timeStamp);

                        if (!hash.startsWith("0x000000")) {
                            history = new TxHistory();
                            history.setBlockGasLimit(Web3Proxy.GAS_LIMIT.toString());
                            history.setTransactionFrom(from);
                            history.setTransactionTo(to);
                            history.setPkHash(hash);
                            history.setType(1);
                            history.setValue("0");
                            history.setChainId(AppSettings.getAppSettings().getCurrentChainId());
                            history.setAddress(tokenAddr);
                            history.setWalletAddr(walletEntity.getAddress());
                            history.setTokenTransferTo(to);
                            history.setTokenTransfer(value);
                            history.setBlockNumber(-1);
                            history.setState(2);
                            SimpleDateFormat dateformat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
                            String dateStr = dateformat.format(System.currentTimeMillis());
                            history.setBlockTimesStr(dateStr);
                        } else {
                            CrashReport.postCatchedException(new Exception(from + "代币交易失败 txHash:" + hash));
                            onTransactionFailed(-1, "交易失败", callBack);
                            return;
                        }
                        onTransactionSuccess(history, callBack);
                    }
                } catch (Exception e) {
                    onTransactionFailed(-1, "交易失败", callBack);
                }

            }
        });
    }

    public int getNonce(String from) throws ExecutionException, InterruptedException {
        WalletEntity walletEntity = WalletDBManager.getManager().getWalletEntity(from);
        Credentials credentials = Credentials.create(walletEntity.getPrivateKey());
        HttpService httpService = new HttpService(ApiConfig.getWeb3jUrlProxy(), HttpUtils.createOkHttpClient(), false);
        httpService.addHeader(Constant.METHOD_KEY, Constant.METHOD_VALUE);
        Web3j web3j = Web3jFactory.build(httpService);  // defaults to http://localhost:8545/
        EthGetTransactionCount ethGetTransactionCount = web3j.ethGetTransactionCount(from, DefaultBlockParameterName.LATEST).sendAsync().get();
        BigInteger nonce = ethGetTransactionCount.getTransactionCount();
        return nonce.intValue();
    }

    /**
     * 获取主币的回执
     */
    public TransactionReceipt getEthTransactionReceipt(String hash, String from) {
        try {
            WalletEntity walletEntity = WalletDBManager.getManager().getWalletEntity(from);
            Credentials credentials = Credentials.create(walletEntity.getPrivateKey());
            HttpService httpService = new HttpService(ApiConfig.getWeb3jUrlProxy());
            httpService.addHeader(Constant.PROXY_FLAG_KEY, Constant.PROXY_FLAG_VALUE);
            Web3j web3j = Web3jFactory.build(httpService);
            EthGetTransactionReceipt ethGetTransactionReceipt = web3j.ethGetTransactionReceipt(hash).sendAsync().get();
            TransactionReceipt receipt = ethGetTransactionReceipt.getResult();

            return receipt;
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }

    }

    /**
     * 获取代币的回执
     */
    public TransactionReceipt getTokenTransactionReceipt(String hash, String from, String tokenAddr) {
        try {
            WalletEntity walletEntity = WalletDBManager.getManager().getWalletEntity(from);
            Credentials credentials = Credentials.create(walletEntity.getPrivateKey());
            Web3j web3j = Web3jFactory.build(new HttpService(ApiConfig.getWeb3jUrlProxy()));
            NulsStandardToken nulsStandardToken = NulsStandardToken.load(tokenAddr, web3j, credentials, Web3Proxy.GAS_PRICE, Web3Proxy.GAS_LIMIT);
            EthGetTransactionReceipt ethGetTransactionReceipt = web3j.ethGetTransactionReceipt(hash).sendAsync().get();
            List<NulsStandardToken.TransferEventResponse> responseList = nulsStandardToken.getTransferEvents(ethGetTransactionReceipt.getResult());
            android.util.Log.i("TxPresenter", "size=" + responseList.size());
            if (ethGetTransactionReceipt.getResult() != null && ethGetTransactionReceipt.getResult().getLogs().size() > 0) {
                return ethGetTransactionReceipt.getResult();
            }
            return null;
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    private void onTransactionSuccess(final TxHistory history, final ModelCallBack<TxHistory> callBack) {
        ThreadManager.getInstance().postFrontUITask(new Runnable() {
            @Override
            public void run() {
                callBack.onResponseSuccess(history);
            }
        });
    }

    private void onTransactionFailed(final int errorCode, final String msg, final ModelCallBack<TxHistory> callBack) {
        ThreadManager.getInstance().postFrontUITask(new Runnable() {
            @Override
            public void run() {
                callBack.onResponseFailed(errorCode, msg);
            }
        });
    }

    private void inputDouTx(String from,String to,long time){
        DoubleTxEntity entity=new DoubleTxEntity();
        entity.setFrom(from);
        entity.setTo(to);
        entity.setTime(time);
        DoubleTxManager.getManager().inputDouTx(entity);
    }
}
