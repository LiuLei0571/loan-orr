package com.load.third.jqm.service;

import android.app.Service;
import android.content.Intent;
import android.os.IBinder;

import com.igexin.sdk.GTServiceManager;

/**
 * Created by Administrator on 2017/4/13.
 */

public class GetuiPushService extends Service {

    @Override
    public void onCreate() {
        GTServiceManager.getInstance( ).onCreate(this);
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        return GTServiceManager.getInstance( ).onStartCommand(this, intent, flags, startId);
    }

    @Override
    public IBinder onBind(Intent intent) {
        return GTServiceManager.getInstance( ).onBind(intent);
    }

    @Override
    public void onDestroy() {
    }

    @Override
    public void onLowMemory() {
        GTServiceManager.getInstance( ).onLowMemory( );
    }
}
