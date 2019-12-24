package bd.com.appcore.util;

import android.content.Context;
import android.content.SharedPreferences;
import android.text.TextUtils;

public class AppSettings {
    private static final String LOCK_PATTERN = "lock_pattern";

    private static final AppSettings APP_SETTINGS = new AppSettings();

    private SharedPreferences preferences; // 文件存储

    // 默认城市
    private static final String DEFAULT_CITY = "深圳市";

    private AppSettings() {

    }

    public static AppSettings getAppSettings() {
        return APP_SETTINGS;
    }

    public void init(Context ctx) {
        preferences = ctx
                .getSharedPreferences("vec.com.vec.utils.AppSettings", Context.MODE_PRIVATE);
    }

    public void clear() {
        preferences.edit().clear().commit();
    }

    private String getStringItem(String key, String defaultvalue) {
        return preferences.getString(key, defaultvalue);
    }

    private void setStringItem(String key, String value) {
        preferences.edit().putString(key, value).commit();
    }

    private int getIntItem(String key, int defaultvalue) {
        return preferences.getInt(key, defaultvalue);
    }

    private void setIntItem(String key, int value) {
        preferences.edit().putInt(key, value).commit();
    }

    private long getLongItem(String key, long defaultvalue) {
        return preferences.getLong(key, defaultvalue);
    }

    private void setLongItem(String key, long value) {
        preferences.edit().putLong(key, value).commit();
    }

    private boolean getBooleanItem(String key, boolean defaultvalue) {
        return preferences.getBoolean(key, defaultvalue);
    }

    private void setBooleanItem(String key, boolean value) {
        preferences.edit().putBoolean(key, value).commit();
    }

    public void setHasLogin(boolean hasLogin) {
        setBooleanItem("hasLogin", hasLogin);
    }

    public boolean getHasLogin() {
        return getBooleanItem("hasLogin", false);
    }

    public void setDBHasReseted(boolean reseted) {
        setBooleanItem("db_has_reseted_v38", reseted);
    }

    public boolean getDBHasReseted() {
        return getBooleanItem("db_has_reseted_v38", false);
    }

    public void setCurrentAddress(String currentAddress) {
        setStringItem("currentAddress", currentAddress);
    }

    public boolean getIgnoreBackUpNotice() {
        return getBooleanItem("IgnoreBackUpNotice", false);
    }

    public void setIgnoreBackUpNotice(boolean needNotice) {
        setBooleanItem("IgnoreBackUpNotice", needNotice);
    }

    public String getCurrentAddress() {
        return getStringItem("currentAddress", null);
    }

    public void setCurrentChainIp(String currentAddress) {
        setStringItem("currentChainIp", currentAddress);
    }

    public boolean getWalletIsBacked(String walletAddress) {
        return getBooleanItem(walletAddress, false);
    }

    public void setWalletIsBacked(String walletAddress, boolean isBacked) {
        setBooleanItem(walletAddress, isBacked);
    }

    public boolean getEnableBugly() {
        return getBooleanItem("getEnableBugly", true);
    }

    public void setEnableBugly(boolean enableBugly) {
        setBooleanItem("getEnableBugly", enableBugly);
    }

    public void setSosoCount(int count) {
        setIntItem("sosoCount", count);
    }

    public int getSosoCount() {
        return getIntItem("sosoCount", 0);
    }

    public void setLastSosoTokenAddr(String tokenAddr) {
        setStringItem("lastsosotokenaddr", tokenAddr);
    }

    public String getLastSosoTokenAddr() {
        return getStringItem("lastsosotokenaddr", "");
    }

    public void setLastSosoTime(String time) {
        setStringItem("lastsosotime", time);
    }

    public String getLastSosoTime() {
        return getStringItem("lastsosotime", "");
    }

    public String getCurrentChainIp() {
        return getStringItem("currentChainIp", null);
    }

    public void setCurrentChinId(String chainId) {
        setStringItem("currentChainId", chainId);
    }

    public String getCurrentChainId() {
        return getStringItem("currentChainId", "SCT_LB");
    }

    public boolean isLockPatternOpen() {
        return !TextUtils.isEmpty(getLockPattern());
    }


    public long getLockPatternTime() {
        return getLongItem("lockpatterntime", TimeConstant.min_30);
    }

    public void setLockPatternTime(long time) {
        setLongItem("lockpatterntime", time);
    }

    public long getOnlineTime() {
        return getLongItem("OnlineTime", TimeConstant.day_30);
    }

    public void setOnlineTime(long time) {
        setLongItem("OnlineTime", time);
    }

    public String getLockPattern() {
        return getStringItem(LOCK_PATTERN, null);
    }

    public void setLockPattern(String lockPattern) {
        setStringItem(LOCK_PATTERN, lockPattern);
    }

}
