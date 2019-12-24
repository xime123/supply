package bd.com.supply.module.common;


public class Constant {
    public static final String JPUSH_APP_KEY = "14e28f354788885d7ec025ba";
    public static final String JPUSH_MASTER_SECRET = "62ecc2a2b78c8d69f0557a6b";

    /**
     * 消息相关
     */
    public static final String MSG_1001 = "1001";
    public static final String MSG_1002 = "1002";

    /**
     * 交易和查receipt加头信息
     */
    public static final String METHOD_KEY = "METHOD";
    public static final String METHOD_VALUE = "eth_sendRawTransaction";

    public static final String PROXY_FLAG_KEY = "METHOD";
    public static final String PROXY_FLAG_VALUE = "eth_getTransactionReceipt";

    public static final String SCT_MARKET = "sct_qc";
    public static final String ETH_MARKET = "eth_qc";
}
