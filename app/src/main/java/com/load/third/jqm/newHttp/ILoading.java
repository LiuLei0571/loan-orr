package com.load.third.jqm.newHttp;

import android.content.Context;

/**
 * 用途：
 * 作者：Created by liulei on 2017/11/22.
 * 邮箱：liulei2@aixuedai.com
 */


public interface ILoading {
    void showLoading(Context context);
    void showLoading(Context context,String content);
    void dissLoading();
}
