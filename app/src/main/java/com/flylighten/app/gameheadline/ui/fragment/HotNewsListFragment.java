package com.flylighten.app.gameheadline.ui.fragment;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ListView;

import com.flylighten.app.gameheadline.R;
import com.flylighten.app.gameheadline.data.DuoShuoCountsItem;
import com.flylighten.app.gameheadline.data.NewsListItem;
import com.flylighten.app.gameheadline.model.DuoShuoCountsDataModel;
import com.flylighten.app.gameheadline.model.HotNewsListDataModel;
import com.flylighten.app.gameheadline.ui.activity.NewsDetailActivity;
import com.flylighten.app.gameheadline.ui.adapter.NewsListAdapter;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import in.srain.cube.app.CubeFragment;
import in.srain.cube.request.CacheAbleRequest;
import in.srain.cube.views.ptr.PtrDefaultHandler;
import in.srain.cube.views.ptr.PtrFrameLayout;
import in.srain.cube.views.ptr.PtrHandler;

/**
 * Created by Administrator on 2016/4/2.
 */
public class HotNewsListFragment extends Fragment {

    private static final String TAG = "HotNewsListFragment";

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        ViewGroup view = (ViewGroup) inflater.inflate(R.layout.fragment_news_list, null);
        initNewsList(view);
        return view;
    }

    public enum HOT_TYPE {
        HOT_TYPE_WEEKLY,
        HOT_TYPE_MONTHLY,
    };

    private HOT_TYPE mType = HOT_TYPE.HOT_TYPE_WEEKLY;

    public void set_type(HOT_TYPE type) {
        mType = type;
    }

    ArrayList<NewsListItem> mNewsListData = new ArrayList<NewsListItem>();
    NewsListAdapter mAdapter;
    public ListView mListView;
    public PtrFrameLayout mPtrFrame;

    private void initNewsList(View view) {

        ////////////////////////////////////////////////////////////////////////////////////
        // 绑定视图与数据
        mAdapter = new NewsListAdapter(view.getContext(), R.layout.news_list_item, mNewsListData);
        mListView = (ListView) view.findViewById(R.id.news_list_list_view);
        mListView.setAdapter(mAdapter);
        ////////////////////////////////////////////////////////////////////////////////////


        ////////////////////////////////////////////////////////////////////////////////////
        //处理点击事件
        mListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                NewsListItem item = mNewsListData.get(position);
                Intent intent = new Intent();
                intent.putExtra("linkmd5id", item.linkmd5id);
                intent.putExtra("title", item.title);
                intent.setClass(getContext(), NewsDetailActivity.class);
                startActivity(intent);
            }
        });
        ////////////////////////////////////////////////////////////////////////////////////

        ////////////////////////////////////////////////////////////////////////////////////
        // 初始化下拉刷新组件
        mPtrFrame = (PtrFrameLayout) view.findViewById(R.id.ptr_frame_news_list);
        mPtrFrame.setLoadingMinTime(200);
        mPtrFrame.setPtrHandler(new PtrHandler() {
            @Override
            public boolean checkCanDoRefresh(PtrFrameLayout frame, View content, View header) {
                return PtrDefaultHandler.checkContentCanBePulledDown(frame, mListView, header);
            }

            @Override
            public void onRefreshBegin(PtrFrameLayout frame) {

                switch (mType) {
                    case HOT_TYPE_WEEKLY:
                    default:
                        HotNewsListDataModel.query("weekly", nDataHandler);
                        break;

                    case HOT_TYPE_MONTHLY:
                        HotNewsListDataModel.query("monthly", nDataHandler);
                        break;
                }
            }
        });

        //自动加载第一页
        mPtrFrame.postDelayed(new Runnable() {
            @Override
            public void run() {
                mPtrFrame.autoRefresh(false);
            }
        }, 150);
        ////////////////////////////////////////////////////////////////////////////////////
    }

    //拉取数据回调处理
    private HotNewsListDataModel.DataHandler nDataHandler = new HotNewsListDataModel.DataHandler() {

        @Override
        public void onData(List<NewsListItem> Counts, CacheAbleRequest.ResultType type, boolean outOfDate) {
            //清空现有数据
            mNewsListData.clear();

            //添加新数据
            mNewsListData.addAll(Counts);

            //更新界面
            mAdapter.notifyDataSetChanged();

            //允许下拉刷新
            mPtrFrame.refreshComplete();


            //拉取评论数
            ArrayList<String> linkmd5idllist = new ArrayList<String>();
            for (int i = 0; i < mNewsListData.size(); ++i) {
                linkmd5idllist.add(mNewsListData.get(i).linkmd5id);
            }
            DuoShuoCountsDataModel.query(linkmd5idllist, new DuoShuoCountsDataModel.DataHandler() {

                @Override
                public void onData(HashMap<String, DuoShuoCountsItem> Counts, CacheAbleRequest.ResultType type, boolean outOfDate) {

                    //更新评论数、转发数、阅读数、点赞数、踩
                    for (int i = 0; i < mNewsListData.size(); ++i) {
                        DuoShuoCountsItem item = Counts.get(mNewsListData.get(i).linkmd5id);
                        if (null != item) {
                            mNewsListData.get(i).comments = item.comments;
                            mNewsListData.get(i).reposts = item.reposts;
                            mNewsListData.get(i).views = item.views;
                            mNewsListData.get(i).likes = item.likes;
                            mNewsListData.get(i).dislikes = item.dislikes;
                        }
                    }

                    //更新界面
                    mAdapter.notifyDataSetChanged();
                }
            });
        }
    };
}
