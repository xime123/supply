package bd.com.supply.module.wallet;


import bd.com.supply.BuildConfig;

public class ApiConfig {

    public static String BASE_URL = BuildConfig.base_url;//钱包服务器（历史记录）
    public static String SOSO_BASE_URL = BuildConfig.soso_url;//溯源服务器
    public static String SOSO_SOURCE = BuildConfig.soso_source_url;//溯源资源服务器
    public static String SOSO_INDEX = BuildConfig.soso_index_url;//溯源索引服务器


    private static String ETHSCAN_PRICE = "https://api.etherscan.io/api?module=stats&action=ethprice&apikey=YourApiKeyToken";
    public static String ETHSCAN_TX = "https://etherscan.io/tx/";
    public static String getSctTxWebUrl = getBaseUrl() + "/transaction/getTbTransactionDetailPage.page?pkHash=";

    /**
     * 交易记录
     */
    private static String TRANSACTION_HISTORY;

    /**
     * token列表
     */
    private static String TOKEN_LIST;
    private static String CHAIN_LIST;

    private static String CHECK_APP_UPDATE;

    private static String REGISTER_DVICE;

    public static String getBaseUrl() {
        return BASE_URL;
    }

    public static String getSosoBaseUrl() {
        return SOSO_BASE_URL;
    }

    public static void setSosoBaseUrl(String sosoBaseUrl) {
        SOSO_BASE_URL = sosoBaseUrl;
    }

    public static String getSosoIndex() {
        return SOSO_INDEX;
    }

    public static void setSosoSource(String sosoSource) {
        SOSO_SOURCE = sosoSource;
    }

    public static String getWeb3jUrlProxy() {
        return getBaseUrl() + "/turn/invoke.json";//波哥;
    }

    public static String getEthscanPrice() {
        return ETHSCAN_PRICE;
    }

    public static String getTransactionHistory() {
        return getBaseUrl() + "/transaction/tokenListByPage.json";
    }

    public static String getBatchList() {
        return getSosoBaseUrl() + "/sy/listCategory.json";
    }

    public static String getBindData() {
        return getSosoBaseUrl() + "/sy/bindTxHash.json";
    }

    public static String getHashByUUid() {
        return getSosoBaseUrl() + "/sy/getTxByUUID.json";
    }

    public static String getListAucForPage() {
        return getSosoBaseUrl() + "/sy/listAuc.json";
    }

    public static String getReportProductForBox() {
        return getSosoBaseUrl() + "/sy/reportProductForBox.json";
    }

    public static String getReportBigBoxPacking() {
        return getSosoBaseUrl() + "/sy/reportBigBoxPacking.json";
    }

    public static String reportTraceRecord() {
        return getSosoBaseUrl() + "/sy/reportTraceRecord.json";
    }



    public static String isLegalForBox() {
        return getSosoBaseUrl() + "/sy/isLegalForBox.json";
    }

    public static String isLegalFoProduct() {
        return getSosoBaseUrl() + "/sy/isLegalForProducts.json";
    }

    public static String getHashByUUids() {
        return getSosoBaseUrl() + "/sy/getTxByUUIDs.json";
    }

    public static String getProductList() {
        return getSosoBaseUrl() + "/sy/listProduct.json";
    }

    public static String tracingSrc() {
        return getSosoBaseUrl() + "/sy/tracingSrc.json";
    }

    public static String listAd() {
        return getSosoBaseUrl() + "/sy/api/listAd.json";
    }

    public static String getPriceListByMarketName() {
        return getBaseUrl() + "/coin/getCoinPriceByMarketName.json";
    }

    public static String getTokenList() {
        return getBaseUrl() + "/token/tokenListByPage.json";
    }

    public static String getChainList() {
        return getBaseUrl() + "/chain/chainList.json";
    }

    public static String getCheckAppUpdate() {
        return getBaseUrl() + "/device/versionCheck.json";
    }

    public static String getRegisterDvice() {
        return getBaseUrl() + "/device/deviceRegistry.json";
    }

    public static String getTxDetail() {
        return getBaseUrl() + "/transaction/getTbTransactionByPkHash.json";
    }

    public static String getSugGasUrl() {
        return getBaseUrl() + "/coin/getGasPrice.json";
    }

    public static String getListIndexUrl() {
        return getSosoIndex() + "/sy/listIndexUrl.json";
    }


}
