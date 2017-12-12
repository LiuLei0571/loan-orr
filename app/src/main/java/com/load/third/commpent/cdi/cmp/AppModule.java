package com.load.third.commpent.cdi.cmp;

import android.content.Context;

import dagger.Module;

/**
 * 用途：
 * 作者：Created by liulei on 2017/12/10.
 * 邮箱：liulei2@aixuedai.com
 */

@Module
public class AppModule {
    public Context context;
    public AppModule(Context appCompent) {
    this.context=appCompent;
    }
}
