package com.load.third.jqm.activity.info;

import android.app.Activity;
import android.content.Context;
import android.os.Bundle;
import android.util.Base64;
import android.view.View;
import android.webkit.ValueCallback;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.load.third.jqm.R;
import com.load.third.jqm.activity.info.taobao.CrawlerMetaModel;
import com.load.third.jqm.bean.UserDao;
import com.load.third.jqm.help.UrlHelp;
import com.load.third.jqm.tips.DialogUtils;
import com.load.third.jqm.view.progress.WaveProgress;
import com.load.third.okhttp.OkHttpUtils;
import com.load.third.okhttp.callback.Callback;

import org.apache.commons.lang3.StringUtils;

import java.math.BigDecimal;
import java.nio.charset.Charset;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import okhttp3.Call;
import okhttp3.Response;


/**
 * 淘宝认证
 */
public class TaoBaoVerifyActivity extends Activity {

    @BindView(R.id.iv_back)
    ImageView ivBack;
    @BindView(R.id.tv_title)
    TextView tvTitle;
    @BindView(R.id.wbv_web_view)
    WebView wbvWebView;
    @BindView(R.id.wave_progress_bar)
    WaveProgress waveProgressBar;
    @BindView(R.id.tv_progress_tips)
    TextView tvProgressTips;
    @BindView(R.id.ll_indicator)
    LinearLayout llIndicator;

    private Context context;
    private String userId;
    private CrawlerMetaModel crawlerMetaModel;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_tao_bao_verify);
        ButterKnife.bind(this);
        context = this;
        initView();
    }

    private void initView() {
        tvTitle.setText("淘宝认证");
        userId = UrlHelp.getDecode(UserDao.getInstance(context).getToken());

        initWebView();
        waveProgressBar.setValue(BigDecimal.ZERO.floatValue());
        toggleIndicator(false);
        startAuth(wbvWebView);
    }

    private void initWebView() {
        WebSettings webSettings = wbvWebView.getSettings();
        //设置WebView属性，能够执行Javascript脚本
        webSettings.setJavaScriptEnabled(true);
        //设置可以访问文件
        webSettings.setAllowFileAccess(true);
        //设置支持缩放
        webSettings.setBuiltInZoomControls(false);
        //设置Web视图
        wbvWebView.setWebViewClient(new CustomWebViewClient());
    }

    public class CustomWebViewClient extends WebViewClient {
        public void onPageFinished(WebView view, String url) {
            System.out.println("加载完成:" + url);
            if (crawlerMetaModel != null) {
                String state = crawlerMetaModel.getState();
                if (StringUtils.equals(state, CrawlerMetaModel.Login)) {
                    if (url.startsWith(crawlerMetaModel.getLoginSuccessUrl())) {
                        state = CrawlerMetaModel.Crawler;
                        crawlerMetaModel.setState(state);
                        toggleIndicator(true);
                    }
                }
                System.out.println("CrawlerMetaModel.Crawler:" + state);
                if (StringUtils.equals(state, CrawlerMetaModel.Crawler)) {
                    startCrawler(url);
                }
            }
        }
    }

    public void toggleIndicator(boolean isIndicatorShow) {
        if (isIndicatorShow) {
            llIndicator.setVisibility(View.VISIBLE);
            wbvWebView.setVisibility(View.GONE);
        } else {
            llIndicator.setVisibility(View.GONE);
            wbvWebView.setVisibility(View.VISIBLE);
        }
    }

    public void startAuth(final WebView webView) {
        OkHttpUtils.get().url("http://crawler.jietiaozhan.com/crawler/auth/get")
                .addParams("userId", userId)
                .addParams("key", "taobao")
                .build().execute(new Callback() {
            @Override
            public Object parseNetworkResponse(Response response, int id) throws Exception {
                String json = response.body().string();
                if (StringUtils.isNotBlank(json)) {
                    JSONObject jsonObject = JSON.parseObject(json);
                    Boolean result = jsonObject.getBoolean("result");
                    String code = jsonObject.getString("code");
                    String message = jsonObject.getString("message");
                    if (result == true) {
                        CrawlerMetaModel model = jsonObject.getObject("object", CrawlerMetaModel.class);
                        if (model != null) {
                            crawlerMetaModel = model;
                            final String url = model.getLoginUrl();
                            crawlerMetaModel.setState(CrawlerMetaModel.Login);
                            crawlerMetaModel.setNextCrawlerUrl(model.getLoginSuccessUrl());
                            crawlerMetaModel.setMessage(message);
                            webView.post(new Runnable() {
                                @Override
                                public void run() {
                                    webView.loadUrl(url);
                                }
                            });
                        }
                    } else if (code.equals("200")) {
                        crawlerMetaModel.setPercent(BigDecimal.ONE);
                        webView.post(new Runnable() {
                            @Override
                            public void run() {
                                triggerFinished();
                            }
                        });
                    }
                }
                return null;
            }

            @Override
            public void onError(final Call call, final Exception e, final int id) {
                webView.post(new Runnable() {
                    @Override
                    public void run() {
                        triggerError(call, e, id);
                    }
                });
            }

            @Override
            public void onResponse(Object response, int id) {

            }
        });
    }

    public void startCrawler(final String currentUrl) {
        wbvWebView.evaluateJavascript("document.getElementsByTagName('html')[0].outerHTML", new ValueCallback<String>() {
            @Override
            public void onReceiveValue(String value) {
                String initUrl = crawlerMetaModel.getNextCrawlerUrl();
                String url = currentUrl;
                String html = value.replace("\\u003C", "<");

                OkHttpUtils.post().url("http://crawler.jietiaozhan.com/crawler/parser")
                        .addParams("userId", userId)
                        .addParams("initUrl", Base64.encodeToString(initUrl.getBytes(Charset.forName("utf8")), Base64.DEFAULT))
                        .addParams("url", Base64.encodeToString(url.getBytes(Charset.forName("utf8")), Base64.DEFAULT))
                        .addParams("html", Base64.encodeToString(html.getBytes(Charset.forName("utf8")), Base64.DEFAULT))
                        .build().execute(new Callback() {
                    @Override
                    public Object parseNetworkResponse(Response response, int id) throws Exception {
                        String json = response.body().string();
                        if (StringUtils.isNotBlank(json)) {
                            JSONObject jsonObject = JSON.parseObject(json);
                            Boolean result = jsonObject.getBoolean("result");
                            String code = jsonObject.getString("code");
                            final String message = jsonObject.getString("message");
                            if (result == true) {
                                final CrawlerMetaModel model = jsonObject.getObject("object", CrawlerMetaModel.class);
                                if (model != null) {
                                    final String nextCrawlerUrl = model.getNextCrawlerUrl();
                                    crawlerMetaModel.setNextCrawlerUrl(nextCrawlerUrl);
                                    crawlerMetaModel.setPercent(model.getPercent());
                                    crawlerMetaModel.setMessage(message);
                                    wbvWebView.post(new Runnable() {
                                        @Override
                                        public void run() {
                                            wbvWebView.loadUrl(nextCrawlerUrl);
                                            updatePercent();
                                        }
                                    });
                                }
                            } else if (code.equals("200")) {
                                wbvWebView.post(new Runnable() {
                                    @Override
                                    public void run() {
                                        crawlerMetaModel.setPercent(BigDecimal.ONE);
                                        crawlerMetaModel.setMessage(message);
                                        triggerFinished();
                                    }
                                });
                            }
                        }
                        return null;
                    }

                    @Override
                    public void onError(final Call call, final Exception e, final int id) {
                        wbvWebView.post(new Runnable() {
                            @Override
                            public void run() {
                                triggerError(call, e, id);
                            }
                        });
                    }

                    @Override
                    public void onResponse(Object response, int id) {

                    }
                });
            }
        });
    }

    public void updatePercent() {
        BigDecimal p = crawlerMetaModel.getPercent();
        if (p == null) {
            p = BigDecimal.ZERO;
        }
        waveProgressBar.setValue(p.floatValue() * waveProgressBar.getMaxValue());
        System.out.println("设置mProgressTips:" + crawlerMetaModel.getMessage());
        tvProgressTips.setText(crawlerMetaModel.getMessage());
    }

    public void triggerFinished() {
        updatePercent();
    }

    public void triggerError(final Call call, final Exception e, final int id) {
        tvProgressTips.setText("网络加载错误:" + e.getLocalizedMessage());
    }

    private void back() {
        DialogUtils.getInstance(context).showTipsDialog("是否退出淘宝认证？", new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                DialogUtils.getInstance(context).dismiss();
                finish();
            }
        });
    }

    @Override
    public void onBackPressed() {
        back();
    }

    @OnClick(R.id.iv_back)
    public void onViewClicked() {
        back();
    }
}
