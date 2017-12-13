package com.load.third.commpent.cdi;

import android.content.Context;

import com.load.third.commpent.cdi.cmp.ActivityComponent;
import com.load.third.commpent.cdi.cmp.ActivityModule;
import com.load.third.commpent.cdi.cmp.AppComponent;
import com.load.third.commpent.cdi.cmp.AppModule;
import com.load.third.commpent.cdi.cmp.DaggerAppComponent;
import com.load.third.commpent.cdi.cmp.ManagerModule;
import com.load.third.jqm.activity.BaseActivity;

/**
 * 用途：
 * 作者：Created by liulei on 2017/12/10.
 * 邮箱：liulei2@aixuedai.com
 */


public class CDI {
    private static AppComponent appComponent;

    public static void init(Context context) {
        AppModule appModule = new AppModule(context);
        ManagerModule managerModule = new ManagerModule();
        appComponent = DaggerAppComponent.builder().appModule(appModule)
                .managerModule(managerModule).build();
    }

    public static AppComponent getAppComponent() {
        return appComponent;
    }
    public static ActivityComponent createActivityComponent(BaseActivity baseActivity){
        ActivityComponent activityComponent = appComponent.plus(new ActivityModule(baseActivity));
        return activityComponent;
    }
}
