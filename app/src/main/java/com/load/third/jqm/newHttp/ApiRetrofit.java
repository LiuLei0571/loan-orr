package com.load.third.jqm.newHttp;

import com.load.third.jqm.bean.HomeExpenseDataBean;
import com.load.third.jqm.bean.RepaymentDataBean;
import com.load.third.jqm.bean.RepaymentUserBean;
import com.load.third.jqm.bean.UserBean;
import com.load.third.jqm.bean.newBean.BorrowInfo;
import com.load.third.jqm.bean.newBean.CheckPhone;
import com.load.third.jqm.bean.newBean.QiniuName;
import com.load.third.jqm.bean.newBean.QiniuToken;
import com.load.third.jqm.bean.newBean.UserStatus;

import io.reactivex.Observable;
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

    @GET
    Observable<BaseResponse<String>> getSmsCode(@Url String url);

    @GET
    Observable<BaseResponse<UserBean>> getLogin(@Url String url);

    @GET
    Observable<BaseResponse<UserBean>> getLoginWithToken(@Url String url);

    @GET
    Observable<BaseResponse<UserStatus>> getStatus(@Url String url);

    @GET
    Observable<BaseResponse<RepaymentDataBean>> getRepaymentData(@Url String url);

    @GET
    Observable<BaseResponse<RepaymentUserBean>> getRepaymentUser(@Url String url);

    @GET
    Observable<BaseResponse<CheckPhone>> getCheckPhone(@Url String url);

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
    Observable<BaseResponse<String>> getBindBankCard(@Url String url);

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

