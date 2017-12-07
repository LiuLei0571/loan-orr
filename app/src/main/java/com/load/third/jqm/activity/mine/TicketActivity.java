package com.load.third.jqm.activity.mine;

import android.content.Context;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;

import com.load.third.jqm.R;
import com.load.third.jqm.activity.BaseActivity;
import com.load.third.jqm.adapter.TicketListAdapter;
import com.load.third.jqm.tips.DialogUtils;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import me.everything.android.ui.overscroll.OverScrollDecoratorHelper;

/*
 * 优惠券界面，1.0.1版本是隐藏的
 */

public class TicketActivity extends BaseActivity {
    public static final String TYPE_TICKET = "type_ticket";
    /**
     * 从个人中心点击进入，查看优惠券列表
     */
    public static final int TYPE_TICKET_LIST = 0;
    /**
     * 从首页点击进入，选取优惠券
     */
    public static final int TYPE_TICKET_CHOSE = 1;
    @BindView(R.id.iv_back)
    ImageView ivBack;
    @BindView(R.id.tv_title)
    TextView tvTitle;
    @BindView(R.id.tv_right)
    TextView tvRight;
    @BindView(R.id.ll_ticket_null)
    LinearLayout llTicketNull;
    @BindView(R.id.lv_ticket)
    ListView lvTicket;
    @BindView(R.id.btn_invite)
    Button btnInvite;

    private Context context;
    private int type;
    private TicketListAdapter adapter;
    private List<String> list = new ArrayList<>();

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_ticket);
        ButterKnife.bind(this);
        context = this;
        initView();
    }

    private void initView() {
        tvTitle.setText("借贷券");
        OverScrollDecoratorHelper.setUpOverScroll(lvTicket);
        type = getIntent().getIntExtra(TYPE_TICKET, TYPE_TICKET_LIST);
        switch (type) {
            case TYPE_TICKET_LIST:
                tvRight.setText("规则说明");
                break;
            case TYPE_TICKET_CHOSE:
                tvRight.setText("不使用代金券");
                break;
            default:
                break;
        }
        setLvTicket();
    }

    private void setLvTicket() {
        adapter = new TicketListAdapter(context, list, type);
        lvTicket.setAdapter(adapter);
        lvTicket.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int itemPosition, long id) {
                if (type == TYPE_TICKET_CHOSE) {
                    view.setSelected(true);
                    finish();
                }
            }
        });
    }

    @OnClick({R.id.iv_back, R.id.tv_right, R.id.btn_invite})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.iv_back:
                finish();
                break;
            case R.id.tv_right:
                switch (type) {
                    case TYPE_TICKET_LIST:
                        break;
                    case TYPE_TICKET_CHOSE:
                        finish();
                        break;
                    default:
                        break;
                }
                break;
            case R.id.btn_invite:
                DialogUtils.showInviteDialog(context);
                break;
            default:
                break;
        }
    }
}
