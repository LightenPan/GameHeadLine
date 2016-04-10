package com.flylighten.app.gameheadline.model;

import com.flylighten.app.gameheadline.data.NewsListItem;
import com.flylighten.app.gameheadline.event.LatestNewsListDataEvent;
import com.flylighten.app.gameheadline.request.API;
import com.flylighten.app.gameheadline.request.CommCacheRequest;

import java.util.ArrayList;

import in.srain.cube.request.CacheAbleRequest;
import in.srain.cube.request.CacheAbleRequestDefaultHandler;
import in.srain.cube.request.JsonData;
import in.srain.cube.request.RequestData;

/**
 * Created by Administrator on 2016/3/30.
 */
public class LatestNewsListDataModel {

    private int mCurPageIndex = 0;
    private int mPageCount = 10;

    public static interface DataHandler {
        public void onData(LatestNewsListDataEvent event, CacheAbleRequest.ResultType type, boolean outOfDate);
    }

    public void queryFirstPage(final DataHandler handler) {
        CommCacheRequest<LatestNewsListDataEvent> request =
                new CommCacheRequest<LatestNewsListDataEvent>(new CacheAbleRequestDefaultHandler<LatestNewsListDataEvent>() {

                    @Override
                    public LatestNewsListDataEvent processOriginData(JsonData jsonData) {
                        LatestNewsListDataEvent event = new LatestNewsListDataEvent();
                        event.dataList = new ArrayList<>();

                        JsonData array = jsonData.optJson("news_list");
                        for (int i = 0; i < array.length(); ++i) {
                            JsonData obj = array.optJson(i);
                            NewsListItem item = new NewsListItem();
                            item.source = obj.optString("source");
                            item.updated = obj.optString("updated");
                            item.title = obj.optString("title");
                            item.linkmd5id = obj.optString("linkmd5id");
                            item.link = obj.optString("link");
                            item.date = obj.optString("date");
                            item.digest = obj.optString("digest");
                            event.dataList.add(item);
                        }

                        event.pageIndex = mCurPageIndex;
                        event.isEmpty = event.dataList.isEmpty();
                        if (array.length() >= mPageCount) {
                            event.hasMore = true;
                        }
                        mCurPageIndex = mCurPageIndex + 1;
                        return event;
                    }

                    @Override
                    public void onCacheAbleRequestFinish(LatestNewsListDataEvent data, CacheAbleRequest.ResultType type, boolean outOfDate) {
                        handler.onData(data, type, outOfDate);
                    }
                });

        mCurPageIndex = 0;
        RequestData requestData = request.getRequestData();
        requestData.addQueryData("offset", mCurPageIndex*mPageCount);
        requestData.addQueryData("count", mPageCount);
        request.getRequestData().setRequestUrl(API.GetDigestList);
        request.send();
    }

    public void queryNextPage(final DataHandler handler) {
        CommCacheRequest<LatestNewsListDataEvent> request =
                new CommCacheRequest<LatestNewsListDataEvent>(new CacheAbleRequestDefaultHandler<LatestNewsListDataEvent>() {
                    @Override
                    public LatestNewsListDataEvent processOriginData(JsonData jsonData) {
                        LatestNewsListDataEvent event = new LatestNewsListDataEvent();
                        event.dataList = new ArrayList<>();

                        JsonData array = jsonData.optJson("news_list");
                        for (int i = 0; i < array.length(); ++i) {
                            JsonData obj = array.optJson(i);
                            NewsListItem item = new NewsListItem();
                            item.source = obj.optString("source");
                            item.updated = obj.optString("updated");
                            item.title = obj.optString("title");
                            item.linkmd5id = obj.optString("linkmd5id");
                            item.link = obj.optString("link");
                            item.date = obj.optString("date");
                            item.digest = obj.optString("digest");
                            event.dataList.add(item);
                        }

                        event.pageIndex = mCurPageIndex;
                        event.isEmpty = event.dataList.isEmpty();
                        if (array.length() >= mPageCount) {
                            event.hasMore = true;
                            event.isEmpty = false;
                        } else {
                            event.hasMore = false;
                            event.isEmpty = true;
                        }
                        mCurPageIndex = mCurPageIndex + 1;
                        return event;
                    }

                    @Override
                    public void onCacheAbleRequestFinish(LatestNewsListDataEvent data, CacheAbleRequest.ResultType type, boolean outOfDate) {
                        handler.onData(data, type, outOfDate);
                    }
                });

        RequestData requestData = request.getRequestData();
        requestData.addQueryData("offset", mCurPageIndex*mPageCount);
        requestData.addQueryData("count", mPageCount);
        request.getRequestData().setRequestUrl(API.GetDigestList);
        request.send();
    }
}
