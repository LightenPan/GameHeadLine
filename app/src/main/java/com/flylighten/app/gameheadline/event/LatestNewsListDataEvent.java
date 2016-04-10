package com.flylighten.app.gameheadline.event;

import com.flylighten.app.gameheadline.data.NewsListItem;

import java.util.List;

public class LatestNewsListDataEvent {

    public int pageIndex;
    public boolean hasMore;
    public boolean isEmpty;
    public List<NewsListItem> dataList;
}