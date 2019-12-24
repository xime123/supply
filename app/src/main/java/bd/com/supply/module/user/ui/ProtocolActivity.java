package bd.com.supply.module.user.ui;

import android.view.ViewGroup;
import android.webkit.WebView;

import java.io.BufferedReader;
import java.io.InputStreamReader;

import bd.com.appcore.mvp.IBasePresenter;
import bd.com.appcore.mvp.IBaseView;
import bd.com.appcore.ui.activity.BaseUiActivity;
import bd.com.supply.R;

/**
 * author:     xumin
 * date:       2018/10/27
 * email:      xumin2@evergrande.cn
 */
public class ProtocolActivity extends BaseUiActivity {
    private WebView webView;
    @Override
    protected IBasePresenter initPresenter() {
        return null;
    }

    @Override
    protected IBaseView initView() {
        return null;
    }

    @Override
    protected int getLayoutId() {
        return R.layout.activity_protocol;
    }

    @Override
    protected void initTooBar() {
        super.initTooBar();
        actionBar.setTitle("服务条款");
    }

    @Override
    protected void initContentView(ViewGroup containerView) {
        super.initContentView(containerView);
        webView=findViewById(R.id.web_view);
        //webView.loadUrl("file:////android_assets/protocol.html");
        String tpl = getFromAssets("protocol.html");
        webView.loadDataWithBaseURL(null, tpl, "text/html", "utf-8", null);

    }

    /*
     * 获取html文件
     */
    public String getFromAssets(String fileName) {
        try {
            InputStreamReader inputReader = new InputStreamReader(getResources().getAssets().open(fileName));
            BufferedReader bufReader = new BufferedReader(inputReader);
            String line = "";
            String Result = "";
            while ((line = bufReader.readLine()) != null) Result += line;
            return Result;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return "";

    }
}
