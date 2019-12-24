package bd.com.supply.util;

import java.util.ArrayList;
import java.util.List;

import bd.com.appcore.util.AppSettings;
import bd.com.supply.module.common.GlobConfig;
import bd.com.walletdb.entity.TokenEntity;


public class FakeDataHelper {
    public static String ETH_TOKEN_ADDR_PRE = "0xeth";
    public static String SCT_TOKEN_ADDR_PRE = "0xsct";
    public static String MAIN_SCT_SYMBOL = "SCT";
    public static String SCT_SYMBOL = "SCT";
    public static String MAIN_ETH_SYMBOL = "ETH";

    public static List<TokenEntity> getCoins() {
        List<TokenEntity> coinBeans = new ArrayList<>();
        String currentChainId = AppSettings.getAppSettings().getCurrentChainId();
        if (!GlobConfig.SCT_02_CHAIN_ID.equals(currentChainId)) { //v3没有主币
            TokenEntity coinBean = new TokenEntity();
            if (GlobConfig.isEth()) {
                coinBean.setAddress(ETH_TOKEN_ADDR_PRE);
                coinBean.setName("ETH");
                coinBean.setSymbol(MAIN_ETH_SYMBOL);
                coinBean.setIcon("eth_icon");
                coinBean.setWalletAddress(GlobConfig.getCurrentWalletAddress());
                coinBean.setChainId(GlobConfig.ETH_CHAIN_ID);
                coinBean.setChecked(true);
                coinBeans.add(coinBean);
            } else {
                coinBean.setAddress(SCT_TOKEN_ADDR_PRE);
                coinBean.setName("SCT");
                coinBean.setSymbol(MAIN_SCT_SYMBOL);
                coinBean.setChecked(true);
                coinBean.setWalletAddress(GlobConfig.getCurrentWalletAddress());
                coinBean.setChainId(GlobConfig.DEFAULT_CHAIN);
                coinBean.setIcon("ic_category_32");
                coinBeans.add(coinBean);
            }
        }

        return coinBeans;
    }

    public static String getEthTokenAddr() {
        return ETH_TOKEN_ADDR_PRE;
    }

    public static String getSctTokenAddr() {
        return SCT_TOKEN_ADDR_PRE;
    }
}
