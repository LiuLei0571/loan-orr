package com.load.third.jqm.newHttp;

import com.load.third.jqm.bean.HomeExpenseDataBean;
import com.load.third.jqm.bean.RepaymentDataBean;
import com.load.third.jqm.bean.RepaymentUserBean;
import com.load.third.jqm.bean.UserBean;

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
    Observable<BaseResponse<HomeExpenseDataBean>> retrofitHomeExpenseData(@Path(value = "homeUrl", encoded = true) String homeUrl);

    @GET("{url}")
    Observable<BaseResponse<String>> getSmsCode(@Path(value = "url", encoded = true) String url);

    @GET("{url}")
    Observable<BaseResponse<UserBean>> getLogin(@Path(value = "url", encoded = true) String url);

    @GET("{url}")
    Observable<BaseResponse<UserBean>> getLoginWithToken(@Path(value = "url", encoded = true) String url);

    @GET("{url}")
    Observable<BaseResponse<String>> getStatus(@Path(value = "url", encoded = true) String url);

    @GET("{url}")
    Observable<BaseResponse<RepaymentDataBean>> getRepaymentData(@Path(value = "url", encoded = true) String url);

    @GET("{url}")
    Observable<BaseResponse<RepaymentUserBean>> getRepaymentUser(@Path(value = "url", encoded = true) String url);

    @GET("{url}")
    Observable<BaseResponse<String>> getCheckPhone(@Path(value = "url", encoded = true) String url);

    @GET("{url}")
    Observable<BaseResponse<String>> getBorrowInfo(@Path(value = "url", encoded = true) String url);

    @GET("{url}")
    Observable<BaseResponse<String>> getMyInfoFirst(@Path(value = "url", encoded = true) String url);

    @GET("{url}")
    Observable<BaseResponse<String>> getMyInfoSecond(@Path(value = "url", encoded = true) String url);

    @GET("{url}")
    Observable<BaseResponse<String>> getMyInfoThird(@Path(value = "url", encoded = true) String url);

    @GET("{url}")
    Observable<BaseResponse<String>> getBankName(@Path(value = "url", encoded = true) String url);

    @GET("{url}")
    Observable<BaseResponse<String>> getBindBankCard(@Path(value = "url", encoded = true) String url);

    @GET("{url}")
    Observable<BaseResponse<String>> getQiNiuName(@Path(value = "url", encoded = true) String url);

    @GET("{url}")
    Observable<BaseResponse<String>> getQiNiuToken(@Path(value = "url", encoded = true) String url);

    @GET("{url}")
    Observable<BaseResponse<String>> getBindIdCard(@Path(value = "url", encoded = true) String url);

    @GET("{url}")
    Observable<BaseResponse<String>> getContacts(@Path(value = "url", encoded = true) String url);

    @GET("{url}")
    Observable<BaseResponse<String>> getBindGetuiCid(@Path(value = "url", encoded = true) String url);

    @GET("{url}")
    Observable<BaseResponse<String>> getIsStudentAuth(@Path(value = "url", encoded = true) String url);

    @GET("{url}")
    Observable<BaseResponse<String>> getStudyAuth(@Path(value = "url", encoded = true) String url);

    @GET("{url}")
    Observable<BaseResponse<String>> getPosition(@Path(value = "url", encoded = true) String url);

    @GET("{url}")
    Observable<BaseResponse<String>> getIsChecking(@Path(value = "url", encoded = true) String url);


}

