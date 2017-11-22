package com.load.third.jqm.activity.home;

import android.app.Activity;
import android.content.Context;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ScrollView;
import android.widget.TextView;

import com.load.third.jqm.MyApp;
import com.load.third.jqm.R;
import com.load.third.jqm.bean.RepaymentUserBean;
import com.load.third.jqm.bean.UserDao;
import com.load.third.jqm.http.ApiClient;
import com.load.third.jqm.http.OkHttpClientManager;
import com.load.third.jqm.http.result.DataJsonResult;
import com.load.third.jqm.httpUtil.TokenLoginUtil;
import com.load.third.jqm.tips.ProgressDialog;
import com.load.third.jqm.tips.ToastUtils;
import com.load.third.jqm.utils.IntentUtils;
import com.load.third.jqm.utils.StringUtils;
import com.load.third.jqm.utils.TextWatcherUtil;
import com.load.third.jqm.utils.Urls;
import com.squareup.okhttp.Request;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import me.everything.android.ui.overscroll.OverScrollDecoratorHelper;

import static com.load.third.jqm.httpUtil.TokenLoginUtil.MSG_TOKEN_LOGIN_SUCCESS;

/**
 * 还款
 */
public class RepaymentActivity extends Activity {
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
    private Handler handler = new Handler( ) {
        public void handleMessage(Message msg) {
            switch (msg.what) {
                case MSG_TOKEN_LOGIN_SUCCESS:
                    getRepaymentUser( );
                    break;
            }
        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_repayment);
        ButterKnife.bind(this);
        context = this;
        initView( );
    }

    private void initView() {
        tvTitle.setText("还款");
        tvRepaymentMoney.setText(getIntent( ).getStringExtra(MONEY_REPAY));
        OverScrollDecoratorHelper.setUpOverScroll(scrollView);
        addTextListener( );
        if (!MyApp.isNeedUpdate) {
            TokenLoginUtil.loginWithToken(context, handler);
        }
        if (!MyApp.isNeedUpdate) {
            TokenLoginUtil.loginWithToken(context, handler);
        }
    }

    private void addTextListener() {
        View[] viewArr = {editName, editIdCard, editBankCard};
        TextWatcherUtil watcherListener = new TextWatcherUtil(viewArr, btnNext);
        TextWatcherUtil.setListener(viewArr, watcherListener);
    }

    private void getRepaymentUser() {
        ProgressDialog.showProgressBar(context, "请稍后...");
        String token = UserDao.getInstance(context).getToken( );
        ApiClient.getInstance( ).getRepaymentUser(token, new OkHttpClientManager.ResultCallback<DataJsonResult<RepaymentUserBean>>( ) {

            @Override
            public void onError(Request request, Exception e, String error) {
                ProgressDialog.cancelProgressBar( );
                ToastUtils.showToast(context, "网络请求失败");
            }

            @Override
            public void onResponse(DataJsonResult<RepaymentUserBean> response) {
                ProgressDialog.cancelProgressBar( );
                if (response.getSuccess( ) == "true") {
                    editName.setText(response.getData( ).getName( ));
                    editIdCard.setText(response.getData( ).getIdcard( ));
                    editBankCard.setText(response.getData( ).getBankcard( ));
                } else {
                    ToastUtils.showToast(context, response.getMessage( ));
                }
            }
        });
    }

    private void repayment() {
        String token = UserDao.getInstance(context).getToken( );
        String name = StringUtils.getTextValue(editName);
        String idcard = StringUtils.getTextValue(editIdCard);
        String bankcard = StringUtils.getTextValue(editBankCard);
        IntentUtils.toWebViewActivity(context, "还款", Urls.url_repayment + "?token=" + token + "&name=" + name + "&idcard=" + idcard + "&bankcard=" + bankcard);
    }

    @OnClick({R.id.btn_next, R.id.iv_back})
    public void onViewClicked(View view) {
        switch (view.getId( )) {
            case R.id.btn_next:
                repayment( );
                break;
            case R.id.iv_back:
                finish( );
                break;
        }
    }
}
