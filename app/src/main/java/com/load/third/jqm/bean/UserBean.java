package com.load.third.jqm.bean;

public class UserBean {
    /**
     * user_id : 100012
     * token : xxxx
     */

    private int user_id;
    private String token;
    private String mobile;
    private String cid;
    private boolean address_list;
    private int taobao;

    public int getUser_id() {
        return user_id;
    }

    public void setUser_id(int user_id) {
        this.user_id = user_id;
    }

    public String getToken() {
        return token;
    }

    public void setToken(String token) {
        this.token = token;
    }

    public String getMobile() {
        return mobile;
    }

    public void setMobile(String mobile) {
        this.mobile = mobile;
    }

    public String getCid() {
        return cid;
    }

    public void setCid(String cid) {
        this.cid = cid;
    }

    public boolean getAddress_list() {
        return address_list;
    }

    public void setAddress_list(boolean address_list) {
        this.address_list = address_list;
    }

    public int getTaobao() {
        return taobao;
    }

    public void setTaobao(int taobao) {
        this.taobao = taobao;
    }
}
