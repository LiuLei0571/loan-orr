package com.load.third.jqm.httpUtil;

import android.content.Context;
import android.os.Handler;
import android.os.Message;
import android.util.Log;

import com.alibaba.fastjson.JSONObject;
import com.load.third.jqm.activity.info.MyInfoFirstActivity;
import com.load.third.jqm.bean.HomeExpenseDataBean;
import com.load.third.jqm.bean.RepaymentDataBean;
import com.load.third.jqm.bean.UserDao;
import com.load.third.jqm.fragment.HomeFragment;
import com.load.third.jqm.http.ApiClient;
import com.load.third.jqm.http.OkHttpClientManager;
import com.load.third.jqm.http.result.DataJsonResult;
import com.load.third.jqm.tips.DialogUtils;
import com.load.third.jqm.tips.ProgressDialog;
import com.load.third.jqm.tips.ToastUtils;
import com.load.third.jqm.utils.Consts;
import com.load.third.jqm.utils.IntentUtils;
import com.load.third.jqm.utils.StringUtils;
import com.squareup.okhttp.Request;

/**
 * Created by Administrator on 2017/4/12.
 */

public class HomeGetUtils {
    @Deprecated
    public static void getStatus(final Context context, final Handler handler) {
        String token = UserDao.getInstance(context).getToken();
        ApiClient.getInstance().getStatus(token, new OkHttpClientManager.ResultCallback<DataJsonResult<JSONObject>>() {

            @Override
            public void onError(Request request, Exception e, String error) {
                ToastUtils.showToast(context, "用户状态信息获取失败");
                Log.e("http_msg", "用户状态信息获取，网络请求失败");
                Message message = handler.obtainMessage();
                message.what = HomeFragment.MSG_GET_STATUS_ERROR;
                handler.sendMessage(message);
            }

            @Override
            public void onResponse(DataJsonResult<JSONObject> response) {
                if (response.getSuccess() == "true") {
                    Message message = handler.obtainMessage();
                    message.what = HomeFragment.MSG_GET_STATUS;
                    message.obj = response.getData().getIntValue("lend_status");
                    handler.sendMessage(message);
                    Log.d("http_msg", "用户状态-----" + response.getData().getIntValue("lend_status"));
                } else {
                    ToastUtils.showToast(context, response.getMessage());
                    Log.e("http_msg", "用户状态信息获取" + response.getMessage());
                    Message message = handler.obtainMessage();
                    message.what = HomeFragment.MSG_GET_STATUS_ERROR;
                    handler.sendMessage(message);
                }
            }
        });
    }

    @Deprecated
    public static void getHomeExpenseData(final Context context, final Handler handler) {
        ProgressDialog.showProgressBar(context, "请稍后...");
        ApiClient.getInstance().getHomeExpenseData(new OkHttpClientManager.ResultCallback<DataJsonResult<HomeExpenseDataBean>>() {

            @Override
            public void onError(Request request, Exception e, String error) {
                ProgressDialog.cancelProgressBar();
                ToastUtils.showToast(context, "息费信息获取失败");
                Log.e("http_msg", "息费信息获取，网络请求失败");
                Message message = handler.obtainMessage();
                message.what = HomeFragment.MSG_GET_STATUS_ERROR;
                handler.sendMessage(message);
            }

            @Override
            public void onResponse(DataJsonResult<HomeExpenseDataBean> response) {
                ProgressDialog.cancelProgressBar();
                if (response.getSuccess() == "true") {
                    Message message = handler.obtainMessage();
                    message.what = HomeFragment.MSG_GET_EXPENSE_DATA;
                    message.obj = response.getData().getList();
                    handler.sendMessage(message);
                    Log.d("http_msg", "息费信息获取成功");
                } else {
                    ToastUtils.showToast(context, response.getMessage());
                    Log.e("http_msg", "息费信息获取" + response.getMessage());
                }
            }
        });
    }

    @Deprecated
    public static void getRepayment(final Context context, final Handler handler) {
        String token = UserDao.getInstance(context).getToken();
        ApiClient.getInstance().getRepaymentData(token, new OkHttpClientManager.ResultCallback<DataJsonResult<RepaymentDataBean>>() {

            @Override
            public void onError(Request request, Exception e, String error) {
                ToastUtils.showToast(context, "还款信息获取失败");
                Log.e("http_msg", "还款信息获取，网络请求失败");
                Message message = handler.obtainMessage();
                message.what = HomeFragment.MSG_GET_STATUS_ERROR;
                handler.sendMessage(message);
            }

            @Override
            public void onResponse(DataJsonResult<RepaymentDataBean> response) {
                if (response.getSuccess() == "true") {
                    Message message = handler.obtainMessage();
                    message.what = HomeFragment.MSG_GET_REPAYMENT_DATA;
                    message.obj = response.getData();
                    handler.sendMessage(message);
                    Log.d("http_msg", "还款信息获取成功");
                } else {
                    ToastUtils.showToast(context, response.getMessage());
                    Log.e("http_msg", "还款信息获取" + response.getMessage());
                }
            }
        });
    }

    @Deprecated
    public static void postBorrowInfo(final Context context, final int status, String day, String money) {
        ProgressDialog.showProgressBar(context, "请稍后...");
        String token = UserDao.getInstance(context).getToken();
        ApiClient.getInstance().postBorrowInfo(token, day, money, new OkHttpClientManager.ResultCallback<DataJsonResult<JSONObject>>() {

            @Override
            public void onError(Request request, Exception e, String error) {
                ProgressDialog.cancelProgressBar();
                ToastUtils.showToast(context, "网络请求失败");
            }

            @Override
            public void onResponse(DataJsonResult<JSONObject> response) {
                ProgressDialog.cancelProgressBar();
                if (response.getSuccess() == "true") {
                    if (status == Consts.STATUS_BORROW_FIRST)
                        IntentUtils.toActivity(context, MyInfoFirstActivity.class);
                    else if (status == Consts.STATUS_BORROW_AGAIN)
                        checkPhone(context);
                } else {
                    String frozen_time = response.getData().getString("frozen_time");
                    if (StringUtils.isBlank(frozen_time))
                        ToastUtils.showToast(context, response.getMessage());
                    else
                        DialogUtils.getInstance(context).showOkTipsDialog(response.getMessage() + "\n账号还需" + frozen_time + "天解冻");
                }
            }
        });
    }

    @Deprecated
    public static void checkPhone(final Context context) {
        ProgressDialog.showProgressBar(context, "请稍后...");
        String token = UserDao.getInstance(context).getToken();
        ApiClient.getInstance().checkPhone(token, new OkHttpClientManager.ResultCallback<DataJsonResult<JSONObject>>() {

            @Override
            public void onError(Request request, Exception e, String error) {
                ProgressDialog.cancelProgressBar();
                ToastUtils.showToast(context, "网络请求失败");
            }

            @Override
            public void onResponse(DataJsonResult<JSONObject> response) {
                ProgressDialog.cancelProgressBar();
                if (response.getSuccess() == "true") {
                    IntentUtils.toWebViewActivity(context, "手机验证", response.getData().getString("redirectUrl"));
                } else {
                    ToastUtils.showToast(context, response.getMessage());
                }
            }
        });
    }
}
