package com.flylighten.app.gameheadline.restful;

import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.os.Message;
import android.widget.Toast;

import com.flylighten.app.gameheadline.MyApplication;
import com.loopj.android.http.AsyncHttpClient;
import com.loopj.android.http.AsyncHttpResponseHandler;
import com.loopj.android.http.JsonHttpResponseHandler;
import com.loopj.android.http.RequestParams;

import org.apache.http.conn.ConnectTimeoutException;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.net.SocketTimeoutException;

import cz.msebera.android.httpclient.Header;

public class BaseNetTool {

    private static final int HTTP_SUCCESS = 1;
    private static final int HTTP_FAILED = 2;
    private static final int HTTP_TIMEOUT = 3;
    private Handler m_handler;

    protected JSONObject getResultFromCache(RequestParams params) {
        return null;
    }

    protected void saveResultToCache(RequestParams params, JSONObject msg) {

    }

    public void get(final Looper looper, final String url, final RequestParams params, final RequestCallback callback) {

        //如果有缓冲，先返回缓存数据，然后再去请求新数据
        JSONObject cacheResult = getResultFromCache(params);
        if (cacheResult != null && callback != null) {
            callback.onSuccess(cacheResult);
        }

        m_handler = new Handler(looper) {
            @Override
            public void handleMessage(Message msg) {
                switch (msg.what) {
                    case HTTP_SUCCESS: {
                        JSONObject myJsonObject = (JSONObject) msg.obj;
                        saveResultToCache(params, myJsonObject);
                        callback.onSuccess(myJsonObject);
                    }
                    break;

                    case HTTP_FAILED: {
                        String errmsg = (String) msg.obj;
                        callback.onFailure(errmsg);
                    }
                    break;

                    case HTTP_TIMEOUT: {
                        callback.onTimeOut();
                    }
                    break;
                }
            }
        };

        DefaultRestfulClient.inst().get(url, params, new AsyncHttpResponseHandler(looper) {

            @Override
            public void onSuccess(int statusCode, Header[] headers, byte[] responseBody) {
                try {
                    //将字符串转换成jsonObject对象
                    String resp = new String(responseBody);
                    JSONObject myJsonObject = new JSONObject(resp);

                    //发送消息
                    Message msg = new Message();
                    msg.what = HTTP_SUCCESS;
                    msg.obj = myJsonObject;
                    m_handler.sendMessage(msg);
                } catch (JSONException e) {
                    //发送消息
                    Message msg = new Message();
                    msg.what = HTTP_FAILED;
                    msg.obj = e.getMessage();
                    m_handler.sendMessage(msg);
                }
            }

            @Override
            public void onFailure(int statusCode, Header[] headers, byte[] responseBody, Throwable error) {
                try {
                    if (error instanceof SocketTimeoutException) {
                        //发送消息
                        Message msg = new Message();
                        msg.what = HTTP_TIMEOUT;
                        m_handler.sendMessage(msg);
                    } else if (error instanceof ConnectTimeoutException) {
                        //发送消息
                        Message msg = new Message();
                        msg.what = HTTP_TIMEOUT;
                        m_handler.sendMessage(msg);
                    } else {
                        //发送消息
                        Message msg = new Message();
                        msg.what = HTTP_FAILED;
                        msg.obj = error.getMessage();
                        m_handler.sendMessage(msg);
                    }
                } catch (Exception e) {
                    //发送消息
                    Message msg = new Message();
                    msg.what = HTTP_FAILED;
                    msg.obj = e.getMessage();
                    m_handler.sendMessage(msg);
                }
            }
        });
    }
}