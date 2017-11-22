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

import com.bigkoo.pickerview.OptionsPickerView;
import com.load.third.jqm.MyApp;
import com.load.third.jqm.R;
import com.load.third.jqm.bean.UserDao;
import com.load.third.jqm.http.ApiClient;
import com.load.third.jqm.http.OkHttpClientManager;
import com.load.third.jqm.http.result.DataJsonResult;
import com.load.third.jqm.httpUtil.TokenLoginUtil;
import com.load.third.jqm.tips.ProgressDialog;
import com.load.third.jqm.tips.ToastUtils;
import com.load.third.jqm.utils.IntentUtils;
import com.load.third.jqm.utils.PickerVewUtil;
import com.load.third.jqm.utils.StringUtils;
import com.load.third.jqm.utils.TextCheckUtil;
import com.squareup.okhttp.Request;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import me.everything.android.ui.overscroll.OverScrollDecoratorHelper;

import static com.load.third.jqm.httpUtil.TokenLoginUtil.MSG_TOKEN_LOGIN_SUCCESS;

/**
 * 职业信息
 */
public class MyInfoSecondActivity extends Activity {
    @BindView(R.id.tv_job)
    TextView tvJob;
    @BindView(R.id.tv_wages)
    TextView tvWages;
    @BindView(R.id.tv_wages_date)
    TextView tvWagesDate;
    @BindView(R.id.edit_company_name)
    EditText editCompanyName;
    @BindView(R.id.tv_company_province)
    TextView tvCompanyProvince;
    @BindView(R.id.tv_company_city)
    TextView tvCompanyCity;
    @BindView(R.id.edit_company_address)
    EditText editCompanyAddress;
    @BindView(R.id.tv_area_code)
    TextView tvAreaCode;
    @BindView(R.id.edit_company_phone)
    EditText editCompanyPhone;
    @BindView(R.id.tv_job_time)
    TextView tvJobTime;
    @BindView(R.id.scrollView)
    ScrollView scrollView;
    @BindView(R.id.iv_back)
    ImageView ivBack;
    @BindView(R.id.tv_title)
    TextView tvTitle;
    @BindView(R.id.tv_right)
    TextView tvRight;

    private Context context;
    private List<View> viewArr;
    private List<String> province;
    private List<String[]> cityList;
    private List<String> list_job;
    private List<String> list_wages;
    private List<String> list_wages_date;
    private List<String> list_job_time;
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
        setContentView(R.layout.activity_my_info_second);
        ButterKnife.bind(this);
        context = this;
        initView( );
    }

    private void initView() {
        tvTitle.setText("个人信息");
        tvRight.setText("下一步");
        OverScrollDecoratorHelper.setUpOverScroll(scrollView);
        viewArr = Arrays.asList(new View[]{tvJob, tvWages, tvWagesDate, editCompanyName, tvCompanyProvince, tvCompanyCity,
                editCompanyAddress, tvAreaCode, editCompanyPhone, tvJobTime});
        setPickViewData( );
        list_job = Arrays.asList(getResources( ).getStringArray(R.array.list_job));
        list_wages = Arrays.asList(getResources( ).getStringArray(R.array.list_wages));
        list_job_time = Arrays.asList(getResources( ).getStringArray(R.array.list_job_time));
        setInfo( );
        addTextListener( );
    }

    private void setInfo() {
        SharedPreferences sp = getSharedPreferences("myInfoSecond", Context.MODE_PRIVATE);
        tvJob.setText(sp.getString("job", ""));
        tvWages.setText(sp.getString("wages", ""));
        tvWagesDate.setText(sp.getString("wagesDate", ""));
        editCompanyName.setText(sp.getString("companyName", ""));
        tvCompanyProvince.setText(sp.getString("companyProvince", ""));
        tvCompanyCity.setText(sp.getString("companyCity", ""));
        editCompanyAddress.setText(sp.getString("companyAddress", ""));
        tvAreaCode.setText(sp.getString("areaCode", ""));
        editCompanyPhone.setText(sp.getString("companyPhone", ""));
        tvJobTime.setText(sp.getString("jobTime", ""));
    }

    private void saveInfo() {
        SharedPreferences sp = getSharedPreferences("myInfoSecond", Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sp.edit( );
        editor.putString("job", StringUtils.getTextValue(tvJob));
        editor.putString("wages", StringUtils.getTextValue(tvWages));
        editor.putString("wagesDate", StringUtils.getTextValue(tvWagesDate));
        editor.putString("companyName", StringUtils.getTextValue(editCompanyName));
        editor.putString("companyProvince", StringUtils.getTextValue(tvCompanyProvince));
        editor.putString("companyCity", StringUtils.getTextValue(tvCompanyCity));
        editor.putString("companyAddress", StringUtils.getTextValue(editCompanyAddress));
        editor.putString("areaCode", StringUtils.getTextValue(tvAreaCode));
        editor.putString("companyPhone", StringUtils.getTextValue(editCompanyPhone));
        editor.putString("jobTime", StringUtils.getTextValue(tvJobTime));
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
            int positionId = PickerVewUtil.getSelectItem(list_job, StringUtils.getTextValue(tvJob)) + 1;
            int incomeId = PickerVewUtil.getSelectItem(list_wages, StringUtils.getTextValue(tvWages)) + 1;
            int payDate = PickerVewUtil.getSelectItem(list_wages_date, StringUtils.getTextValue(tvWagesDate)) + 1;
            String companyName = StringUtils.getTextValue(editCompanyName);
            String companyProvince = StringUtils.getTextValue(tvCompanyProvince);
            String companyCity = StringUtils.getTextValue(tvCompanyCity);
            String companyAddress = StringUtils.getTextValue(editCompanyAddress);
            String companyCode = StringUtils.getTextValue(tvAreaCode);
            String companyTel = StringUtils.getTextValue(editCompanyPhone);
            ProgressDialog.showProgressBar(context, "请稍后...");
            ApiClient.getInstance( ).myInfoSecond(token, positionId, incomeId, payDate, companyName, companyProvince,
                    companyCity, companyAddress, companyCode, companyTel, new OkHttpClientManager.ResultCallback<DataJsonResult<String>>( ) {

                        @Override
                        public void onError(Request request, Exception e, String error) {
                            ProgressDialog.cancelProgressBar( );
                            ToastUtils.showToast(context, "网络请求失败");
                        }

                        @Override
                        public void onResponse(DataJsonResult<String> response) {
                            ProgressDialog.cancelProgressBar( );
                            if (response.getSuccess( ) == "true") {
                                IntentUtils.toActivity(context, MyInfoThirdActivity.class);
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

    private void choseCity() {
        int cityPosition = -1;
        for (int i = 0; i < province.size( ); i++) {
            if (province.get(i).equals(StringUtils.getTextValue(tvCompanyProvince)))
                cityPosition = i;
        }
        if (cityPosition == -1) {
            ToastUtils.showToast(context, "请先选择单位所在省");
        } else {
            final List<String> options1Items = new ArrayList<>( );
            final List<String> city = Arrays.asList(cityList.get(cityPosition));
            for (int j = 0; j < city.size( ); j++) {
                options1Items.add(city.get(j).split(",")[0]);
            }
            PickerVewUtil.showChoseArea(context, options1Items, tvCompanyCity, new OptionsPickerView.OnOptionsSelectListener( ) {
                @Override
                public void onOptionsSelect(int options1, int options2, int options3, View v) {
                    tvCompanyCity.setText(options1Items.get(options1));
                    tvAreaCode.setText(city.get(options1).split(",")[1]);
                }
            });
        }
    }

    @OnClick({R.id.tv_job, R.id.tv_wages, R.id.tv_wages_date, R.id.tv_company_province, R.id.tv_company_city, R.id.tv_job_time, R.id.iv_back, R.id.tv_right})
    public void onViewClicked(View view) {
        switch (view.getId( )) {
            case R.id.iv_back:
                finish( );
                break;
            case R.id.tv_right:
                if (!MyApp.isNeedUpdate) {
                    TokenLoginUtil.loginWithToken(context, handler);
                }
                break;
            case R.id.tv_job:
                PickerVewUtil.showPickerView(context, list_job, tvJob);
                break;
            case R.id.tv_wages:
                PickerVewUtil.showPickerView(context, list_wages, tvWages);
                break;
            case R.id.tv_wages_date:
                PickerVewUtil.showPickerView(context, list_wages_date, tvWagesDate);
                break;
            case R.id.tv_company_province:
                PickerVewUtil.showChoseArea(context, province, tvCompanyProvince, new OptionsPickerView.OnOptionsSelectListener( ) {
                    @Override
                    public void onOptionsSelect(int options1, int options2, int options3, View v) {
                        tvCompanyProvince.setText(province.get(options1));
                        tvCompanyCity.setText("");
                        tvAreaCode.setText("");
                    }
                });
                break;
            case R.id.tv_company_city:
                choseCity( );
                break;
            case R.id.tv_job_time:
                PickerVewUtil.showPickerView(context, list_job_time, tvJobTime);
                break;
        }
    }

    private void setPickViewData() {
        list_wages_date = new ArrayList<>( );
        for (int i = 0; i < 31; i++) {
            list_wages_date.add((i + 1) + "号");
        }
        province = Arrays.asList(getResources( ).getStringArray(R.array.list_province));
        cityList = new ArrayList<>( );
        cityList.add(getResources( ).getStringArray(R.array.beijing));
        cityList.add(getResources( ).getStringArray(R.array.tianjing));
        cityList.add(getResources( ).getStringArray(R.array.shanghai));
        cityList.add(getResources( ).getStringArray(R.array.chongqing));
        cityList.add(getResources( ).getStringArray(R.array.hebei));
        cityList.add(getResources( ).getStringArray(R.array.shanxi));
        cityList.add(getResources( ).getStringArray(R.array.liaoning));
        cityList.add(getResources( ).getStringArray(R.array.jiling));
        cityList.add(getResources( ).getStringArray(R.array.heilongjiang));
        cityList.add(getResources( ).getStringArray(R.array.jiangsu));
        cityList.add(getResources( ).getStringArray(R.array.zhejiang));
        cityList.add(getResources( ).getStringArray(R.array.anhui));
        cityList.add(getResources( ).getStringArray(R.array.fujian));
        cityList.add(getResources( ).getStringArray(R.array.jiangxi));
        cityList.add(getResources( ).getStringArray(R.array.shandong));
        cityList.add(getResources( ).getStringArray(R.array.henan));
        cityList.add(getResources( ).getStringArray(R.array.hubei));
        cityList.add(getResources( ).getStringArray(R.array.hunan));
        cityList.add(getResources( ).getStringArray(R.array.guangdong));
        cityList.add(getResources( ).getStringArray(R.array.hainan));
        cityList.add(getResources( ).getStringArray(R.array.sichuan));
        cityList.add(getResources( ).getStringArray(R.array.guizhou));
        cityList.add(getResources( ).getStringArray(R.array.yunnan));
        cityList.add(getResources( ).getStringArray(R.array.shengxi));
        cityList.add(getResources( ).getStringArray(R.array.gansu));
        cityList.add(getResources( ).getStringArray(R.array.qinghai));
        cityList.add(getResources( ).getStringArray(R.array.xianggang));
        cityList.add(getResources( ).getStringArray(R.array.aomen));
        cityList.add(getResources( ).getStringArray(R.array.taiwan));
        cityList.add(getResources( ).getStringArray(R.array.guangxi));
        cityList.add(getResources( ).getStringArray(R.array.neimenggu));
        cityList.add(getResources( ).getStringArray(R.array.xizang));
        cityList.add(getResources( ).getStringArray(R.array.ningxia));
        cityList.add(getResources( ).getStringArray(R.array.xinjiang));
    }
}
