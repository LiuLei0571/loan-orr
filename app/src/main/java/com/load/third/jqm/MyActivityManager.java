package com.load.third.jqm;

import android.app.Activity;

/**
 * Created by Administrator on 2017/4/19.
 * 单例模式判断应用是否在前台运行，topActivity是否为null
 */

public class MyActivityManager {

    private static MyActivityManager activityManager;
    private static Activity topActivity;

    private MyActivityManager() {

    }

    public static MyActivityManager getInstance() {
        if (activityManager == null) {
            activityManager = new MyActivityManager( );
        }
        return activityManager;
    }

    public Activity getTopActivity() {
        return topActivity;
    }

    public void setTopActivity(Activity topActivity) {
        this.topActivity = topActivity;
    }
}
