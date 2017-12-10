package com.load.third.jqm.help;

import android.content.Context;
import android.os.Handler;
import android.os.Message;
import android.util.Log;

import com.load.third.jqm.tips.ProgressDialog;
import com.load.third.jqm.tips.ToastUtils;
import com.qiniu.android.common.AutoZone;
import com.qiniu.android.http.ResponseInfo;
import com.qiniu.android.storage.Configuration;
import com.qiniu.android.storage.UpCompletionHandler;
import com.qiniu.android.storage.UploadManager;

import static com.load.third.jqm.utils.Urls.BASE_URL;

/**
 * Created by Administrator on 2017/4/17.
 */

public class QiNiuGetUtils {
    public static final int MSG_GET_QINIU_TOKEN = 1000;
    public static final int MSG_GET_QINIU_NAME = 1001;
    public static final int MSG_GET_QINIU_UPLOAD = 1002;

    public static void uploadToQianNiuYun(final Context context, final Handler handler, String qiniuToken, String qiniuName, String path) {
        UploadManager uploadManager = new UploadManager(new Configuration.Builder().zone(AutoZone.autoZone).build());
        uploadManager.put(path, qiniuName, qiniuToken, new UpCompletionHandler() {
            @Override
            public void complete(String key, ResponseInfo info, org.json.JSONObject res) {
                if (info.isOK()) {
                    String qiniuUrl = BASE_URL + key;
                    Log.i("qiniu", "Upload Success" + key);
                    Log.i("qiniu", "Upload Success" + qiniuUrl);
                    Message message = handler.obtainMessage();
                    message.what = MSG_GET_QINIU_UPLOAD;
                    message.obj = qiniuUrl;
                    handler.sendMessage(message);
                } else {
                    ProgressDialog.cancelProgressBar();
                    ToastUtils.showToast(context, "网络请求失败");
                    Log.i("qiniu", "Upload Fail " + key);
                }
            }
        }, null);
    }
}
