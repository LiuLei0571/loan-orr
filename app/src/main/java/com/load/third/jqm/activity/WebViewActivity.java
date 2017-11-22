package com.load.third.jqm.activity;

import android.app.Activity;
import android.content.Context;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.webkit.WebChromeClient;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.ImageView;
import android.widget.TextView;

import com.load.third.jqm.R;
import com.load.third.jqm.tips.DialogUtils;
import com.load.third.jqm.utils.IntentUtils;
import com.load.third.jqm.utils.StringUtils;
import com.load.third.jqm.utils.Urls;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class WebViewActivity extends Activity {
    public static final String WEB_URL = "web_url";
    public static final String WEB_TITLE = "web_title";
    @BindView(R.id.iv_back)
    ImageView ivBack;
    @BindView(R.id.iv_close)
    ImageView ivClose;
    @BindView(R.id.tv_title)
    TextView tvTitle;
    @BindView(R.id.webView)
    WebView webView;

    private Context context;
    private String Url;
    private String title;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_web_view);
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
        Url = getIntent( ).getStringExtra(WEB_URL);
        title = getIntent( ).getStringExtra(WEB_TITLE);
        if (Url.contains(Urls.url_repayment)) {
            ivClose.setVisibility(View.VISIBLE);
        } else {
            ivBack.setVisibility(View.VISIBLE);
        }
        if (StringUtils.isBlank(title)) {
            WebChromeClient wvcc = new WebChromeClient( ) {
                @Override
                public void onReceivedTitle(WebView view, String title) {
                    super.onReceivedTitle(view, title);
                    tvTitle.setText(title);
                }
            };
            webView.setWebChromeClient(wvcc);
        } else {
            tvTitle.setText(title);
        }
        webView.getSettings( ).setJavaScriptEnabled(true);
        webView.getSettings( ).setDomStorageEnabled(true);
//        ProgressDialog.showProgressBar(context, "请稍后...");
//        webView.setWebChromeClient(new WebChromeClient( ) {
//            @Override
//            public void onProgressChanged(WebView view, int newProgress) {
//                if (newProgress == 100) {
//                    ProgressDialog.cancelProgressBar( );
//                } else {
//                }
//                super.onProgressChanged(view, newProgress);
//            }
//        });
        webView.loadUrl(Url);
        webView.setWebViewClient(new WebViewClient( ) {
            @Override
            public boolean shouldOverrideUrlLoading(WebView view, String url) {
                view.loadUrl(url);
                return true;
            }
        });
        WebSettings set = webView.getSettings( );
        set.setSavePassword(false);
        set.setSaveFormData(false);
    }

    private void back() {
        if ("手机验证".equals(title)) {
            Log.d("手机验证", webView.getUrl( ) + "");
            if (webView.getUrl( ) != null && webView.getUrl( ).contains(Urls.url_tianji_check_success)) {
                IntentUtils.toMainActivity(context);
            } else if (webView.canGoBack( )) {
                webView.goBack( );
            } else {
                DialogUtils.getInstance(context).showTipsDialog("是否退出手机验证？", new View.OnClickListener( ) {
                    @Override
                    public void onClick(View v) {
                        DialogUtils.getInstance(context).dismiss( );
                        IntentUtils.toMainActivity(context);
                    }
                });
            }
        } else {
            finish( );
        }
    }

    private void close() {
        if ("还款".equals(title)) {
            DialogUtils.getInstance(context).showTipsDialog("是否退出还款？", new View.OnClickListener( ) {
                @Override
                public void onClick(View v) {
                    DialogUtils.getInstance(context).dismiss( );
                    IntentUtils.toMainActivity(context);
                }
            });
        }
    }

    @Override
    public void onBackPressed() {
        if ("还款".equals(title)) {
            close( );
        } else {
            back( );
        }
    }

    @OnClick({R.id.iv_back, R.id.iv_close})
    public void onViewClicked(View view) {
        switch (view.getId( )) {
            case R.id.iv_back:
                back( );
                break;
            case R.id.iv_close:
                close( );
                break;
        }
    }
}
