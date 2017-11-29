package com.load.third.jqm.bean;

import android.content.Context;

import com.load.third.jqm.MyApp;
import com.load.third.jqm.utils.MySharedPreference;

public class UserDao {
    private static UserDao dao;
    Context mContext;
    private static MySharedPreference mys;

    public UserDao() {
    }

    public static UserDao getInstance(Context context) {
        if (dao == null) {
            dao = new UserDao();
        }
        return dao;
    }

    static {
        dao = new UserDao();
        dao.mContext = MyApp.getContext();
        mys = new MySharedPreference(dao.mContext);
    }

    public void setToken(String token) {
        mys.setKeyStr("token", token);
    }

    public String getToken() {
        String account = mys.getKeyStr("token");
        return account;
    }

    public void setUserId(int userId) {
        mys.setKeyInt("userId", userId);
    }

    public int getUserId() {
        int userId = mys.getKeyInt("userId");
        return userId;
    }

    public String getMobile() {
        String mobile = mys.getKeyStr("mobile");
        return mobile;
    }

    public void setMobile(String mobile) {
        mys.setKeyStr("mobile", mobile);
    }

    public String getCid() {
        String cid = mys.getKeyStr("cid");
        return cid;
    }

    public void setCid(String cid) {
        mys.setKeyStr("cid", cid);
    }

    public boolean getAddress_list() {
        boolean mobile = mys.getKeyBoolean("address_list");
        return mobile;
    }

    public void setAddress_list(boolean address_list) {
        mys.setKeyBoolean("address_list", address_list);
    }

    public void setTaobao(int taobao) {
        mys.setKeyInt("taobao", taobao);
    }

    public int getTaobao() {
        int taobao = mys.getKeyInt("taobao");
        return taobao;
    }

    public void setAllData(UserBean userBean) {
        setToken(userBean.getToken());
        setAllDataWithoutToken(userBean);
    }

    public void setAllDataWithoutToken(UserBean userBean) {
        setUserId(userBean.getUser_id());
        setMobile(userBean.getMobile());
        setCid(userBean.getCid());
        setAddress_list(userBean.getAddress_list());
        setTaobao(userBean.getTaobao());
    }

    public void clearAll() {

    }
}
