package com.load.third.jqm.http.result;

/**
 * Created by Administrator on 2016/4/21.
 */
public class DataJsonResult<T> extends JsonResult {
    /**
     *
     */
    public DataJsonResult() {
    }

    public DataJsonResult(T data) {
        setData(data);
    }

    private T data;


    public T getData(){
        return data;
    }

    public void setData(T data) {
        this.data = data;
    }

    public static <T> DataJsonResult<T> newResult(T data) {
        DataJsonResult<T> result = new DataJsonResult<T>(data);
        return result;
    }
}
