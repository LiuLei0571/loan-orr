package com.load.third.jqm.newHttp;

import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;

import com.load.third.jqm.MyApp;

/**
 * 用途：
 * 作者：Created by liulei on 2017/11/30.
 * 邮箱：liulei2@aixuedai.com
 */


public class DeviceHelper {

    /**
     * 判断当前有没有网络连接
     */
    public static boolean getNetworkState() {
        Context context = MyApp.getContext();
        if (context != null) {
            ConnectivityManager manager = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
            NetworkInfo networkinfo = manager.getActiveNetworkInfo();
            return !(networkinfo == null || !networkinfo.isAvailable());
        }
        return false;
    }
}
