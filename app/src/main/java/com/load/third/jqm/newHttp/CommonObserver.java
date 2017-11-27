package com.load.third.jqm.newHttp;

import com.load.third.jqm.tips.ProgressDialog;

import io.reactivex.Observer;
import io.reactivex.disposables.Disposable;

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
            doFinish();
        } else {
            doFail(result.getMessage());
        }
    }

    @Override
    public void onError(Throwable e) {
        doFail(e.toString());
    }

    @Override
    public void onComplete() {

    }

    @Override
    public void doSubscribe(Disposable d) {

    }

    @Override
    public void doFail(String msg) {
         doFinish();
    }

    @Override
    public void doFinish() {
        ProgressDialog.cancelProgressBar();
    }
}
