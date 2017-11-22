package com.load.third.okhttp.builder;

import com.load.third.okhttp.OkHttpUtils;
import com.load.third.okhttp.request.OtherRequest;
import com.load.third.okhttp.request.RequestCall;

/**
 * Created by zhy on 16/3/2.
 */
public class HeadBuilder extends GetBuilder
{
    @Override
    public RequestCall build()
    {
        return new OtherRequest(null, null, OkHttpUtils.METHOD.HEAD, url, tag, params, headers,id).build();
    }
}
