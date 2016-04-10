package com.flylighten.app.gameheadline.Utils;

import android.content.Context;
import android.webkit.CookieManager;
import android.webkit.CookieSyncManager;

import com.flylighten.app.gameheadline.app.MyApplication;

import cz.msebera.android.httpclient.cookie.Cookie;


/**
 * Created by Administrator on 2016/4/1.
 */
public class WebViewUtils {
    public static String HtmlScaleWrap(String html){
        //图片缩放设置
        //width=device-width; 表示宽度是设备屏幕的宽度
        //initial-scale=1.0; 表示初始的缩放比例
        //minimum-scale=0.5; 表示最小的缩放比例
        //maximum-scale=2.0; 表示最大的缩放比例
        //user-scalable=yes; 表示用户是否可以调整缩放比例
        String content = "<meta name=\"viewport\" content=\"width=device-width, initial-scale=1.0, minimum-scale=1.0, maximum-scale=5.0, user-scalable=yes\" />";
        return content + html;
    }

    public static void synCookies(Context context, String url) {
        CookieSyncManager.createInstance(context);
        CookieManager cookieManager = CookieManager.getInstance();
        cookieManager.setAcceptCookie(true);
        cookieManager.removeSessionCookie();//移除

        String duoshuo_token = String.format("duoshuo_token=%s", MyApplication.getDuoShuoToken());
        cookieManager.setCookie(url, duoshuo_token);

        String token = String.format("token=%s", MyApplication.getToken());
        cookieManager.setCookie(url, token);

        String uid = String.format("uid=%s", MyApplication.getUid());
        cookieManager.setCookie(url, uid);

        CookieSyncManager.getInstance().sync();
    }
}
