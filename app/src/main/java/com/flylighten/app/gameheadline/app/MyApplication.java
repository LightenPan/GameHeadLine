package com.flylighten.app.gameheadline.app;

import android.app.Application;
import android.content.Context;

import in.srain.cube.cache.CacheManagerFactory;
import in.srain.cube.request.RequestCacheManager;

/**
 * Created by Administrator on 2016/3/30.
 */
public class MyApplication extends Application {
    private static Context context;

    @Override
    public void onCreate() {
        super.onCreate();

        //崩溃处理
        CrashHandler crashHandler = CrashHandler.getInstance();
        crashHandler.init(getApplicationContext());

        context = getApplicationContext();

        initRequestCache();

        // init local cache, just use default
        CacheManagerFactory.initDefaultCache(this, null, -1, -1);
    }

    public static Context getContext() {
        return context;
    }

    private void initRequestCache() {
        String dir = "request-cache";
        RequestCacheManager.init(this, dir, 1024 * 10, 1024 * 10);
    }
}
