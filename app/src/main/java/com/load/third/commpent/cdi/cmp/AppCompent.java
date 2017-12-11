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
public interface AppCompent {
    ActivityCompent plus(ActivityCompent activityCompent);

    FragmentCompent plus(FragmentCompent fragmentCompent);

    DialogCompent plus(DialogCompent dialogCompent);

    ServiceCompent plus(ServiceCompent serviceCompent);
    void plus(CDIHelper cdiHelper);
}
