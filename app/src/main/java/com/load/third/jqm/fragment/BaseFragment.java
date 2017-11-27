package com.load.third.jqm.fragment;

import android.annotation.TargetApi;
import android.app.Fragment;
import android.content.Context;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.Nullable;

import com.load.third.jqm.bean.HomeExpenseDataBean;
import com.load.third.jqm.newHttp.ApiManager;
import com.load.third.jqm.newHttp.ApiRetrofit;
import com.load.third.jqm.newHttp.BaseResponse;
import com.load.third.jqm.newHttp.CommonObserver;
import com.load.third.jqm.newHttp.CustomConsumer;
import com.load.third.jqm.newHttp.ILoading;
import com.load.third.jqm.tips.ProgressDialog;

import io.reactivex.Observable;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.Disposable;
import io.reactivex.schedulers.Schedulers;

/**
 * 用途：
 * 作者：Created by liulei on 2017/11/22.
 * 邮箱：liulei2@aixuedai.com
 */


public class BaseFragment extends Fragment implements ILoading {
    public ApiRetrofit apiRetrofit;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        apiRetrofit = ApiManager.apiManager.initRetrofit();
    }

    @Override
    public void showLoading(Context context) {
        ProgressDialog.showProgressBar(context);
    }

    @Override
    public void showLoading(Context context, String content) {
        ProgressDialog.showProgressBar(context, content);

    }

    @Override
    public void dissLoading() {
        ProgressDialog.cancelProgressBar();
    }

    public ILoading getLoading() {
        return this;
    }

    @TargetApi(Build.VERSION_CODES.M)
    public void submitTask(Observable<BaseResponse<HomeExpenseDataBean>> baseResponseObservable, CommonObserver commonObserver) {
        baseResponseObservable.subscribeOn(Schedulers.io())
                .doOnSubscribe(new CustomConsumer<Disposable>(getContext()))
                .subscribeOn(AndroidSchedulers.mainThread())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(commonObserver);

    }
}
