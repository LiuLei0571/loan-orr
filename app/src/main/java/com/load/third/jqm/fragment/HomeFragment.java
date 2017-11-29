package com.load.third.jqm.fragment;

import android.annotation.TargetApi;
import android.content.Context;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.text.Html;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.ScrollView;
import android.widget.TextView;

import com.load.third.jqm.MyApp;
import com.load.third.jqm.R;
import com.load.third.jqm.activity.MainActivity;
import com.load.third.jqm.activity.home.BorrowProgressActivity;
import com.load.third.jqm.activity.info.BindBankCardActivity;
import com.load.third.jqm.activity.info.BindIdCardActivity;
import com.load.third.jqm.activity.info.MyInfoFirstActivity;
import com.load.third.jqm.activity.mine.LoginActivity;
import com.load.third.jqm.activity.mine.TicketActivity;
import com.load.third.jqm.bean.HomeExpenseDataBean;
import com.load.third.jqm.bean.RepaymentDataBean;
import com.load.third.jqm.bean.UserBean;
import com.load.third.jqm.bean.UserDao;
import com.load.third.jqm.bean.newBean.BrrowInfo;
import com.load.third.jqm.bean.newBean.UserStatus;
import com.load.third.jqm.help.UserHelper;
import com.load.third.jqm.httpUtil.HomeGetUtils;
import com.load.third.jqm.newHttp.ApiException;
import com.load.third.jqm.newHttp.Apis;
import com.load.third.jqm.newHttp.BaseResponse;
import com.load.third.jqm.newHttp.CommonObserver;
import com.load.third.jqm.newHttp.CustomConsumer;
import com.load.third.jqm.newHttp.UrlParams;
import com.load.third.jqm.tips.DialogUtils;
import com.load.third.jqm.tips.ToastUtils;
import com.load.third.jqm.utils.Consts;
import com.load.third.jqm.utils.IntentUtils;
import com.load.third.jqm.utils.StringUtils;
import com.load.third.jqm.utils.Urls;
import com.load.third.jqm.view.CycleWheelView;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
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

import static com.load.third.jqm.httpUtil.TokenLoginUtil.MSG_TOKEN_LOGIN_SUCCESS;
import static com.load.third.jqm.utils.Consts.STATUS_BORROW_AGAIN;
import static com.load.third.jqm.utils.Consts.STATUS_PAY_SUCCESS;
import static com.load.third.jqm.utils.Consts.STATUS_POAT_ID_CARD;
import static com.load.third.jqm.utils.Consts.STATUS_POST_BANK_CARD;
import static com.load.third.jqm.utils.Consts.STATUS_PSOT_INFO;

public class HomeFragment extends BaseFragment {
    public static final int MSG_GET_STATUS_ERROR = 1000;
    public static final int MSG_GET_STATUS = 1001;
    public static final int MSG_GET_EXPENSE_DATA = 1002;
    public static final int MSG_GET_REPAYMENT_DATA = 1003;

    @BindView(R.id.btn_repeyment)
    Button btnRepeyment;
    @BindView(R.id.tv_shenxunfei)
    TextView tvShenxunfei;
    @BindView(R.id.tv_zonglixi)
    TextView tvZonglixi;
    @BindView(R.id.tv_guanlifei)
    TextView tvGuanlifei;
    @BindView(R.id.tv_shijidaozhang)
    TextView tvShijidaozhang;
    @BindView(R.id.iv_ticket)
    TextView ivTicket;
    @BindView(R.id.ll_ticket)
    LinearLayout llTicket;
    @BindView(R.id.tv_agreement)
    TextView tvAgreement;
    @BindView(R.id.btn_borrow)
    Button btnBorrow;
    @BindView(R.id.ll_home_bottom_borrow)
    ScrollView llHomeBottomBorrow;
    @BindView(R.id.tv_request)
    TextView tvRequest;
    @BindView(R.id.iv_logo)
    ImageView ivLogo;
    @BindView(R.id.tv_check_progress)
    TextView tvCheckProgress;
    @BindView(R.id.ll_home_bottom)
    RelativeLayout llHomeBottom;
    @BindView(R.id.iv_head)
    ImageView ivHead;
    @BindView(R.id.tv_repayment_day)
    TextView tvRepaymentDay;
    @BindView(R.id.tv_repayment_time)
    TextView tvRepaymentTime;
    @BindView(R.id.tv_repayment_money)
    TextView tvRepaymentMoney;
    @BindView(R.id.ll_home_title_repayment)
    LinearLayout llHomeTitleRepayment;
    @BindView(R.id.wheel_borrow_money)
    CycleWheelView wheelBorrowMoney;
    @BindView(R.id.wheel_borrow_time)
    CycleWheelView wheelBorrowTime;
    @BindView(R.id.ll_home_title_borrow)
    LinearLayout llHomeTitleBorrow;
    @BindView(R.id.ll_home_title)
    RelativeLayout llHomeTitle;

    private Context context;
    public static int status;
    private List<HomeExpenseDataBean.ListBean> expenseDataBean;
    private List<String> moneyList = new ArrayList<>();
    private List<String> dayList = new ArrayList<>();
    private String money, day;
    private RepaymentDataBean repaymentDataBean;
    private int repay_amount;
    private Handler handler = new Handler() {
        public void handleMessage(Message msg) {
            switch (msg.what) {
                case MSG_TOKEN_LOGIN_SUCCESS:
                    HomeGetUtils.getStatus(context, handler);
                    break;
                case MSG_GET_STATUS_ERROR:
                    requestError();
                    break;
                case MSG_GET_STATUS:
                    status = (int) msg.obj;
                    tvRequest.setVisibility(View.GONE);
                    setStatus();
                    break;
                case MSG_GET_EXPENSE_DATA:
                    expenseDataBean = (List<HomeExpenseDataBean.ListBean>) msg.obj;
                    tvRequest.setVisibility(View.GONE);
                    setHomeExpenseData();
                    break;
                case MSG_GET_REPAYMENT_DATA:
                    repaymentDataBean = (RepaymentDataBean) msg.obj;
                    tvRequest.setVisibility(View.GONE);
                    setHomeRepayment();
                    break;
            }
        }
    };

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_home, container, false);
        ButterKnife.bind(this, rootView);
        context = getActivity();
        initView();
        return rootView;
    }

    @Override
    public void onResume() {
        super.onResume();
        requestData();
    }

    private void initView() {
        tvAgreement.setText(Html.fromHtml("点击确认借贷即表示您已同意<font color='#43b890'>" + "《用户协议》" + "</font>"));
        OverScrollDecoratorHelper.setUpOverScroll(llHomeBottomBorrow);
    }

    @TargetApi(Build.VERSION_CODES.M)
    private void requestData() {
        if (StringUtils.isBlank(UserHelper.getUserToken())) {
            status = -1;
            setStatus();
        } else {
            if (!MyApp.isNeedUpdate) {
                apiRetrofit.getLoginWithToken(Apis.loginWithToken.getUrl())
                        .flatMap(new Function<BaseResponse<UserBean>, Observable<BaseResponse<UserStatus>>>() {
                            public Observable<BaseResponse<UserStatus>> apply(BaseResponse<UserBean> response) throws Exception {
                                if (response.getSuccess().equals("true")) {
                                    UserDao.getInstance(context).setAllDataWithoutToken(response.getData());
                                    MyApp.isNeedUpdate = false;
                                    return apiRetrofit.getStatus(Apis.getStatus.getUrl());
                                } else {
                                    return Observable.error(new ApiException(response.getMessage()));
                                }
                            }
                        })
                        .flatMap(new Function<BaseResponse<UserStatus>, Observable<BaseResponse<HomeExpenseDataBean>>>() {
                            public Observable<BaseResponse<HomeExpenseDataBean>> apply(BaseResponse<UserStatus> response) throws Exception {
                                if (response.getSuccess().equals("true")) {
                                    return apiRetrofit.retrofitHomeExpenseData(Apis.home.getUrl());
                                } else {
                                    return Observable.error(new ApiException(response.getMessage()));

                                }
                            }
                        })
                        .subscribeOn(Schedulers.io())
                        .doOnSubscribe(new CustomConsumer<Disposable>(getContext()))
                        .subscribeOn(AndroidSchedulers.mainThread())
                        .observeOn(AndroidSchedulers.mainThread())
                        .subscribe(new CommonObserver<HomeExpenseDataBean>() {
                            @Override
                            public void doSuccess(BaseResponse<HomeExpenseDataBean> result) {
                                if (result.getData() != null) {
                                    expenseDataBean = result.getData().getList();
                                    setHomeExpenseData();
                                    tvRequest.setVisibility(View.GONE);
                                } else {
                                    ToastUtils.showToast(context, result.getMessage());
                                }
                            }

                            @Override
                            public void doFail(String msg) {
                                super.doFail(msg);
                                requestError();
                            }
                        });
                setButtonStatus();
            }
        }
    }

    /**
     * 错误提示
     */
    private void requestError() {
        tvRequest.setVisibility(View.VISIBLE);
        llHomeTitleBorrow.setVisibility(View.GONE);//选择借款金额和时间的布局
        llHomeBottomBorrow.setVisibility(View.GONE);//借款相关费用和借款按钮的布局
        llHomeBottom.setVisibility(View.GONE);//底部小图标和查看借款进度的布局
        llHomeTitleRepayment.setVisibility(View.GONE);//借款成功后的信息
        btnRepeyment.setVisibility(View.GONE);//还款按钮
    }

    /**
     * 还款信息
     */
    private void setHomeRepayment() {
        String due_time = repaymentDataBean.getDue_time();
        repay_amount = repaymentDataBean.getRepay_amount();
        int surplus_time = repaymentDataBean.getSurplus_time();
        llHomeTitleBorrow.setVisibility(View.GONE);
        llHomeBottomBorrow.setVisibility(View.GONE);
        llHomeBottom.setVisibility(View.VISIBLE);
        llHomeTitleRepayment.setVisibility(View.VISIBLE);
        btnRepeyment.setVisibility(View.VISIBLE);
        tvRepaymentMoney.setText("到期还款: " + repay_amount + "元");
        tvRepaymentDay.setText(surplus_time + "");
        tvRepaymentTime.setText("到期日期: " + due_time);
    }

    public void setHomeExpenseData() {
        llHomeTitleBorrow.setVisibility(View.VISIBLE);
        llHomeBottomBorrow.setVisibility(View.VISIBLE);
        llHomeBottom.setVisibility(View.VISIBLE);
        llHomeTitleRepayment.setVisibility(View.GONE);
        btnRepeyment.setVisibility(View.GONE);
        for (int i = 0; i < expenseDataBean.size(); i++) {
            if (!moneyList.contains(expenseDataBean.get(i).getAmount()))
                moneyList.add(expenseDataBean.get(i).getAmount());
            dayList.add(expenseDataBean.get(i).getPeriod());
        }
        setWheelView();
    }

    private void setWheelView() {
        wheelBorrowMoney.setLabels(moneyList);
        wheelBorrowMoney.setCycleEnable(true);
        wheelBorrowMoney.setOnWheelItemSelectedListener(new CycleWheelView.WheelItemSelectedListener() {
            @Override
            public void onItemSelected(int position, String label) {
                money = label;
                setExpenseInfo();
            }
        });
        wheelBorrowTime.setLabels(dayList);
        wheelBorrowTime.setCycleEnable(true);
        wheelBorrowTime.setOnWheelItemSelectedListener(new CycleWheelView.WheelItemSelectedListener() {
            @Override
            public void onItemSelected(int position, String label) {
                day = label;
                setExpenseInfo();
            }
        });
    }

    private void setExpenseInfo() {
        for (int i = 0; i < expenseDataBean.size(); i++) {
            if (expenseDataBean.get(i).getAmount().equals(money) && expenseDataBean.get(i).getPeriod().equals(day)) {
                if (expenseDataBean != null) {
                    tvShenxunfei.setText(expenseDataBean.get(i).getServiceFee() + "元");
                    tvZonglixi.setText(expenseDataBean.get(i).getInterestFee() + "元");
                    tvGuanlifei.setText(expenseDataBean.get(i).getAccountManageFee() + "元");
                    tvShijidaozhang.setText(expenseDataBean.get(i).getRepay() + "元");
                }

            }
        }
    }

    @TargetApi(Build.VERSION_CODES.M)
    private void setStatus() {
        if (status == STATUS_PAY_SUCCESS) {
//            HomeGetUtils.getRepayment(context, handler);
            submitTask(apiRetrofit.getRepaymentData(Apis.getRepaymentData.getUrl()), new CommonObserver<RepaymentDataBean>() {
                @Override
                public void doSuccess(BaseResponse<RepaymentDataBean> result) {
                    if (result.getData() != null) {
                        repaymentDataBean = result.getData();
                        tvRequest.setVisibility(View.GONE);
                        setHomeRepayment();
                    }
                }

                @Override
                public void doFail(String msg) {
                    super.doFail(msg);
                    ToastUtils.showToast(context, "还款信息获取失败");
                    requestError();
                }
            });
        } else {
            //                HomeGetUtils.getHomeExpenseData(context, handler);
            submitTask(apiRetrofit.retrofitHomeExpenseData(Apis.home.getUrl()), new CommonObserver<HomeExpenseDataBean>() {
                @Override
                public void doSuccess(BaseResponse<HomeExpenseDataBean> result) {
                    if (result.getData() != null) {
                        expenseDataBean = result.getData().getList();
                        setHomeExpenseData();
                        tvRequest.setVisibility(View.GONE);
                    } else {
                        ToastUtils.showToast(context, result.getMessage());
                    }
                }

                @Override
                public void doFail(String msg) {
                    ToastUtils.showToast(context, "息费信息获取失败");
                }
            });

        }
        setButtonStatus();

    }

    private void btnBorrowClick() {
        switch (status) {
            case Consts.STATUS_BORROW_FIRST:
            case STATUS_BORROW_AGAIN:
                setBrrowInfo(day, money);
                break;
            case STATUS_PSOT_INFO:
                IntentUtils.toActivity(context, MyInfoFirstActivity.class);
                break;
            case STATUS_POST_BANK_CARD:
                IntentUtils.toActivity(context, BindBankCardActivity.class);
                break;
            case STATUS_POAT_ID_CARD:
            case Consts.STATUS_REPOST_ID_CARD:
                IntentUtils.toActivity(context, BindIdCardActivity.class);
                break;
            case Consts.STATUS_CHECKING_INFO:
            case Consts.STATUS_CHECKING_ID_CARD:
            case STATUS_PAY_SUCCESS:
            case Consts.STATUS_PAY_ERROR:
            case Consts.STATUS_WAIT_PAY_13:
            case Consts.STATUS_WAIT_PAY_14:
                break;
            default:
                break;
        }
    }

    @OnClick({R.id.ll_ticket, R.id.btn_repeyment, R.id.tv_agreement, R.id.btn_borrow, R.id.tv_request,
            R.id.tv_check_progress, R.id.iv_head})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.ll_ticket:
                //选择优惠券,布局已隐藏
                if (StringUtils.isBlank(UserDao.getInstance(context).getToken()))
                    IntentUtils.toActivity(context, LoginActivity.class);
                else
                    IntentUtils.toTicketActivity(context, TicketActivity.TYPE_TICKET_CHOSE);
                break;
            case R.id.btn_borrow:
                if (StringUtils.isBlank(UserDao.getInstance(context).getToken()))
                    IntentUtils.toActivity(context, LoginActivity.class);
                else {
                    btnBorrowClick();
                }
                break;
            case R.id.btn_repeyment:
                if (StringUtils.isBlank(UserDao.getInstance(context).getToken()))
                    IntentUtils.toActivity(context, LoginActivity.class);
                else
                    IntentUtils.toRepaymentActivity(context, repay_amount + "");
                break;
            case R.id.tv_check_progress:
                if (StringUtils.isBlank(UserDao.getInstance(context).getToken()))
                    IntentUtils.toActivity(context, LoginActivity.class);
                else
                    IntentUtils.toActivity(context, BorrowProgressActivity.class);
                break;
            case R.id.iv_head:
                MainActivity mainActivity = (MainActivity) getActivity();
                mainActivity.openMineDrawer();
                break;
            case R.id.tv_request:
                requestData();
                tvRequest.setVisibility(View.GONE);
                break;
            case R.id.tv_agreement:
                IntentUtils.toWebViewActivity(context, "用户协议", Urls.url_agreement);
                break;

        }
    }

    public void setUiState(String content, boolean isClick) {
        btnBorrow.setText(content);
        btnBorrow.setEnabled(isClick);
    }
    public void setButtonStatus() {
        switch (status) {
            case -1:
                setUiState("确认借贷", true);
                break;
            case Consts.STATUS_BORROW_FIRST:
                setUiState("确认借贷", true);
                break;
            case STATUS_BORROW_AGAIN:
                setUiState("确认借贷", true);
                break;
            case STATUS_PSOT_INFO:
                setUiState("提交个人资料", true);
                break;
            case Consts.STATUS_CHECKING_INFO:
                setUiState("个人资料审核中...", true);
                break;
            case STATUS_POST_BANK_CARD:
                setUiState("绑定银行卡", true);
                break;
            case STATUS_POAT_ID_CARD:
                setUiState("绑定证件照", true);
                break;
            case Consts.STATUS_CHECKING_ID_CARD:
                setUiState("证件照审核中...", false);
                break;
            case STATUS_PAY_SUCCESS:
                break;
            case Consts.STATUS_PAY_ERROR:
                setUiState("放款失败", false);
                break;
            case Consts.STATUS_REPOST_ID_CARD:
                setUiState("重新绑定证件照", true);
                break;
            case Consts.STATUS_WAIT_PAY_13:
                setUiState("等待放款", false);
                break;
            case Consts.STATUS_WAIT_PAY_14:
                setUiState("等待放款", false);
                break;
            default:
                break;
        }
    }

    public void setBrrowInfo(String day, String money) {
        Map<String, Object> params = new HashMap<>();
        params.put("borrowPeriod", day);
        params.put("borrowMoney", money);
        String url = UrlParams.getUrl(Apis.postBorrowInfo.getUrl(), params);
        submitTask(apiRetrofit.getBorrowInfo(url), new CommonObserver<BrrowInfo>() {

            @Override
            public void doSuccess(BaseResponse<BrrowInfo> response) {
                if (response.getSuccess().equals("true")) {
                    if (status == Consts.STATUS_BORROW_FIRST) {
                        IntentUtils.toActivity(context, MyInfoFirstActivity.class);
                    } else if (status == Consts.STATUS_BORROW_AGAIN) {
                        HomeGetUtils.checkPhone(context);
                    }
                    return;
                }
                if (StringUtils.isBlank(response.getData().getFrozen_time())) {
                    ToastUtils.showToast(context, response.getMessage());
                    return;
                }
                DialogUtils.getInstance(context).showOkTipsDialog(response.getMessage() + "\n账号还需" + response.getData().getFrozen_time() + "天解冻");
            }
        });
    }
}
