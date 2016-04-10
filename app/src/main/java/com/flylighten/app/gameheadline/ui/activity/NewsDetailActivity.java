package com.flylighten.app.gameheadline.ui.activity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.webkit.CookieManager;
import android.webkit.CookieSyncManager;
import android.webkit.WebChromeClient;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.ProgressBar;

import com.flylighten.app.gameheadline.R;
import com.flylighten.app.gameheadline.Utils.WebViewUtils;
import com.flylighten.app.gameheadline.base.AppTitleBaseActivity;
import com.flylighten.app.gameheadline.request.API;

import in.srain.cube.util.CLog;

/**
 * Created by Administrator on 2016/4/2.
 */
public class NewsDetailActivity extends AppTitleBaseActivity {

    private String Tag = "NewsDetailActivity";
    private String mLinkMd5Id;
    private String mTitle;
    private WebView mWebView;
    private ProgressBar mProgressBar;

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
        mWebView.getSettings().setCacheMode(WebSettings.LOAD_NO_CACHE);
        mWebView.getSettings().setAppCacheEnabled(true);
        mWebView.getSettings().setDomStorageEnabled(true);
        mWebView.getSettings().setDatabaseEnabled(true);

        mWebView.setBackgroundColor(0);

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

        String url = API.GetDetail + mLinkMd5Id;

        //将票据写入cookie中
        WebViewUtils.synCookies(this, url);

        //设置webview内的用本地webview打开
        mWebView.setWebViewClient(new WebViewClient() {
            @Override
            public boolean shouldOverrideUrlLoading(WebView view, String url) {
                view.loadUrl(url);
                return true;
            }

            @Override
            public void onPageFinished(WebView view, String url) {
                CookieManager cookieManager = CookieManager.getInstance();
                String CookieStr = cookieManager.getCookie(url);
                CLog.e("onPageFinished", "Cookies: " + CookieStr);
                super.onPageFinished(view, url);
            }
        });

        mProgressBar.setVisibility(View.VISIBLE);
        mWebView.loadUrl(url);
    }
}
