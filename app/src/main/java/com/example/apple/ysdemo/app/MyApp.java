package com.example.apple.ysdemo.app;

import android.app.Application;

import com.videogo.openapi.EZOpenSDK;


/**
 * @author 张先磊
 * @date 2018/4/17
 */

public class MyApp extends Application {
    //开发者需要填入自己申请的appkey
    public static String AppKey = "373f90fa7c534e02bbbf020ade384bf5";

    public static EZOpenSDK getOpenSDK() {
        return EZOpenSDK.getInstance();
    }

    @Override
    public void onCreate() {
        super.onCreate();
        // 初始化萤石开放平台
        initSDK();
    }

    private void initSDK() {
        {
            /**
             * sdk日志开关，正式发布需要去掉
             */
            EZOpenSDK.showSDKLog(true);

            /**
             * 设置是否支持P2P取流,详见api
             */
            EZOpenSDK.enableP2P(true);

            /**
             * APP_KEY请替换成自己申请的
             */
            EZOpenSDK.initLib(this, AppKey);
        }
    }
}
