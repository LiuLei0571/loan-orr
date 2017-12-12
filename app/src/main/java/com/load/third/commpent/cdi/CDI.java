package com.load.third.commpent.cdi;

import android.content.Context;

import com.load.third.commpent.cdi.cmp.AppCompent;
import com.load.third.commpent.cdi.cmp.AppModule;
import com.load.third.commpent.cdi.cmp.ManagerModule;

/**
 * 用途：
 * 作者：Created by liulei on 2017/12/10.
 * 邮箱：liulei2@aixuedai.com
 */


public class CDI {
    private static AppCompent appCompent;

    public static void init(Context context) {
        AppModule appModule = new AppModule(context);
        ManagerModule managerModule = new ManagerModule();
        appCompent=DaggerAppComponent.builder().appModule(appModule)
                .managerModule(managerModule).build();
    }
}
