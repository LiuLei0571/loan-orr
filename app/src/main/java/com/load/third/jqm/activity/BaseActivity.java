package com.load.third.jqm.activity;

import android.app.Activity;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;

import com.load.third.commpent.cdi.CDI;
import com.load.third.commpent.cdi.cmp.ActivityComponent;
import com.load.third.jqm.newHttp.ApiManager;
import com.load.third.jqm.newHttp.ApiRetrofit;
import com.load.third.jqm.newHttp.CommonObserver;
import com.load.third.jqm.newHttp.CustomConsumer;

import io.reactivex.Observable;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.Disposable;
import io.reactivex.schedulers.Schedulers;

/**
 * 用途：
 * 作者：Created by liulei on 2017/11/22.
 * 邮箱：liulei2@aixuedai.com
 */


public class BaseActivity extends AppCompatActivity {
    public ApiRetrofit apiRetrofit;
    private ActivityComponent activityComponent;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        apiRetrofit = ApiManager.apiManager.initRetrofit();
        activityComponent = CDI.createActivityComponent(this);
    }


    @Override
    protected void onPostCreate(@Nullable Bundle savedInstanceState) {
        super.onPostCreate(savedInstanceState);
    }

    public void submitTask(Observable baseResponseObservable, CommonObserver commonObserver) {
        baseResponseObservable.subscribeOn(Schedulers.io())
                .doOnSubscribe(new CustomConsumer<Disposable>(this))
                .subscribeOn(AndroidSchedulers.mainThread())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(commonObserver);
    }

    public Activity getBaseActivity(){
        return this;
    }
}
