package com.load.third.jqm.newHttp;

import com.load.third.jqm.bean.HomeExpenseDataBean;

import io.reactivex.Observable;
import retrofit2.http.GET;
import retrofit2.http.Path;

/**
 * 用途：
 * 作者：Created by liulei on 2017/11/20.
 * 邮箱：liulei2@aixuedai.com
 */


public interface ApiRetrofit {
    /**
     * 获取首页数据
     *
     * @return
     */
    @GET("{homeUrl}")
    Observable<BaseResponse<HomeExpenseDataBean>> retrofitHomeExpenseData(@Path(value = "homeUrl",encoded = true)String homeUrl);

}

