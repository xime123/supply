package bd.com.supply.module.transaction;

import java.util.ArrayList;
import java.util.List;

import bd.com.supply.widget.SelectPerioDialog;

/**
 * author:     xumin
 * date:       2019/1/21
 * email:      xumin2@evergrande.cn
 * 同一个周期用&连接，不同周期用$连接，地址与信息用@连接
 */
public class SoSoConfig {
    private static final String ONE = "种植";
    private static final String TWO = "移苗";
    private static final String THREE = "喷药";
    private static final String FOUR = "施肥";
    private static final String FIVE = "灌溉";
    private static final String SIX = "除草";
    private static final String SEVEN = "采摘";
    public static final List<SelectPerioDialog.Perios> PERIOS = new ArrayList<>();

    static {
        for (int i = 0; i < 7; i++) {
            SelectPerioDialog.Perios perios = new SelectPerioDialog.Perios();
            perios.setName(generateByPeroid(i));
            PERIOS.add(perios);
        }
    }

    public static final String PEROID_GAP = "\\$";
    public static final String CONTENT_GAP = "&";
    public static final String ADDR_GAP = "@";
    public static final String SOSO_TOKEN_ADDRESS = "0x20b4f5dfbe931492abf6fb03b7c5e8f2db9c255b";

    public static String generateByPeroid(int peroid) {
        switch (peroid) {
            case 0:
                return "种植";
            case 1:
                return "移苗";
            case 2:
                return "喷药";
            case 3:
                return "施肥";
            case 4:
                return "灌溉";
            case 5:
                return "除草";
            case 6:
                return "采摘";
        }
        return "运输";
    }

    public static String generateByTitle(String title) {
        switch (title) {
            case ONE:
                return "0";
            case TWO:
                return "1";
            case THREE:
                return "2";
            case FOUR:
                return "3";
            case FIVE:
                return "4";
            case SIX:
                return "5";
            case SEVEN:
                return "6";
        }
        return "运输";
    }

    public static boolean NEED_PWD = true;
    public static boolean NEED_SWITCH_ENV = true;
}
