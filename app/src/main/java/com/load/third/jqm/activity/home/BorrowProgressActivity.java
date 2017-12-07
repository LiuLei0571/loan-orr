package com.load.third.jqm.activity.home;

import android.content.Context;
import android.os.Bundle;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.ScrollView;
import android.widget.TextView;

import com.load.third.jqm.MyApp;
import com.load.third.jqm.R;
import com.load.third.jqm.activity.BaseActivity;
import com.load.third.jqm.adapter.ProgressListAdapter;
import com.load.third.jqm.bean.UserBean;
import com.load.third.jqm.bean.UserDao;
import com.load.third.jqm.bean.newBean.UserStatus;
import com.load.third.jqm.newHttp.ApiException;
import com.load.third.jqm.newHttp.Apis;
import com.load.third.jqm.newHttp.BaseResponse;
import com.load.third.jqm.newHttp.CommonObserver;
import com.load.third.jqm.newHttp.CustomConsumer;
import com.load.third.jqm.tips.ToastUtils;
import com.load.third.jqm.utils.Consts;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import io.reactivex.Observable;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.Disposable;
import io.reactivex.functions.Function;
import io.reactivex.schedulers.Schedulers;
import me.everything.android.ui.overscroll.OverScrollDecoratorHelper;

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
    private List<String> list = new ArrayList<>( );


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_borrow_progress);
        ButterKnife.bind(this);
        context = this;
        initView( );
    }

    @Override
    protected void onResume() {
        super.onResume( );
        if (!MyApp.isNeedUpdate) {
//            TokenLoginUtil.loginWithToken(context, handler);
            initData();
        }
    }

    public void initData() {
        apiRetrofit.getLoginWithToken(Apis.loginWithToken.getUrl())
                .flatMap(new Function<BaseResponse<UserBean>, Observable<BaseResponse<UserStatus>>>() {
                    @Override
                    public Observable<BaseResponse<UserStatus>> apply(BaseResponse<UserBean> response) throws Exception {
                        if (response.getSuccess()) {
                            UserDao.getInstance(context).setAllDataWithoutToken(response.getData());
                            MyApp.isNeedUpdate = false;
                            return apiRetrofit.getStatus(Apis.getStatus.getUrl());
                        } else {
                            return Observable.error(new ApiException(response.getMessage()));
                        }
                    }
                })

                .subscribeOn(Schedulers.io())
                .doOnSubscribe(new CustomConsumer<Disposable>(getBaseActivity()))
                .subscribeOn(AndroidSchedulers.mainThread())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new CommonObserver<UserStatus>() {
                    @Override
                    public void doSuccess(BaseResponse<UserStatus> result) {
                        if (result.getData() != null) {
                            status=result.getData().getLend_status();
                            setLvBorrowProgress();
                        } else {
                            ToastUtils.showToast(context, result.getMessage());
                        }
                    }

                    @Override
                    public void doFail(String msg) {
                        super.doFail(msg);
                    }
                });

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
        finish( );
    }
}
