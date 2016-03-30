package com.flylighten.app.gameheadline.restful;

import android.text.TextUtils;

import com.loopj.android.http.RequestParams;

import org.json.JSONObject;

public abstract class CacheNetTool extends BaseNetTool {

    protected abstract String getCacheKey(RequestParams param);

    @Override
    protected JSONObject getResultFromCache(RequestParams param) {
        JSONObject result = null;
        String key = getCacheKey(param);
        if (!TextUtils.isEmpty(key)) {
//            Serializable serializable = Pool.Factory.dbPool().get(key);
//            if (serializable != null){
//                try {
//                    result = unpack(param, (Message) serializable);
//                    //result = (Result) serializable;
//                } catch (Exception e){
//                    e.printStackTrace();
//                    TLog.e(TAG, "decode from cache", e);
//                }
//            }
        }
        return result;
    }

    @Override
    protected void saveResultToCache(RequestParams params, JSONObject msg) {
//        if (msg instanceof Serializable) {
//            String cacheKey = getCacheKey(param);
//            Pool.Factory.dbPool().put(cacheKey,  msg);
//        }
    }
}