package com.flylighten.app.gameheadline.ui.adapter;


import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.flylighten.app.gameheadline.R;
import com.flylighten.app.gameheadline.data.NewsListItem;

import java.util.List;

/**
 * Created by Administrator on 2016/3/30.
 */
public class NewsListAdapter extends BaseAdapter {

    private Context mContext;
    private int mResource;
    private List<NewsListItem> mDataList;

    class ViewHolder {
        TextView source;
        TextView date;
        TextView title;
        TextView digest;
        TextView comments;
        TextView reposts;
        TextView views;
        TextView likes;
        TextView dislikes;
    }

    public NewsListAdapter(Context context, int resource,
                           List<NewsListItem> dataList) {
        mContext = context;
        mResource = resource;
        this.mDataList = dataList;
    }

    @Override
    public int getCount() {
        return mDataList.size();
    }

    @Override
    public Object getItem(int position) {
        return mDataList.get(position);
    }

    @Override
    public long getItemId(int position) {
        return 0;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        NewsListItem item = (NewsListItem) getItem(position);
        View view;
        ViewHolder viewHolder;
        if (convertView == null) {
            view = LayoutInflater.from(mContext).inflate(mResource, null);
            viewHolder = new ViewHolder();
            viewHolder.source = (TextView) view.findViewById(R.id.source);
            viewHolder.date = (TextView) view.findViewById(R.id.date);
            viewHolder.title = (TextView) view.findViewById(R.id.title);
            viewHolder.digest = (TextView) view.findViewById(R.id.digest);

            viewHolder.comments = (TextView) view.findViewById(R.id.comments);
            viewHolder.reposts = (TextView) view.findViewById(R.id.reposts);
            viewHolder.views = (TextView) view.findViewById(R.id.views);
            viewHolder.likes = (TextView) view.findViewById(R.id.likes);
            viewHolder.dislikes = (TextView) view.findViewById(R.id.dislikes);

            view.setTag(viewHolder);
        } else {
            view = convertView;
            viewHolder = (ViewHolder) view.getTag();
        }

        viewHolder.title.setText(item.title);
        viewHolder.date.setText(item.date);
        viewHolder.title.setText(item.title);
        viewHolder.digest.setText(item.digest);
        viewHolder.source.setText("来自:" + item.source);

        viewHolder.comments.setText("评论:" + item.comments);
        viewHolder.reposts.setText("转发:" + item.reposts);
        viewHolder.views.setText("阅读:" + item.views);
        viewHolder.likes.setText("赞:" + item.likes);
        viewHolder.dislikes.setText("踩:" + item.dislikes);

        return view;
    }
}
