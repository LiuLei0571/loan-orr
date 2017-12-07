package com.load.third.jqm.activity.home;

import android.app.Dialog;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

import com.load.third.jqm.R;
import com.load.third.jqm.activity.BaseActivity;

import static com.load.third.jqm.service.GetuiIntentService.payloadData;

/**
 * 收到个推透传消息,显示弹窗
 */
public class GetuiDialogActivity extends BaseActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_getui_dialog);
        showGetuiDialog();
    }

    private void showGetuiDialog() {
        final Dialog dialog = new Dialog(this, R.style.CommonDialogStyle);
        dialog.setContentView(R.layout.dialog_tips);
        ((TextView) dialog.findViewById(R.id.tv_tips)).setText(payloadData);
        dialog.findViewById(R.id.tv_dialog_ok).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                payloadData = "";
                dialog.dismiss();
                finish();
            }
        });
        dialog.setCanceledOnTouchOutside(false);
        dialog.setCancelable(false);
        dialog.show();
    }
}
