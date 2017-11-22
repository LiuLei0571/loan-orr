package com.load.third.jqm.newHttp;

import com.google.gson.annotations.SerializedName;

/**
 * 用途：
 * 作者：Created by liulei on 2017/11/21.
 * 邮箱：liulei2@aixuedai.com
 */


public class ApiResult {
    @SerializedName("message")
    public String message;
    @SerializedName("code")
    public int code;
    @SerializedName("success")
    public String success;


    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public int getCode() {
        return code;
    }

    public void setCode(int code) {
        this.code = code;
    }

    public String getSuccess() {
        return success;
    }

    public void setSuccess(String success) {
        this.success = success;
    }

}
