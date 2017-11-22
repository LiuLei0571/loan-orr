package com.load.third.jqm.activity.info;

import android.app.Activity;
import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ScrollView;
import android.widget.TextView;

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
import com.load.third.jqm.utils.IntentUtils;
import com.load.third.jqm.utils.PickerVewUtil;
import com.load.third.jqm.utils.StringUtils;
import com.load.third.jqm.utils.TextCheckUtil;
import com.squareup.okhttp.Request;

import java.util.Arrays;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import me.everything.android.ui.overscroll.OverScrollDecoratorHelper;

import static com.load.third.jqm.httpUtil.TokenLoginUtil.MSG_TOKEN_LOGIN_SUCCESS;

/**
 * 个人资料
 */
public class MyInfoFirstActivity extends Activity {

    @BindView(R.id.iv_back)
    ImageView ivBack;
    @BindView(R.id.tv_title)
    TextView tvTitle;
    @BindView(R.id.tv_right)
    TextView tvRight;
    @BindView(R.id.edit_name)
    EditText editName;
    @BindView(R.id.edit_id_card)
    EditText editIdCard;
    @BindView(R.id.edit_QQ)
    EditText editQQ;
    @BindView(R.id.edit_email)
    EditText editEmail;
    @BindView(R.id.tv_education)
    TextView tvEducation;
    @BindView(R.id.tv_marriage)
    TextView tvMarriage;
    @BindView(R.id.tv_child)
    TextView tvChild;
    @BindView(R.id.edit_address)
    EditText editAddress;
    @BindView(R.id.tv_address_time)
    TextView tvAddressTime;
    @BindView(R.id.scrollView)
    ScrollView scrollView;

    private Context context;
    private List<View> viewArr;
    private List<String> list_education;
    private List<String> list_marriage;
    private List<String> list_child;
    private List<String> list_address_time;
    private Handler handler = new Handler( ) {
        public void handleMessage(Message msg) {
            switch (msg.what) {
                case MSG_TOKEN_LOGIN_SUCCESS:
                    postInfo( );
                    break;
            }
        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_my_info_first);
        ButterKnife.bind(this);
        context = this;
        initView( );
    }

    private void initView() {
        tvTitle.setText("个人信息");
        tvRight.setText("下一步");
        OverScrollDecoratorHelper.setUpOverScroll(scrollView);
        viewArr = Arrays.asList(new View[]{editName, editIdCard, editQQ, editEmail, tvEducation,
                tvMarriage, tvChild, editAddress, tvAddressTime});
        list_education = Arrays.asList(getResources( ).getStringArray(R.array.list_education));
        list_marriage = Arrays.asList(getResources( ).getStringArray(R.array.list_marriage));
        list_child = Arrays.asList(getResources( ).getStringArray(R.array.list_child));
        list_address_time = Arrays.asList(getResources( ).getStringArray(R.array.list_address_time));
        setInfo( );
        addTextListener( );
    }

    private void setInfo() {
        SharedPreferences sp = getSharedPreferences("myInfoFirst", Context.MODE_PRIVATE);
        editName.setText(sp.getString("name", ""));
        editIdCard.setText(sp.getString("idcard", ""));
        editQQ.setText(sp.getString("qq", ""));
        editEmail.setText(sp.getString("email", ""));
        tvEducation.setText(sp.getString("education", ""));
        tvMarriage.setText(sp.getString("marriage", ""));
        tvChild.setText(sp.getString("child", ""));
        editAddress.setText(sp.getString("address", ""));
        tvAddressTime.setText(sp.getString("address_time", ""));
    }

    private void saveInfo() {
        SharedPreferences sp = getSharedPreferences("myInfoFirst", Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sp.edit( );
        editor.putString("name", StringUtils.getTextValue(editName));
        editor.putString("idcard", StringUtils.getTextValue(editIdCard));
        editor.putString("qq", StringUtils.getTextValue(editQQ));
        editor.putString("email", StringUtils.getTextValue(editEmail));
        editor.putString("education", StringUtils.getTextValue(tvEducation));
        editor.putString("marriage", StringUtils.getTextValue(tvMarriage));
        editor.putString("child", StringUtils.getTextValue(tvChild));
        editor.putString("address", StringUtils.getTextValue(editAddress));
        editor.putString("address_time", StringUtils.getTextValue(tvAddressTime));
        editor.commit( );
    }

    private void addTextListener() {
        TextWatcher textWatcher = new TextWatcher( ) {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                saveInfo( );
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        };
        for (int i = 0; i < viewArr.size( ); i++) {
            if (viewArr.get(i) instanceof EditText) {
                ((EditText) viewArr.get(i)).addTextChangedListener(textWatcher);
            }
            if (viewArr.get(i) instanceof TextView) {
                ((TextView) viewArr.get(i)).addTextChangedListener(textWatcher);
            }
        }
    }

    private void postInfo() {
        if (TextCheckUtil.checkText(viewArr)) {
            String token = UserDao.getInstance(context).getToken( );
            String name = StringUtils.getTextValue(editName);
            String idcard = StringUtils.getTextValue(editIdCard);
            String qq = StringUtils.getTextValue(editQQ);
            String email = StringUtils.getTextValue(editEmail);
            int education = PickerVewUtil.getSelectItem(list_education, StringUtils.getTextValue(tvEducation)) + 1;
            int marriage = PickerVewUtil.getSelectItem(list_marriage, StringUtils.getTextValue(tvMarriage)) + 1;
            int child = PickerVewUtil.getSelectItem(list_child, StringUtils.getTextValue(tvChild));
            String temporaryAddress = StringUtils.getTextValue(editAddress);
            int temporaryTime = PickerVewUtil.getSelectItem(list_address_time, StringUtils.getTextValue(tvAddressTime)) + 1;
            ProgressDialog.showProgressBar(context, "请稍后...");
            ApiClient.getInstance( ).myInfoFirst(token, name, idcard, qq, email, education, marriage, child, temporaryAddress,
                    temporaryTime, new OkHttpClientManager.ResultCallback<DataJsonResult<String>>( ) {

                        @Override
                        public void onError(Request request, Exception e, String error) {
                            ProgressDialog.cancelProgressBar( );
                            ToastUtils.showToast(context, "网络请求失败");
                        }

                        @Override
                        public void onResponse(DataJsonResult<String> response) {
                            ProgressDialog.cancelProgressBar( );
                            if (response.getSuccess( ) == "true") {
                                IntentUtils.toActivity(context, MyInfoSecondActivity.class);
                                ToastUtils.showToast(context, "信息已提交");
                            } else {
                                ToastUtils.showToast(context, response.getMessage( ));
                            }
                        }
                    });
        } else {
            ToastUtils.showToast(context, "请填入完整的信息");
        }
    }

    private void back() {
        DialogUtils.getInstance(context).showTipsDialog("是否退出借贷，下次再借？", new View.OnClickListener( ) {
            @Override
            public void onClick(View v) {
                DialogUtils.getInstance(context).dismiss();
                finish( );
            }
        });
    }

    @Override
    public void onBackPressed() {
        back( );
    }

    @OnClick({R.id.iv_back, R.id.tv_right, R.id.tv_education, R.id.tv_marriage, R.id.tv_child, R.id.tv_address_time})
    public void onViewClicked(View view) {
        switch (view.getId( )) {
            case R.id.iv_back:
                back( );
                break;
            case R.id.tv_right:
                if (!MyApp.isNeedUpdate) {
                    TokenLoginUtil.loginWithToken(context, handler);
                }
                break;
            case R.id.tv_education:
                PickerVewUtil.showPickerView(context, list_education, tvEducation);
                break;
            case R.id.tv_marriage:
                PickerVewUtil.showPickerView(context, list_marriage, tvMarriage);
                break;
            case R.id.tv_child:
                PickerVewUtil.showPickerView(context, list_child, tvChild);
                break;
            case R.id.tv_address_time:
                PickerVewUtil.showPickerView(context, list_address_time, tvAddressTime);
                break;
        }
    }
}
