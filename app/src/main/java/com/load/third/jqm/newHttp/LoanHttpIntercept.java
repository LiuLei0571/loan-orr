package com.load.third.jqm.newHttp;

import com.load.third.jqm.tips.ToastUtils;

import java.io.IOException;
import java.util.Map;

import okhttp3.CacheControl;
import okhttp3.Interceptor;
import okhttp3.Request;
import okhttp3.Response;

/**
 * 用途：
 * 作者：Created by liulei on 2017/11/30.
 * 邮箱：liulei2@aixuedai.com
 */


public class LoanHttpIntercept implements Interceptor {
    Map<String, Object> params;
    @Override
    public Response intercept(Chain chain) throws IOException {
        Request request = chain.request();
        Request.Builder builder = request.newBuilder();
        if (!DeviceHelper.getNetworkState()) {
            builder = request.newBuilder()
                    .cacheControl(CacheControl.FORCE_CACHE);

            ToastUtils.showToast("暂无网络");
        }
        builder.addHeader("key", "value")
                .build();
        Response response = chain.proceed(request);
        if (DeviceHelper.getNetworkState()) {
            String cacheControl = request.cacheControl().toString();
            return response.newBuilder()
                    .header("key", cacheControl)
                    .removeHeader("Pragma")
                    .build();
        } else {
            return response.newBuilder()
                    .header("key", "public,only-if-cached,max-stake=2419200")
                    .removeHeader("Pragma")
                    .build();
        }
    }
}

