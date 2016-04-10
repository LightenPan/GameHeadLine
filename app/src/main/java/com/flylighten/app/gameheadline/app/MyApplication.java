package com.flylighten.app.gameheadline.app;

import android.app.Application;
import android.content.Context;
import android.content.SharedPreferences;

import org.json.JSONException;
import org.json.JSONObject;

import in.srain.cube.cache.CacheManagerFactory;
import in.srain.cube.cache.Query;
import in.srain.cube.cache.QueryJsonHandler;
import in.srain.cube.request.JsonData;
import in.srain.cube.request.RequestCacheManager;
import in.srain.cube.util.CLog;

/**
 * Created by Administrator on 2016/3/30.
 */
public class MyApplication extends Application {
    private String TAG = "MyApplication";
    private static Context mContext;
    private static String mUid;
    private static String mToken;
    private static String mDuoShuoToken;
    private static long mExpireTime = 0;
    private static MyApplication mInst;

    public static MyApplication inst() {
        return mInst;
    }

    @Override
    public void onCreate() {
        super.onCreate();

        //崩溃处理
        CrashHandler crashHandler = CrashHandler.getInstance();
        crashHandler.init(getApplicationContext());

        mContext = getApplicationContext();

        // 初始化默认cache
        CacheManagerFactory.initDefaultCache(this, null, -1, -1);

        // 初始化请求cache
        initRequestCache();

        // 从cache读取token
        readTokenFromCache();

        mInst = this;
    }

    public static Context getContext() {
        return mContext;
    }

    public static String getUid() {
        return mUid;
    }

    public static String getToken() {
        return mToken;
    }

    public static String getDuoShuoToken() {
        return mDuoShuoToken;
    }

    public static boolean isLogin() {
        int currentTime = (int)(System.currentTimeMillis()/1000);
        return (null != mToken) && (mToken.length() > 0) && (currentTime < mExpireTime);
    }

    private void initRequestCache() {
        String dir = "request-cache";
        RequestCacheManager.init(this, dir, 1024 * 10, 1024 * 10);
    }

    private void readTokenFromCache() {
        SharedPreferences preferences = getSharedPreferences("user_token", Context.MODE_PRIVATE);
        mUid = preferences.getString("uid", "");
        mToken = preferences.getString("token", "");
        mDuoShuoToken = preferences.getString("duoshuo_token", "");
        mExpireTime = preferences.getInt("expire_time", 0);
    }

    public void saveTokenToCache(String uid, String token, String duoshuo_token, int expire_time) {
        SharedPreferences preferences = getSharedPreferences("user_token",Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = preferences.edit();
        editor.putString("uid", uid);
        editor.putString("token", token);
        editor.putString("duoshuo_token", duoshuo_token);
        editor.putInt("expire_time", expire_time);
        editor.commit();

        mUid = uid;
        mToken = token;
        mExpireTime = expire_time;
        mDuoShuoToken = duoshuo_token;
    }
}
