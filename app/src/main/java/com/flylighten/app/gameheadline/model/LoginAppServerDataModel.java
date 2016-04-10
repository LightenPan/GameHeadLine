package com.flylighten.app.gameheadline.model;

import com.flylighten.app.gameheadline.event.LoginAppServerDataEvent;
import com.flylighten.app.gameheadline.request.API;
import com.flylighten.app.gameheadline.request.CommCacheRequest;

import in.srain.cube.request.CacheAbleRequest;
import in.srain.cube.request.CacheAbleRequestDefaultHandler;
import in.srain.cube.request.FailData;
import in.srain.cube.request.JsonData;
import in.srain.cube.request.RequestData;

/**
 * Created by Administrator on 2016/3/30.
 */
public class LoginAppServerDataModel {

    public static interface DataHandler {
        public void onData(LoginAppServerDataEvent event, CacheAbleRequest.ResultType type, boolean outOfDate);
        public void onNetFail();
    }

    public static void doPost(String openappid, String access_token, final DataHandler handler) {
        CommCacheRequest<LoginAppServerDataEvent> request =
                new CommCacheRequest<LoginAppServerDataEvent>(new CacheAbleRequestDefaultHandler<LoginAppServerDataEvent>() {

                    @Override
                    public void onRequestFail(FailData failData) {
                        super.onRequestFail(failData);

                        if (FailData.ERROR_NETWORK == failData.getErrorType()) {
                            handler.onNetFail();
                        }
                    }

                    @Override
                    public LoginAppServerDataEvent processOriginData(JsonData jsonData) {
                        LoginAppServerDataEvent event = new LoginAppServerDataEvent();

                        event.action_status = jsonData.optInt("ActionStatus");
                        event.error_code = jsonData.optInt("ErrorCode");
                        event.error_info = jsonData.optString("ErrorInfo");
                        event.uid = jsonData.optString("uid");
                        event.token = jsonData.optString("token");
                        event.duoshuo_token = jsonData.optString("duoshuo_token");
                        event.expire_time = jsonData.optInt("expire_time");

                        return event;
                    }

                    @Override
                    public void onCacheAbleRequestFinish(LoginAppServerDataEvent data, CacheAbleRequest.ResultType type, boolean outOfDate) {
                        handler.onData(data, type, outOfDate);
                    }
                });

        RequestData requestData = request.getRequestData();
        requestData.addPostData("openappid", openappid);
        requestData.addPostData("access_token", access_token);
        request.getRequestData().setRequestUrl(API.LoginAppServer);
        request.send();
    }
}
