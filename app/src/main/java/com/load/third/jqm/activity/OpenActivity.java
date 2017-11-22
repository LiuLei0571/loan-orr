package com.load.third.jqm.activity;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;

import com.load.third.jqm.R;
import com.load.third.jqm.http.ApiClient;
import com.load.third.jqm.http.OkHttpClientManager;
import com.load.third.jqm.utils.IntentUtils;
import com.squareup.okhttp.Request;

public class OpenActivity extends Activity {
    private Context context;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_open);
        context = this;
        isChecking( );
    }

    private void isChecking() {
        ApiClient.getInstance( ).isChecking(new OkHttpClientManager.ResultCallback<String>( ) {
            @Override
            public void onError(Request request, Exception e, String error) {
                IntentUtils.toMainActivity(context);
            }

            @Override
            public void onResponse(String response) {
                Log.i("http_msg", response);
                if (response.contains("false")) {
                    Intent intent = new Intent(context, TestWebViewActivity.class);
                    intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                    context.startActivity(intent);
                } else {
                    IntentUtils.toMainActivity(context);
                }
            }
        });
    }
}
