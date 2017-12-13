package com.load.third.commpent.cdi.cmp;

import com.load.third.commpent.helper.CDIHelper;

import javax.inject.Singleton;

import dagger.Component;

/**
 * 用途：
 * 作者：Created by liulei on 2017/12/10.
 * 邮箱：liulei2@aixuedai.com
 */

@Singleton
@Component(modules = {ManagerModule.class, AppModule.class})
public interface AppComponent {
    ActivityComponent plus(ActivityModule activityCompent);

    FragmentComponent plus(FragmentModule fragmentCompent);

    DialogComponent plus(DialogModule dialogCompent);

    ServiceComponent plus(ServiceModule serviceCompent);
    void plus(CDIHelper cdiHelper);

}
