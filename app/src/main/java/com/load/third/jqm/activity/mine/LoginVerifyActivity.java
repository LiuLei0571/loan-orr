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
import java.util.LinkedHashMap;
import java.util.Map;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

import static com.load.third.jqm.activity.mine.LoginActivity.mobile;

/** 短信登录
 * @author liulei
 */
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
        Map<String, Object> params = new HashMap<>();
        params.put("mobile", mobile);
        submitTask(apiRetrofit.getSmsCode(UrlParams.getUrl(Apis.smsCode.getUrl(), params)), new CommonObserver() {
            @Override
            public void doSuccess(BaseResponse result) {
                ToastUtils.showToast(context, "发送验证码成功");
                new CountDownTimerUtil(120 * 1000, 1000, tvVerifyCode).start();
            }
        });

    }

    private void login() {
        String code = StringUtils.getTextValue(editVerifyCode);
        ProgressDialog.showProgressBar(context, "登录中...");
        String version = VersionUtils.getVersionName(context);
        Log.e("http_msg", "version code:" + VersionUtils.getVersionName(context));
        Map<String, Object> params = new LinkedHashMap<>();
        params.put("mobile", mobile);
        params.put("code", code);
        params.put("mobile_type", "android");
        params.put("version", UrlHelp.getEncode(version + ""));
        submitTask(apiRetrofit.getLogin(UrlParams.getUrl(Apis.login.getUrl(), params)), new CommonObserver<UserBean>() {
            @Override
            public void doSuccess(BaseResponse<UserBean> response) {
                if (response.getData() != null) {
                    UserBean userBean = response.getData();
                    userBean.setToken(UrlHelp.getEncode(userBean.getToken()));
                    UserDao.getInstance(context).setAllData(userBean);
                    IntentUtils.toMainActivity(context);
                    ToastUtils.showToast(context, "登录成功");
                }

            }
        });

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
            default:
                break;
        }
    }
}
