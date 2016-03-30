package com.flylighten.app.gameheadline;

import android.app.Application;
import android.content.Context;

import com.flylighten.app.gameheadline.restful.DefaultRestfulClient;

/**
 * Created by Administrator on 2016/3/30.
 */
public class MyApplication extends Application {
    private static Context context;

    @Override
    public void onCreate() {
        super.onCreate();
        context = getApplicationContext();

        DefaultRestfulClient.inst().init(context);
    }

    public static Context getContext() {
        return context;
    }
}
