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
        TextView other;
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
            viewHolder.other = (TextView) view.findViewById(R.id.other);

            view.setTag(viewHolder);
        } else {
            view = convertView;
            viewHolder = (ViewHolder) view.getTag();
        }

        viewHolder.title.setText(item.title);
        viewHolder.date.setText(item.date);
        viewHolder.title.setText(item.title);
        viewHolder.digest.setText(item.digest);

        return view;
    }
}
