package com.load.third.jqm.newHttp;

import okhttp3.OkHttpClient;
import retrofit2.Retrofit;
import retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory;
import retrofit2.converter.gson.GsonConverterFactory;

/**
 * 用途：
 * 作者：Created by liulei on 2017/11/21.
 * 邮箱：liulei2@aixuedai.com
 */


public class ApiManager {
    public ApiRetrofit apiRetrofit;
    public static ApiManager apiManager = getInstance();
    public static OkHttpClient okHttpClient;

    private static ApiManager getInstance() {
        if (apiManager == null && okHttpClient == null) {
            synchronized (ApiManager.class) {
                apiManager = new ApiManager();
                okHttpClient = new OkHttpClient.Builder().build();

            }
        }
        return apiManager;
    }

    public ApiRetrofit getHomeExpenseData() {
        if (apiRetrofit == null) {
            apiRetrofit = getBaseRetrofit().create(ApiRetrofit.class);
        }
        return apiRetrofit;
    }

    public Retrofit getBaseRetrofit() {
        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl(UrlUtils.host)
                .addCallAdapterFactory(RxJava2CallAdapterFactory.create())
                .addConverterFactory(GsonConverterFactory.create())
                .build();
        apiRetrofit = retrofit.create(ApiRetrofit.class);
        return retrofit;
    }
}
