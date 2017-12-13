package com.load.third.commpent.helper;

import com.load.third.commpent.cdi.CDI;

/**
 * 用途：
 * 作者：Created by liulei on 2017/12/10.
 * 邮箱：liulei2@aixuedai.com
 */


public final class CDIHelper {
    public CDIHelper() {
        CDI.getAppComponent().plus(this);
    }
    private static CDIHelper inistance=null;
    public static synchronized CDIHelper inistance(){
        if (inistance == null) {
            inistance=new CDIHelper();
        }
        return inistance;
    }
}
