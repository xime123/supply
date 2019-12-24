package bd.com.supply.util;

import android.text.TextUtils;

public class AddressUtil {
    public static String getAddrFromCode(String code) {
        if (TextUtils.isEmpty(code)) return "";
        String result = code;
        String res[] = result.split("0x");
        String addr = "0x" + res[1];
        return addr;
    }
}
