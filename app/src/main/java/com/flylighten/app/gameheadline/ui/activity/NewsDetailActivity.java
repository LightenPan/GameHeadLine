package com.flylighten.app.gameheadline.ui.activity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.webkit.WebChromeClient;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.ProgressBar;

import com.flylighten.app.gameheadline.R;
import com.flylighten.app.gameheadline.base.AppTitleBaseActivity;
import com.flylighten.app.gameheadline.event.CommonEventHandler;
import com.flylighten.app.gameheadline.event.ErrorMessageDataEvent;
import com.flylighten.app.gameheadline.event.EventCenter;
import com.flylighten.app.gameheadline.event.NewsDetailDataEvent;
import com.flylighten.app.gameheadline.model.NewsDetailDataModel;
import com.flylighten.app.gameheadline.request.API;

import org.greenrobot.eventbus.Subscribe;

import in.srain.cube.util.CLog;
import in.srain.cube.views.ptr.PtrClassicFrameLayout;
import in.srain.cube.views.ptr.PtrDefaultHandler;
import in.srain.cube.views.ptr.PtrFrameLayout;
import in.srain.cube.views.ptr.PtrHandler;

/**
 * Created by Administrator on 2016/4/2.
 */
public class NewsDetailActivity extends AppTitleBaseActivity {

    private String Tag = "NewsDetailActivity";
    private String mLinkMd5Id;
    private String mTitle;
    private WebView mWebView;
    private ProgressBar mProgressBar;
    private PtrClassicFrameLayout mPtrFrame;
    private NewsDetailDataModel mDataModel = new NewsDetailDataModel();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        Intent intent = getIntent();
        if (null != intent) {
            mLinkMd5Id = intent.getStringExtra("linkmd5id");
            mTitle = intent.getStringExtra("title");
        }

        setHeaderTitle(mTitle);
        setContentView(R.layout.fragment_news_detail);

        initView(mContentContainer);
    }

    private void initView(View view) {
        //页面加载进度条
        mProgressBar = (ProgressBar) view.findViewById(R.id.news_detail_progress_bar);

        ////////////////////////////////////////////////////////////////////////////////////
        //设置webview
        mWebView = (WebView) view.findViewById(R.id.news_detail_web_view);

        //设置加载进来的页面自适应手机屏幕
//        mWebView.getSettings().setUseWideViewPort(true);
//        mWebView.getSettings().setLoadWithOverviewMode(true);

        mWebView.getSettings().setLayoutAlgorithm(WebSettings.LayoutAlgorithm.SINGLE_COLUMN);

        //设置视频播放
        mWebView.getSettings().setJavaScriptEnabled(true);
        mWebView.getSettings().setJavaScriptCanOpenWindowsAutomatically(true);
        mWebView.getSettings().setPluginState(WebSettings.PluginState.ON);
//        mWebView.getSettings().setPluginsEnabled(true);
        mWebView.getSettings().setAllowFileAccess(true);
        mWebView.getSettings().setLoadWithOverviewMode(true);
        mWebView.setBackgroundColor(0);

        //设置webview内的用本地webview打开
        mWebView.setWebViewClient(new WebViewClient() {
            @Override
            public boolean shouldOverrideUrlLoading(WebView view, String url) {
                view.loadUrl(url);
                return true;
            }
        });

        //显示进度
        mWebView.setWebChromeClient(new WebChromeClient() {

            @Override
            public void onProgressChanged(WebView view, int newProgress) {
                mProgressBar.setProgress(newProgress);
                if (newProgress >= 100) {
                    mProgressBar.setVisibility(View.GONE);
                }
            }
        });
        ////////////////////////////////////////////////////////////////////////////////////

        ////////////////////////////////////////////////////////////////////////////////////
        // 初始化下拉刷新组件
        mPtrFrame = (PtrClassicFrameLayout) view.findViewById(R.id.news_detail_ptr_layout);
        mPtrFrame.setLoadingMinTime(300);
        mPtrFrame.setLastUpdateTimeRelateObject(this);
        mPtrFrame.setPtrHandler(new PtrHandler() {
            @Override
            public boolean checkCanDoRefresh(PtrFrameLayout frame, View content, View header) {
                return PtrDefaultHandler.checkContentCanBePulledDown(frame, mWebView, header);
            }

            @Override
            public void onRefreshBegin(PtrFrameLayout frame) {
                mDataModel.queryNewsDetail(mLinkMd5Id);
            }
        });
        ////////////////////////////////////////////////////////////////////////////////////

        //处理拉取数据成功事件
        EventCenter.bindContainerAndHandler(this, new CommonEventHandler() {

            @Subscribe
            public void onEvent(NewsDetailDataEvent event) {
                String url = API.GetDetail2 + "?linkmd5id=" + mLinkMd5Id;
                CLog.i(Tag, url);
                mProgressBar.setVisibility(View.VISIBLE);
                mWebView.loadUrl(url);

                mPtrFrame.refreshComplete();
            }

            @Subscribe
            public void onEvent(ErrorMessageDataEvent event) {
//                loadMoreListViewContainer.loadMoreError(0, event.message);
            }

        }).tryToRegisterIfNot();

        // auto load data
        mPtrFrame.postDelayed(new Runnable() {
            @Override
            public void run() {
                mPtrFrame.autoRefresh(false);
            }
        }, 150);
    }
}
