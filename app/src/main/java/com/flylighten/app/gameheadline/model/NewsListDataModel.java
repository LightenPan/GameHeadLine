package com.flylighten.app.gameheadline.model;

import com.flylighten.app.gameheadline.protocol.ProxyGetNewsList;

import java.util.List;

/**
 * Created by Administrator on 2016/3/30.
 */
public class NewsListDataModel {
    class Item {
        public int source;
        public String date;
        public String title;
        public String digest;
        public String other;
    }

    private int mCurPageIndex = 0;
    private int mPageCount = 10;
    private List<NewsListItemModel> mDataList;

    public void queryFirstPage() {

        ProxyGetNewsList proxy = new ProxyGetNewsList();
        proxy.doNet(0, mPageCount, new ProxyGetNewsList.Callback() {

            @Override
            protected void onSuccess(List<NewsListItemModel> newsList) {
                mDataList.clear();
                mDataList.addAll(newsList);
            }
        });
    }

    public void queryNextPage() {

    }
}
