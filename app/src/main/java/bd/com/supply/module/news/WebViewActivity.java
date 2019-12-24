package bd.com.supply.module.news;

import android.content.Intent;
import android.net.Uri;
import android.support.annotation.Keep;
import android.text.TextUtils;
import android.view.View;
import android.view.ViewGroup;
import android.webkit.ValueCallback;
import android.webkit.WebChromeClient;
import android.webkit.WebResourceError;
import android.webkit.WebResourceRequest;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.ProgressBar;

import bd.com.appcore.IntentKey;
import bd.com.appcore.ui.activity.BaseUiActivity;
import bd.com.supply.R;
import bd.com.supply.module.news.presenter.WebViewFragmentPresneter;

/**
 * author:     xumin
 * date:       2018/8/24
 * email:      xumin2@evergrande.cn
 */
public class WebViewActivity extends BaseUiActivity<WebViewFragmentPresneter, WebViewFragmentPresneter.View> implements WebViewFragmentPresneter.View {
    private WebView mWebView;
    private String url;
    private ProgressBar progressBar;

    @Override
    protected WebViewFragmentPresneter initPresenter() {
        return new WebViewFragmentPresneter();
    }

    @Override
    protected WebViewFragmentPresneter.View initView() {
        return this;
    }

    @Override
    protected int getLayoutId() {
        return R.layout.activity_webview_layout;
    }

    @Override
    protected void initContentView(ViewGroup containerView) {
        super.initContentView(containerView);
        mWebView = findViewById(R.id.wb);
        progressBar = (ProgressBar) findViewById(R.id.pb);
        progressBar.setVisibility(View.VISIBLE);
        progressBar.setMax(100);
        initWebView();
    }

    @Override
    protected void initData() {
        super.initData();
        url = getIntent().getStringExtra(IntentKey.NEWS_URL);
        if (TextUtils.isEmpty(url)) {
            finish();
            return;
        }
        mWebView.loadUrl(url);
        actionBar.setTitle("溯源");
    }

    private void initWebView() {
        WebSettings settings = mWebView.getSettings();

        settings.setJavaScriptEnabled(true);
        settings.setMinimumFontSize(settings.getMinimumFontSize() + 8);
        settings.setDisplayZoomControls(false);
        settings.setUseWideViewPort(true);
        settings.setLoadWithOverviewMode(true);

        mWebView.setWebChromeClient(new WebChromeClient() {
            @Override
            public void onProgressChanged(WebView view, int newProgress) {
                super.onProgressChanged(view, newProgress);
                progressBar.setProgress(newProgress);
                if (newProgress == 100) {
                    progressBar.setVisibility(View.GONE);
                    String title = mWebView.getTitle();
                    if (!TextUtils.isEmpty(title)) {
                        actionBar.setTitle(title);
                    }
                } else {
                    progressBar.setVisibility(View.VISIBLE);
                }
            }
        });

        mWebView.setWebViewClient(new WebViewClient() {

            @Override
            public boolean shouldOverrideUrlLoading(WebView view, WebResourceRequest request) {
                // view.loadUrl(url);
                return true;
            }

            @Override
            public void onReceivedError(WebView view, WebResourceRequest request, WebResourceError error) {
                super.onReceivedError(view, request, error);
            }

            @Override
            public void onReceivedError(WebView view, int errorCode, String description, String failingUrl) {
                super.onReceivedError(view, errorCode, description, failingUrl);
            }
        });
    }
}
