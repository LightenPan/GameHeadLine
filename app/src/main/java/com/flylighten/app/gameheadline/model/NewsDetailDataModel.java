package com.flylighten.app.gameheadline.model;

import com.flylighten.app.gameheadline.event.EventCenter;
import com.flylighten.app.gameheadline.event.NewsDetailDataEvent;
import com.flylighten.app.gameheadline.request.API;
import com.flylighten.app.gameheadline.request.CommCacheRequest;

import in.srain.cube.request.CacheAbleRequest;
import in.srain.cube.request.CacheAbleRequestDefaultHandler;
import in.srain.cube.request.JsonData;
import in.srain.cube.request.RequestData;

/**
 * Created by Administrator on 2016/3/30.
 */
public class NewsDetailDataModel {

    public void queryNewsDetail(String linkmd5id) {
        CommCacheRequest<NewsDetailDataEvent> request =
                new CommCacheRequest<NewsDetailDataEvent>(new CacheAbleRequestDefaultHandler<NewsDetailDataEvent>() {
                    @Override
                    public NewsDetailDataEvent processOriginData(JsonData jsonData) {
                        //获取成功
                        JsonData detail = jsonData.optJson("detail");
                        NewsDetailDataEvent event = new NewsDetailDataEvent();
                        event.updated = detail.optString("updated");
                        event.title = detail.optString("title");
                        event.linkmd5id = detail.optString("linkmd5id");
                        event.link = detail.optString("link");
                        event.date = detail.optString("date");
                        event.digest = detail.optString("digest");
                        event.content = detail.optString("content");
                        return event;
                    }

                    @Override
                    public void onCacheAbleRequestFinish(NewsDetailDataEvent data, CacheAbleRequest.ResultType type, boolean outOfDate) {
                        EventCenter.inst().post(data);
                    }
                });
        RequestData requestData = request.getRequestData();
        requestData.addQueryData("linkmd5id", linkmd5id);
        request.setCacheKey("linkmd5id_" + linkmd5id);
        request.getRequestData().setRequestUrl(API.GetDetail);
        request.send();
    }
}
