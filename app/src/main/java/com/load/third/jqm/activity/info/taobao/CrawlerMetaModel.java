package com.load.third.jqm.activity.info.taobao;

import java.math.BigDecimal;

/**
 * Created by Administrator on 2017/4/14.
 */
public class CrawlerMetaModel {
    public static final String Login = "login";
    public static final String Crawler = "crawler";
    String state;
    String loginUrl;
    String loginSuccessUrl;
    String nextCrawlerUrl;
    String message;
    BigDecimal percent;

    public String getState() {
        return state;
    }

    public void setState(String state) {
        this.state = state;
    }

    public String getLoginUrl() {
        return loginUrl;
    }

    public void setLoginUrl(String loginUrl) {
        this.loginUrl = loginUrl;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public String getLoginSuccessUrl() {
        return loginSuccessUrl;
    }

    public void setLoginSuccessUrl(String loginSuccessUrl) {
        this.loginSuccessUrl = loginSuccessUrl;
    }

    public String getNextCrawlerUrl() {
        return nextCrawlerUrl;
    }

    public void setNextCrawlerUrl(String nextCrawlerUrl) {
        this.nextCrawlerUrl = nextCrawlerUrl;
    }

    public BigDecimal getPercent() {
        return percent;
    }

    public void setPercent(BigDecimal percent) {
        this.percent = percent;
    }
}
