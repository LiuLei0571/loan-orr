package com.load.third.jqm.bean.newBean;

import com.google.gson.annotations.SerializedName;
import com.load.third.jqm.bean.HomeExpenseDataBean;

/**
 * 用途：
 * 作者：Created by liulei on 2017/11/21.
 * 邮箱：liulei2@aixuedai.com
 */


public class HomeExpenseResult  {
    @SerializedName("data")
    private HomeExpenseDataBean data;

    public HomeExpenseDataBean getData() {
        return data;
    }

    public void setData(HomeExpenseDataBean data) {
        this.data = data;
    }
}
