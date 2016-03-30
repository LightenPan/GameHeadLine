package com.flylighten.app.gameheadline.restful;

import android.widget.Toast;

import com.flylighten.app.gameheadline.MyApplication;
import com.loopj.android.http.AsyncHttpClient;
import com.loopj.android.http.JsonHttpResponseHandler;
import com.loopj.android.http.RequestParams;

import org.apache.http.Header;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.net.SocketTimeoutException;

public class BaseNetTool {

    protected JSONObject getResultFromCache(RequestParams params){
        return null;
    }

    protected void saveResultToCache(RequestParams params, JSONObject msg){

    }

    public void get(final String url, final RequestParams params, final RequestCallback callback) {

        //如果有缓冲，先返回缓存数据，然后再去请求新数据
        JSONObject cacheResult = getResultFromCache(params);
        if (cacheResult != null && callback != null){
            callback.onSuccess(cacheResult);
        }

        DefaultRestfulClient.inst().get(url, params, new JsonHttpResponseHandler() {

            @Override
            public void onFailure(int statusCode, Header[] headers, String responseString, Throwable throwable) {
                AsyncHttpClient.log.w("BaseNetTool", "onFailure. responseString: " + responseString + ", message: " + throwable.getMessage());

                Toast.makeText(MyApplication.getContext(), responseString, Toast.LENGTH_LONG).show();
                if (throwable instanceof SocketTimeoutException) {
                    callback.onTimeOut();
                } else {
                    callback.onFailure(statusCode, throwable.getMessage());
                }
            }

            @Override
            public void onSuccess(int statusCode, Header[] headers, String responseString) {
                AsyncHttpClient.log.w("BaseNetTool", "onSuccess");
                try
                {
                    //将字符串转换成jsonObject对象
                    JSONObject myJsonObject = new JSONObject(responseString);
                    saveResultToCache(params, myJsonObject);
                    callback.onSuccess(myJsonObject);
                }
                catch (JSONException e)
                {
                    callback.onFailure(statusCode, e.getMessage());
                }

            }
        });
    }

    public void post(final String url, final RequestParams params, final RequestCallback callback) {

        //如果有缓冲，先返回缓存数据，然后再去请求新数据
        JSONObject cacheResult = getResultFromCache(params);
        if (cacheResult != null && callback != null){
            callback.onSuccess(cacheResult);
        }

        DefaultRestfulClient.inst().post(url, params, new JsonHttpResponseHandler() {

            public void onSuccess(int statusCode, Header[] headers, JSONObject response) {
                AsyncHttpClient.log.w("JsonHttpRH", "onSuccess(int, Header[], JSONObject) was not overriden, but callback was received");
                saveResultToCache(params, response);
                callback.onSuccess(response);
            }

            public void onFailure(int statusCode, Header[] headers, Throwable throwable, JSONObject errorResponse) {
                AsyncHttpClient.log.w("JsonHttpRH", "onFailure(int, Header[], Throwable, JSONObject) was not overriden, but callback was received", throwable);

                if (throwable instanceof SocketTimeoutException) {
                    callback.onTimeOut();
                } else {
                    callback.onFailure(statusCode, throwable.getMessage());
                }
            }
        });
    }
}