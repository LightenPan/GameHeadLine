package com.flylighten.app.gameheadline.data;

import android.widget.Toast;

//import com.flylighten.app.gameheadline.MyApplication;
import com.flylighten.app.gameheadline.model.NewsListItemModel;
import com.flylighten.app.gameheadline.restful.BaseNetTool;
import com.flylighten.app.gameheadline.restful.RequestCallback;
import com.loopj.android.http.RequestParams;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

public class DataUtils {


    public static List<NewsListItemModel> getExampleList() {
        List<NewsListItemModel> lst = new ArrayList<NewsListItemModel>();

        NewsListItemModel item = new NewsListItemModel();
        item.source = 1;
        item.title = "2016-3-30 01:29:59";
        item.title = "标题";
        item.digest = "摘要";
        item.other = "其他";
        lst.add(item);

        item = new NewsListItemModel();
        item.source = 1;
        item.title = "2016-3-30 01:29:59";
        item.title = "标题";
        item.digest = "摘要";
        item.other = "其他";
        lst.add(item);

        item = new NewsListItemModel();
        item.source = 1;
        item.title = "2016-3-30 01:29:59";
        item.title = "标题";
        item.digest = "摘要";
        item.other = "其他";
        lst.add(item);

        item = new NewsListItemModel();
        item.source = 1;
        item.title = "2016-3-30 01:29:59";
        item.title = "标题";
        item.digest = "摘要";
        item.other = "其他";
        lst.add(item);

        item = new NewsListItemModel();
        item.source = 1;
        item.title = "2016-3-30 01:29:59";
        item.title = "标题";
        item.digest = "摘要";
        item.other = "其他";
        lst.add(item);

        return lst;
    }
}
