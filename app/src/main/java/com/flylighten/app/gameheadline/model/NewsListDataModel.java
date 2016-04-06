package com.flylighten.app.gameheadline.model;

import com.flylighten.app.gameheadline.data.NewsListItem;
import com.flylighten.app.gameheadline.event.EventCenter;
import com.flylighten.app.gameheadline.event.NewsListDataEvent;
import com.flylighten.app.gameheadline.request.API;
import com.flylighten.app.gameheadline.request.CommCacheRequest;

import java.util.ArrayList;
import java.util.List;

import in.srain.cube.request.CacheAbleRequest;
import in.srain.cube.request.CacheAbleRequestDefaultHandler;
import in.srain.cube.request.JsonData;
import in.srain.cube.request.RequestData;

/**
 * Created by Administrator on 2016/3/30.
 */
public class NewsListDataModel {

    private int mCurPageIndex = 0;
    private int mPageCount = 30;
    private List<NewsListItem> dataList = new ArrayList<NewsListItem>();

    public List<NewsListItem> getDataList() {
        return dataList;
    }


    public void queryFirstPage() {
        CommCacheRequest<NewsListDataEvent> request =
                new CommCacheRequest<NewsListDataEvent>(new CacheAbleRequestDefaultHandler<NewsListDataEvent>() {
                    @Override
                    public NewsListDataEvent processOriginData(JsonData jsonData) {
                        dataList.clear();

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
                            dataList.add(item);
                        }

                        //获取成功
                        NewsListDataEvent event = new NewsListDataEvent();
                        event.pageIndex = mCurPageIndex;
                        event.isEmpty = dataList.isEmpty();
                        if (array.length() >= mPageCount) {
                            event.hasMore = true;
                        }
                        mCurPageIndex = mCurPageIndex + 1;
                        return event;
                    }

                    @Override
                    public void onCacheAbleRequestFinish(NewsListDataEvent data, CacheAbleRequest.ResultType type, boolean outOfDate) {
                        EventCenter.inst().post(data);
                    }
                });

        mCurPageIndex = 0;
        request.setDisableCache(true);
        RequestData requestData = request.getRequestData();
        requestData.addQueryData("offset", mCurPageIndex*mPageCount);
        requestData.addQueryData("count", mPageCount);
        request.getRequestData().setRequestUrl(API.GetDigestList);
        request.send();
    }

    public void queryNextPage() {
        CommCacheRequest<NewsListDataEvent> request =
                new CommCacheRequest<NewsListDataEvent>(new CacheAbleRequestDefaultHandler<NewsListDataEvent>() {
                    @Override
                    public NewsListDataEvent processOriginData(JsonData jsonData) {
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
                            dataList.add(item);
                        }

                        //获取成功
                        NewsListDataEvent event = new NewsListDataEvent();
                        event.pageIndex = mCurPageIndex;
                        event.isEmpty = dataList.isEmpty();
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
                    public void onCacheAbleRequestFinish(NewsListDataEvent data, CacheAbleRequest.ResultType type, boolean outOfDate) {
                        EventCenter.inst().post(data);
                    }
                });

        request.setDisableCache(true);
        RequestData requestData = request.getRequestData();
        requestData.addQueryData("offset", mCurPageIndex*mPageCount);
        requestData.addQueryData("count", mPageCount);
        request.getRequestData().setRequestUrl(API.GetDigestList);
        request.send();
    }
}
