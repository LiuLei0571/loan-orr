package com.load.third.jqm.activity.info;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.PixelFormat;
import android.graphics.Point;
import android.graphics.Rect;
import android.hardware.Camera;
import android.os.Bundle;
import android.view.MotionEvent;
import android.view.Surface;
import android.view.SurfaceHolder;
import android.view.SurfaceView;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.load.third.jqm.R;
import com.load.third.jqm.activity.BaseActivity;
import com.load.third.jqm.utils.ImageFactory;
import com.load.third.jqm.utils.Utils;
import com.load.third.jqm.view.MaskView;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

import static com.load.third.jqm.utils.TempUtils.tempPicDirectory;
import static com.load.third.jqm.utils.TempUtils.tempPicFile;

/**
 * @author liulei
 */
public class CameraActivity extends BaseActivity {
    public static final String TYPE_CAMERA = "type_camera";
    public static final int TYPE_CAMERA_FRONT = 0;
    public static final int TYPE_CAMERA_BACK = 1;

    public static final int CAMERA_WIDTH = 400;
    public static final int CAMERA_HEIGH = 250;

    @BindView(R.id.surfaceView)
    SurfaceView surfaceView;
    @BindView(R.id.iv_idcard_back)
    ImageView ivIdcardBack;
    @BindView(R.id.iv_idcard_front)
    ImageView ivIdcardFront;
    @BindView(R.id.tv_tips)
    TextView tvTips;
    @BindView(R.id.iv_take)
    ImageView ivTake;
    @BindView(R.id.iv_back)
    ImageView ivBack;
    @BindView(R.id.maskView)
    MaskView maskView;

    private Context context;
    private int type;
    private Camera camera;
    private Camera.Parameters parameters = null;
    private Point screenMetrics;
    private int wRect;
    private int hRect;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_camera);
        ButterKnife.bind(this);
        context = this;
        setSurfaceView();
        initView();
    }

    private void setSurfaceView() {
        screenMetrics = Utils.getScreenMetrics(context);
        wRect = Utils.dip2px(context, CAMERA_WIDTH);
        hRect = Utils.dip2px(context, CAMERA_HEIGH);
        surfaceView.getHolder().setType(SurfaceHolder.SURFACE_TYPE_PUSH_BUFFERS);
        surfaceView.getHolder().setFixedSize(176, 144); //设置Surface分辨率
        surfaceView.getHolder().setKeepScreenOn(true);// 屏幕常亮
        surfaceView.getHolder().addCallback(new SurfaceCallback());//为SurfaceView的句柄添加一个回调函数
        if (maskView != null) {
            maskView.setCenterRect(createCenterScreenRect());
        }
        surfaceView.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                camera.autoFocus(autoFocusCallback);
                return true;
            }
        });
    }

    private void initView() {
        type = getIntent().getIntExtra(TYPE_CAMERA, TYPE_CAMERA_FRONT);
        switch (type) {
            case TYPE_CAMERA_FRONT:
                tvTips.setText("请拍摄身份证（人像面），并尝试与线框对齐");
                ivIdcardFront.setVisibility(View.VISIBLE);
                break;
            case TYPE_CAMERA_BACK:
                tvTips.setText("请拍摄身份证（国徽面），并尝试与线框对齐");
                ivIdcardBack.setVisibility(View.VISIBLE);
                break;
            default:
                break;
        }
    }

    private Camera.AutoFocusCallback autoFocusCallback = new Camera.AutoFocusCallback() {
        @Override
        public void onAutoFocus(boolean success, Camera camera) {
        }
    };

    @OnClick({R.id.iv_take, R.id.iv_back})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.iv_take:
                camera.takePicture(null, null, new MyPictureCallback());
                break;
            case R.id.iv_back:
                finish();
                break;
            default:
                break;
        }
    }

    private final class MyPictureCallback implements Camera.PictureCallback {

        @Override
        public void onPictureTaken(byte[] data, Camera camera) {
            try {
                saveBitmap(data);
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

    private void saveBitmap(byte[] data) {
        if (!tempPicFile.exists()) {
            tempPicFile.mkdirs();
        }
        Bitmap bitmap = BitmapFactory.decodeByteArray(data, 0, data.length);
        double rate = (float) bitmap.getWidth() / (float) screenMetrics.x;
//        int width = (int) (wRect * rate);
//        int height = (int) (hRect * rate);
//        int x = (bitmap.getWidth() - width - (int) (Utils.dip2px(context, 78) * rate)) / 2;
//        int y = (bitmap.getHeight() - height - (int) (Utils.dip2px(context, 31) * rate)) / 2;
//        Bitmap rectBitmap = Bitmap.createBitmap(bitmap, x, y, width, height);
        Bitmap rectBitmap = bitmap;
        String outPath = tempPicDirectory + System.currentTimeMillis() + ".jpg";
        new ImageFactory().compressAndGenImage(rectBitmap, outPath, 2000000);
        Intent resultIntent = new Intent();
        Bundle bundle = new Bundle();
        bundle.putString("camera_result", outPath);
        resultIntent.putExtras(bundle);
        setResult(RESULT_OK, resultIntent);
        finish();
    }

    private Rect createCenterScreenRect() {
        int x1 = (screenMetrics.x - wRect - Utils.dip2px(context, 78)) / 2;
        int y1 = (screenMetrics.y - hRect) / 2 - Utils.dip2px(context, 31);
        int x2 = x1 + wRect;
        int y2 = y1 + hRect;
        return new Rect(x1, y1, x2, y2);
    }

    private final class SurfaceCallback implements SurfaceHolder.Callback {
        @Override
        public void surfaceChanged(SurfaceHolder holder, int format, int width,
                                   int height) {
            parameters = camera.getParameters(); // 获取各项参数
            parameters.setPictureFormat(PixelFormat.JPEG); // 设置图片格式
            parameters.setPreviewSize(width, height); // 设置预览大小
            parameters.setPreviewFrameRate(5);  //设置每秒显示4帧
            parameters.setPictureSize(width, height); // 设置保存的图片尺寸
            parameters.setJpegQuality(100); // 设置照片质量
        }

        // 开始拍照时调用该方法
        @Override
        public void surfaceCreated(SurfaceHolder holder) {
            try {
                camera = Camera.open(); // 打开摄像头
                camera.setPreviewDisplay(holder); // 设置用于显示拍照影像的SurfaceHolder对象
                camera.setDisplayOrientation(getPreviewDegree(CameraActivity.this));
                camera.startPreview(); // 开始预览
            } catch (Exception e) {
                e.printStackTrace();
            }
        }

        // 停止拍照时调用该方法
        @Override
        public void surfaceDestroyed(SurfaceHolder holder) {
            if (camera != null) {
                camera.release(); // 释放照相机
                camera = null;
            }
        }
    }

    // 提供一个静态方法，用于根据手机方向获得相机预览画面旋转的角度
    public static int getPreviewDegree(Activity activity) {
        // 获得手机的方向
        int rotation = activity.getWindowManager().getDefaultDisplay().getRotation();
        int degree = 0;
        // 根据手机的方向计算相机预览画面应该选择的角度
        switch (rotation) {
            case Surface.ROTATION_0:
                degree = 90;
                break;
            case Surface.ROTATION_90:
                degree = 0;
                break;
            case Surface.ROTATION_180:
                degree = 270;
                break;
            case Surface.ROTATION_270:
                degree = 180;
                break;
            default:
                break;
        }
        return degree;
    }
}
