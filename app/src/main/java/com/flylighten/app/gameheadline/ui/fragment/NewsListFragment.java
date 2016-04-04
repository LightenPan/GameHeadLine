package com.flylighten.app.gameheadline.ui.fragment;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.Toast;

import com.flylighten.app.gameheadline.R;
import com.flylighten.app.gameheadline.base.AppTitleBaseFragment;
import com.flylighten.app.gameheadline.data.NewsListItem;
import com.flylighten.app.gameheadline.event.CommonEventHandler;
import com.flylighten.app.gameheadline.event.ErrorMessageDataEvent;
import com.flylighten.app.gameheadline.event.EventCenter;
import com.flylighten.app.gameheadline.event.NewsListDataEvent;
import com.flylighten.app.gameheadline.model.NewsListDataModel;
import com.flylighten.app.gameheadline.ui.activity.NewsDetailActivity;
import com.flylighten.app.gameheadline.ui.adapter.NewsListAdapter;

import org.greenrobot.eventbus.Subscribe;

import in.srain.cube.app.CubeFragment;
import in.srain.cube.app.ICubeFragment;
import in.srain.cube.app.lifecycle.IComponentContainer;
import in.srain.cube.app.lifecycle.LifeCycleComponent;
import in.srain.cube.app.lifecycle.LifeCycleComponentManager;
import in.srain.cube.mints.base.TitleHeaderBar;
import in.srain.cube.util.CLog;
import in.srain.cube.views.loadmore.LoadMoreContainer;
import in.srain.cube.views.loadmore.LoadMoreHandler;
import in.srain.cube.views.loadmore.LoadMoreListViewContainer;
import in.srain.cube.views.ptr.PtrDefaultHandler;
import in.srain.cube.views.ptr.PtrFrameLayout;
import in.srain.cube.views.ptr.PtrHandler;

/**
 * Created by Administrator on 2016/4/2.
 */
public class NewsListFragment extends CubeFragment {

    private static final String TAG = "NewsListFragment";

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        ViewGroup view = (ViewGroup) inflater.inflate(R.layout.fragment_news_list, null);
        initNewsList(view);
        return view;
    }

    private LifeCycleComponentManager mComponentContainer = new LifeCycleComponentManager();

    @Override
    protected View createView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
//        View view = inflater.inflate(R.layout.fragment_news_list, null);
//        initNewsList(view);
//        return view;
        return null;
    }

    NewsListAdapter mAdapter;
    public ListView mListView;
    public PtrFrameLayout mPtrFrame;
    public NewsListDataModel mDataModel = new NewsListDataModel();

    private void initNewsList(View view) {

        ////////////////////////////////////////////////////////////////////////////////////
        // 绑定视图与数据
        mAdapter = new NewsListAdapter(view.getContext(), R.layout.news_list_item, mDataModel.getDataList());
        mListView = (ListView) view.findViewById(R.id.news_list_list_view);
        mListView.setAdapter(mAdapter);
        ////////////////////////////////////////////////////////////////////////////////////


        ////////////////////////////////////////////////////////////////////////////////////
        //处理点击事件
        mListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                NewsListItem item = mDataModel.getDataList().get(position);
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
                mDataModel.queryFirstPage();
            }
        });
        ////////////////////////////////////////////////////////////////////////////////////

        ////////////////////////////////////////////////////////////////////////////////////
        //处理加载更多容器
        final LoadMoreListViewContainer loadMoreListViewContainer =
                (LoadMoreListViewContainer) view.findViewById(R.id.load_more_list_view_container);
        loadMoreListViewContainer.useDefaultHeader();
        loadMoreListViewContainer.setLoadMoreHandler(new LoadMoreHandler() {
            @Override
            public void onLoadMore(LoadMoreContainer loadMoreContainer) {
                //向上滑动到最底部，加载更多
                mDataModel.queryNextPage();
            }
        });

        //处理拉取数据成功事件
        EventCenter.bindContainerAndHandler(this, new CommonEventHandler() {

            @Subscribe
            public void onEvent(NewsListDataEvent event) {
                mPtrFrame.refreshComplete();
                mAdapter.notifyDataSetChanged();
                loadMoreListViewContainer.loadMoreFinish(event.isEmpty, event.hasMore);
            }

            @Subscribe
            public void onEvent(ErrorMessageDataEvent event) {
//                loadMoreListViewContainer.loadMoreError(0, event.message);
            }

        }).tryToRegisterIfNot();

        //自动加载第一页
        mPtrFrame.postDelayed(new Runnable() {
            @Override
            public void run() {
                mPtrFrame.autoRefresh(false);
            }
        }, 150);
    }

}
