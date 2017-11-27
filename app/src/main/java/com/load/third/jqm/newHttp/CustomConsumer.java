package com.load.third.jqm.newHttp;

import android.content.Context;

import com.load.third.jqm.tips.ProgressDialog;

import io.reactivex.functions.Consumer;

/**
 * 用途：
 * 作者：Created by liulei on 2017/11/27.
 * 邮箱：liulei2@aixuedai.com
 */


public class CustomConsumer<T> implements Consumer<T> {
    private Context context;

    public CustomConsumer(Context context) {
        this.context = context;
    }

    @Override
    public void accept(T t) throws Exception {
        ProgressDialog.showProgressBar(context);

    }
}
