package com.flylighten.app.gameheadline.request;

import android.widget.Toast;

import com.flylighten.app.gameheadline.app.MyApplication;

import in.srain.cube.cache.CacheManager;
import in.srain.cube.cache.CacheManagerFactory;
import in.srain.cube.cache.Query;
import in.srain.cube.cache.QueryJsonHandler;
import in.srain.cube.request.JsonData;
import in.srain.cube.util.CLog;

public class CommRequestManager {

    private static CommRequestManager sInstance;

    public static CommRequestManager getInstance() {
        if (sInstance == null) {
            sInstance = new CommRequestManager();
        }
        return sInstance;
    }

    public void prepareRequest(in.srain.cube.request.RequestBase request) {

        // you can add some basic data to here
        if (request.getRequestData().shouldPost()) {
            request.getRequestData().addPostData("token", MyApplication.getToken());
        } else {
            request.getRequestData().addQueryData("token", MyApplication.getToken());
        }
    }
}
