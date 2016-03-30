package com.flylighten.app.gameheadline.protocol;

import android.widget.Toast;

import com.flylighten.app.gameheadline.MyApplication;
import com.flylighten.app.gameheadline.model.NewsListItemModel;
import com.flylighten.app.gameheadline.restful.BaseNetTool;
import com.flylighten.app.gameheadline.restful.RequestCallback;
import com.loopj.android.http.RequestParams;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

public class GetNewsListProxy {

    public abstract class Callback {
        protected abstract void onSuccess(List<NewsListItemModel> newsList);
    }

    public static void doNet(int offset, int count, final GetNewsListProxy.Callback callback) {
        final BaseNetTool oNetTool = new BaseNetTool();
        final String url = "http://192.168.171.133/jsonapi/duowan/GetDigestList";
        RequestParams params = new RequestParams();
        params.put("offset", offset);
        params.put("count", count);
        oNetTool.get(url, params, new RequestCallback() {
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
                            //item.date = obj.getLong("date");
                            item.title = obj.getString("title");
                            item.digest = obj.getString("title");
                            item.other = obj.getString("content");
                            newsList.add(item);
                        }
                        callback.onSuccess(newsList);
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }

            @Override
            protected void onFailure(int statusCode, String message) {
                String errmsg = String.format("请求失败. url: %s, code: %d, message: %s", url, statusCode, message);
                Toast.makeText(MyApplication.getContext(), errmsg, Toast.LENGTH_LONG).show();
            }

            @Override
            protected void onTimeOut() {
                String errmsg = String.format("请求超时. url: %s", url);
                Toast.makeText(MyApplication.getContext(), errmsg, Toast.LENGTH_LONG).show();
            }
        });
    }
}