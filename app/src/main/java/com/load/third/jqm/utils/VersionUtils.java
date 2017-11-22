package com.load.third.jqm.utils;

import android.content.Context;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;

/**
 * Created by Administrator on 2017/4/20.
 */

public class VersionUtils {
    public static String getVersionName(Context context) {
        PackageManager packageManager = context.getPackageManager( );
        PackageInfo packageInfo;
        String versionName = "";
        try {
            packageInfo = packageManager.getPackageInfo(context.getPackageName( ), 0);
            versionName = packageInfo.versionName + "";
        } catch (PackageManager.NameNotFoundException e) {
            e.printStackTrace( );
        }
        return versionName;
    }
}
