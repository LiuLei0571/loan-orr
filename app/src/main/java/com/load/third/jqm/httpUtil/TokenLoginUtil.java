package com.load.third.jqm.httpUtil;

import android.content.Context;
import android.os.Handler;
import android.os.Message;
import android.util.Log;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.load.third.jqm.MyApp;
import com.load.third.jqm.bean.UserBean;
import com.load.third.jqm.bean.UserDao;
import com.load.third.jqm.fragment.HomeFragment;
import com.load.third.jqm.http.ApiClient;
import com.load.third.jqm.http.OkHttpClientManager;
import com.load.third.jqm.http.result.DataJsonResult;
import com.load.third.jqm.tips.ToastUtils;
import com.load.third.jqm.tips.UpdateApkDialog;
import com.load.third.jqm.utils.StringUtils;
import com.squareup.okhttp.Request;

/**
 * Created by Administrator on 2017/4/24.
 */

public class TokenLoginUtil {
    public static final int MSG_TOKEN_LOGIN_SUCCESS = 101;

    public static void loginWithToken(final Context context, final Handler handler) {
        String token = UserDao.getInstance(context).getToken( );
        Log.e("http_msg", "login token:" + token);
        if (StringUtils.isNotBlank(token)) {
            ApiClient.getInstance( ).loginWithToken(token, new OkHttpClientManager.ResultCallback<DataJsonResult<JSONObject>>( ) {

                @Override
                public void onError(Request request, Exception e, String error) {
                    ToastUtils.showToast(context, "网络请求失败");
                    Log.e("http_msg", "token登陆网络请求失败");
                    Message message = handler.obtainMessage( );
                    message.what = HomeFragment.MSG_GET_STATUS_ERROR;
                    handler.sendMessage(message);
                    MyApp.isNeedUpdate = false;
                }

                @Override
                public void onResponse(DataJsonResult<JSONObject> response) {
                    if (response.getSuccess( ) == "true") {
                        Log.i("http_msg", "token登陆成功");
                        UserBean userBean = JSON.parseObject(response.getData( ).toString( ), UserBean.class);
                        UserDao.getInstance(context).setAllDataWithoutToken(userBean);
                        Message message = handler.obtainMessage( );
                        message.what = MSG_TOKEN_LOGIN_SUCCESS;
                        handler.sendMessage(message);
                        MyApp.isNeedUpdate = false;
                    } else {
                        Log.e("http_msg", "token登陆 " + response.getMessage( ));
                        if (response.getCode( ) == 1025) {//token过期
                            MyApp.isNeedUpdate = false;
                            ToastUtils.showToast(context, "登录已过期，请重新登录");
                            UserDao.getInstance(context).setToken("");
                        } else if (response.getCode( ) == 1022) {
                            MyApp.isNeedUpdate = true;
                            Log.d("http_msg", "需要更新安装包");
                            final String android_url = response.getData( ).getString("android_url");
                            if (StringUtils.isNotBlank(android_url)) {
                                UpdateApkDialog.getInstance(context).showDialog(android_url);
                            }
                        } else {
                            MyApp.isNeedUpdate = false;
                        }
                    }
                }
            });
        }
    }
}
