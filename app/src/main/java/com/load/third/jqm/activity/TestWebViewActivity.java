package com.load.third.jqm.activity;

import android.app.Activity;
import android.content.Context;
import android.os.Bundle;
import android.webkit.WebView;
import android.webkit.WebViewClient;

import com.load.third.jqm.R;
import com.load.third.jqm.tips.ToastUtils;

import butterknife.BindView;
import butterknife.ButterKnife;

public class TestWebViewActivity extends Activity {

    @BindView(R.id.webView)
    WebView webView;
    private Context context;
    private String Url;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_test_web_view);
        ButterKnife.bind(this);
        context = this;
        initView( );
    }

    @Override
    protected void onDestroy() {
        super.onDestroy( );
        webView.destroy( );
    }

    private void initView() {
        Url = "http://weixin.shike8888.com/";
        webView.getSettings( ).setJavaScriptEnabled(true);
        webView.getSettings( ).setDomStorageEnabled(true);
        webView.loadUrl(Url);
        webView.setWebViewClient(new WebViewClient( ) {
            @Override
            public boolean shouldOverrideUrlLoading(WebView view, String url) {
                view.loadUrl(url);
                return true;
            }
        });
    }

    private void back() {
        if (webView.canGoBack( )) {
            webView.goBack( );
        } else {
            long currentTime = System.currentTimeMillis( );
            if ((currentTime - touchTime) >= waitTime) {
                ToastUtils.showToast(this, "再按一次退出");
                touchTime = currentTime;
            } else {
                System.exit(0);
            }
        }
    }

    private long waitTime = 2000;
    private long touchTime = 0;

    @Override
    public void onBackPressed() {
        back( );
    }
}
