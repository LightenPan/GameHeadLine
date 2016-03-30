package com.flylighten.app.gameheadline.activity;

import android.content.Context;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v4.widget.SwipeRefreshLayout;
import android.view.View;
import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.ListView;

import com.flylighten.app.gameheadline.R;
import com.flylighten.app.gameheadline.adapter.NewsListAdapter;
import com.flylighten.app.gameheadline.model.NewsListItemModel;
import com.flylighten.app.gameheadline.protocol.GetNewsListProxy;

import java.util.List;

import static com.flylighten.app.gameheadline.protocol.GetNewsListProxy.*;

public class HeadLineActivity extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener {

    Context mContext;

    class ViewHolder {
        public SwipeRefreshLayout swipeRefreshLayout;
        public ListView newsListView;
    }

    ViewHolder holder = new ViewHolder();

    class ProxyHolder {
        public GetNewsListProxy getNewsList = new GetNewsListProxy();
    }
    ProxyHolder proxyHolder = new ProxyHolder();

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

        //列表下拉刷新
//        holder.swipeRefreshLayout = (SwipeRefreshLayout) findViewById(R.id.list_view_swipe_refresh_layout);
////        //设置加载圈的背景颜色
////        holder.swipeRefreshLayout.setProgressBackgroundColorSchemeResource(R.color.line_color_run_speed_1);
////        //设置加载圈圈的颜色
////        holder.swipeRefreshLayout.setColorSchemeResources(
////                R.color.line_color_run_speed_7,
////                R.color.line_color_run_speed_9,
////                R.color.line_color_run_speed_11);
//        holder.swipeRefreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
//            @Override
//            public void onRefresh() {
//                //
//                holder.swipeRefreshLayout.postDelayed(new Runnable() {
//                    @Override
//                    public void run() {
//                        holder.swipeRefreshLayout.setRefreshing(false);
//                    }
//                }, 3000);//3秒
//            }
//        });

        //资讯列表视图
        proxyHolder.getNewsList.doNet(0, 5, proxyHolder.getNewsList.new Callback() {
            @Override
            protected void onSuccess(List<NewsListItemModel> newsList) {
                NewsListAdapter newsListAdapter = new NewsListAdapter(mContext, R.layout.news_list_item, newsList);
                holder.newsListView.setAdapter(newsListAdapter);
            }
        });

//        List<NewsListItemModel> dataList = DataUtils.getExampleList();
//        NewsListAdapter newsListAdapter = new NewsListAdapter(this, R.layout.news_list_item, dataList);
//        holder.newsListView.setAdapter(newsListAdapter);
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
}
