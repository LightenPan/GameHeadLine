package com.flylighten.app.gameheadline.protocol;

import android.os.Looper;
import android.os.Message;
import android.widget.Toast;

import com.flylighten.app.gameheadline.MyApplication;
import com.flylighten.app.gameheadline.model.NewsListItemModel;
import com.flylighten.app.gameheadline.restful.BaseNetTool;
import com.flylighten.app.gameheadline.restful.DefaultRestfulClient;
import com.flylighten.app.gameheadline.restful.RequestCallback;
import com.loopj.android.http.AsyncHttpResponseHandler;
import com.loopj.android.http.RequestParams;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

import cz.msebera.android.httpclient.Header;

public class ProxyGetNewsList {

    public static abstract class Callback {
        protected abstract void onSuccess(List<NewsListItemModel> newsList);
    }

    public static void doNet(int offset, int count, final ProxyGetNewsList.Callback callback) {
        final String url = "http://23.251.58.227/jsonapi/duowan/GetDigestList";
        RequestParams params = new RequestParams();
        params.put("offset", offset);
        params.put("count", count);

        DefaultRestfulClient.inst().get(url, params, new AsyncHttpResponseHandler() {
            @Override
            public void onSuccess(int statusCode, Header[] headers, byte[] responseBody) {
                try {
                    //将字符串转换成jsonObject对象
                    String response = new String(responseBody);
                    JSONObject resp = new JSONObject(response);
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
                            item.other = "";
                            newsList.add(item);
                        }
                        callback.onSuccess(newsList);
                    }
                } catch (JSONException e) {
//                    e.printStackTrace();
                    Toast.makeText(MyApplication.getContext(), e.getMessage(), Toast.LENGTH_LONG).show();
                }
            }

            @Override
            public void onFailure(int statusCode, Header[] headers, byte[] responseBody, Throwable error) {
                Toast.makeText(MyApplication.getContext(), error.getMessage(), Toast.LENGTH_LONG).show();
            }
        });
    }
}