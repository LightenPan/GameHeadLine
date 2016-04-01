package com.flylighten.app.gameheadline.activity;

import android.content.Context;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.NavigationView;
import android.support.design.widget.Snackbar;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ListView;

import com.flylighten.app.gameheadline.R;
import com.flylighten.app.gameheadline.Utils.LocalDisplay;
import com.flylighten.app.gameheadline.adapter.NewsListAdapter;
import com.flylighten.app.gameheadline.model.NewsListItemModel;
import com.flylighten.app.gameheadline.protocol.ProxyGetNewsList;

import java.util.List;

import in.srain.cube.views.loadmore.LoadMoreContainer;
import in.srain.cube.views.loadmore.LoadMoreHandler;
import in.srain.cube.views.loadmore.LoadMoreListViewContainer;
import in.srain.cube.views.ptr.PtrDefaultHandler;
import in.srain.cube.views.ptr.PtrFrameLayout;
import in.srain.cube.views.ptr.PtrHandler;
import in.srain.cube.views.ptr.header.StoreHouseHeader;

public class HeadLineActivity extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener {

    Context mContext;

    class ViewHolder {
        public SwipeRefreshLayout swipeRefreshLayout;
        NewsListAdapter newsListAdapter;
        public ListView newsListView;
        public PtrFrameLayout mPtrFrame;
    }
    ViewHolder viewHolder = new ViewHolder();

    class DataHolder {
        List<NewsListItemModel> newsDataList;
    }
    DataHolder dataHolder = new DataHolder();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_head_line);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        mContext = this;

        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG)
                        .setAction("Action", null).show();
            }
        });

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.setDrawerListener(toggle);
        toggle.syncState();

        NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);

        //初始化新闻列表
        initNewsList();
    }

    private void initNewsList() {
        // the following are default settings
        viewHolder.mPtrFrame = (PtrFrameLayout) findViewById(R.id.head_line_list_view_frame);
        viewHolder.mPtrFrame.setResistance(1.7f);
        viewHolder.mPtrFrame.setRatioOfHeaderHeightToRefresh(1.2f);
        viewHolder.mPtrFrame.setDurationToClose(200);
        viewHolder.mPtrFrame.setDurationToCloseHeader(1000);
        // default is false
        viewHolder.mPtrFrame.setPullToRefresh(false);
        // default is true
        viewHolder.mPtrFrame.setKeepHeaderWhenRefresh(true);
        // header
        final StoreHouseHeader header = new StoreHouseHeader(this);
        header.setPadding(0, LocalDisplay.dp2px(15, this), 0, 0);

        /**
         * using a string, support: A-Z 0-9 - .
         * you can add more letters by {@link in.srain.cube.views.ptr.header.StoreHousePath#addChar}
         */
        header.initWithString("Alibaba");

        viewHolder.mPtrFrame.setPtrHandler(new PtrHandler() {
            @Override
            public void onRefreshBegin(PtrFrameLayout frame) {
                ProxyGetNewsList proxy = new ProxyGetNewsList();
                proxy.doNet(0, 50, new ProxyGetNewsList.Callback() {

                    @Override
                    protected void onSuccess(List<NewsListItemModel> newsList) {
                        viewHolder.mPtrFrame.refreshComplete();
                    }
                });
            }

            @Override
            public boolean checkCanDoRefresh(PtrFrameLayout frame, View content, View header) {
                return PtrDefaultHandler.checkContentCanBePulledDown(frame, content, header);
            }
        });

//        viewHolder.newsListAdapter = new NewsListAdapter(this, R.layout.news_list_item, viewHolder.newsDataList);
//        viewHolder.newsListView = (ListView) findViewById(R.id.head_line_list_view);
//        viewHolder.newsListView.setAdapter(viewHolder.newsListAdapter);

        // load more container
//        final LoadMoreListViewContainer loadMoreListViewContainer = (LoadMoreListViewContainer) view.findViewById(R.id.load_more_list_view_container);
//        loadMoreListViewContainer.useDefaultHeader();
//        loadMoreListViewContainer.setLoadMoreHandler(new LoadMoreHandler() {
//            @Override
//            public void onLoadMore(LoadMoreContainer loadMoreContainer) {
//                mDataModel.queryNextPage();
//            }
//        });
    }

    @Override
    public void onBackPressed() {
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        } else {
            super.onBackPressed();
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.head_line, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    @SuppressWarnings("StatementWithEmptyBody")
    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        // Handle navigation view item clicks here.
        int id = item.getItemId();

        if (id == R.id.nav_camera) {
            // Handle the camera action
        } else if (id == R.id.nav_gallery) {

        } else if (id == R.id.nav_slideshow) {

        } else if (id == R.id.nav_manage) {

        } else if (id == R.id.nav_share) {

        } else if (id == R.id.nav_send) {

        }

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }

    private Handler handler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            // super.handleMessage(msg);
            switch (msg.what) {
                case 1:
                    // UI线程中调用此方法通知观察者(源码中关于adapter存在一个observer,未深究！)adapter数据已改变，刷新view
                    viewHolder.newsListAdapter.notifyDataSetChanged();
                    // adapter.notifyDataSetInvalidated();// 与上面
                    // 效果相同，源码中除了注释不同，执行的代码一样，同样未深究
                    // listView.postInvalidate();刷新无效
                    break;
            }
        }
    };
}
