package com.load.third.jqm.activity.info;

import android.content.Context;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ScrollView;
import android.widget.TextView;

import com.load.third.jqm.MyApp;
import com.load.third.jqm.R;
import com.load.third.jqm.activity.BaseActivity;
import com.load.third.jqm.bean.UserBean;
import com.load.third.jqm.bean.newBean.BankName;
import com.load.third.jqm.newHttp.ApiException;
import com.load.third.jqm.newHttp.Apis;
import com.load.third.jqm.newHttp.BaseResponse;
import com.load.third.jqm.newHttp.CommonObserver;
import com.load.third.jqm.newHttp.CustomConsumer;
import com.load.third.jqm.newHttp.UrlParams;
import com.load.third.jqm.tips.DialogUtils;
import com.load.third.jqm.tips.ToastUtils;
import com.load.third.jqm.utils.IntentUtils;
import com.load.third.jqm.utils.StringUtils;

import java.util.HashMap;
import java.util.Map;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import io.reactivex.Observable;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.Disposable;
import io.reactivex.functions.Function;
import io.reactivex.schedulers.Schedulers;
import me.everything.android.ui.overscroll.OverScrollDecoratorHelper;

public class BindBankCardActivity extends BaseActivity {

    @BindView(R.id.edit_bank_card)
    EditText editBankCard;
    @BindView(R.id.tv_bank_name)
    TextView tvBankName;
    @BindView(R.id.edit_bank_detail_name)
    EditText editBankDetailName;
    @BindView(R.id.btn_next)
    Button btnNext;
    @BindView(R.id.scrollView)
    ScrollView scrollView;
    @BindView(R.id.iv_back)
    ImageView ivBack;
    @BindView(R.id.tv_title)
    TextView tvTitle;
    private Context context;
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_bind_bank_card);
        ButterKnife.bind(this);
        context = this;
        initView();
    }

    private void initView() {
        tvTitle.setText("银行卡绑定");
        OverScrollDecoratorHelper.setUpOverScroll(scrollView);
        addTextListener();
    }

    private void postInfo() {
        apiRetrofit.getLoginWithToken(Apis.loginWithToken.getUrl())
                .flatMap(new Function<BaseResponse<UserBean>, Observable<BaseResponse<String>>>() {
                    @Override
                    public Observable<BaseResponse<String>> apply(BaseResponse<UserBean> userBeanBaseResponse) throws Exception {
                        if (userBeanBaseResponse.getSuccess()) {
                            String bankcard = StringUtils.getTextValue(editBankCard);
                            String bankname = StringUtils.getTextValue(tvBankName);
                            String bankAccount = StringUtils.getTextValue(editBankDetailName);
                            Map<String, Object> params = new HashMap<>();
                            params.put("bankcard", bankcard);
                            params.put("bankname", bankname);
                            params.put("bankAccount", bankAccount);
                            return apiRetrofit.getBindBankCard(UrlParams.getUrl(Apis.bindBankCard.getUrl(), params));
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
                        IntentUtils.toActivity(context, BindIdCardActivity.class);
                        ToastUtils.showToast(context, "绑定成功");
                        finish();
                    }

                    @Override
                    public void doFail(String msg) {
                        ToastUtils.showToast("网络异常");
                    }
                });
//        String token = UserDao.getInstance(context).getToken();
//        if (StringUtils.isNotBlank(token)) {
//            ProgressDialog.showProgressBar(context, "请稍后...");
//            String bankcard = StringUtils.getTextValue(editBankCard);
//            String bankname = StringUtils.getTextValue(tvBankName);
//            String bankAccount = StringUtils.getTextValue(editBankDetailName);
//            ApiClient.getInstance().bindBankCard(token, bankcard, bankname, bankAccount, new OkHttpClientManager.ResultCallback<DataJsonResult<String>>() {
//
//                @Override
//                public void onError(Request request, Exception e, String error) {
//                    ProgressDialog.cancelProgressBar();
//                    ToastUtils.showToast(context, "网络请求失败");
//                }
//
//                @Override
//                public void onResponse(DataJsonResult<String> response) {
//                    ProgressDialog.cancelProgressBar();
//                    if (response.getSuccess() == "true") {
//                        IntentUtils.toActivity(context, BindIdCardActivity.class);
//                        ToastUtils.showToast(context, "绑定成功");
//                        finish();
//                    } else {
//                        ToastUtils.showToast(context, response.getMessage());
//                    }
//                }
//            });
//        }
    }

    //    由银行卡号判断所属银行
    private void getBankName(String card) {
        tvBankName.setText("");
        tvBankName.setEnabled(false);
        Map<String, Object> params = new HashMap<>();
        params.put("bankcard", card);
        submitTask(apiRetrofit.getBankName(UrlParams.getUrl(Apis.getBankName.getUrl(), params)), new CommonObserver<BankName>() {

            @Override
            public void doSuccess(BaseResponse<BankName> result) {
                if (result.getData() != null) {
                    tvBankName.setText(result.getData().getName());
                }
            }

            @Override
            public void doFail(String msg) {
                super.doFail(msg);
                tvBankName.setText("识别银行卡失败，点击重试");
                tvBankName.setEnabled(true);

            }
        });
//        ApiClient.getInstance().getBankName(card, new OkHttpClientManager.ResultCallback<DataJsonResult<JSONObject>>() {
//
//            @Override
//            public void onError(Request request, Exception e, String error) {
//                tvBankName.setText("识别银行卡失败，点击重试");
//                tvBankName.setEnabled(true);
//            }
//
//            @Override
//            public void onResponse(DataJsonResult<JSONObject> response) {
//                if (response.getSuccess() == "true") {
//                    String bankName = response.getData().getString("name");
//                    tvBankName.setText(bankName);
//                } else {
//                    tvBankName.setText("未识别的银行卡");
//                }
//            }
//        });
    }

    private void addTextListener() {
        editBankCard.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                btnNext.setEnabled(checkText());
            }

            @Override
            public void afterTextChanged(Editable s) {
                String card = StringUtils.getTextValue(editBankCard);
                getBankName(card);
            }
        });
        editBankDetailName.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                btnNext.setEnabled(checkText());
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });
    }

    private boolean checkText() {
        if (StringUtils.getTextValue(editBankCard).length() <= 0) {
            return false;
        } else if (StringUtils.getTextValue(tvBankName).length() <= 0) {
            return false;
        } else if (StringUtils.getTextValue(editBankDetailName).length() <= 0) {
            return false;
        }
        return true;
    }

    private void back() {
        DialogUtils.getInstance(context).showTipsDialog("是否退出银行卡绑定？", new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                DialogUtils.getInstance(context).dismiss();
                finish();
            }
        });
    }

    @Override
    public void onBackPressed() {
        back();
    }

    @OnClick({R.id.tv_bank_name, R.id.btn_next, R.id.iv_back})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.tv_bank_name:
                String card = StringUtils.getTextValue(editBankCard);
                getBankName(card);
                break;
            case R.id.btn_next:
                if (!MyApp.isNeedUpdate) {
//                    TokenLoginUtil.loginWithToken(context, handler);
                    postInfo();
                }
                break;
            case R.id.iv_back:
                back();
                break;
            default:
                break;
        }
    }
}
