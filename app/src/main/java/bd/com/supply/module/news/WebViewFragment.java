package bd.com.supply.module.news;

import android.content.Intent;
import android.view.View;
import android.view.ViewGroup;

import android.widget.ProgressBar;

import com.tencent.smtt.export.external.interfaces.WebResourceError;
import com.tencent.smtt.export.external.interfaces.WebResourceRequest;
import com.tencent.smtt.sdk.WebChromeClient;
import com.tencent.smtt.sdk.WebSettings;
import com.tencent.smtt.sdk.WebView;
import com.tencent.smtt.sdk.WebViewClient;

import bd.com.appcore.IntentKey;
import bd.com.appcore.ui.fragment.BaseFragment;
import bd.com.appcore.ui.fragment.BaseUiFragment;
import bd.com.supply.R;
import bd.com.supply.module.news.presenter.WebViewPresenter;


public class WebViewFragment extends BaseUiFragment<WebViewPresenter, WebViewPresenter.View> implements WebViewPresenter.View {
    private WebView mWebView;
    private String url = "http://h5.meiti.in/";
    private ProgressBar progressBar;
    protected BackHandledInterface mBackHandledInterface;

    @Override
    protected WebViewPresenter initPresenter() {
        return new WebViewPresenter();
    }

    @Override
    protected WebViewPresenter.View initView() {
        return this;
    }

    @Override
    protected int getLayoutId() {
        return R.layout.webview_fragment_layout;
    }


    @Override
    protected void initContentView(ViewGroup content) {
        super.initContentView(content);
        actionBar.setVisibility(View.GONE);
        mWebView = content.findViewById(R.id.web_view);
        progressBar = (ProgressBar) content.findViewById(R.id.pb);
        progressBar.setVisibility(View.VISIBLE);
        progressBar.setMax(100);
        configWebView();
        mWebView.loadUrl(url);
    }

    private void configWebView() {
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
                } else {
                    progressBar.setVisibility(View.VISIBLE);
                }
            }
        });

        mWebView.setWebViewClient(new WebViewClient() {
            @Override
            public boolean shouldOverrideUrlLoading(WebView view, String url) {
                hideLoadingDialog();
                Intent intent = new Intent(getContext(), WebViewActivity.class);
                intent.putExtra(IntentKey.NEWS_URL, url);
                startActivity(intent);
                // view.loadUrl(url);
                return true;
            }

            @Override
            public void onReceivedError(WebView view, WebResourceRequest request, WebResourceError error) {
                super.onReceivedError(view, request, error);
                hideLoadingDialog();
                showErrorView();
            }
        });
    }

    public boolean onBackPressed() {
        return true;
    }

    public interface BackHandledInterface {
        void setSelectedFragment(WebViewFragment selectedFragment);
    }

    @Override
    protected void onErroViewClicked() {
        showLoadingDialog();
        mWebView.reload();
    }
}
