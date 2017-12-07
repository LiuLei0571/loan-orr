package com.load.third.jqm.newHttp;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ConcurrentHashMap;

import okhttp3.Cookie;
import okhttp3.CookieJar;
import okhttp3.HttpUrl;

/**
 * 用途：
 * 作者：Created by liulei on 2017/12/7.
 * 邮箱：liulei2@aixuedai.com
 */


public class CookieManager implements CookieJar {
    private ConcurrentHashMap<String, List<Cookie>> cookieStore = new ConcurrentHashMap<>();

    @Override
    public void saveFromResponse(HttpUrl url, List<Cookie> cookies) {
        cookieStore.put(url.host(), cookies);

    }

    @Override
    public List<Cookie> loadForRequest(HttpUrl url) {
        List<Cookie> cookies = cookieStore.get(url.host());
        return cookies != null ? cookies : new ArrayList<Cookie>();    }
}
