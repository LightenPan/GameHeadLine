package com.flylighten.app.gameheadline.protocol;

import android.os.Looper;
import android.widget.Toast;

import com.flylighten.app.gameheadline.MyApplication;
import com.flylighten.app.gameheadline.model.NewsListItemModel;
import com.flylighten.app.gameheadline.restful.BaseNetTool;
import com.flylighten.app.gameheadline.restful.DefaultRestfulClient;
import com.flylighten.app.gameheadline.restful.RequestCallback;
import com.loopj.android.http.AsyncHttpResponseHandler;
import com.loopj.android.http.JsonHttpResponseHandler;
import com.loopj.android.http.RequestParams;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

public class GetNewsListProxy {

    public static abstract class Callback {
        protected abstract void onSuccess(List<NewsListItemModel> newsList);
        protected abstract void onFailure(String message);
    }

    public static void doNet(Looper looper, int offset, int count, final GetNewsListProxy.Callback callback) {
        final BaseNetTool oNetTool = new BaseNetTool();
        final String url = "http://23.251.58.227/jsonapi/duowan/GetDigestList";
        RequestParams params = new RequestParams();
        params.put("offset", offset);
        params.put("count", count);

        oNetTool.get(looper, url, params, new RequestCallback(url) {
            @Override
            protected void onSuccess(JSONObject resp) {
                try {
                    int result = resp.getInt("result");
                    JSONArray array = resp.getJSONArray("news_list");
                    if (0 == result && array.length() > 0) {
                        final List<NewsListItemModel> newsList = new ArrayList<NewsListItemModel>();
                        for (int i = 0; i < array.length(); ++i) {
                            JSONObject obj = array.getJSONObject(i);
                            NewsListItemModel item = new NewsListItemModel();
                            item.source = 0;
                            item.date = obj.getString("date");
                            item.title = obj.getString("title");
                            item.digest = obj.getString("title");
//                            item.other = obj.getString("content");
                            item.other = "";
                            newsList.add(item);
                        }
                        callback.onSuccess(newsList);
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                    callback.onFailure(e.getMessage());
                }
            }
        });
    }
}