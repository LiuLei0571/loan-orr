package com.load.third.jqm.newHttp;

/**
 * 用途：
 * 作者：Created by liulei on 2017/11/21.
 * 邮箱：liulei2@aixuedai.com
 */


public class ApiRequest {
    private String url;
    private RequestMethod requestMethod;

    public String getUrl() {
        if (url.startsWith("http")) {
            return url;
        }
        return UrlUtils.host + url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public RequestMethod getRequestMethod() {
        return requestMethod;
    }

    public void setRequestMethod(RequestMethod requestMethod) {
        this.requestMethod = requestMethod;
    }

    public static ApiRequest get(String url) {
        ApiRequest apiRequest = new ApiRequest();
        apiRequest.setUrl(url);
        apiRequest.setRequestMethod(RequestMethod.GET);
        return apiRequest;
    }
}
