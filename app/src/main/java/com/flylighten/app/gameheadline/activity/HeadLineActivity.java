package com.flylighten.app.gameheadline.activity;

import android.content.Context;
import android.os.Bundle;
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
import android.widget.Toast;

import com.flylighten.app.gameheadline.R;
import com.flylighten.app.gameheadline.Utils.LocalDisplay;
import com.flylighten.app.gameheadline.adapter.NewsListAdapter;
import com.flylighten.app.gameheadline.data.DataUtils;
import com.flylighten.app.gameheadline.model.NewsListItemModel;
import com.flylighten.app.gameheadline.protocol.GetNewsListProxy;
import com.flylighten.app.gameheadline.restful.DefaultRestfulClient;
import com.loopj.android.http.AsyncHttpClient;
import com.loopj.android.http.AsyncHttpResponseHandler;
import com.loopj.android.http.JsonHttpResponseHandler;
import com.loopj.android.http.RequestParams;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

import cz.msebera.android.httpclient.Header;
import in.srain.cube.views.ptr.PtrDefaultHandler;
import in.srain.cube.views.ptr.PtrFrameLayout;
import in.srain.cube.views.ptr.PtrHandler;
import in.srain.cube.views.ptr.header.StoreHouseHeader;

public class HeadLineActivity extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener {

    Context mContext;

    class ViewHolder {
        public SwipeRefreshLayout swipeRefreshLayout;
        public ListView newsListView;
        public PtrFrameLayout mPtrFrame;
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

        // the following are default settings
        holder.mPtrFrame = (PtrFrameLayout) findViewById(R.id.head_line_list_view_frame);
        holder.mPtrFrame.setResistance(1.7f);
        holder.mPtrFrame.setRatioOfHeaderHeightToRefresh(1.2f);
        holder.mPtrFrame.setDurationToClose(200);
        holder.mPtrFrame.setDurationToCloseHeader(1000);
        // default is false
        holder.mPtrFrame.setPullToRefresh(false);
        // default is true
        holder.mPtrFrame.setKeepHeaderWhenRefresh(true);
        // header
        final StoreHouseHeader header = new StoreHouseHeader(this);
        header.setPadding(0, LocalDisplay.dp2px(15, this), 0, 0);

        /**
         * using a string, support: A-Z 0-9 - .
         * you can add more letters by {@link in.srain.cube.views.ptr.header.StoreHousePath#addChar}
         */
        header.initWithString("Alibaba");

        holder.mPtrFrame.setPtrHandler(new PtrHandler() {
            @Override
            public void onRefreshBegin(PtrFrameLayout frame) {
//                frame.postDelayed(new Runnable() {
//                    @Override
//                    public void run() {
//                        holder.mPtrFrame.refreshComplete();
//                    }
//                }, 3000);

                frame.post(new Runnable() {
                    @Override
                    public void run() {
                        proxyHolder.getNewsList.doNet(0, 5, new AsyncHttpResponseHandler() {

                            @Override
                            public void onSuccess(int statusCode, Header[] headers, byte[] responseBody) {

                                try {
                                    JSONObject resp = new JSONObject(responseBody.toString());
                                    int result = resp.getInt("result");
                                    JSONArray array = resp.getJSONArray("news_list");
                                    if (0 == result && array.length() > 0) {
                                        final List<NewsListItemModel> newsList = new ArrayList<NewsListItemModel>();
                                        for (int i = 0; i < array.length(); ++i) {
                                            JSONObject obj = array.getJSONObject(i);
                                            NewsListItemModel item = new NewsListItemModel();
                                            //item.date = obj.getLong("date");
                                            item.title = obj.getString("title");
                                            item.digest = obj.getString("title");
                                            item.other = obj.getString("content");
                                            newsList.add(item);
                                        }
                                    }
                                } catch (JSONException e) {
                                    e.printStackTrace();
                                    Toast.makeText(mContext, e.getMessage(), Toast.LENGTH_LONG).show();
                                }
                                holder.mPtrFrame.refreshComplete();
                            }

                            @Override
                            public void onFailure(int statusCode, Header[] headers, byte[] responseBody, Throwable error) {
                                Toast.makeText(mContext, error.getMessage(), Toast.LENGTH_LONG).show();
                            }
                        });
                    }
                });


            }

            @Override
            public boolean checkCanDoRefresh(PtrFrameLayout frame, View content, View header) {
                return PtrDefaultHandler.checkContentCanBePulledDown(frame, content, header);
            }
        });

        List<NewsListItemModel> dataList = DataUtils.getExampleList();
        NewsListAdapter newsListAdapter = new NewsListAdapter(this, R.layout.news_list_item, dataList);
        holder.newsListView = (ListView) findViewById(R.id.head_line_list_view);
        holder.newsListView.setAdapter(newsListAdapter);
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
