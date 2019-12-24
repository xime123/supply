package bd.com.supply.util;

import android.util.Log;

import java.util.concurrent.TimeUnit;

import okhttp3.OkHttpClient;
import okhttp3.logging.HttpLoggingInterceptor;

public class HttpUtils {
    public static OkHttpClient createOkHttpClient() {
        OkHttpClient.Builder builder = new OkHttpClient.Builder();
        builder.connectTimeout(15_1000, TimeUnit.MILLISECONDS);
        builder.writeTimeout(15_1000, TimeUnit.MILLISECONDS);
        builder.readTimeout(15_1000, TimeUnit.MILLISECONDS);
        configureLogging(builder);
        return builder.build();
    }

    private static void configureLogging(OkHttpClient.Builder builder) {

        HttpLoggingInterceptor logging = new HttpLoggingInterceptor(
                new HttpLoggingInterceptor.Logger() {
                    @Override
                    public void log(String msg) {
                        Log.i("HttpUtils", "msg=" + msg);
                    }
                });
        logging.setLevel(HttpLoggingInterceptor.Level.BODY);
        builder.addInterceptor(logging);

    }
}
