package com.load.third.jqm.thread;

import android.app.DownloadManager;
import android.content.Context;
import android.net.Uri;
import android.os.Environment;

import static android.content.Context.DOWNLOAD_SERVICE;

/**
 * Created by Administrator on 2017/4/20.
 */

public class UpdateApkThread extends Thread {
    private Context context;
    private String url;
    private String apkPath;

    public UpdateApkThread(Context context, String url) {
        this.context = context;
        this.url = url;
    }

    @Override
    public void run() {
        DownloadManager manager = (DownloadManager) context.getSystemService(DOWNLOAD_SERVICE);
        //设置下载地址
        DownloadManager.Request down = new DownloadManager.Request(Uri.parse(url));
        // 设置允许使用的网络类型，这里是移动网络和wifi都可以
        down.setAllowedNetworkTypes(DownloadManager.Request.NETWORK_MOBILE
                | DownloadManager.Request.NETWORK_WIFI);
        // 下载时，通知栏显示途中
        down.setNotificationVisibility(DownloadManager.Request.VISIBILITY_VISIBLE);
        // 显示下载界面
        down.setVisibleInDownloadsUi(true);
        // 设置下载后文件存放的位置
        down.setDestinationInExternalPublicDir(Environment.DIRECTORY_DOWNLOADS, "xiaozhoudao.apk");
        // 将下载请求放入队列
        manager.enqueue(down);
    }
}
