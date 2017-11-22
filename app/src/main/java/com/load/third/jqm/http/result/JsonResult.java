package com.load.third.jqm.http.result;

import java.io.Serializable;

/**
 * Created by Administrator on 2016/4/21.
 */
public class JsonResult implements Serializable {

    protected String success;
    protected int code;
    protected String message;

    public String getSuccess() {
        return success;
    }

    public void setSuccess(String success) {
        this.success = success;
    }

    public int getCode() {
        return code;
    }

    public void setCode(int code) {
        this.code = code;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public JsonResult() {
    }

    public JsonResult(String success) {
        setSuccess(success);
    }

}