package com.flylighten.app.gameheadline.model;

import com.flylighten.app.gameheadline.data.DuoShuoCountsItem;
import com.flylighten.app.gameheadline.request.API;
import com.flylighten.app.gameheadline.request.CommCacheRequest;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;

import in.srain.cube.request.CacheAbleRequest;
import in.srain.cube.request.CacheAbleRequestDefaultHandler;
import in.srain.cube.request.JsonData;
import in.srain.cube.request.RequestData;

/**
 * Created by Administrator on 2016/3/30.
 */
public class DuoShuoCountsDataModel {

    public static interface DataHandler {
        public void onData(HashMap<String, DuoShuoCountsItem> Counts, CacheAbleRequest.ResultType type, boolean outOfDate);
    }

    public static void query(List<String> lindmd5idlist, final DataHandler handler) {
        CommCacheRequest<HashMap<String, DuoShuoCountsItem>> request =
                new CommCacheRequest<HashMap<String, DuoShuoCountsItem>>(
                        new CacheAbleRequestDefaultHandler<HashMap<String, DuoShuoCountsItem>>() {
                    @Override
                    public HashMap<String, DuoShuoCountsItem> processOriginData(JsonData jsonData) {
                        HashMap<String, DuoShuoCountsItem> dataList = new HashMap<String, DuoShuoCountsItem>();

                        JsonData array = jsonData.optJson("response");
                        Iterator<String> it = array.keys();
                        while (it.hasNext()) {
                            String key = (String) it.next();
                            JsonData obj = array.optJson(key);
                            DuoShuoCountsItem item = new DuoShuoCountsItem();
                            item.linkmd5id = obj.optString("thread_key");
                            item.comments = obj.optInt("comments");
                            item.reposts = obj.optInt("reposts");
                            item.views = obj.optInt("views");
                            item.likes = obj.optInt("likes");
                            item.dislikes = obj.optInt("dislikes");

                            dataList.put(item.linkmd5id, item);
                        }

                        return dataList;
                    }

                    @Override
                    public void onCacheAbleRequestFinish(HashMap<String, DuoShuoCountsItem> data, CacheAbleRequest.ResultType type, boolean outOfDate) {
                        handler.onData(data, type, outOfDate);
                    }
                });

        //组批量请求包
        String listString = new String();
        for (int i = 0; i < lindmd5idlist.size(); ++i) {
            if (0 == i) {
                listString = listString + lindmd5idlist.get(i);
            } else {
                listString = listString + "," + lindmd5idlist.get(i);
            }
        }

        RequestData requestData = request.getRequestData();
        requestData.addQueryData("short_name", "gameheadline");
        requestData.addQueryData("threads", listString);
        request.getRequestData().setRequestUrl(API.GetDuoShouCounts);
        request.send();
    }
}
