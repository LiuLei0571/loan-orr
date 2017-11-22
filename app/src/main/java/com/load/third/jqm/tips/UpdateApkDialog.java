package com.load.third.jqm.tips;

import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.view.View;
import android.widget.TextView;

import com.load.third.jqm.R;

/**
 * Created by Administrator on 2017/4/20.
 */

public class UpdateApkDialog {
    private static UpdateApkDialog instance;
    private Context context;
    private Dialog dialog;

    private UpdateApkDialog() {
    }

    public static UpdateApkDialog getInstance(Context context) {
        if (instance == null) {
            instance = new UpdateApkDialog( );
        }
        instance.context = context;
        return instance;
    }

    public void showDialog(final String url) {
        dialog = new Dialog(context, R.style.CommonDialogStyle);
        dialog.setContentView(R.layout.dialog_update_apk);
        final TextView tv_tips = (TextView) dialog.findViewById(R.id.tv_tips);
        tv_tips.setText("检测到新版本，请更新");
        dialog.findViewById(R.id.tv_dialog_ok).setOnClickListener(new View.OnClickListener( ) {
            @Override
            public void onClick(View view) {
                tv_tips.setText("正在下载和安装，请稍后...");
                Intent intent = new Intent("ACTION_DOWNLOAD_APK");
                intent.putExtra("apk_url", url);
                context.sendBroadcast(intent);
            }
        });
        dialog.setCanceledOnTouchOutside(false);
        dialog.setCancelable(false);
        dialog.show( );
    }

    public void dismiss() {
        dialog.dismiss( );
    }
}
