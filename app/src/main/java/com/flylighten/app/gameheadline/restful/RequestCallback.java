package com.flylighten.app.gameheadline.restful;

import org.json.JSONObject;

/**
 * Created by lightenpan on 2016/3/30.
 */
public abstract class RequestCallback {
    protected abstract void onSuccess(JSONObject resp);
    protected abstract void onFailure(int statusCode, String message);
    protected abstract void onTimeOut();
}
