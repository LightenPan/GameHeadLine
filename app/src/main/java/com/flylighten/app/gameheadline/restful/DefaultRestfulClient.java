package com.flylighten.app.gameheadline.restful;

import android.content.Context;

import com.loopj.android.http.AsyncHttpClient;
import com.loopj.android.http.AsyncHttpResponseHandler;
import com.loopj.android.http.BinaryHttpResponseHandler;
import com.loopj.android.http.JsonHttpResponseHandler;
import com.loopj.android.http.RequestParams;

public class DefaultRestfulClient {

    private AsyncHttpClient mClient = new AsyncHttpClient();
    private String mToken = "test_token";
    private Context mContext;

    private DefaultRestfulClient() {
    }

    private static class SingletonHolder {
        private final static DefaultRestfulClient INSTANCE = new DefaultRestfulClient();
    }

    public static DefaultRestfulClient inst() {
        return SingletonHolder.INSTANCE;
    }

    public int init(Context context) {
        mContext = context;

        //设置链接超时，单位毫秒，如果不设置，默认为10秒
        mClient.setTimeout(3000);

        return 0;
    }

    //用一个完整url获取一个string对象
    public void get(String url, AsyncHttpResponseHandler handler) {
        RequestParams params = new RequestParams();
        params.put("token", mToken);
        mClient.get(mContext, url, params, handler);
    }

    //url里面带参数
    public void get(String url, RequestParams params, AsyncHttpResponseHandler handler) {
        params.put("token", mToken);
        mClient.get(mContext, url, params, handler);
    }

    //不带参数，获取json对象或者数组
    public void get(String url, JsonHttpResponseHandler handler) {
        RequestParams params = new RequestParams();
        params.put("token", mToken);
        mClient.get(mContext, url, params, handler);
    }

    //带参数，获取json对象或者数组
    public void get(String url, RequestParams params, JsonHttpResponseHandler handler) {
        params.put("token", mToken);
        mClient.get(mContext, url, params, handler);
    }

    //下载数据使用，会返回byte数据
    public void get(String url, BinaryHttpResponseHandler handler) {
        RequestParams params = new RequestParams();
        params.put("token", mToken);
        mClient.get(mContext, url, params, handler);
    }

    //用一个完整url获取一个string对象
    public void post(String url, AsyncHttpResponseHandler handler) {
        RequestParams params = new RequestParams();
        params.put("token", mToken);
        mClient.post(mContext, url, params, handler);
    }

    //url里面带参数
    public void post(String url, RequestParams params, AsyncHttpResponseHandler handler) {
        params.put("token", mToken);
        mClient.post(mContext, url, params, handler);
    }

    //不带参数，获取json对象或者数组
    public void post(String url, JsonHttpResponseHandler handler) {
        RequestParams params = new RequestParams();
        params.put("token", mToken);
        mClient.post(mContext, url, params, handler);
    }

    //带参数，获取json对象或者数组
    public void post(String url, RequestParams params, JsonHttpResponseHandler handler) {
        params.put("token", mToken);
        mClient.post(mContext, url, params, handler);
    }

    //下载数据使用，会返回byte数据
    public void post(String url, BinaryHttpResponseHandler handler) {
        RequestParams params = new RequestParams();
        params.put("token", mToken);
        mClient.post(mContext, url, params, handler);
    }

    public AsyncHttpClient getClient() {
        return mClient;
    }
}