package com.load.third.jqm.newHttp;

import java.util.concurrent.TimeUnit;

import okhttp3.OkHttpClient;

/**
 * 用途：
 * 作者：Created by liulei on 2017/11/22.
 * 邮箱：liulei2@aixuedai.com
 */


public class LoanOkHttpClient {
    public static OkHttpClient okHttpClient;

    static {
    }

    private static OkHttpClient getInstance() {
        if (okHttpClient == null) {
            synchronized (ApiManager.class) {
                okHttpClient = new OkHttpClient.Builder()
                        .connectTimeout(60, TimeUnit.SECONDS)
                        .readTimeout(300, TimeUnit.SECONDS)
                        .writeTimeout(300, TimeUnit.SECONDS)
                        .addInterceptor(new LoanHttpIntercept())
                        .build();

            }
        }
        return okHttpClient;
    }

    public static OkHttpClient getOkHttpClient() {
        return getInstance();
    }
}
