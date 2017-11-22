package com.load.third.jqm.utils;

import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

/**
 * Created by Administrator on 2016/11/25.
 */
public class TextWatcherUtil implements TextWatcher {
    private Button btn;
    private View[] viewArr;

    public TextWatcherUtil(View[] viewArr, Button btn) {
        this.viewArr = viewArr;
        this.btn = btn;
    }

    @Override
    public void beforeTextChanged(CharSequence s, int start, int count, int after) {

    }

    @Override
    public void onTextChanged(CharSequence s, int start, int before, int count) {
        btn.setEnabled(checkText());
    }

    @Override
    public void afterTextChanged(Editable s) {

    }

    private boolean checkText() {
        for (int i = 0; i < viewArr.length; i++) {
            if (viewArr[i] instanceof EditText) {
                if (StringUtils.getTextValue((EditText) viewArr[i]).length() < ((EditText) viewArr[i]).getMaxEms())
                    return false;
                else if (StringUtils.getTextValue((EditText) viewArr[i]).length() <= 0)
                    return false;
            }
            if (viewArr[i] instanceof TextView) {
                if (StringUtils.getTextValue((TextView) viewArr[i]).length() <= 0)
                    return false;
            }
        }
        return true;
    }

    public static void setListener(View[] viewArr,TextWatcherUtil watcherListener){
        for (int i = 0; i < viewArr.length; i++) {
            if (viewArr[i] instanceof EditText) {
                ((EditText) viewArr[i]).addTextChangedListener(watcherListener);
            }
            if (viewArr[i] instanceof TextView) {
                ((TextView) viewArr[i]).addTextChangedListener(watcherListener);
            }
        }
    }
}
