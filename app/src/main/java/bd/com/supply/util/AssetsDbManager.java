package bd.com.supply.util;

import android.content.res.AssetManager;
import android.util.Log;

import com.google.gson.reflect.TypeToken;

import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.List;

import bd.com.appcore.util.GsonUtil;
import bd.com.supply.app.BdApplication;
import bd.com.supply.module.news.domain.News;
import bd.com.supply.app.BdApplication;


/**
 * author：xumin
 * date:   2018/4/3
 * email:  xumin2@evergrande.cn
 * 管理加载assets里的预置文件
 */
public class AssetsDbManager {
    private static final String BANNER_LIST = "banner.json";
    private static final String NEWS_LIST = "news.json";
    public static final String JSON_LIST_KEY = "list";

    private static String TAG = AssetsDbManager.class.getSimpleName(); // for LogCat

    /**
     * 获取产品列表
     *
     * @return
     */
    public static List<News> getNewsList() {
        try {
            //将读出的字符串转换成JSONobject
            JSONObject jsonObject = new JSONObject(getJsonFromAssets(NEWS_LIST));
            String jsonArray = jsonObject.getString(JSON_LIST_KEY);
            TypeToken<ArrayList<News>> typeToken = new TypeToken<ArrayList<News>>() {
            };
            return GsonUtil.jsonToListObject(jsonArray, typeToken);
        } catch (Exception e) {
            Log.e(TAG, "json parse error msg::" + e.getMessage());
            return null;
        }

    }

    /**
     * 获取产品引导页列表
     *
     * @return
     */
    public static List<News> getBannerList() {
        try {
            //将读出的字符串转换成JSONobject
            JSONObject jsonObject = new JSONObject(getJsonFromAssets(BANNER_LIST));
            String jsonArray = jsonObject.getString(JSON_LIST_KEY);
            TypeToken<ArrayList<News>> typeToken = new TypeToken<ArrayList<News>>() {
            };
            return GsonUtil.jsonToListObject(jsonArray, typeToken);
        } catch (Exception e) {
            Log.e(TAG, "getGuidInfos json prase error msg::" + e.getMessage());
            return null;
        }

    }

    public static String getJsonFromAssets(String fileName) {
        //将json数据变成字符串
        StringBuilder stringBuilder = new StringBuilder();
        try {
            //获取assets资源管理器
            AssetManager assetManager = BdApplication.getAppInstance().getAssets();
            //通过管理器打开文件并读取
            BufferedReader bf = new BufferedReader(new InputStreamReader(
                    assetManager.open(fileName)));
            String line;
            while ((line = bf.readLine()) != null) {
                stringBuilder.append(line);
            }
        } catch (IOException e) {
            Log.e(TAG, "getJsonFromAssets error=" + e.getMessage());
        }
        return stringBuilder.toString();
    }

}
