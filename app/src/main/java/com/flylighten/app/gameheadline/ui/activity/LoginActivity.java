package com.flylighten.app.gameheadline.ui.activity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import com.flylighten.app.gameheadline.R;
import com.flylighten.app.gameheadline.app.MyApplication;
import com.flylighten.app.gameheadline.data.ConstantData;
import com.flylighten.app.gameheadline.event.LoginAppServerDataEvent;
import com.flylighten.app.gameheadline.model.LoginAppServerDataModel;
import com.tencent.tauth.IUiListener;
import com.tencent.tauth.Tencent;
import com.tencent.tauth.UiError;

import org.json.JSONObject;

import in.srain.cube.mints.base.MintsBaseActivity;
import in.srain.cube.request.CacheAbleRequest;
import in.srain.cube.util.CLog;

/**
 * Created by Administrator on 2016/4/2.
 */
public class LoginActivity extends MintsBaseActivity {

    private static final String TAG = "HotNewsListFragment";

    @Override
    protected void onCreate(Bundle bundle) {
        super.onCreate(bundle);
        setContentView(R.layout.acivity_login);

        Button button = (Button) findViewById(R.id.qq_login_btn);
        ////////////////////////////////////////////////////////////////////////////////////

        ////////////////////////////////////////////////////////////////////////////////////
        //处理点击事件
        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onClickQQLogin();
            }
        });
        ////////////////////////////////////////////////////////////////////////////////////
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        Tencent.onActivityResultData(requestCode, resultCode, data, mQQLoginListener);
    }

    IUiListener mQQLoginListener = new IUiListener() {

        @Override
        public void onComplete(Object response) {
            if (null == response) {
                CLog.e(TAG, "qq login onComplete 返回为空");
                Toast.makeText(getApplicationContext(), "登录失败", Toast.LENGTH_LONG).show();
                return;
            }

            JSONObject jsonResponse = (JSONObject) response;
            if (null != jsonResponse && jsonResponse.length() == 0) {
                CLog.e(TAG, "qq login onComplete 返回为空");
                Toast.makeText(getApplicationContext(), "登录失败", Toast.LENGTH_LONG).show();
                return;
            }

            //QQ登录成功后，去后台换票据
            Toast.makeText(getApplicationContext(), "QQ登录成功", Toast.LENGTH_LONG).show();

            //登录app服务器
            onLoginAppServer(jsonResponse.optString("openid"), jsonResponse.optString("access_token"));
        }

        @Override
        public void onError(UiError e) {
            Toast.makeText(getApplicationContext(), "qq login onError: " + e.errorDetail, Toast.LENGTH_LONG).show();
        }

        @Override
        public void onCancel() {
            Toast.makeText(getApplicationContext(), "qq login onCancel", Toast.LENGTH_LONG).show();
        }
    };

    private void onClickQQLogin() {
        Tencent tencent = Tencent.createInstance(ConstantData.QQ_APPID, getApplicationContext());
        tencent.login(this, "all", mQQLoginListener);
    }

    private void onLoginAppServer(String openid, String access_token) {

        LoginAppServerDataModel.doPost(openid, access_token, new LoginAppServerDataModel.DataHandler() {
            @Override
            public void onData(LoginAppServerDataEvent event, CacheAbleRequest.ResultType type, boolean outOfDate) {
                if (0 != event.action_status) {
                    Toast.makeText(getApplicationContext(), "登录app服务器失败", Toast.LENGTH_LONG).show();
                    return;
                }

                //保存token
                MyApplication.inst().saveTokenToCache(event.uid, event.token, event.duoshuo_token, event.expire_time);

                //切换到新闻视图
                switchToNewsTabActivity();
            }

            @Override
            public void onNetFail() {
                Toast.makeText(getApplicationContext(), "网络超时或服务器挂了", Toast.LENGTH_LONG).show();
            }
        });
    }

    private void switchToNewsTabActivity() {
        //切换到主视图
        Intent intent = new Intent();
        Class<?> dstClassName = HeadLineActivity.class;
        intent.setClass(this, dstClassName);
        startActivity(intent);

        //如果不关闭当前的会出现好多个页面
        LoginActivity.this.finish();
    }
}
