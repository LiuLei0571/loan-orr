package com.load.third.jqm.newHttp;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

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
    public static Gson gson;

    private static ApiManager getInstance() {
        if (apiManager == null) {
            synchronized (ApiManager.class) {
                apiManager = new ApiManager();

            }
        }
        return apiManager;
    }

    public ApiRetrofit initRetrofit() {
        if (apiRetrofit == null) {
            apiRetrofit = getBaseRetrofit().create(ApiRetrofit.class);
        }
        return apiRetrofit;
    }

    public Retrofit getBaseRetrofit() {
        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl(UrlUtils.host)
                .addCallAdapterFactory(RxJava2CallAdapterFactory.create())
                .addConverterFactory(GsonConverterFactory.create(buidGson()))
                .client(LoanOkHttpClient.getOkHttpClient())
                .build();
        apiRetrofit = retrofit.create(ApiRetrofit.class);
        return retrofit;
    }

    public static Gson buidGson() {
        if (gson == null) {
            gson = new GsonBuilder()
                    .setDateFormat("yyyy-MM-dd HH:mm:ss")
                    .registerTypeAdapter(Object.class, new StringDefaultAdapter())
                    .registerTypeAdapter(String.class, new StringDefaultAdapter())
                    .create();
        }
        return gson;
    }
}
