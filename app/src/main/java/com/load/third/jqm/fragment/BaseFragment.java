package com.load.third.jqm.fragment;

import android.app.Fragment;
import android.content.Context;

import com.load.third.jqm.newHttp.ApiRequest;
import com.load.third.jqm.newHttp.CommonObserver;
import com.load.third.jqm.newHttp.ILoading;
import com.load.third.jqm.tips.ProgressDialog;

/**
 * 用途：
 * 作者：Created by liulei on 2017/11/22.
 * 邮箱：liulei2@aixuedai.com
 */


public class BaseFragment extends Fragment implements ILoading {


    @Override
    public void showLoading(Context context) {
        ProgressDialog.showProgressBar(context);
    }

    @Override
    public void showLoading(Context context, String content) {
        ProgressDialog.showProgressBar(context, content);

    }

    @Override
    public void dissLoading() {
        ProgressDialog.cancelProgressBar();
    }

    public ILoading getLoading() {
        return this;
    }

    public void submitTask(ApiRequest api, CommonObserver commonObserver) {

    }
}
