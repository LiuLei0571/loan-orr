package com.load.third.jqm.utils;

import android.os.CountDownTimer;
import android.widget.TextView;

/**
 * Created by Administrator on 2016/11/29.
 */
public class CountDownTimerUtil extends CountDownTimer {
    public TextView tv;

    public CountDownTimerUtil(long millisInFuture, long countDownInterval, TextView textview) {
        super(millisInFuture, countDownInterval);
        this.tv = textview;
    }

    @Override
    public void onFinish() {
        tv.setText("发送验证码");
        tv.setEnabled(true);
    }

    @Override
    public void onTick(long millisUntilFinished) {
        tv.setText(millisUntilFinished / 1000 + "s后重新发送");
        tv.setEnabled(false);
    }
}
