package com.load.third.jqm.utils;

import android.content.Context;
import android.content.Intent;

import com.load.third.jqm.activity.MainActivity;
import com.load.third.jqm.activity.WebViewActivity;
import com.load.third.jqm.activity.home.RepaymentActivity;
import com.load.third.jqm.activity.info.CameraActivity;
import com.load.third.jqm.activity.mine.TicketActivity;

public class IntentUtils {
    public static void toActivity(Context context, Class<?> mClass) {
        Intent intent = new Intent(context, mClass);
        context.startActivity(intent);
    }

    public static void toWebViewActivity(Context context, String title, String url) {
        Intent intent = new Intent(context, WebViewActivity.class);
        intent.putExtra(WebViewActivity.WEB_URL, url);
        intent.putExtra(WebViewActivity.WEB_TITLE, title);
        context.startActivity(intent);
    }

    public static void toMainActivity(Context context) {
        Intent intent = new Intent(context, MainActivity.class);
        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
        context.startActivity(intent);
    }

    public static void toTicketActivity(Context context, int type) {
        Intent intent = new Intent(context, TicketActivity.class);
        intent.putExtra(TicketActivity.TYPE_TICKET, type);
        context.startActivity(intent);
    }

    public static void toRepaymentActivity(Context context, String money) {
        Intent intent = new Intent(context, RepaymentActivity.class);
        intent.putExtra(RepaymentActivity.MONEY_REPAY, money);
        context.startActivity(intent);
    }

    public static void toCameraActivity(Context context, int type) {
        Intent intent = new Intent(context, CameraActivity.class);
        intent.putExtra(CameraActivity.TYPE_CAMERA, type);
        context.startActivity(intent);
    }
}
