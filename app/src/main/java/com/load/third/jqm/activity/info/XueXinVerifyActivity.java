package com.load.third.jqm.activity.info;

import android.app.Activity;
import android.content.Context;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ScrollView;
import android.widget.TextView;

import com.alibaba.fastjson.JSONObject;
import com.load.third.jqm.ImgUtil.ImageViewUtils;
import com.load.third.jqm.MyApp;
import com.load.third.jqm.R;
import com.load.third.jqm.bean.UserDao;
import com.load.third.jqm.http.ApiClient;
import com.load.third.jqm.http.OkHttpClientManager;
import com.load.third.jqm.http.result.DataJsonResult;
import com.load.third.jqm.httpUtil.TokenLoginUtil;
import com.load.third.jqm.tips.DialogUtils;
import com.load.third.jqm.tips.ProgressDialog;
import com.load.third.jqm.tips.ToastUtils;
import com.load.third.jqm.utils.StringUtils;
import com.load.third.jqm.utils.TextWatcherUtil;
import com.squareup.okhttp.Request;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import me.everything.android.ui.overscroll.OverScrollDecoratorHelper;

import static com.load.third.jqm.httpUtil.TokenLoginUtil.MSG_TOKEN_LOGIN_SUCCESS;

/**
 * 学信认证
 */
public class XueXinVerifyActivity extends Activity {

    @BindView(R.id.edit_name)
    EditText editName;
    @BindView(R.id.edit_psw)
    EditText editPsw;
    @BindView(R.id.iv_verify_code)
    ImageView ivVerifyCode;
    @BindView(R.id.edit_verify_code)
    EditText editVerifyCode;
    @BindView(R.id.tv_get_verify)
    TextView tvGetVerify;
    @BindView(R.id.btn_next)
    Button btnNext;
    @BindView(R.id.scrollView)
    ScrollView scrollView;
    @BindView(R.id.iv_back)
    ImageView ivBack;
    @BindView(R.id.tv_title)
    TextView tvTitle;
    private Context context;
    private String captcha;
    private Handler handler = new Handler( ) {
        public void handleMessage(Message msg) {
            switch (msg.what) {
                case MSG_TOKEN_LOGIN_SUCCESS:
                    studyAuth( );
                    break;
            }
        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_xue_xin_verify);
        ButterKnife.bind(this);
        context = this;
        initView( );
    }

    private void initView() {
        tvTitle.setText("学信认证");
        OverScrollDecoratorHelper.setUpOverScroll(scrollView);
        addTextListener( );
    }

    private void addTextListener() {
        View[] viewArr = {editName, editPsw, editVerifyCode};
        TextWatcherUtil watcherListener = new TextWatcherUtil(viewArr, btnNext);
        TextWatcherUtil.setListener(viewArr, watcherListener);
    }

    private void getVerifyImg() {
        if (StringUtils.isBlank(StringUtils.getTextValue(editName))) {
            ToastUtils.showToast(context, "请输入学信账号");
            return;
        }
        if (StringUtils.isBlank(StringUtils.getTextValue(editPsw))) {
            ToastUtils.showToast(context, "请输入密码");
            return;
        }
        captcha = "";
        if (!MyApp.isNeedUpdate) {
            TokenLoginUtil.loginWithToken(context, handler);
        }
    }

    private void studyAuth() {
        ProgressDialog.showProgressBar(context, "请稍后...");
        String token = UserDao.getInstance(context).getToken( );
        String username = StringUtils.getTextValue(editName);
        String password = StringUtils.getTextValue(editPsw);
        ApiClient.getInstance( ).studyAuth(token, username, password, captcha, new OkHttpClientManager.ResultCallback<DataJsonResult<JSONObject>>( ) {

            @Override
            public void onError(Request request, Exception e, String error) {
                ProgressDialog.cancelProgressBar( );
                ToastUtils.showToast(context, "网络请求失败");
                Log.e("http_msg", error);
            }

            @Override
            public void onResponse(DataJsonResult<JSONObject> response) {
                ProgressDialog.cancelProgressBar( );
                if (response.getSuccess( ) == "true") {
                    ToastUtils.showToast(context, "认证成功");
                    finish( );
                } else if (response.getCode( ) == 1005) {
                    Log.e("http_msg", response.getMessage( ));
                    String url = response.getData( ).getString("url");
                    ImageViewUtils.displayImage(context, url, ivVerifyCode);
                } else
                    ToastUtils.showToast(context, response.getMessage( ));
            }
        });
    }

    private void back() {
        DialogUtils.getInstance(context).showTipsDialog("是否退出学信认证？", new View.OnClickListener( ) {
            @Override
            public void onClick(View v) {
                finish( );
            }
        });
    }

    @Override
    public void onBackPressed() {
        back( );
    }

    @OnClick({R.id.tv_get_verify, R.id.btn_next, R.id.iv_back})
    public void onViewClicked(View view) {
        switch (view.getId( )) {
            case R.id.tv_get_verify:
                getVerifyImg( );
                break;
            case R.id.btn_next:
                captcha = StringUtils.getTextValue(editVerifyCode);
                if (!MyApp.isNeedUpdate) {
                    TokenLoginUtil.loginWithToken(context, handler);
                }
                break;
            case R.id.iv_back:
                back( );
                break;
        }
    }
}
