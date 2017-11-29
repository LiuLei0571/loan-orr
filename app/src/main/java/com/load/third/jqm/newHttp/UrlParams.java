package com.load.third.jqm.newHttp;

import java.util.Iterator;
import java.util.Map;

/**
 * 用途：
 * 作者：Created by liulei on 2017/11/27.
 * 邮箱：liulei2@aixuedai.com
 */


public class UrlParams {
    public static String getUrl(String url, Map<String, Object> params) {
        StringBuffer stringBuffer = new StringBuffer();
        stringBuffer.append(UrlUtils.host);
        stringBuffer.append(url);
        if (url.lastIndexOf(Strings.QMARK) == -1) {
            stringBuffer.append(Strings.QMARK);
        } else {
            stringBuffer.append(Chars.AND);
        }
        Iterator<Map.Entry<String, Object>> it = params.entrySet().iterator();
        while (it.hasNext()) {
            Map.Entry<String, Object> pair = it.next();

            stringBuffer.append(pair.getKey());
            stringBuffer.append(Chars.EQUAL);
            stringBuffer.append(pair.getValue());
            if (it.hasNext()) {
                stringBuffer.append(Strings.AND);
            }
        }
        return stringBuffer.toString();
    }
}
