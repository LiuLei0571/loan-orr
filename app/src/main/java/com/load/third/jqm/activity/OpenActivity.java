package com.load.third.jqm.activity;

import android.content.Intent;
import android.os.Bundle;

import com.load.third.jqm.R;
import com.load.third.jqm.newHttp.Apis;
import com.load.third.jqm.newHttp.BaseResponse;
import com.load.third.jqm.newHttp.CommonObserver;
import com.load.third.jqm.utils.IntentUtils;

public class OpenActivity extends BaseActivity {

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_open);
        isChecking();
    }

    private void isChecking() {
        submitTask(apiRetrofit.getIsChecking(Apis.isChecking.getUrl()), new CommonObserver() {
            @Override
            public void doSuccess(BaseResponse result) {
                Intent intent = new Intent(getBaseContext(), TestWebViewActivity.class);
                intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                getBaseContext().startActivity(intent);
            }

            @Override
            public void doFail(String msg) {
                IntentUtils.toMainActivity(getBaseContext());
            }
        });
//        ApiClient.getInstance().isChecking(new OkHttpClientManager.ResultCallback<String>() {
//            @Override
//            public void onError(Request request, Exception e, String error) {
//                IntentUtils.toMainActivity(context);
//            }
//
//            @Override
//            public void onResponse(String response) {
//                Log.i("http_msg", response);
//                if (response.equals("false")) {
//                    Intent intent = new Intent(context, TestWebViewActivity.class);
//                    intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
//                    context.startActivity(intent);
//                } else {
//                    IntentUtils.toMainActivity(context);
//                }
//            }
//        });
    }
}
