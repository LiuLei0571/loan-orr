package com.load.third.jqm.service;

import android.app.DownloadManager;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.support.v7.app.NotificationCompat;
import android.util.Log;

import com.load.third.jqm.MyActivityManager;
import com.load.third.jqm.R;
import com.load.third.jqm.activity.MainActivity;
import com.load.third.jqm.activity.home.GetuiDialogActivity;
import com.load.third.jqm.thread.UpdateApkThread;

import static android.content.Context.DOWNLOAD_SERVICE;
import static com.load.third.jqm.service.GetuiIntentService.payloadData;

/**
 * Created by Administrator on 2017/4/19.
 * 广播
 */

public class MyBroadcast extends BroadcastReceiver {
    @Override
    public void onReceive(final Context context, Intent intent) {
        //收到个推透传
        if (intent.getAction( ).equals("ACTION_GET_GETUI_MSG")) {
            if (MyActivityManager.getInstance( ).getTopActivity( ) != null) {
                //应用在前台运行时,弹出消息
                intent.setClass(context, GetuiDialogActivity.class);
                intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                context.startActivity(intent);
            } else {
                //应用在后台运行时,通知栏消息
                showNotifiction(context);
            }
        }
        //需要更新APK,开始下载
        if (intent.getAction( ).equals("ACTION_DOWNLOAD_APK")) {
            Log.e("apk_msg", "开始下载apk");
            new UpdateApkThread(context, intent.getStringExtra("apk_url")).run( );
        }
        //APK下载完成,开始安装
        if (intent.getAction( ).equals(DownloadManager.ACTION_DOWNLOAD_COMPLETE)) {
            Log.e("apk_msg", "apk下载完成，开始安装");
            final long downId = intent.getLongExtra(DownloadManager.EXTRA_DOWNLOAD_ID, 0);
            installAPK(context, downId);
        }
    }

    public static void showNotifiction(Context context) {
        NotificationCompat.Builder builder = new NotificationCompat.Builder(context);
        builder.setAutoCancel(true);//点击后消失
        builder.setSmallIcon(R.mipmap.ic_launcher);//设置通知栏消息标题的头像
        builder.setDefaults(NotificationCompat.DEFAULT_SOUND);//设置通知铃声
        builder.setContentText(payloadData);//通知内容
        builder.setContentTitle("金钱门");

        Intent intent = new Intent(context, MainActivity.class);//将要跳转的界面
        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
        PendingIntent intentPend = PendingIntent.getActivity(context, 0, intent, PendingIntent.FLAG_CANCEL_CURRENT);
        builder.setContentIntent(intentPend);
        NotificationManager notifyManager = (NotificationManager) context.getSystemService(Context.NOTIFICATION_SERVICE);
        //通过builder.build()方法生成Notification对象,并发送通知,id=(int) System.currentTimeMillis( )
        notifyManager.notify((int) System.currentTimeMillis( ), builder.build( ));
    }

    private void installAPK(Context context, long downId) {
        // 通过Intent安装APK文件
        DownloadManager manager = (DownloadManager) context.getSystemService(DOWNLOAD_SERVICE);
        Uri uri = manager.getUriForDownloadedFile(downId);
        Intent install = new Intent(Intent.ACTION_VIEW);
        install.setDataAndType(uri, "application/vnd.android.package-archive");
        install.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        context.startActivity(install);
//        System.exit(0);
    }

}
