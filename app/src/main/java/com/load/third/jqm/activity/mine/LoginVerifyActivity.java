package com.load.third.jqm.activity.mine;

import android.content.Context;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import com.load.third.jqm.R;
import com.load.third.jqm.activity.BaseActivity;
import com.load.third.jqm.bean.UserBean;
import com.load.third.jqm.bean.UserDao;
import com.load.third.jqm.help.UrlHelp;
import com.load.third.jqm.newHttp.Apis;
import com.load.third.jqm.newHttp.BaseResponse;
import com.load.third.jqm.newHttp.CommonObserver;
import com.load.third.jqm.newHttp.UrlParams;
import com.load.third.jqm.tips.ProgressDialog;
import com.load.third.jqm.tips.ToastUtils;
import com.load.third.jqm.utils.CountDownTimerUtil;
import com.load.third.jqm.utils.IntentUtils;
import com.load.third.jqm.utils.StringUtils;
import com.load.third.jqm.utils.TextWatcherUtil;
import com.load.third.jqm.utils.VersionUtils;

import java.util.HashMap;
import java.util.Map;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

import static com.load.third.jqm.activity.mine.LoginActivity.mobile;

public class LoginVerifyActivity extends BaseActivity {

    @BindView(R.id.iv_back)
    ImageView ivBack;
    @BindView(R.id.tv_title)
    TextView tvTitle;
    @BindView(R.id.tv_right)
    TextView tvRight;
    @BindView(R.id.edit_verify_code)
    EditText editVerifyCode;
    @BindView(R.id.tv_verify_code)
    TextView tvVerifyCode;
    @BindView(R.id.btn_next)
    Button btnNext;

    private Context context;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login_verify);
        ButterKnife.bind(this);
        context = this;
        initView();
    }

    private void initView() {
        tvTitle.setText("登录/注册");
        addTextListener();
    }

    private void addTextListener() {
        View[] viewArr = {editVerifyCode};
        TextWatcherUtil watcherListener = new TextWatcherUtil(viewArr, btnNext);
        TextWatcherUtil.setListener(viewArr, watcherListener);
    }

    private void getSmsCode() {
        ProgressDialog.showProgressBar(context, "请稍后...");

        submitTask(apiRetrofit.getSmsCode(Apis.smsCode.getUrl()), new CommonObserver() {
            @Override
            public void doSuccess(BaseResponse response) {
                if (response.getSuccess() == "true") {
                    ToastUtils.showToast(context, "发送验证码成功");
                    new CountDownTimerUtil(120 * 1000, 1000, tvVerifyCode).start();
                } else {
                    ToastUtils.showToast(context, response.getMessage());
                }
            }
        });

//        ApiClient.getInstance().getSMScode(mobile, new OkHttpClientManager.ResultCallback<DataJsonResult<String>>() {
//
//            @Override
//            public void onError(Request request, Exception e, String error) {
//                ProgressDialog.cancelProgressBar();
//                ToastUtils.showToast(context, "网络请求失败");
//            }
//
//            @Override
//            public void onResponse(DataJsonResult<String> response) {
//                ProgressDialog.cancelProgressBar();
//                if (response.getSuccess() == "true") {
//                    ToastUtils.showToast(context, "发送验证码成功");
//                    new CountDownTimerUtil(120 * 1000, 1000, tvVerifyCode).start();
//                } else {
//                    ToastUtils.showToast(context, response.getMessage());
//                }
//            }
//        });
    }

    private void login() {
        String code = StringUtils.getTextValue(editVerifyCode);
        ProgressDialog.showProgressBar(context, "登录中...");
        String version = VersionUtils.getVersionName(context);
        Log.e("http_msg", "version code:" + VersionUtils.getVersionName(context));
        Map<String, Object> params = new HashMap<>();
        params.put("mobile", mobile);
        params.put("code", code);
        params.put("mobile_type", "android");
        params.put("version", version);
        submitTask(apiRetrofit.getLogin(UrlParams.getUrl(Apis.login.getUrl(), params)), new CommonObserver<UserBean>() {
            @Override
            public void doSuccess(BaseResponse<UserBean> response) {
                if (response.getData() != null) {
                    UserBean userBean = response.getData();
                    userBean.setToken(UrlHelp.getDecode(userBean.getToken()));
                    UserDao.getInstance(context).setAllData(userBean);
                    IntentUtils.toMainActivity(context);
                    ToastUtils.showToast(context, "登录成功");
                }

            }
        });

//        ApiClient.getInstance().login(mobile, code, "android", version, new OkHttpClientManager.ResultCallback<DataJsonResult<UserBean>>() {
//
//            @Override
//            public void onError(Request request, Exception e, String error) {
//                ProgressDialog.cancelProgressBar();
//                ToastUtils.showToast(context, "网络请求失败");
//                Log.e("http_msg", "登录失败");
//            }
//
//            @Override
//            public void onResponse(DataJsonResult<UserBean> response) {
//                ProgressDialog.cancelProgressBar();
//                if (response.getSuccess() == "true") {
//                    Log.d("http_msg", "登录成功");
////                    用户信息实体类
//                    UserBean userBean = response.getData();
//                    Log.d("http_msg", "response token:" + userBean.getToken());
//                    String token = userBean.getToken();
//                    //网络请求的token需要转码，将获得的token先转码
//                    try {
//                        token = URLEncoder.encode(userBean.getToken(), "UTF-8");
//                    } catch (UnsupportedEncodingException e) {
//                        e.printStackTrace();
//                    }
//                    userBean.setToken(token);
//                    //利用UserDao类使用MySharedPreference储存用户信息
//                    UserDao.getInstance(context).setAllData(userBean);
//                    UserDao.getInstance(context).setMobile(mobile);
//                    IntentUtils.toMainActivity(context);
//                    ToastUtils.showToast(context, "登录成功");
//                    Log.d("http_msg", "userBean token:" + UserDao.getInstance(context).getToken());
//                } else {
//                    ToastUtils.showToast(context, response.getMessage());
//                    Log.e("http_msg", "登录失败" + response.getMessage());
//                }
//            }
//        });
    }

    @OnClick({R.id.iv_back, R.id.tv_verify_code, R.id.btn_next})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.iv_back:
                finish();
                break;
            case R.id.tv_verify_code:
                getSmsCode();
                break;
            case R.id.btn_next:
                login();
                break;
        }
    }
}
