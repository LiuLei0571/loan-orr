package com.load.third.jqm.help;

import com.load.third.jqm.bean.UserBean;
import com.load.third.jqm.bean.UserDao;

/**
 * 用途：
 * 作者：Created by liulei on 2017/11/27.
 * 邮箱：liulei2@aixuedai.com
 */


public class UserHelper {
    private static UserDao userDao;
    private static UserBean emptyUserBean = new UserBean();

    static {
        userDao = new UserDao();
    }

    public static String getUserToken() {

        return userDao.getToken();
    }
}
