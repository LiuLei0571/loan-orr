package com.load.third.jqm.tips;

import android.app.Activity;
import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.widget.TextView;

import com.load.third.jqm.R;
import com.load.third.jqm.utils.Consts;
import com.load.third.jqm.utils.Urls;

/**
 * Created by Administrator on 2016/4/29.
 */
public class DialogUtils {
    private static DialogUtils instance;
    private Context context;
    private Dialog dialog;

    private DialogUtils() {
    }

    public static DialogUtils getInstance(Context context) {
        if (instance == null) {
            instance = new DialogUtils();
        }
        instance.context = context;
        return instance;
    }

    public void showTipsDialog(String tips, View.OnClickListener onClickListener) {
        dialog = new Dialog(context, R.style.CommonDialogStyle);
        dialog.setContentView(R.layout.dialog_tips);
        ((TextView) dialog.findViewById(R.id.tv_tips)).setText(tips);
        TextView tv_cancel = (TextView) dialog.findViewById(R.id.tv_dialog_cancel);
        tv_cancel.setVisibility(View.VISIBLE);
        DialogUtils.setDialogCancelListener(tv_cancel, dialog);
        dialog.findViewById(R.id.tv_dialog_ok).setOnClickListener(onClickListener);
        dialog.show();
    }

    public void showOkTipsDialog(String tips) {
        dialog = new Dialog(context, R.style.CommonDialogStyle);
        dialog.setContentView(R.layout.dialog_tips);
        ((TextView) dialog.findViewById(R.id.tv_tips)).setText(tips);
        DialogUtils.setDialogCancelListener(dialog.findViewById(R.id.tv_dialog_ok), dialog);
        dialog.show();
    }

    public void dismiss() {
        dialog.dismiss();
    }

    public static void setDialogCancelListener(View cannel, final Dialog dialog) {
        cannel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                dialog.dismiss();
            }
        });
    }

    public static void setDialogFullWidth(Activity activity, Dialog dialog) {
        Window window = dialog.getWindow();
        WindowManager.LayoutParams wl = window.getAttributes();
        wl.y = activity.getWindowManager().getDefaultDisplay().getHeight();
        wl.width = ViewGroup.LayoutParams.MATCH_PARENT;
        dialog.onWindowAttributesChanged(wl);
    }

    public static void showInviteDialog(final Context context) {
        final Dialog dialog = new Dialog(context, R.style.CommonDialogStyle);
        dialog.setContentView(R.layout.dialog_invite);
        DialogUtils.setDialogFullWidth((Activity) context, dialog);
        DialogUtils.setDialogCancelListener(dialog.findViewById(R.id.tv_dialog_cancel), dialog);
        dialog.findViewById(R.id.btn_invite_code).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.dismiss();
                showInviteCodeDialog(context);
            }
        });
        dialog.show();
    }

    public static void showInviteCodeDialog(Context context) {
        final Dialog dialog = new Dialog(context, R.style.CommonDialogStyle);
        dialog.setContentView(R.layout.dialog_invite_code);
        dialog.show();
    }

    public static void showQQServiceDialog(final Context context) {
        final Dialog dialog = new Dialog(context, R.style.CommonDialogStyle);
        dialog.setContentView(R.layout.dialog_qq_service);
        DialogUtils.setDialogFullWidth((Activity) context, dialog);
        DialogUtils.setDialogCancelListener(dialog.findViewById(R.id.tv_dialog_cancel), dialog);
        dialog.findViewById(R.id.tv_after_borrow).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                openQQ(context, Urls.url_open_qq + Consts.QQ_after_borrow);
            }
        });
        dialog.findViewById(R.id.tv_before_borrow).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                openQQ(context, Urls.url_open_qq + Consts.QQ_before_borrow);
            }
        });
        dialog.show();
    }

    private static void openQQ(Context context, String url) {
        try {
            context.startActivity(new Intent(Intent.ACTION_VIEW, Uri.parse(url)));
        } catch (Exception e) {

        }
    }

}
