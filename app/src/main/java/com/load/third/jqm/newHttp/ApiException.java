package com.load.third.jqm.newHttp;

import com.load.third.jqm.MyApp;
import com.load.third.jqm.tips.ToastUtils;

/**
 * 用途：
 * 作者：Created by liulei on 2017/11/29.
 * 邮箱：liulei2@aixuedai.com
 */


public class ApiException extends RuntimeException {
    public ApiException(int resultCode) {
        this(getApiExceptionMessage(resultCode));
    }

    public ApiException(String detailMessage) {
        ToastUtils.showToast(detailMessage);
    }

    private static String getApiExceptionMessage(int code) {
        String message;
        switch (code) {
            case 1025:
                MyApp.isNeedUpdate = false;
                message = "登录过期,请重新登录";
                break;
            case 1022:
                MyApp.isNeedUpdate = true;
                message = "密码错误";
                break;
            default:
                MyApp.isNeedUpdate = false;
                message = "未知错误";

        }
        return message;
    }
}
