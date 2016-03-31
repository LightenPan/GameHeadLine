package com.flylighten.app.gameheadline.Utils;

import android.content.Context;

/**
 * Created by Administrator on 2016/4/1.
 */
public class LocalDisplay {
    public static int dp2px(int dip, Context context){
        float scale = context.getResources().getDisplayMetrics().density;
        return (int)(dip * scale + 0.5f);
    }
}
