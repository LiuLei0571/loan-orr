package com.load.third.jqm.newHttp;

import com.load.third.jqm.bean.HomeExpenseDataBean;
import com.load.third.jqm.bean.RepaymentDataBean;
import com.load.third.jqm.bean.RepaymentUserBean;
import com.load.third.jqm.bean.UserBean;
import com.load.third.jqm.bean.newBean.BorrowInfo;
import com.load.third.jqm.bean.newBean.QiniuName;
import com.load.third.jqm.bean.newBean.QiniuToken;
import com.load.third.jqm.bean.newBean.UserStatus;

import io.reactivex.Observable;
import io.reactivex.Observer;
import retrofit2.http.GET;
import retrofit2.http.Path;
import retrofit2.http.Url;

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

    @GET
    Observable<BaseResponse<UserBean>> getLoginWithToken(@Url String url);

    @GET
    Observable<BaseResponse<UserStatus>> getStatus(@Url String url);

    @GET
    Observable<BaseResponse<RepaymentDataBean>> getRepaymentData(@Url String url);

    @GET
    Observable<BaseResponse<RepaymentUserBean>> getRepaymentUser(@Url String url);

    @GET
    Observable<BaseResponse<String>> getCheckPhone(@Url String url);

    @GET
    Observable<BaseResponse<BorrowInfo>> getBorrowInfo(@Url String url);

    @GET
    Observable<BaseResponse<String>> getMyInfoFirst(@Url String url);

    @GET
    Observable<BaseResponse<String>> getMyInfoSecond(@Url String url);

    @GET
    Observable<BaseResponse<String>> getMyInfoThird(@Url String url);

    @GET
    Observable<BaseResponse<String>> getBankName(@Url String url);

    @GET
    Observer<BaseResponse<String>> getBindBankCard(@Url String url);

    @GET
    Observable<BaseResponse<QiniuName>> getQiNiuName(@Url String url);

    @GET
    Observable<BaseResponse<QiniuToken>> getQiNiuToken(@Url String url);

    @GET
    Observable<BaseResponse<String>> getBindIdCard(@Url String url);

    @GET
    Observable<BaseResponse<String>> getContacts(@Url String url);

    @GET
    Observable<BaseResponse<String>> getBindGetuiCid(@Url String url);

    @GET
    Observable<BaseResponse<String>> getIsStudentAuth(@Url String url);

    @GET
    Observable<BaseResponse<String>> getStudyAuth(@Url String url);

    @GET
    Observable<BaseResponse<String>> getPosition(@Url String url);

    @GET
    Observable<BaseResponse<String>> getIsChecking(@Url String url);


}

