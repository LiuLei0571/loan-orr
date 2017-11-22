package com.load.third.jqm.newHttp;


import io.reactivex.Observer;
import io.reactivex.disposables.Disposable;

/**
 * 用途：
 * 作者：Created by liulei on 2017/11/22.
 * 邮箱：liulei2@aixuedai.com
 */


public abstract   class LoanObserver<T> implements Observer<BaseResponse<T>> {

    @Override
    public void onSubscribe(Disposable d) {
        //绑定观察对象，注意在界面的ondestory或者onpouse方法中调用presenter.unsubcription();
    }



    @Override
    public void onError(Throwable e) {

    }

    @Override
    public void onComplete() {

    }
}
