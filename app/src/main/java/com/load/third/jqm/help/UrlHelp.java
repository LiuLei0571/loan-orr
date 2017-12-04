package com.load.third.jqm.help;

import java.io.UnsupportedEncodingException;
import java.net.URLDecoder;
import java.net.URLEncoder;

/**
 * 用途：
 * 作者：Created by liulei on 2017/12/4.
 * 邮箱：liulei2@aixuedai.com
 */


public class UrlHelp {
    public static String getEncode(String url) {
        try {
            return URLEncoder.encode(url, "UTF-8");
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }
        return "";
    }
    public static String getDecode(String url) {
        try {
            return URLDecoder.decode(url, "UTF-8");
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }
        return "";
    }

}
