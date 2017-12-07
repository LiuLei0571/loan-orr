package com.load.third.jqm.activity.mine;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.load.third.jqm.R;
import com.load.third.jqm.activity.BaseActivity;
import com.load.third.jqm.bean.UserDao;
import com.load.third.jqm.tips.DialogUtils;
import com.load.third.jqm.tips.ToastUtils;
import com.load.third.jqm.utils.IntentUtils;
import com.load.third.jqm.utils.TempUtils;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class SettingActivity extends BaseActivity {

    @BindView(R.id.iv_back)
    ImageView ivBack;
    @BindView(R.id.tv_title)
    TextView tvTitle;
    @BindView(R.id.tv_cache)
    TextView tvCache;
    @BindView(R.id.ll_clean_cache)
    LinearLayout llCleanCache;
    @BindView(R.id.btn_set_mine_info)
    Button btnSetMineInfo;
    @BindView(R.id.btn_login_out)
    Button btnLoginOut;
    private Context context;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_setting);
        ButterKnife.bind(this);
        context = this;
        initView();
    }

    private void initView() {
        tvTitle.setText("设置");
        tvCache.setText(TempUtils.convertFileSize(TempUtils.getFolderSize(TempUtils.tempFile)));
    }

    private void cleanCache() {
        DialogUtils.getInstance(context).showTipsDialog("是否清除缓存？", new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                TempUtils.deleteFolderFile(TempUtils.tempDirectory, false);
                tvCache.setText("0 B");
                DialogUtils.getInstance(context).dismiss();
            }
        });
    }

    private void loginOut() {
        DialogUtils.getInstance(context).showTipsDialog("是否退出登录？", new View.OnClickListener() {
            @Override
            public void onClick(View v) {
//                清空token
                UserDao.getInstance(context).setToken("");
//                清空缓存的个人三块信息
                SharedPreferences myInfoFirst = getSharedPreferences("myInfoFirst", Context.MODE_PRIVATE);
                myInfoFirst.edit().clear().commit();
                SharedPreferences myInfoSecond = getSharedPreferences("myInfoSecond", Context.MODE_PRIVATE);
                myInfoSecond.edit().clear().commit();
                SharedPreferences myInfoThird = getSharedPreferences("myInfoThird", Context.MODE_PRIVATE);
                myInfoThird.edit().clear().commit();
                DialogUtils.getInstance(context).dismiss();
                IntentUtils.toMainActivity(context);
            }
        });
    }

    @OnClick({R.id.iv_back, R.id.ll_clean_cache, R.id.btn_set_mine_info, R.id.btn_login_out})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.iv_back:
                finish();
                break;
            case R.id.ll_clean_cache:
                cleanCache();
                break;
            case R.id.btn_set_mine_info:
                ToastUtils.showToast(context, "建设中...");
                break;
            case R.id.btn_login_out:
                loginOut();
                break;
            default:
                break;
        }
    }
}
