package bd.com.appcore.util;


import java.util.ArrayList;
import java.util.List;

import bd.com.appcore.R;


/**
 * Created by 徐敏 on 2017/8/29.
 */

public class TimeConstant {
    /** 手势启动时间*/
    public static List<String> times = new ArrayList<>();
    public static List<GestureTime> timeObjs = new ArrayList<>();
    /** 手势启动时间*/
    public static List<String> onlineTimes = new ArrayList<>();
    public static List<GestureTime> onlineTimeObjs = new ArrayList<>();

    public static final long min_10=10*60*1000;
    public static final long min_30=30*60*1000;
    public static final long one_hour=60*60*1000;
    public static final long six_hour=6*60*60*1000;
    public static final long one_day=24*60*60*1000;
    public static final long three_day=3*24*60*60*1000;
    public static final long seven_day=7*24*60*60*1000;
    public static final long day_30=30*24*60*60*1000;

    static {
        times.add(TDString.getStr(R.string.min_10));
        times.add(TDString.getStr(R.string.min_30));
        times.add(TDString.getStr(R.string.one_hour));
        times.add(TDString.getStr(R.string.one_day));
        times.add(TDString.getStr(R.string.three_day));
        timeObjs.add(new GestureTime(TDString.getStr(R.string.min_10), TimeConstant.min_10));
        timeObjs.add(new GestureTime(TDString.getStr(R.string.min_30),TimeConstant.min_30));
        timeObjs.add(new GestureTime(TDString.getStr(R.string.one_hour),TimeConstant.one_hour));
        timeObjs.add(new GestureTime(TDString.getStr(R.string.one_day),TimeConstant.one_hour));
        timeObjs.add(new GestureTime(TDString.getStr(R.string.three_day),TimeConstant.three_day));

        onlineTimes.add(TDString.getStr(R.string.six_hour));
        onlineTimes.add(TDString.getStr(R.string.one_day));
        onlineTimes.add(TDString.getStr(R.string.three_day));
        onlineTimes.add(TDString.getStr(R.string.seven_day));
        onlineTimes.add(TDString.getStr(R.string.day_30));
        onlineTimeObjs.add(new GestureTime(TDString.getStr(R.string.six_hour),TimeConstant.six_hour));
        onlineTimeObjs.add(new GestureTime(TDString.getStr(R.string.one_day),TimeConstant.one_day));
        onlineTimeObjs.add(new GestureTime(TDString.getStr(R.string.three_day),TimeConstant.three_day));
        onlineTimeObjs.add(new GestureTime(TDString.getStr(R.string.seven_day),TimeConstant.seven_day));
        onlineTimeObjs.add(new GestureTime(TDString.getStr(R.string.day_30),TimeConstant.day_30));

    }
    public static class GestureTime{
        public String timeName;
        public long time;

        public GestureTime(String timeName, long time) {
            this.timeName = timeName;
            this.time = time;
        }
    }
}
