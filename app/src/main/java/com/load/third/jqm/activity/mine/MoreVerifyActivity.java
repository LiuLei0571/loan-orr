package com.load.third.jqm.activity.mine;

import android.content.Context;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.load.third.jqm.MyApp;
import com.load.third.jqm.R;
import com.load.third.jqm.activity.BaseActivity;
import com.load.third.jqm.activity.info.TaoBaoVerifyActivity;
import com.load.third.jqm.activity.info.XueXinVerifyActivity;
import com.load.third.jqm.bean.UserBean;
import com.load.third.jqm.bean.UserDao;
import com.load.third.jqm.newHttp.ApiException;
import com.load.third.jqm.newHttp.Apis;
import com.load.third.jqm.newHttp.BaseResponse;
import com.load.third.jqm.newHttp.CommonObserver;
import com.load.third.jqm.newHttp.CustomConsumer;
import com.load.third.jqm.utils.IntentUtils;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import io.reactivex.Observable;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.Disposable;
import io.reactivex.functions.Function;
import io.reactivex.schedulers.Schedulers;

public class MoreVerifyActivity extends BaseActivity {

    @BindView(R.id.iv_back)
    ImageView ivBack;
    @BindView(R.id.tv_title)
    TextView tvTitle;
    @BindView(R.id.tv_xuexin_status)
    TextView tvXuexinStatus;
    @BindView(R.id.ll_xuexin_verify)
    LinearLayout llXuexinVerify;
    @BindView(R.id.tv_taobao_status)
    TextView tvTaobaoStatus;
    @BindView(R.id.ll_taobao_verify)
    LinearLayout llTaobaoVerify;

    private Context context;
//    private Handler handler = new Handler() {
//        public void handleMessage(Message msg) {
//            switch (msg.what) {
//                case MSG_TOKEN_LOGIN_SUCCESS:
//                    isStudentAuth();
//                    break;
//            }
//        }
//    };

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_more_verify);
        ButterKnife.bind(this);
        context = this;
        initView();
    }

    @Override
    public void onResume() {
        super.onResume();
        if (!MyApp.isNeedUpdate) {
//            TokenLoginUtil.loginWithToken(context, handler);
            apiRetrofit.getLoginWithToken(Apis.loginWithToken.getUrl())
                    .flatMap(new Function<BaseResponse<UserBean>, Observable<BaseResponse<String>>>() {
                        @Override
                        public Observable<BaseResponse<String>> apply(BaseResponse<UserBean> userBeanBaseResponse) throws Exception {
                            if (userBeanBaseResponse.getSuccess()) {
                                return apiRetrofit.getIsStudentAuth(Apis.isStudentAuth.getUrl());

                            } else {
                                return Observable.error(new ApiException(userBeanBaseResponse.getMessage()));
                            }
                        }
                    })
                    .subscribeOn(Schedulers.io())
                    .doOnSubscribe(new CustomConsumer<Disposable>(getBaseActivity()))
                    .subscribeOn(AndroidSchedulers.mainThread())
                    .observeOn(AndroidSchedulers.mainThread())
                    .subscribe(new CommonObserver<String>() {
                        @Override
                        public void doSuccess(BaseResponse<String> result) {
                            tvXuexinStatus.setText("已认证");
                            tvXuexinStatus.setTextColor(getResources().getColor(R.color.main_color));
                            llXuexinVerify.setEnabled(false);
                        }

                        @Override
                        public void doFail(String msg) {
                            tvXuexinStatus.setText("未认证");
                            tvXuexinStatus.setTextColor(getResources().getColor(R.color.text_8d8d8d));
                            llXuexinVerify.setEnabled(true);
                        }
                    });

        }
    }

    private void initView() {
        tvTitle.setText("优先认证");
        if (UserDao.getInstance(context).getTaobao() == 1) {
            tvTaobaoStatus.setText("已认证");
            tvTaobaoStatus.setTextColor(getResources().getColor(R.color.main_color));
            llTaobaoVerify.setEnabled(false);
        } else {
            tvTaobaoStatus.setText("未认证");
            tvTaobaoStatus.setTextColor(getResources().getColor(R.color.text_8d8d8d));
            llTaobaoVerify.setEnabled(true);
        }
    }

    //是否学信认证，请求接口，设置界面
//    private void isStudentAuth() {
//        String token = UserDao.getInstance(context).getToken();
//        ApiClient.getInstance().isStudentAuth(token, new OkHttpClientManager.ResultCallback<DataJsonResult<String>>() {
//
//            @Override
//            public void onError(Request request, Exception e, String error) {
//            }
//
//            @Override
//            public void onResponse(DataJsonResult<String> response) {
//                if (response.getSuccess() == "true") {
//                    tvXuexinStatus.setText("已认证");
//                    tvXuexinStatus.setTextColor(getResources().getColor(R.color.main_color));
//                    llXuexinVerify.setEnabled(false);
//                } else {
//                    tvXuexinStatus.setText("未认证");
//                    tvXuexinStatus.setTextColor(getResources().getColor(R.color.text_8d8d8d));
//                    llXuexinVerify.setEnabled(true);
//                }
//            }
//        });
//    }

    @OnClick({R.id.iv_back, R.id.ll_xuexin_verify, R.id.ll_taobao_verify})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.iv_back:
                finish();
                break;
            case R.id.ll_xuexin_verify:
                IntentUtils.toActivity(context, XueXinVerifyActivity.class);
                break;
            case R.id.ll_taobao_verify:
                IntentUtils.toActivity(context, TaoBaoVerifyActivity.class);
                break;
            default:
                break;
        }
    }
}
