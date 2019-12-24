package bd.com.supply.module.common;

import android.text.TextUtils;

import bd.com.appcore.util.AppSettings;
import bd.com.supply.util.FakeDataHelper;
import bd.com.supply.util.FakeDataHelper;

/**
 * author:     xumin
 * date:       2018/10/27
 * email:      xumin2@evergrande.cn
 */
public class GlobConfig {
    public static final String ETH_CHAIN_ID = "GETH";
    public static final String SCT_02_CHAIN_ID = "SCT_02";
    public static final String DEFAULT_CHAIN = "DEFAULT";

    public static boolean isEth() {
        String currentChainID = AppSettings.getAppSettings().getCurrentChainId();
        if (GlobConfig.ETH_CHAIN_ID.equals(currentChainID)) {
            return true;
        } else {
            return false;
        }
    }

    /**
     * 获取后缀
     *
     * @return
     */
    public static String getMainTokenType() {
        if (isEth()) {
            return "ether";
        } else {
            return "sct";
        }
    }

    public static String getMainTokenName() {
        if (isEth()) {
            return FakeDataHelper.MAIN_ETH_SYMBOL;
        } else {
            return FakeDataHelper.MAIN_SCT_SYMBOL;
        }
    }


    public static boolean isMianTokenTransaction(String tokenAddr) {
        return TextUtils.equals(FakeDataHelper.getEthTokenAddr(), tokenAddr) || TextUtils.equals(FakeDataHelper.getSctTokenAddr(), tokenAddr);
    }

    public static String getCurrentChainId() {
        String currentChainId = AppSettings.getAppSettings().getCurrentChainId();
        return currentChainId;
    }

    public static String getCurrentWalletAddress() {
        return AppSettings.getAppSettings().getCurrentAddress();
    }
}
