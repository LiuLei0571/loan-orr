package com.load.third.jqm.newHttp;

import java.io.IOException;
import java.util.Map;

import okhttp3.CacheControl;
import okhttp3.Interceptor;
import okhttp3.Request;
import okhttp3.Response;

/**
 * 用途：网络拦截器
 * 作者：Created by liulei on 2017/11/30.
 * 邮箱：liulei2@aixuedai.com
 */


public class LoanHttpIntercept implements Interceptor {
    Map<String, Object> params;

    @Override
    public Response intercept(Chain chain) throws IOException {
        Request.Builder builder = chain.request()
                .newBuilder();

        if (!DeviceHelper.getNetworkState()) {
            builder.cacheControl(CacheControl.FORCE_CACHE).build();
        }

        Request request = builder
                .addHeader("Content-Type", "application/x-www-form-urlencoded; charset=UTF-8")
                .addHeader("Accept-Encoding", "gzip, deflate")
                .addHeader("Connection", "keep-alive")
                .addHeader("Accept", "*/*")
                .build();


//        Response response = chain.proceed(request);
//        response.newBuilder()
//                .addHeader("Accept-Encoding", "gzip, deflate")
//                .addHeader("Connection", "keep-alive")
//                .addHeader("Accept", "*/*")
//                .addHeader("key1", "value1")
//                .build();

        return chain.proceed(request);
    }
}

