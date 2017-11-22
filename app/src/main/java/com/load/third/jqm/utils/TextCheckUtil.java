package com.load.third.jqm.utils;

import android.view.View;
import android.widget.EditText;
import android.widget.TextView;

import java.util.List;

/**
 * Created by Administrator on 2017/3/30.
 */

public class TextCheckUtil {

    public static boolean checkText(List<View> viewArr) {
        for (int i = 0; i < viewArr.size(); i++) {
            if (viewArr.get(i) instanceof EditText) {
                if (StringUtils.getTextValue((EditText) viewArr.get(i)).length() <= 0)
                    return false;
            }
            if (viewArr.get(i) instanceof TextView) {
                if (StringUtils.getTextValue((TextView) viewArr.get(i)).length() <= 0)
                    return false;
            }
        }
        return true;
    }
}
