package com.load.third.jqm.activity.home;

import android.content.Context;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.ScrollView;
import android.widget.TextView;

import com.load.third.jqm.MyApp;
import com.load.third.jqm.R;
import com.load.third.jqm.activity.BaseActivity;
import com.load.third.jqm.adapter.ProgressListAdapter;
import com.load.third.jqm.httpUtil.HomeGetUtils;
import com.load.third.jqm.utils.Consts;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import me.everything.android.ui.overscroll.OverScrollDecoratorHelper;

import static com.load.third.jqm.fragment.HomeFragment.MSG_GET_STATUS;
import static com.load.third.jqm.httpUtil.TokenLoginUtil.MSG_TOKEN_LOGIN_SUCCESS;
import static com.load.third.jqm.utils.Consts.STATUS_BORROW_AGAIN;
import static com.load.third.jqm.utils.Consts.STATUS_PAY_SUCCESS;
import static com.load.third.jqm.utils.Consts.STATUS_POAT_ID_CARD;
import static com.load.third.jqm.utils.Consts.STATUS_PSOT_INFO;

/**
 * 借贷进度
 */
public class BorrowProgressActivity extends BaseActivity {

    @BindView(R.id.iv_back)
    ImageView ivBack;
    @BindView(R.id.tv_title)
    TextView tvTitle;
    @BindView(R.id.tv_right)
    TextView tvRight;
    @BindView(R.id.scrollView)
    ScrollView scrollView;
    @BindView(R.id.lv_borrow_progress)
    ListView lvBorrowProgress;

    private Context context;
    private int status;
    private ProgressListAdapter adapter;
    private List<String> list = new ArrayList<>();
    private Handler handler = new Handler() {
        public void handleMessage(Message msg) {
            switch (msg.what) {
                case MSG_TOKEN_LOGIN_SUCCESS:
                    HomeGetUtils.getStatus(context, handler);
                    break;
                case MSG_GET_STATUS:
                    status = (int) msg.obj;
                    setLvBorrowProgress();
                    break;
            }
        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_borrow_progress);
        ButterKnife.bind(this);
        context = this;
        initView();
    }

    @Override
    protected void onResume() {
        super.onResume();
        if (!MyApp.isNeedUpdate) {
            initData();
        }
    }

    public void initData() {

    }

    private void initView() {
        tvTitle.setText("借贷进度");
        OverScrollDecoratorHelper.setUpOverScroll(scrollView);
    }

    private void setLvBorrowProgress() {
        list.add("借贷金额提交");
        list.add("个人信息提交");
        list.add("个人信息审核");
        list.add("银行卡绑定");
        list.add("证件照提交");
        list.add("证件照审核");
        list.add("开始放款");
        List<String> stringList = Arrays.asList(new String[]{"", "", "", "", "", "", ""});
        switch (status) {
            case Consts.STATUS_BORROW_FIRST:
                stringList = Arrays.asList(new String[]{"待提交", "待提交", "待审核", "待绑定", "待提交", "待审核", "等待"});
                break;
            case STATUS_BORROW_AGAIN:
                stringList = Arrays.asList(new String[]{"待提交", "待提交", "待审核", "待绑定", "待提交", "待审核", "等待"});
                break;
            case STATUS_PSOT_INFO:
                stringList = Arrays.asList(new String[]{"已提交", "待提交", "待审核", "待绑定", "待提交", "待审核", "等待"});
                break;
            case Consts.STATUS_CHECKING_INFO:
                stringList = Arrays.asList(new String[]{"已提交", "已提交", "审核中", "待绑定", "待提交", "待审核", "等待"});
                break;
            case Consts.STATUS_POST_BANK_CARD:
                stringList = Arrays.asList(new String[]{"已提交", "已提交", "已审核", "待绑定", "待提交", "待审核", "等待"});
                break;
            case STATUS_POAT_ID_CARD:
                stringList = Arrays.asList(new String[]{"已提交", "已提交", "已审核", "已绑定", "待提交", "待审核", "等待"});
                break;
            case Consts.STATUS_CHECKING_ID_CARD:
                stringList = Arrays.asList(new String[]{"已提交", "已提交", "已审核", "已绑定", "已提交", "审核中", "等待"});
                break;
            case STATUS_PAY_SUCCESS:
                stringList = Arrays.asList(new String[]{"已提交", "已提交", "已审核", "已绑定", "已提交", "已审核", "放款成功"});
                break;
            case Consts.STATUS_PAY_ERROR:
                stringList = Arrays.asList(new String[]{"已提交", "已提交", "已审核", "已绑定", "已提交", "已审核", "放款失败"});
                break;
            case Consts.STATUS_REPOST_ID_CARD:
                stringList = Arrays.asList(new String[]{"已提交", "已提交", "已审核", "已绑定", "待重新提交", "待审核", "等待"});
                break;
            case Consts.STATUS_WAIT_PAY_13:
                stringList = Arrays.asList(new String[]{"已提交", "已提交", "已审核", "已绑定", "已提交", "已审核", "放款中"});
                break;
            case Consts.STATUS_WAIT_PAY_14:
                stringList = Arrays.asList(new String[]{"已提交", "已提交", "已审核", "已绑定", "已提交", "已审核", "放款中"});
                break;
            default:
                break;
        }
        adapter = new ProgressListAdapter(context, list, stringList);
        lvBorrowProgress.setAdapter(adapter);
    }

    @OnClick(R.id.iv_back)
    public void onViewClicked() {
        finish();
    }
}
