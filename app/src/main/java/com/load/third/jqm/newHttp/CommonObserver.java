package com.load.third.jqm.newHttp;

import com.google.gson.JsonParseException;
import com.load.third.jqm.MyApp;
import com.load.third.jqm.tips.ProgressDialog;
import com.load.third.jqm.tips.ToastUtils;

import org.json.JSONException;

import java.io.InterruptedIOException;
import java.net.ConnectException;
import java.net.UnknownHostException;
import java.text.ParseException;

import io.reactivex.Observer;
import io.reactivex.disposables.Disposable;
import retrofit2.HttpException;

/**
 * 用途：
 * 作者：Created by liulei on 2017/11/22.
 * 邮箱：liulei2@aixuedai.com
 */


public abstract class CommonObserver<T> implements Observer<BaseResponse<T>>, ISubscribe<T> {
    @Override
    public void onSubscribe(Disposable d) {
        doSubscribe(d);
    }

    @Override
    public void onNext(BaseResponse<T> result) {
        if ("true".equals(result.getSuccess())) {
            doSuccess(result);
        } else {
            doFail(result.getMessage());
        }
        doFinish();
    }

    @Override
    public void onError(Throwable e) {
        doFail(e.toString());
        if (e instanceof HttpException) {     //   HTTP错误
            doFail("请求异常");
        } else if (e instanceof ConnectException
                || e instanceof UnknownHostException) {   //   连接错误
            doFail("请求超时");
        } else if (e instanceof InterruptedIOException) {   //  连接超时
            doFail("请求超时");
        } else if (e instanceof JsonParseException
                || e instanceof JSONException
                || e instanceof ParseException) {   //  解析错误
            doFail("请求超时");
        } else {
            doFail("请求超时");
        }
    }
    @Override
    public void onComplete() {

    }

    @Override
    public void doSubscribe(Disposable d) {

    }

    @Override
    public void doFail(String msg) {
        ToastUtils.showToast(MyApp.getContext(), "网络请求失败");
        doFinish();
    }

    @Override
    public void doFinish() {
        ProgressDialog.cancelProgressBar();
    }
}
