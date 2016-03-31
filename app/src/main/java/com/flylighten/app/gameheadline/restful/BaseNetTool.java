package com.flylighten.app.gameheadline.restful;

import android.os.Looper;
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

        DefaultRestfulClient.inst().get(url, params, new AsyncHttpResponseHandler(looper) {

            @Override
            public void onSuccess(int statusCode, Header[] headers, byte[] responseBody) {
                try {
                    //将字符串转换成jsonObject对象
                    String resp = responseBody.toString();
                    JSONObject myJsonObject = new JSONObject(resp);
                    saveResultToCache(params, myJsonObject);
                    callback.onSuccess(myJsonObject);
                } catch (JSONException e) {
                    callback.onFailure(statusCode, e.getMessage());
                }
            }

            @Override
            public void onFailure(int statusCode, Header[] headers, byte[] responseBody, Throwable error) {
                try {
                    if (error instanceof SocketTimeoutException) {
                        callback.onTimeOut();
                    } else if (error instanceof ConnectTimeoutException) {
                        callback.onTimeOut();
                    } else {
                        callback.onFailure(statusCode, error.getMessage());
                    }
                } catch (Exception e) {
                    callback.onFailure(statusCode, e.getMessage());
                }
            }
        });

//        DefaultRestfulClient.inst().get(url, params, new JsonHttpResponseHandler() {
//
//            @Override
//            public void onSuccess(int statusCode, Header[] headers, JSONArray response) {
////                AsyncHttpClient.log.w("BaseNetTool", "onSuccess(int, Header[], JSONArray). responseString: " + response.toString());
//                callback.onFailure(statusCode, response.toString());
//            }
//
//            @Override
//            public void onFailure(int statusCode, Header[] headers, Throwable throwable, JSONObject errorResponse) {
////                AsyncHttpClient.log.w("BaseNetTool", "onSuccess(int, Header[], Throwable, JSONObject). throwable: " + throwable.getMessage() + ", errorResponse: " + errorResponse.toString());
//                try {
//                    if (throwable instanceof SocketTimeoutException) {
//                        callback.onTimeOut();
//                    } else if (throwable instanceof ConnectTimeoutException) {
//                        callback.onTimeOut();
//                    } else {
//                        callback.onFailure(statusCode, throwable.getMessage());
//                    }
//                } catch (Exception e) {
//                    callback.onFailure(statusCode, e.getMessage());
//                }
//            }
//
//            @Override
//            public void onFailure(int statusCode, Header[] headers, String responseString, Throwable throwable) {
////                AsyncHttpClient.log.w("BaseNetTool", "onFailure. responseString: " + responseString + ", message: " + throwable.getMessage());
//
//                Toast.makeText(MyApplication.getContext(), responseString, Toast.LENGTH_LONG).show();
//                if (throwable instanceof SocketTimeoutException) {
//                    callback.onTimeOut();
//                } else {
//                    callback.onFailure(statusCode, throwable.getMessage());
//                }
//            }
//
//            @Override
//            public void onSuccess(int statusCode, Header[] headers, String responseString) {
//                AsyncHttpClient.log.w("BaseNetTool", "onSuccess");
//                try {
//                    //将字符串转换成jsonObject对象
//                    JSONObject myJsonObject = new JSONObject(responseString);
//                    saveResultToCache(params, myJsonObject);
//                    callback.onSuccess(myJsonObject);
//                } catch (JSONException e) {
//                    callback.onFailure(statusCode, e.getMessage());
//                }
//
//            }
//        });
    }

    public void post(final Looper looper, final String url, final RequestParams params, final RequestCallback callback) {

        //如果有缓冲，先返回缓存数据，然后再去请求新数据
        JSONObject cacheResult = getResultFromCache(params);
        if (cacheResult != null && callback != null) {
            callback.onSuccess(cacheResult);
        }


        DefaultRestfulClient.inst().get(url, params, new AsyncHttpResponseHandler(looper) {
            @Override
            public void onSuccess(int statusCode, Header[] headers, byte[] responseBody) {
                try {
                    //将字符串转换成jsonObject对象
                    String resp = responseBody.toString();
                    JSONObject myJsonObject = new JSONObject(resp);
                    saveResultToCache(params, myJsonObject);
                    callback.onSuccess(myJsonObject);
                } catch (JSONException e) {
                    callback.onFailure(statusCode, e.getMessage());
                }
            }

            @Override
            public void onFailure(int statusCode, Header[] headers, byte[] responseBody, Throwable error) {
                try {
                    if (error instanceof SocketTimeoutException) {
                        callback.onTimeOut();
                    } else if (error instanceof ConnectTimeoutException) {
                        callback.onTimeOut();
                    } else {
                        callback.onFailure(statusCode, error.getMessage());
                    }
                } catch (Exception e) {
                    callback.onFailure(statusCode, e.getMessage());
                }
            }
        });

//        DefaultRestfulClient.inst().post(url, params, new JsonHttpResponseHandler() {
//
//            public void onSuccess(int statusCode, Header[] headers, JSONObject response) {
//                AsyncHttpClient.log.w("JsonHttpRH", "onSuccess(int, Header[], JSONObject) was not overriden, but callback was received");
//                saveResultToCache(params, response);
//                callback.onSuccess(response);
//            }
//
//            public void onFailure(int statusCode, Header[] headers, Throwable throwable, JSONObject errorResponse) {
//                AsyncHttpClient.log.w("JsonHttpRH", "onFailure(int, Header[], Throwable, JSONObject) was not overriden, but callback was received", throwable);
//
//                if (throwable instanceof SocketTimeoutException) {
//                    callback.onTimeOut();
//                } else {
//                    callback.onFailure(statusCode, throwable.getMessage());
//                }
//            }
//        });
    }
}