package com.load.third.jqm.activity.info;

import android.Manifest;
import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.provider.MediaStore;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ScrollView;
import android.widget.TextView;

import com.load.third.jqm.ImgUtil.ImageViewUtils;
import com.load.third.jqm.MyApp;
import com.load.third.jqm.R;
import com.load.third.jqm.activity.BaseActivity;
import com.load.third.jqm.bean.newBean.QiniuName;
import com.load.third.jqm.httpUtil.QiNiuGetUtils;
import com.load.third.jqm.httpUtil.TokenLoginUtil;
import com.load.third.jqm.newHttp.Apis;
import com.load.third.jqm.newHttp.BaseResponse;
import com.load.third.jqm.newHttp.CommonObserver;
import com.load.third.jqm.newHttp.UrlParams;
import com.load.third.jqm.tips.DialogUtils;
import com.load.third.jqm.tips.ProgressDialog;
import com.load.third.jqm.tips.ToastUtils;
import com.load.third.jqm.utils.GlideLoader;
import com.load.third.jqm.utils.ImageFactory;
import com.load.third.jqm.utils.IntentUtils;
import com.load.third.jqm.utils.StringUtils;
import com.yancy.imageselector.ImageConfig;
import com.yancy.imageselector.ImageSelector;
import com.yancy.imageselector.ImageSelectorActivity;

import java.io.File;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import io.reactivex.schedulers.Schedulers;
import me.everything.android.ui.overscroll.OverScrollDecoratorHelper;

import static com.load.third.jqm.httpUtil.QiNiuGetUtils.MSG_GET_QINIU_UPLOAD;
import static com.load.third.jqm.httpUtil.TokenLoginUtil.MSG_TOKEN_LOGIN_SUCCESS;
import static com.load.third.jqm.utils.TempUtils.tempPicDirectory;
import static com.load.third.jqm.utils.TempUtils.tempPicFile;
import static com.load.third.jqm.utils.TempUtils.tempPicPath;

public class BindIdCardActivity extends BaseActivity {
    private static final int PERMISSIONS_REQUEST_EXTERNAL_STORAGE = 1;
    private static final int PERMISSIONS_REQUEST_CAMERA = 2;
    public static final int select_idcard_front = 101;
    public static final int select_idcard_back = 102;
    public static final int select_idcard_photo = 103;
    public static final int select_life_photo = 104;
    public static final int select_work_photo = 105;

    @BindView(R.id.iv_idcard_front)
    ImageView ivIdcardFront;
    @BindView(R.id.iv_idcard_front_select)
    ImageView ivIdcardFrontSelect;
    @BindView(R.id.iv_idcard_back)
    ImageView ivIdcardBack;
    @BindView(R.id.iv_idcard_back_select)
    ImageView ivIdcardBackSelect;
    @BindView(R.id.iv_idcard_photo)
    ImageView ivIdcardPhoto;
    @BindView(R.id.iv_idcard_photo_select)
    ImageView ivIdcardPhotoSelect;
    @BindView(R.id.iv_life_photo)
    ImageView ivLifePhoto;
    @BindView(R.id.iv_life_photo_select)
    ImageView ivLifePhotoSelect;
    @BindView(R.id.iv_work_photo)
    ImageView ivWorkPhoto;
    @BindView(R.id.iv_work_photo_select)
    ImageView ivWorkPhotoSelect;
    @BindView(R.id.btn_next)
    Button btnNext;
    @BindView(R.id.scrollView)
    ScrollView scrollView;
    @BindView(R.id.iv_back)
    ImageView ivBack;
    @BindView(R.id.tv_title)
    TextView tvTitle;
    @BindView(R.id.tv_right)
    TextView tvRight;

    private Context context;
    private int select_type;
    private String pic_idcard_front;//身份证人像面,本地路径
    private String pic_idcard_back;//身份证国徽面,本地路径
    private String pic_idcard_photo;//持证照片,本地路径
    private String pic_life_photo;//生活照,本地路径
    private String pic_work_photo;//工作照,本地路径
    private List<String> picList = new ArrayList<>();//上传图片到七牛后返回的url
    private String qiniuToken;
    @SuppressLint("HandlerLeak")
    private Handler handler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            switch (msg.what) {
                case MSG_TOKEN_LOGIN_SUCCESS:
                    ProgressDialog.showProgressBar(context, "请稍后...");
                    QiNiuGetUtils.getQiNiuToken(context, handler);
                    break;
                case QiNiuGetUtils.MSG_GET_QINIU_TOKEN:
                    qiniuToken = (String) msg.obj;
                    if (StringUtils.isNotBlank(qiniuToken)) {
                        String[] picArray = {pic_idcard_front, pic_idcard_back, pic_idcard_photo, pic_life_photo, pic_work_photo};
                        for (int i = 0; i < picArray.length; i++) {
                            getQiNiuName(picArray[i]);
                        }
                    }
                    break;
                case MSG_GET_QINIU_UPLOAD:
                    if (StringUtils.isNotBlank((String) msg.obj)) {
                        picList.add((String) msg.obj);
                        if (picList.size() == 5) {
                            bindIdCard();
                        }
                    }
                    break;
                default:
                    break;
            }
        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_bind_id_card);
        ButterKnife.bind(this);
        context = this;
        initView();
    }

    private void initView() {
        tvTitle.setText("证件照绑定");
        OverScrollDecoratorHelper.setUpOverScroll(scrollView);
    }

    //    判断读取文件权限
    private void checkPermission() {
        if (ContextCompat.checkSelfPermission(context,
                Manifest.permission.READ_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(this,
                    new String[]{Manifest.permission.READ_EXTERNAL_STORAGE}, PERMISSIONS_REQUEST_EXTERNAL_STORAGE);
        } else {
            selectPic();
        }
    }

    //    打开本地相册选取照片
    private void selectPic() {
        if (!tempPicFile.exists()) {
            tempPicFile.mkdirs();
        }
        ImageConfig imageConfig
                = new ImageConfig.Builder(new GlideLoader())
                .titleBgColor(getResources().getColor(R.color.main_color))
                .titleSubmitTextColor(getResources().getColor(R.color.main_color))
                .titleTextColor(getResources().getColor(R.color.white))
                .singleSelect()
                .showCamera()
                .filePath(tempPicPath)
                .build();
        ImageSelector.open(this, imageConfig);
    }

    //    判断拍照权限
    private void checkCameraPermission(int type, int resultCode) {
        if (ContextCompat.checkSelfPermission(context,
                Manifest.permission.CAMERA) != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(this,
                    new String[]{Manifest.permission.CAMERA}, PERMISSIONS_REQUEST_CAMERA);
        } else {
            if (resultCode == select_idcard_front || resultCode == select_idcard_back) {
                toCameraActivity(type, resultCode);
            } else {
                if (resultCode == select_idcard_photo) {
                    takePhoto();
                }
            }
        }
    }

    //    身份证拍照,打开自定义相机
    private void toCameraActivity(int type, int resultCode) {
        Intent intent = new Intent(context, CameraActivity.class);
        intent.putExtra(CameraActivity.TYPE_CAMERA, type);
        startActivityForResult(intent, resultCode);
    }

    private Uri takePhotoUrl;//拍摄持证照片的Url

    private void takePhoto() {
        takePhotoUrl = Uri.fromFile(new File(tempPicDirectory, System.currentTimeMillis() + ".jpg"));
        Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        intent.putExtra(android.provider.MediaStore.EXTRA_OUTPUT, takePhotoUrl);
        startActivityForResult(intent, select_idcard_photo);
    }

    private void setImageView(String path, String outPath, ImageView imageView) {
        try {
            new ImageFactory().compressAndGenImage(path, outPath, 200, false);
            ImageViewUtils.displayImage(context, outPath, imageView);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void uploadPic() {
        if (StringUtils.isBlank(pic_idcard_front)) {
            ToastUtils.showToast(context, "请上传身份证（人像面）");
            return;
        }
        if (StringUtils.isBlank(pic_idcard_back)) {
            ToastUtils.showToast(context, "请上传身份证（国徽面）");
            return;
        }
        if (StringUtils.isBlank(pic_idcard_photo)) {
            ToastUtils.showToast(context, "请上传正面执持证照");
            return;
        }
        if (StringUtils.isBlank(pic_life_photo)) {
            ToastUtils.showToast(context, "请上传生活照");
            return;
        }
        if (StringUtils.isBlank(pic_work_photo)) {
            ToastUtils.showToast(context, "请上传工作照");
            return;
        }
        if (!MyApp.isNeedUpdate) {
            TokenLoginUtil.loginWithToken(context, handler);
        }
    }

    private void getQiNiuName(final String picPath) {

        apiRetrofit.getQiNiuName(Apis.getQiNiuName.getUrl())

                .subscribeOn(Schedulers.io())
                .subscribe(new CommonObserver<QiniuName>() {
                    @Override
                    public void doSuccess(BaseResponse<QiniuName> result) {
                        if (result != null) {
                            String qiniuName = result.getData().getFileName() + ".jpg";
                            if (StringUtils.isNotBlank(qiniuName) && StringUtils.isNotBlank(picPath)) {
                                QiNiuGetUtils.uploadToQianNiuYun(context, handler, qiniuToken, qiniuName, picPath);
                            }
                        }

                    }
                });
        //        ApiClient.getInstance().getQiNiuName(new OkHttpClientManager.ResultCallback<DataJsonResult<JSONObject>>() {
//
//            @Override
//            public void onError(Request request, Exception e, String error) {
//                ProgressDialog.cancelProgressBar();
//                ToastUtils.showToast(context, "网络请求失败");
//                Log.e("http_msg", "获取七牛name失败");
//            }
//
//            @Override
//            public void onResponse(DataJsonResult<JSONObject> response) {
//                if (response.getSuccess() == "true") {
//                    String qiniuName = response.getData().getString("fileName") + ".jpg";
//                    if (StringUtils.isNotBlank(qiniuName) && StringUtils.isNotBlank(picPath)) {
//                        QiNiuGetUtils.uploadToQianNiuYun(context, handler, qiniuToken, qiniuName, picPath);
//                    }
//                } else {
//                    ProgressDialog.cancelProgressBar();
//                    ToastUtils.showToast(context, "网络请求失败");
//                    Log.e("http_msg", "获取七牛name失败");
//                }
//            }
//        });
    }

    private void bindIdCard() {

        Map<String, Object> params = new HashMap<>();
        for (int i = 0; i < picList.size(); i++) {
            params.put("url" + i, picList.get(i));
        }
        submitTask(apiRetrofit.getBindIdCard(UrlParams.getUrl(Apis.bindIdCard.getUrl(), params)), new CommonObserver() {
            @Override
            public void doSuccess(BaseResponse result) {
                IntentUtils.toMainActivity(context);
                ToastUtils.showToast(context, "绑定成功");
            }

        });

//        String token = UserDao.getInstance(context).getToken();
//        ApiClient.getInstance().bindIdCard(token, picList.get(0), picList.get(1), picList.get(2),
//                picList.get(4), picList.get(3), new OkHttpClientManager.ResultCallback<DataJsonResult<String>>() {
//
//                    @Override
//                    public void onError(Request request, Exception e, String error) {
//                        ProgressDialog.cancelProgressBar();
////                        ToastUtils.showToast(context, error);
//                        ToastUtils.showToast(context, "绑定失败，请重试");
//                    }
//
//                    @Override
//                    public void onResponse(DataJsonResult<String> response) {
//                        ProgressDialog.cancelProgressBar();
//                        if (response.getSuccess() == "true") {
//                            IntentUtils.toMainActivity(context);
//                            ToastUtils.showToast(context, "绑定成功");
//                        } else {
//                            ToastUtils.showToast(context, response.getMessage());
//                        }
//                    }
//                });
    }

    @Override
    protected void onActivityResult(final int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == select_idcard_front && resultCode == RESULT_OK && data != null) {
            pic_idcard_front = data.getExtras().getString("camera_result");
            ImageViewUtils.displayImage(context, pic_idcard_front, ivIdcardFront);
        } else if (requestCode == select_idcard_back && resultCode == RESULT_OK && data != null) {
            pic_idcard_back = data.getExtras().getString("camera_result");
            ImageViewUtils.displayImage(context, pic_idcard_back, ivIdcardBack);
        } else if (requestCode == select_idcard_photo && resultCode == RESULT_OK && takePhotoUrl != null) {
//                手持证件照，直接拍照
            pic_idcard_photo = takePhotoUrl.getPath();
            ImageViewUtils.displayImage(context, pic_idcard_photo, ivIdcardPhoto);
        } else if (requestCode == ImageSelector.IMAGE_REQUEST_CODE && resultCode == RESULT_OK && data != null) {
            switch (select_type) {
//                手持证件照，去相册选取
//                case select_idcard_photo:
//                    pic_idcard_photo = data.getStringArrayListExtra(ImageSelectorActivity.EXTRA_RESULT).get(0);
//                    ImageViewUtils.displayImage(context, pic_idcard_photo, ivIdcardPhoto);
//                    break;
                case select_life_photo:
                    pic_life_photo = data.getStringArrayListExtra(ImageSelectorActivity.EXTRA_RESULT).get(0);
                    ImageViewUtils.displayImage(context, pic_life_photo, ivLifePhoto);
                    break;
                case select_work_photo:
                    pic_work_photo = data.getStringArrayListExtra(ImageSelectorActivity.EXTRA_RESULT).get(0);
                    ImageViewUtils.displayImage(context, pic_work_photo, ivWorkPhoto);
                    break;
                default:
                    break;
            }
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, String[] permissions, int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        switch (requestCode) {
            case PERMISSIONS_REQUEST_EXTERNAL_STORAGE:
                if (grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    selectPic();
                } else {
                    ToastUtils.showToast(context, "请在设置中打开访问设备文件权限");
                }
                break;
            case PERMISSIONS_REQUEST_CAMERA:
                if (grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    ToastUtils.showToast(context, "获取权限成功");
                } else {
                    ToastUtils.showToast(context, "请在设置中打开相机权限");
                }
                break;
            default:
                break;
        }
    }

    private void back() {
        DialogUtils.getInstance(context).showTipsDialog("是否退出证件照绑定？", new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                DialogUtils.getInstance(context).dismiss();
                finish();
            }
        });
    }

    @Override
    public void onBackPressed() {
        back();
    }

    @OnClick({R.id.iv_idcard_front, R.id.iv_idcard_back, R.id.iv_idcard_photo, R.id.iv_life_photo, R.id.iv_work_photo, R.id.btn_next, R.id.iv_back})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.iv_idcard_front:
                checkCameraPermission(CameraActivity.TYPE_CAMERA_FRONT, select_idcard_front);
                break;
            case R.id.iv_idcard_back:
                checkCameraPermission(CameraActivity.TYPE_CAMERA_BACK, select_idcard_back);
                break;
            case R.id.iv_idcard_photo:
//                手持证件照，直接去拍照
                checkCameraPermission(3, select_idcard_photo);
//                手持证件照，去相册选取
//                select_type = select_idcard_photo;
//                checkPermission( );
                break;
            case R.id.iv_life_photo:
                select_type = select_life_photo;
                checkPermission();
                break;
            case R.id.iv_work_photo:
                select_type = select_work_photo;
                checkPermission();
                break;
            case R.id.btn_next:
                uploadPic();
                break;
            case R.id.iv_back:
                back();
                break;
            default:
                break;
        }
    }
}
