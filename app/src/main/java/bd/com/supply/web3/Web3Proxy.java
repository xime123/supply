package bd.com.supply.web3;

import org.web3j.crypto.Credentials;
import org.web3j.protocol.Web3j;
import org.web3j.protocol.Web3jFactory;
import org.web3j.protocol.core.methods.response.EthGasPrice;
import org.web3j.protocol.http.HttpService;
import org.web3j.tx.ChainId;
import org.web3j.tx.RawTransactionManager;
import org.web3j.tx.response.NoOpProcessor;
import org.web3j.tx.response.TransactionReceiptProcessor;
import org.web3j.utils.Convert;

import java.io.IOException;
import java.math.BigDecimal;
import java.math.BigInteger;

import bd.com.appcore.util.AppSettings;
import bd.com.supply.module.wallet.ApiConfig;
import bd.com.supply.util.HttpUtils;
import bd.com.supply.web3.contract.NulsStandardToken;
import bd.com.supply.web3.contract.SuperConductToken;
import bd.com.supply.module.wallet.ApiConfig;
import bd.com.supply.web3.contract.SuperConductToken;
import bd.com.walletdb.entity.TokenEntity;
import bd.com.walletdb.entity.WalletEntity;
import bd.com.walletdb.manager.WalletDBManager;


public class Web3Proxy {
    //private String contractaddress = "0x0f46a24b42923aae949cd82b78b90b21e990cbad";
    private String contractaddress = "0x0f46a24b42923aae949cd82b78b90b21e990cbad";
    public static final String SCT_CONTRACT_ADDRESS = "0xsctcontractaddress";
    //    public static final BigInteger GAS_PRICE = BigInteger.valueOf(10000000000L);
//    public static BigInteger GAS_LIMIT = BigInteger.valueOf(2000000);
    public static BigInteger GAS_PRICE = BigInteger.valueOf(22_000_000_000L);
    //public static  BigInteger GAS_LIMIT = BigInteger.valueOf(2200000);
    public static BigInteger GAS_LIMIT = BigInteger.valueOf(60000);
    public static BigInteger DEPLOY_GAS_LIMIT = BigInteger.valueOf(2000000);
    public static BigInteger PACK_SOSO_GAS_LIMIT = BigInteger.valueOf(1_000_010_000L);
    private static Web3Proxy web3Proxy = new Web3Proxy();

    private Web3Proxy() {
    }

    public Web3j getWeb3j() {
        HttpService httpService = new HttpService(ApiConfig.getWeb3jUrlProxy(), HttpUtils.createOkHttpClient(), false);
        Web3j web3j = Web3jFactory.build(httpService);
        return web3j;
    }

    public static Web3Proxy getWeb3Proxy() {
        return web3Proxy;
    }


    //    public NulsStandardToken deploy(Credentials credentials) throws Exception {
//        NulsStandardToken token = NulsStandardToken.deploy(web3j, credentials, GAS_PRICE, GAS_LIMIT, new BigInteger("100000000000000000000000000"), "MANA", new BigInteger("18"), "MANA").send();
//        String conaddress = token.getContractAddress();
//        setContractaddress(conaddress);
//        return token;
//    }
//deploy(Web3j web3j, Credentials credentials, BigInteger gasPrice, BigInteger gasLimit)
    public SuperConductToken deploy(Credentials credentials) throws Exception {
        SuperConductToken token = SuperConductToken.deploy(getWeb3j(), credentials, GAS_PRICE, GAS_LIMIT).send();
        String conaddress = token.getContractAddress();
        // setContractaddress(conaddress);
        return token;
    }

    public String deploy(Credentials credentials, String initAumount, String name, String symbol) throws Exception {
        NulsStandardToken token = NulsStandardToken.deploy(getWeb3j(), credentials, GAS_PRICE, DEPLOY_GAS_LIMIT, new BigInteger(initAumount), name, new BigInteger("18"), symbol).send();
        String conaddress = token.getContractAddress();
        //setContractaddress(conaddress);
        return conaddress;
    }


    public org.web3j.tx.TransactionManager getPoolTransactionManager() {
        // Web3j web3j, Credentials credentials, byte chainId, int attempts, long sleepDuration
        org.web3j.tx.TransactionManager transactionManager = new RawTransactionManager(getWeb3j(), getCredentials(), ChainId.NONE, 200,300);
        return transactionManager;
    }
//    public CoinBean getNulsCoinInfo(Credentials credentials) throws Exception {
//        BigInteger gasPrice = requestCurrentGasPrice();
//        NulsStandardToken token = NulsStandardToken.load(getContractaddress(), web3j, credentials, GAS_PRICE, GAS_LIMIT);
//        BigInteger balance = token.balanceOf(credentials.getAddress()).send();
//        BigDecimal balacnebd = new BigDecimal(balance);
//        BigDecimal interestRate = new BigDecimal("100"); //利率
//        BigDecimal interest = balacnebd.divide(interestRate); //相处
//        String newBiginteger = interest.setScale(2, BigDecimal.ROUND_HALF_UP).toString();
//        String version = token.version().send();
//        CoinBean bean = new CoinBean();
//        bean.setName("联盟币");
//        bean.setIcon("ic_category_31");
//        bean.setBalance(newBiginteger);
//        bean.setVersion(version);
//        return bean;
//    }

    /**
     * BigDecimal balanceBD = new BigDecimal(bigInteger); //余额。单位eth
     * BigDecimal interestRate = new BigDecimal("1000000000000000000"); //利率
     * BigDecimal interest = balanceBD.divide(interestRate); //相处
     * String newBiginteger = interest.setScale(8, BigDecimal.ROUND_HALF_UP).toString();
     */

    public String getERC20Balance(Credentials credentials, TokenEntity tokenEntity) throws Exception {
        NulsStandardToken token = NulsStandardToken.load(tokenEntity.getAddress(), getWeb3j(), credentials, GAS_PRICE, GAS_LIMIT);
        BigInteger balance = token.balanceOf(credentials.getAddress()).send();
        if (balance.intValue() != 0) {
            BigDecimal valueBD = Convert.fromWei(balance.toString(), Convert.Unit.ETHER);

            return valueBD.toString();
        } else {
            return "0";
        }
    }

    public BigInteger requestCurrentGasPrice() throws IOException {
        EthGasPrice ethGasPrice = getWeb3j().ethGasPrice().send();

        return ethGasPrice.getGasPrice();
    }

    public String getContractaddress() {
        System.out.print("当前合约地址;" + contractaddress);
        return contractaddress;
    }

    public void setContractaddress(String contractaddress) {
        System.out.print("当前合约地址;" + contractaddress);
        this.contractaddress = contractaddress;
    }

    public int getEth_BlockNumber() throws IOException {
        return web3Proxy.getWeb3j().ethBlockNumber().send().getBlockNumber().intValue();
    }

    public static Credentials getCredentials() {
        final String cuurentAddress = AppSettings.getAppSettings().getCurrentAddress();
        WalletEntity entity = WalletDBManager.getManager().getWalletEntity(cuurentAddress);
        final Credentials credentials = org.web3j.crypto.Credentials.create(entity.getPrivateKey());
        return credentials;
    }
}
