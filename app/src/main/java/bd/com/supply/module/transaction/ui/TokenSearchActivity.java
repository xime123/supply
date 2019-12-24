package bd.com.supply.module.transaction.ui;

import android.graphics.Bitmap;
import android.os.Handler;
import android.support.annotation.NonNull;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ImageView;

import java.util.List;
import java.util.Map;

import bd.com.appcore.ui.activity.BaseListActivity;
import bd.com.appcore.ui.adapter.CommonAdapter;
import bd.com.appcore.ui.adapter.base.ViewHolder;
import bd.com.appcore.ui.view.CircleImageView;
import bd.com.supply.R;
import bd.com.supply.module.transaction.presenter.TokenSearchPresenter;
import bd.com.supply.module.wallet.bus.TokenChangeEvent;
import bd.com.supply.util.ImageUtils;
import bd.com.supply.module.transaction.presenter.TokenSearchPresenter;
import bd.com.supply.module.wallet.bus.TokenChangeEvent;
import bd.com.supply.util.ImageUtils;
import bd.com.walletdb.entity.TokenEntity;
import bd.com.walletdb.manager.TokenManager;
import de.greenrobot.event.EventBus;

/**
 * author:     labixiaoxin
 * date:       2018/7/21
 * email:      labixiaoxin2@qq.cn
 */
public class TokenSearchActivity extends BaseListActivity<TokenSearchPresenter, TokenSearchPresenter.View, TokenEntity> implements TokenSearchPresenter.View {
    private ImageView backIv;
    private EditText searchEt;
    private List<TokenEntity> tokenEntityList;
    private Handler handler = new Handler();
    private String key = "";
    private Runnable delayRun = new Runnable() {

        @Override
        public void run() {
            if (!TextUtils.isEmpty(key)) {
                mPresenter.search(key, tokenEntityList);
            }
        }
    };

    @Override
    protected void initTooBar() {
        super.initTooBar();
        actionBar.setVisibility(View.GONE);
    }

    @Override
    protected void initContentView(ViewGroup contentView) {
        super.initContentView(contentView);
        View topView = View.inflate(this, R.layout.transaction_serach_layout, null);
        backIv = topView.findViewById(R.id.iv_back);
        backIv.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
        searchEt = topView.findViewById(R.id.search_et);
        searchEt.addTextChangedListener(new MyTextWatcher());
        mRecyclerView.setPullRefreshEnabled(false);
        mRecyclerView.setLoadingMoreEnabled(false);
        addTopFloatView(topView);
        loadEmpty();
    }


    @Override
    protected void initData() {
        super.initData();
        this.tokenEntityList = mPresenter.getTokenList();
    }

    @Override
    protected void fetchListItems(@NonNull Map<String, Object> params) {

    }

    @Override
    protected CommonAdapter<TokenEntity> createAdapter() {
        return new CommonAdapter<TokenEntity>(this, R.layout.wallet_item_token_layout, datas) {
            @Override
            protected void convert(ViewHolder holder, final TokenEntity tokenEntity, final int position) {
                if (!TextUtils.isEmpty(tokenEntity.getIcon())) {
                    CircleImageView tokenIc = holder.getView(R.id.token_icon);
                    Bitmap bitmap = ImageUtils.decodeBase64ToBitmap(tokenEntity.getIcon());
                    tokenIc.setImageBitmap(bitmap);
                }
                holder.setText(R.id.token_name, tokenEntity.getSymbol());
                holder.setText(R.id.token_symbol, tokenEntity.getPublisher());
                holder.setText(R.id.token_address, tokenEntity.getAddress());
                holder.setChecked(R.id.switch_token_check, tokenEntity.getChecked());
                holder.setOnClickListener(R.id.switch_token_check, new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        onTokenChecked(tokenEntity, position);
                    }
                });
            }
        };
    }

    @Override
    protected TokenSearchPresenter initPresenter() {
        return new TokenSearchPresenter();
    }

    @Override
    protected TokenSearchPresenter.View initView() {
        return this;
    }

    private class MyTextWatcher implements TextWatcher {

        @Override
        public void beforeTextChanged(CharSequence s, int start, int count, int after) {

        }

        @Override
        public void onTextChanged(CharSequence s, int start, int before, int count) {

        }

        @Override
        public void afterTextChanged(Editable s) {
            if (delayRun != null) {
                //每次editText有变化的时候，则移除上次发出的延迟线程
                handler.removeCallbacks(delayRun);
            }
            key = s.toString();
            //延迟800ms，如果不再输入字符，则执行该线程的run方法
            handler.postDelayed(delayRun, 800);
        }
    }

    private void onTokenChecked(TokenEntity tokenEntity, int position) {
        tokenEntity.setChecked(!tokenEntity.getChecked());
        mAdapter.notifyDataSetChanged();
        TokenManager.getManager().updateToken(tokenEntity);
        EventBus.getDefault().post(new TokenChangeEvent());
    }
}
