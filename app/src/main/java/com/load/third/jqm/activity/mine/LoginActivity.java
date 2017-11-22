package com.load.third.jqm.activity.mine;

import android.app.Activity;
import android.content.Context;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import com.load.third.jqm.R;
import com.load.third.jqm.tips.ToastUtils;
import com.load.third.jqm.utils.IntentUtils;
import com.load.third.jqm.utils.StringUtils;
import com.load.third.jqm.utils.TextWatcherUtil;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class LoginActivity extends Activity {

    @BindView(R.id.iv_back)
    ImageView ivBack;
    @BindView(R.id.tv_title)
    TextView tvTitle;
    @BindView(R.id.tv_right)
    TextView tvRight;
    @BindView(R.id.edit_phone)
    EditText editPhone;
    @BindView(R.id.btn_next)
    Button btnNext;

    private Context context;
    public static String mobile;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        ButterKnife.bind(this);
        context = this;
        initView( );
    }

    private void initView() {
        tvTitle.setText("登录/注册");
        addTextListener( );
    }

    private void addTextListener() {
        View[] viewArr = {editPhone};
        TextWatcherUtil watcherListener = new TextWatcherUtil(viewArr, btnNext);
        TextWatcherUtil.setListener(viewArr, watcherListener);
    }

    @OnClick({R.id.iv_back, R.id.btn_next})
    public void onViewClicked(View view) {
        switch (view.getId( )) {
            case R.id.iv_back:
                finish( );
                break;
            case R.id.btn_next:
                mobile = StringUtils.getTextValue(editPhone);
                if (StringUtils.isMobileNO(mobile)) {
                    IntentUtils.toActivity(context, LoginVerifyActivity.class);
                } else {
                    ToastUtils.showToast(context, "请输入正确的手机号");
                }
                break;
        }
    }
}
