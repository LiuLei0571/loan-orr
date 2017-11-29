package com.load.third.jqm.newHttp;

import com.load.third.jqm.help.UserHelper;

/**
 * 用途：
 * 作者：Created by liulei on 2017/11/21.
 * 邮箱：liulei2@aixuedai.com
 */


public class ApiRequest {
    private String url;
    private RequestMethod requestMethod;
    private boolean hasToken;

    public String getUrl() {
        String strUrl;
        if (url.startsWith("http")) {
            return url;
        }
        if (hasToken) {
            url = UrlUtils.host + url + "?token=" + UserHelper.getUserToken();
        }
        return url;
    }

    public boolean isHasToken() {
        return hasToken;
    }

    public ApiRequest setHasToken(boolean hasToken) {
        this.hasToken = hasToken;
        return this;
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
