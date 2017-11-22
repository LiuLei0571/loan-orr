package com.load.third.jqm.tips;

import android.content.Context;
import android.widget.Toast;

import com.load.third.jqm.MyApp;

/**
 * Created by Administrator on 2016/11/16.
 */
public class ToastUtils {

    private static Toast toast;

    public static void showToast(Context context,
                                 int content) {
        if (toast == null) {
            toast = Toast.makeText(context, content, Toast.LENGTH_SHORT);
        } else {
            toast.setText(content);
        }
        toast.show();
    }

    public static void showToast(Context context,
                                 String content) {
        if (toast == null) {
            toast = Toast.makeText(context, content, Toast.LENGTH_SHORT);
        } else {
            toast.setText(content);
        }
        toast.show();
    }

    public static void showToast(String content) {
        if (toast == null) {
            toast = Toast.makeText(MyApp.getContext(), content, Toast.LENGTH_SHORT);
        } else {
            toast.setText(content);
        }
        toast.show();
    }
}
