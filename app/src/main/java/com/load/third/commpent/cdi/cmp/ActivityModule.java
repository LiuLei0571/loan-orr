package com.load.third.commpent.cdi.cmp;

import android.content.Context;

import com.load.third.commpent.cdi.annimation.ActivityScope;
import com.load.third.jqm.activity.BaseActivity;

import dagger.Module;
import dagger.Provides;

/**
 * 用途：
 * 作者：Created by liulei on 2017/12/10.
 * 邮箱：liulei2@aixuedai.com
 */

@Module
public class ActivityModule {
    private final BaseActivity activity;

    public ActivityModule(BaseActivity activity) {
        this.activity = activity;
    }

    @Provides
    @ActivityScope
    BaseActivity provideBaseActivity() {
        return this.activity;
    }


    @Provides
    @ActivityScope
    Context provideContext() {
        return this.activity;
    }


}
