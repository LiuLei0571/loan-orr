package com.load.third.jqm.fragment;

import android.content.ClipboardManager;
import android.content.Context;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.load.third.jqm.R;
import com.load.third.jqm.activity.mine.LoginActivity;
import com.load.third.jqm.activity.mine.MoreVerifyActivity;
import com.load.third.jqm.activity.mine.SettingActivity;
import com.load.third.jqm.bean.UserDao;
import com.load.third.jqm.tips.DialogUtils;
import com.load.third.jqm.tips.ToastUtils;
import com.load.third.jqm.utils.IntentUtils;
import com.load.third.jqm.utils.StringUtils;
import com.load.third.jqm.utils.Urls;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class MineFragment extends BaseFragment {

    @BindView(R.id.iv_head)
    ImageView ivHead;
    @BindView(R.id.tv_nickname)
    TextView tvNickname;
    @BindView(R.id.btn_mine_ticket)
    Button btnMineTicket;
    @BindView(R.id.btn_mine_verify)
    Button btnMineVerify;
    @BindView(R.id.btn_mine_service)
    Button btnMineService;
    @BindView(R.id.btn_mine_setting)
    Button btnMineSetting;
    @BindView(R.id.btn_mine_help)
    Button btnMineHelp;
    @BindView(R.id.btn_mine_about_us)
    Button btnMineAboutUs;
    @BindView(R.id.btn_mine_wechat)
    Button btnMineWechat;
    private Context context;

    public MineFragment() {
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_mine, container, false);
        ButterKnife.bind(this, rootView);
        context = getActivity();
        initView();
        return rootView;
    }

    @Override
    public void onResume() {
        super.onResume();
        initView();
    }

    private void initView() {
        tvNickname.setText(StringUtils.isBlank(UserDao.getInstance(context).getToken()) ?
                "登录/注册" : UserDao.getInstance(context).getMobile());
    }

    private void copyWechatName() {
        ClipboardManager cm = (ClipboardManager) context.getSystemService(Context.CLIPBOARD_SERVICE);
        cm.setText("金钱门");
        ToastUtils.showToast(context, "已复制微信公众号到剪切板，快到微信搜索关注吧");
    }

    @OnClick({R.id.tv_nickname, R.id.btn_mine_ticket, R.id.btn_mine_verify, R.id.btn_mine_service, R.id.btn_mine_setting, R.id.btn_mine_help, R.id.btn_mine_about_us, R.id.btn_mine_wechat})
    public void onViewClicked(View view) {
        if (StringUtils.isBlank(UserDao.getInstance(context).getToken())) {
            IntentUtils.toActivity(context, LoginActivity.class);
        } else {
            switch (view.getId()) {
                case R.id.tv_nickname:
                    break;
                case R.id.btn_mine_ticket:
                    ToastUtils.showToast(context, "建设中...");
//                    IntentUtils.toTicketActivity(context, TicketActivity.TYPE_TICKET_LIST);
                    break;
                case R.id.btn_mine_verify:
                    IntentUtils.toActivity(context, MoreVerifyActivity.class);
                    break;
                case R.id.btn_mine_service:
                    DialogUtils.showQQServiceDialog(context);
                    break;
                case R.id.btn_mine_setting:
                    IntentUtils.toActivity(context, SettingActivity.class);
                    break;
                case R.id.btn_mine_help:
                    IntentUtils.toWebViewActivity(context, "借贷攻略", Urls.url_help);
                    break;
                case R.id.btn_mine_about_us:
                    IntentUtils.toWebViewActivity(context, "关于我们", Urls.url_aboutUs);
                    break;
                case R.id.btn_mine_wechat:
                    copyWechatName();
                    break;
            }
        }
    }
}
