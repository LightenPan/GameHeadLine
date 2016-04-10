package com.flylighten.app.gameheadline.model;

import com.flylighten.app.gameheadline.data.NewsListItem;
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
public class HotNewsListDataModel {

    public static interface DataHandler {
        public void onData(List<NewsListItem> Counts, CacheAbleRequest.ResultType type, boolean outOfDate);
    }

    public static void query(String range, final DataHandler handler) {
        CommCacheRequest<ArrayList<String>> request =
                new CommCacheRequest<ArrayList<String>>(new CacheAbleRequestDefaultHandler<ArrayList<String>>() {
                    @Override
                    public void onCacheAbleRequestFinish(final ArrayList<String> data, CacheAbleRequest.ResultType type, boolean outOfDate) {

                        CommCacheRequest<List<NewsListItem>> request =
                                new CommCacheRequest<List<NewsListItem>>(new CacheAbleRequestDefaultHandler<List<NewsListItem>>() {
                                    @Override
                                    public List<NewsListItem> processOriginData(JsonData jsonData) {
                                        List<NewsListItem> dataList = new ArrayList<NewsListItem>();

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
                                        return dataList;
                                    }

                                    @Override
                                    public void onCacheAbleRequestFinish(List<NewsListItem> data, CacheAbleRequest.ResultType type, boolean outOfDate) {
                                        handler.onData(data, type, outOfDate);
                                    }
                                });


                        //组批量请求包
                        String listString = new String();
                        for (int i = 0; i < data.size(); ++i) {
                            if (0 == i) {
                                listString = listString + data.get(i);
                            } else {
                                listString = listString + "," + data.get(i);
                            }
                        }
                        RequestData requestData = request.getRequestData();
                        requestData.addQueryData("linkmd5idlist", listString);
                        request.getRequestData().setRequestUrl(API.GetDigestListByIdList);
                        request.send();
                    }

                    @Override
                    public ArrayList<String> processOriginData(JsonData jsonData) {
                        ArrayList<String> linkMd5IdList = new ArrayList<String>();
                        JsonData array = jsonData.optJson("response");
                        for (int i = 0; i < array.length(); ++i) {
                            JsonData obj = array.optJson(i);
                            String linkmd5id = obj.optString("thread_key");
                            linkMd5IdList.add(linkmd5id);
                        }
                        return linkMd5IdList;
                    }
                });

        RequestData requestData = request.getRequestData();
        requestData.addQueryData("short_name", "gameheadline");
        requestData.addQueryData("num_items", 20);
        requestData.addQueryData("range", range);
        request.getRequestData().setRequestUrl(API.GetDuoShouTopList);
        request.send();
    }
}
