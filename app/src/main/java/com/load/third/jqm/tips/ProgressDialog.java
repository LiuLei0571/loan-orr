package com.load.third.jqm.tips;

import android.app.Dialog;
import android.content.Context;
import android.view.View;
import android.widget.TextView;

import com.load.third.jqm.R;
import com.load.third.jqm.utils.StringUtils;

/**
 * Created by Administrator on 2017/3/24.
 */

public class ProgressDialog {
    private static Dialog progressBar;
    private static TextView tv_tips;

    private ProgressDialog() {
    }

    public static void showProgressBar(Context context, String tips) {
        if (progressBar!=null&&progressBar.isShowing( )) {
            if (StringUtils.isNotBlank(tips)) {
                tv_tips.setVisibility(View.VISIBLE);
                tv_tips.setText(tips);
            }
        } else {
            progressBar = new Dialog(context, R.style.TransparentDialogStyle);
            progressBar.setContentView(R.layout.dialog_progressbar);
            tv_tips=(TextView) progressBar.findViewById(R.id.tv_tips);
            if (StringUtils.isNotBlank(tips)) {
                tv_tips.setVisibility(View.VISIBLE);
                tv_tips.setText(tips);
            }
            progressBar.setCanceledOnTouchOutside(false);
            progressBar.setCancelable(false);
            progressBar.show( );
        }
    }

    public static void cancelProgressBar() {
        progressBar.dismiss( );
    }
}
