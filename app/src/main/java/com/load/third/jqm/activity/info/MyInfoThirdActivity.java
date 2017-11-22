package com.load.third.jqm.activity.info;

import android.Manifest;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.provider.ContactsContract;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
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
import com.load.third.jqm.httpUtil.HomeGetUtils;
import com.load.third.jqm.httpUtil.TokenLoginUtil;
import com.load.third.jqm.tips.ProgressDialog;
import com.load.third.jqm.tips.ToastUtils;
import com.load.third.jqm.utils.ContactsTxtUtil;
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
import static com.load.third.jqm.utils.TempUtils.tempDirectory;

/**
 * 提交社会关系
 */
public class MyInfoThirdActivity extends Activity {
    private static final int PERMISSIONS_REQUEST_READ_CONTACTS = 1;
    private static final int RESULT_CHOSE_FAMILY = 101;
    private static final int RESULT_CHOSE_SOCIAL = 102;

    @BindView(R.id.tv_relation_family)
    TextView tvRelationFamily;
    @BindView(R.id.tv_phone_family)
    TextView tvPhoneFamily;
    @BindView(R.id.tv_relation_social)
    TextView tvRelationSocial;
    @BindView(R.id.tv_phone_social)
    TextView tvPhoneSocial;
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
    private List<String> myContactsList;
    private int chose_type;
    private List<String> list_relation_family;
    private List<String> list_relation_social;
    private String phoneFamily;
    private String phoneSocial;
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
        setContentView(R.layout.activity_my_info_third);
        ButterKnife.bind(this);
        context = this;
        initView( );
    }

    private void initView() {
        tvTitle.setText("个人信息");
        tvRight.setText("下一步");
        OverScrollDecoratorHelper.setUpOverScroll(scrollView);
        viewArr = Arrays.asList(new View[]{tvRelationFamily, tvPhoneFamily, tvRelationSocial, tvPhoneSocial});
        list_relation_family = Arrays.asList(getResources( ).getStringArray(R.array.list_relation_family));
        list_relation_social = Arrays.asList(getResources( ).getStringArray(R.array.list_relation_social));
        setInfo( );
        addTextListener( );
    }

    private void setInfo() {
        SharedPreferences sp = getSharedPreferences("myInfoThird", Context.MODE_PRIVATE);
        tvRelationFamily.setText(sp.getString("relationFamily", ""));
        tvPhoneFamily.setText(sp.getString("phoneFamily", ""));
        tvRelationSocial.setText(sp.getString("relationSocial", ""));
        tvPhoneSocial.setText(sp.getString("phoneSocial", ""));
    }

    private void saveInfo() {
        SharedPreferences sp = getSharedPreferences("myInfoThird", Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sp.edit( );
        editor.putString("relationFamily", StringUtils.getTextValue(tvRelationFamily));
        editor.putString("phoneFamily", StringUtils.getTextValue(tvPhoneFamily));
        editor.putString("relationSocial", StringUtils.getTextValue(tvRelationSocial));
        editor.putString("phoneSocial", StringUtils.getTextValue(tvPhoneSocial));
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
            ProgressDialog.showProgressBar(context, "请稍后...");
            String token = UserDao.getInstance(context).getToken( );
            int cognateRelation = PickerVewUtil.getSelectItem(list_relation_family, StringUtils.getTextValue(tvRelationFamily)) + 1;
            int socialRelation = PickerVewUtil.getSelectItem(list_relation_social, StringUtils.getTextValue(tvRelationSocial)) + 1;
            ApiClient.getInstance( ).myInfoThird(token, cognateRelation, phoneFamily, socialRelation, phoneSocial, new OkHttpClientManager.ResultCallback<DataJsonResult<String>>( ) {

                @Override
                public void onError(Request request, Exception e, String error) {
                    ProgressDialog.cancelProgressBar( );
                    ToastUtils.showToast(context, "网络请求失败");
                    Log.e("http_msg", "社会关系信息提交失败");
                }

                @Override
                public void onResponse(DataJsonResult<String> response) {
                    ProgressDialog.cancelProgressBar( );
                    if (response.getSuccess( ) == "true") {
                        HomeGetUtils.checkPhone(context);
                        ToastUtils.showToast(context, "信息已提交");
                        Log.e("http_msg", "社会关系信息已提交");
                    } else {
                        ToastUtils.showToast(context, response.getMessage( ));
                        Log.e("http_msg", "社会关系信息提交失败");
                    }
                }
            });
        } else {
            ToastUtils.showToast(context, "请填入完整的信息");
        }
    }

    private void checkPermission() {
        if (ContextCompat.checkSelfPermission(context, Manifest.permission.READ_CONTACTS) != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions((Activity) context, new String[]{Manifest.permission.READ_CONTACTS}, PERMISSIONS_REQUEST_READ_CONTACTS);
        } else {
            chosePeople( );
        }
    }

    private void chosePeople() {
        Intent intent = new Intent(Intent.ACTION_GET_CONTENT);
        intent.setType(ContactsContract.CommonDataKinds.Phone.CONTENT_ITEM_TYPE);
        startActivityForResult(intent, chose_type);
    }

    private void getContact(int requestCode, Intent data) {
        if (data != null) {
            try {
                Uri uri = data.getData( );
                if (uri != null) {
                    Cursor cursor = getContentResolver( )
                            .query(uri, new String[]{ContactsContract.CommonDataKinds.Phone.NUMBER,
                                    ContactsContract.CommonDataKinds.Phone.DISPLAY_NAME}, null, null, null);
                    while (cursor.moveToNext( )) {
                        switch (requestCode) {
                            case RESULT_CHOSE_FAMILY:
                                phoneFamily = cursor.getString(0).replaceAll(" ", "");
                                tvPhoneFamily.setText(cursor.getString(1) + "  (" + cursor.getString(0).replaceAll(" ", "") + ")");
                                break;
                            case RESULT_CHOSE_SOCIAL:
                                phoneSocial = cursor.getString(0).replaceAll(" ", "");
                                tvPhoneSocial.setText(cursor.getString(1) + "  (" + cursor.getString(0).replaceAll(" ", "") + ")");
                                break;
                        }
                    }
                }
            } catch (Exception e) {
                e.printStackTrace( );
            }
        }
    }

    private void getContactsList() {
        myContactsList = new ArrayList<>( );
        try {
            Uri contactUri = ContactsContract.CommonDataKinds.Phone.CONTENT_URI;
            Cursor cursor = getContentResolver( )
                    .query(contactUri, new String[]{ContactsContract.CommonDataKinds.Phone.NUMBER,
                            ContactsContract.CommonDataKinds.Phone.DISPLAY_NAME}, null, null, null);
            String contactName;
            String contactNumber;
            while (cursor.moveToNext( )) {
                contactName = cursor.getString(cursor.getColumnIndex(ContactsContract.CommonDataKinds.Phone.DISPLAY_NAME));
                contactNumber = cursor.getString(cursor.getColumnIndex(ContactsContract.CommonDataKinds.Phone.NUMBER));
                if (contactName != null)
                    myContactsList.add(contactName + "  " + contactNumber);
            }
            cursor.close( );
        } catch (Exception e) {
            e.printStackTrace( );
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        getContact(requestCode, data);
        if (myContactsList == null) {
            getContactsList( );
            //保存通讯录到本地
            String txtName = System.currentTimeMillis( ) + ".txt";
            String txtPath = tempDirectory + txtName;
            for (int i = 0; i < myContactsList.size( ); i++) {
                ContactsTxtUtil.writeTxtToFile(txtName, (i + 1) + "  " + myContactsList.get(i));
            }
            SharedPreferences sp = getSharedPreferences("contactsTxt", Context.MODE_PRIVATE);
            SharedPreferences.Editor editor = sp.edit( );
            editor.putString("txtPath", txtPath);
            editor.commit( );
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, String[] permissions, int[] grantResults) {
        if (requestCode == PERMISSIONS_REQUEST_READ_CONTACTS) {
            if (grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                ToastUtils.showToast(context, "获取权限成功");
                chosePeople( );
            } else {
                ToastUtils.showToast(context, "请在设置中打开读取通讯录权限");
            }
            return;
        }
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
    }

    @OnClick({R.id.tv_relation_family, R.id.tv_phone_family, R.id.tv_relation_social, R.id.tv_phone_social, R.id.iv_back, R.id.tv_right})
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
            case R.id.tv_relation_family:
                PickerVewUtil.showPickerView(context, list_relation_family, tvRelationFamily);
                break;
            case R.id.tv_phone_family:
                chose_type = RESULT_CHOSE_FAMILY;
                checkPermission( );
                break;
            case R.id.tv_relation_social:
                PickerVewUtil.showPickerView(context, list_relation_social, tvRelationSocial);
                break;
            case R.id.tv_phone_social:
                chose_type = RESULT_CHOSE_SOCIAL;
                checkPermission( );
                break;
        }
    }
}
