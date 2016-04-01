package com.flylighten.app.gameheadline.restful;

import android.widget.Toast;

import com.flylighten.app.gameheadline.MyApplication;

import org.json.JSONObject;

/**
 * Created by lightenpan on 2016/3/30.
 */
public abstract class RequestCallback {
    protected String mUrl;
    public RequestCallback(String url) {
        mUrl = url;
    }
    protected abstract void onSuccess(JSONObject resp);
    protected void onFailure(String message) {
        String errmsg = "请求失败. url: " + mUrl + ", message: " + message;
        Toast.makeText(MyApplication.getContext(), errmsg, Toast.LENGTH_LONG).show();
    }
    protected void onTimeOut() {
        String errmsg = "请求超时. url: " + mUrl;
        Toast.makeText(MyApplication.getContext(), errmsg, Toast.LENGTH_LONG).show();
    }
}
