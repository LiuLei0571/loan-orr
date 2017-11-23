package com.load.third.jqm.newHttp;

import okhttp3.OkHttpClient;

/**
 * 用途：
 * 作者：Created by liulei on 2017/11/22.
 * 邮箱：liulei2@aixuedai.com
 */


public class LoanOkHttpClient {
    public static OkHttpClient okHttpClient;

    private static OkHttpClient getInstance() {
        if (okHttpClient == null) {
            synchronized (ApiManager.class) {
                okHttpClient = new OkHttpClient.Builder().build();

            }
        }
        return okHttpClient;
    }
    public static OkHttpClient getOkHttpClient(){
      return   getInstance();
    }
}
