package com.load.third.jqm.bean;

import android.content.Context;

import com.load.third.jqm.MyApp;
import com.load.third.jqm.utils.MySharedPreference;

public class UserDao {
	private static UserDao dao;
	Context mContext;

	public UserDao() {
	}

	public static UserDao getInstance(Context context) {
		if (dao == null) {
			dao = new UserDao();
			dao.mContext = MyApp.getContext();
		}
		return dao;
	}
	static {
		dao = new UserDao();
		dao.mContext = MyApp.getContext();
	}

	public void setToken(String token) {
		MySharedPreference mys = new MySharedPreference(mContext);
		mys.setKeyStr("token", token);
	}

	public String getToken() {
		MySharedPreference mys = new MySharedPreference(mContext);
		String account = mys.getKeyStr("token");
		return account;
	}

	public void setUserId(int userId) {
		MySharedPreference mys = new MySharedPreference(mContext);
		mys.setKeyInt("userId", userId);
	}

	public int getUserId() {
		MySharedPreference mys = new MySharedPreference(mContext);
		int userId = mys.getKeyInt("userId");
		return userId;
	}

    public String getMobile() {
        MySharedPreference mys = new MySharedPreference(mContext);
        String mobile = mys.getKeyStr("mobile");
        return mobile;
    }

    public void setMobile(String mobile) {
        MySharedPreference mys = new MySharedPreference(mContext);
        mys.setKeyStr("mobile", mobile);
    }

	public String getCid() {
		MySharedPreference mys = new MySharedPreference(mContext);
		String cid = mys.getKeyStr("cid");
		return cid;
	}

	public void setCid(String cid) {
		MySharedPreference mys = new MySharedPreference(mContext);
		mys.setKeyStr("cid", cid);
	}

	public boolean getAddress_list() {
		MySharedPreference mys = new MySharedPreference(mContext);
		boolean mobile = mys.getKeyBoolean("address_list");
		return mobile;
	}

	public void setAddress_list(boolean address_list) {
		MySharedPreference mys = new MySharedPreference(mContext);
		mys.setKeyBoolean("address_list", address_list);
	}

	public void setTaobao(int taobao) {
		MySharedPreference mys = new MySharedPreference(mContext);
		mys.setKeyInt("taobao", taobao);
	}

	public int getTaobao() {
		MySharedPreference mys = new MySharedPreference(mContext);
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
}
