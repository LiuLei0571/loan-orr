package com.load.third.jqm.newHttp;

import io.reactivex.annotations.NonNull;
import io.reactivex.disposables.Disposable;

/**
 * 用途：
 * 作者：Created by liulei on 2017/11/22.
 * 邮箱：liulei2@aixuedai.com
 */


public interface ISubscribe<T > {
    void doSubscribe(@NonNull Disposable d);

    void doSuccess(@NonNull BaseResponse<T> result);

    void doFail(String msg);

}
