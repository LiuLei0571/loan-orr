package com.load.third.jqm.activity.home;

import android.content.Context;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ScrollView;
import android.widget.TextView;

import com.load.third.jqm.MyApp;
import com.load.third.jqm.R;
import com.load.third.jqm.activity.BaseActivity;
import com.load.third.jqm.bean.RepaymentUserBean;
import com.load.third.jqm.bean.UserBean;
import com.load.third.jqm.bean.UserDao;
import com.load.third.jqm.newHttp.ApiException;
import com.load.third.jqm.newHttp.Apis;
import com.load.third.jqm.newHttp.BaseResponse;
import com.load.third.jqm.newHttp.CommonObserver;
import com.load.third.jqm.newHttp.CustomConsumer;
import com.load.third.jqm.utils.IntentUtils;
import com.load.third.jqm.utils.StringUtils;
import com.load.third.jqm.utils.TextWatcherUtil;
import com.load.third.jqm.utils.Urls;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import io.reactivex.Observable;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.Disposable;
import io.reactivex.functions.Function;
import io.reactivex.schedulers.Schedulers;
import me.everything.android.ui.overscroll.OverScrollDecoratorHelper;

/**
 * 还款
 */
public class RepaymentActivity extends BaseActivity {
    public static final String MONEY_REPAY = "money_repay";

    @BindView(R.id.tv_repayment_money)
    TextView tvRepaymentMoney;
    @BindView(R.id.edit_name)
    EditText editName;
    @BindView(R.id.edit_id_card)
    EditText editIdCard;
    @BindView(R.id.edit_bank_card)
    EditText editBankCard;
    @BindView(R.id.btn_next)
    Button btnNext;
    @BindView(R.id.scrollView)
    ScrollView scrollView;
    @BindView(R.id.iv_back)
    ImageView ivBack;
    @BindView(R.id.tv_title)
    TextView tvTitle;
    @BindView(R.id.tv_right)
    TextView tvRight;

    private Context context;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_repayment);
        ButterKnife.bind(this);
        context = this;
        initView();
    }

    private void initView() {
        tvTitle.setText("还款");
        tvRepaymentMoney.setText(getIntent().getStringExtra(MONEY_REPAY));
        OverScrollDecoratorHelper.setUpOverScroll(scrollView);
        addTextListener();
        if (!MyApp.isNeedUpdate) {
            getRepaymentUser();
        }
    }

    private void addTextListener() {
        View[] viewArr = {editName, editIdCard, editBankCard};
        TextWatcherUtil watcherListener = new TextWatcherUtil(viewArr, btnNext);
        TextWatcherUtil.setListener(viewArr, watcherListener);
    }

    private void getRepaymentUser() {

        apiRetrofit.getLoginWithToken(Apis.loginWithToken.getUrl())
                .flatMap(new Function<BaseResponse<UserBean>, Observable<BaseResponse<RepaymentUserBean>>>() {
                    @Override
                    public Observable<BaseResponse<RepaymentUserBean>> apply(BaseResponse<UserBean> userBeanBaseResponse) throws Exception {
                        if (userBeanBaseResponse.getSuccess()) {
                            return apiRetrofit.getRepaymentUser(Apis.getRepaymentUser.getUrl());
                        } else {
                            return Observable.error(new ApiException(userBeanBaseResponse.getMessage()));
                        }
                    }
                })
                .subscribeOn(Schedulers.io())
                .doOnSubscribe(new CustomConsumer<Disposable>(getBaseActivity()))
                .subscribeOn(AndroidSchedulers.mainThread())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new CommonObserver<RepaymentUserBean>() {
                    @Override
                    public void doSuccess(BaseResponse<RepaymentUserBean> response) {
                        editName.setText(response.getData().getName());
                        editIdCard.setText(response.getData().getIdcard());
                        editBankCard.setText(response.getData().getBankcard());
                    }
                });
    }

    private void repayment() {
        String token = UserDao.getInstance(context).getToken();
        String name = StringUtils.getTextValue(editName);
        String idcard = StringUtils.getTextValue(editIdCard);
        String bankcard = StringUtils.getTextValue(editBankCard);
        IntentUtils.toWebViewActivity(context, "还款", Urls.url_repayment + "?token=" + token + "&name=" + name + "&idcard=" + idcard + "&bankcard=" + bankcard);
    }

    @OnClick({R.id.btn_next, R.id.iv_back})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.btn_next:
                repayment();
                break;
            case R.id.iv_back:
                finish();
                break;
            default:
                break;
        }
    }
}
