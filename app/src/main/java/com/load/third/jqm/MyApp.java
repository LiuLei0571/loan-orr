package com.load.third.jqm;

import android.app.Activity;
import android.app.Application;
import android.content.Context;
import android.os.Bundle;
import android.os.StrictMode;
import android.util.Log;

/**
 * Created by Administrator on 2017/4/17.
 */

public class MyApp extends Application {
    public static boolean isNeedUpdate = false;
    public static Context mContext;

    public static Context getContext() {
        if ((mContext != null)) {
            return mContext;
        }
        return null;
    }

    @Override
    public void onCreate() {
        super.onCreate();
        mContext = getApplicationContext();
        initStrict();
        //监听应用内Activity的生命周期
        registerActivityLifecycleCallbacks(new ActivityLifecycleCallbacks() {
            @Override
            public void onActivityCreated(Activity activity, Bundle savedInstanceState) {

            }

            @Override
            public void onActivityStarted(Activity activity) {

            }

            @Override
            public void onActivityResumed(Activity activity) {
                MyActivityManager.getInstance().setTopActivity(activity);
                Log.e("MyActivityManager", activity.getLocalClassName() + " resume");
            }

            @Override
            public void onActivityPaused(Activity activity) {
                MyActivityManager.getInstance().setTopActivity(null);
                Log.e("MyActivityManager", activity.getLocalClassName() + " pause");
            }

            @Override
            public void onActivityStopped(Activity activity) {
            }

            @Override
            public void onActivitySaveInstanceState(Activity activity, Bundle outState) {

            }

            @Override
            public void onActivityDestroyed(Activity activity) {
            }
        });
    }

    private void initStrict() {
        StrictMode.setThreadPolicy(new StrictMode.ThreadPolicy.Builder()
                .detectDiskReads()
                .detectDiskWrites()
                .detectNetwork()
                .detectAll()
                .penaltyLog()
                .build());
        StrictMode.setVmPolicy(new StrictMode.VmPolicy.Builder()
                .detectLeakedSqlLiteObjects()
                .detectAll()
                .penaltyLog()
                .build());
    }
}
