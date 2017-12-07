package com.load.third.jqm.newHttp;

import com.google.gson.Gson;
import com.load.third.jqm.MyApp;

import java.io.File;
import java.util.concurrent.TimeUnit;

import okhttp3.Cache;
import okhttp3.OkHttpClient;

/**
 * 用途：
 * 作者：Created by liulei on 2017/11/22.
 * 邮箱：liulei2@aixuedai.com
 */


public class LoanOkHttpClient {
    public static OkHttpClient okHttpClient;
    public static File cacheFile = new File(MyApp.getContext().getCacheDir(), "caheData");
    public static Gson gson;
    //设置缓存大小
    public static Cache cache = new Cache(cacheFile, 50 * 1024 * 1024);//google建议放到这里

    /**
     *初始化操作
    **/
    private static OkHttpClient getInstance() {
        if (okHttpClient == null) {
            synchronized (ApiManager.class) {
                okHttpClient = new OkHttpClient.Builder()
                        .connectTimeout(60, TimeUnit.SECONDS)
                        .readTimeout(300, TimeUnit.SECONDS)
                        .writeTimeout(300, TimeUnit.SECONDS)
                        .addNetworkInterceptor(new LoanHttpIntercept())
                        .cache(cache)
                        .cookieJar(new CookieManager())
                        .build();

            }
        }
        return okHttpClient;
    }


    public static OkHttpClient getOkHttpClient() {
        return getInstance();
    }
}
