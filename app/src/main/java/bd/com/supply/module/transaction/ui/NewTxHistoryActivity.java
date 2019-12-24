package bd.com.supply.module.transaction.ui;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.RequiresApi;
import android.support.design.widget.AppBarLayout;
import android.support.design.widget.CollapsingToolbarLayout;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.jcodecraeer.xrecyclerview.XRecyclerView;

import org.web3j.utils.Convert;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.TimeUnit;

import bd.com.appcore.IntentKey;
import bd.com.appcore.rx.RxTaskCallBack;
import bd.com.appcore.rx.RxTaskScheduler;
import bd.com.appcore.ui.activity.BaseCoreActivity;
import bd.com.appcore.ui.adapter.CommonAdapter;
import bd.com.appcore.ui.adapter.base.ViewHolder;
import bd.com.appcore.ui.view.ItemTouchHelperCallBack;
import bd.com.appcore.ui.view.OnRecyclerItemClickListener;
import bd.com.appcore.util.AppSettings;
import bd.com.supply.R;
import bd.com.supply.main.TransferStartEvent;
import bd.com.supply.main.TransferSuccessEvent;
import bd.com.supply.module.common.GlobConfig;
import bd.com.supply.module.transaction.presenter.NewTransactionHisActPresenter;
import bd.com.supply.module.transaction.view.ITransactionHisActView;
import bd.com.supply.module.wallet.bus.WalletChangeEvent;
import bd.com.supply.util.DateKit;
import bd.com.supply.util.FakeDataHelper;
import bd.com.supply.util.PriceUtil;
import bd.com.supply.util.XlImageLoadUtil;
import bd.com.supply.widget.RatioImageView;
import bd.com.supply.module.transaction.presenter.NewTransactionHisActPresenter;
import bd.com.supply.widget.RatioImageView;
import bd.com.walletdb.action.WalletAction;
import bd.com.walletdb.entity.TokenEntity;
import bd.com.walletdb.entity.TxHistory;
import bd.com.walletdb.entity.WalletEntity;
import bd.com.walletdb.greendao.WalletEntityDao;
import bd.com.walletdb.manager.TxHistoryDBManager;
import de.greenrobot.event.EventBus;
import io.reactivex.Observable;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.functions.Consumer;

public class NewTxHistoryActivity extends BaseCoreActivity implements XRecyclerView.LoadingListener, ITransactionHisActView {
    private RatioImageView coinIv;
    private ImageView coinBgIv;
    private TextView walletNameTv;
    private TextView balanceTv;
    private TextView moneyTv;
    private AppBarLayout mAppBarLayout;
    private CollapsingToolbarLayout mCollapsingToolbarLayout;
    private XRecyclerView mXRecyclerView;
    private ImageView ivBackBtn;
    private TextView tvCenter;
    private CommonAdapter<TxHistory> mAdapter;
    private List<TxHistory> datas = new ArrayList<>();
    private String symbol;
    private TokenEntity entity;
    protected int pageNumber = 1;
    protected int pageSize = 50;
    private TextView translateTv, shouzhangTv;
    private NewTransactionHisActPresenter mPresenter;
    private View emptyView;
    private boolean isRefreshing = false;

    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EventBus.getDefault().register(this);

        setContentView(R.layout.activity_new_tx_history);

        initView();
        initData();
        immerse();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        EventBus.getDefault().unregister(this);
        mPresenter.onDetachView();
    }

    private void initView() {
        translateTv = findViewById(R.id.translate_tv);
        shouzhangTv = findViewById(R.id.shouzhang_tv);
        coinIv = findViewById(R.id.iv_audio_album);
        coinBgIv = findViewById(R.id.iv_audio_album_bg);
        walletNameTv = findViewById(R.id.tv_wallet_name);
        balanceTv = findViewById(R.id.tv_balance);
        mAppBarLayout = findViewById(R.id.appbarlayout);
        mCollapsingToolbarLayout = findViewById(R.id.collapsing_tool_bar);
        mXRecyclerView = findViewById(R.id.rcv_txhistory);
        tvCenter = findViewById(R.id.common_index_header_tv_title);
        ivBackBtn = findViewById(R.id.common_index_header_rl_back);
        moneyTv = findViewById(R.id.tv_money);
        emptyView = findViewById(R.id.empty_view);
        Bitmap bitmap = BitmapFactory.decodeResource(getResources(), R.mipmap.coin_bg);
        XlImageLoadUtil.blur(bitmap, coinBgIv);
        initRecyclerView();
        setToolBarChange();
        ivBackBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
        translateTv.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                gotoTranslate(v);
            }
        });

        shouzhangTv.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                gotoQrcode(v);
            }
        });
        tvCenter.setText("交易记录");

    }

    private void initData() {
        mPresenter = new NewTransactionHisActPresenter();
        mPresenter.onAttachView(this);
        entity = getIntent().getParcelableExtra(IntentKey.WALLET_ENTITY);
        if (entity == null) {
            finish();
        }
        mXRecyclerView.refresh();
        RxTaskScheduler.postIoMainTask(new RxTaskCallBack<WalletEntity>() {
            @Override
            public void onSuccess(WalletEntity walletEntity) {
                super.onSuccess(walletEntity);
                if (walletEntity != null) {
                    walletNameTv.setText(walletEntity.getName());
                } else {
                    Log.e("getCurrentWallet", "数据库没查到数据");
                }
            }

            @Override
            public WalletEntity doWork() {
                WalletAction action = new WalletAction();
                String currentAddress = AppSettings.getAppSettings().getCurrentAddress();
                List<WalletEntity> walletEntities = action.eq(WalletEntityDao.Properties.Address, currentAddress).queryAnd();
                if (walletEntities != null && walletEntities.size() > 0) {
                    return walletEntities.get(0);
                }
                return null;
            }
        });
    }


    public void gotoTranslate(View view) {
        Intent intent = new Intent(this, TxActivity.class);
        intent.putExtra(IntentKey.TRANSLATE_COIN_TYPE_SYMBLE, symbol);
        intent.putExtra(IntentKey.TOEKN_ADDR, entity.getAddress());
        startActivity(intent);
        // test();
    }

    public void gotoQrcode(View view) {
        Intent intent = new Intent(this, QrcodeActivity.class);
        intent.putExtra(IntentKey.TRANSLATE_COIN_TYPE_SYMBLE, GlobConfig.getMainTokenName());
        startActivity(intent);
    }

    private void setToolBarChange() {
        mAppBarLayout = findViewById(R.id.appbarlayout);
        mAppBarLayout.addOnOffsetChangedListener(new AppBarLayout.OnOffsetChangedListener() {
            @SuppressLint("ResourceAsColor")
            @Override
            public void onOffsetChanged(AppBarLayout appBarLayout, int verticalOffset) {
                if (verticalOffset == 0) {
                    //展开状态
                    tvCenter.setVisibility(View.VISIBLE);
                    ivBackBtn.setVisibility(View.VISIBLE);
                    tvCenter.setTextColor(getResources().getColor(R.color.white));
                    ivBackBtn.setImageResource(R.mipmap.icn_back_white);
                    mCollapsingToolbarLayout.setContentScrim(null);
                } else if (Math.abs(verticalOffset) >= appBarLayout.getTotalScrollRange()) {
                    //折叠状态
                    tvCenter.setVisibility(View.VISIBLE);
                    ivBackBtn.setVisibility(View.VISIBLE);
                    //  tvCenter.setTextColor(getResources().getColor(R.color.black));
                    // ivBackBtn.setImageResource(R.mipmap.icn_back_black);
                    mCollapsingToolbarLayout.setContentScrimResource(R.color.main_color_normal);
                } else {
                    //中间状态
                    tvCenter.setVisibility(View.INVISIBLE);
                    ivBackBtn.setImageResource(R.mipmap.icn_back_white);
                    //ivBackBtn.setVisibility(View.INVISIBLE);
                    mCollapsingToolbarLayout.setContentScrim(null);
                }
            }
        });
    }


    private void initRecyclerView() {
        mXRecyclerView = (XRecyclerView) findViewById(R.id.rcv_txhistory);
        mXRecyclerView.setPullRefreshEnabled(false);
        mXRecyclerView.setLoadingMoreEnabled(true);
        mXRecyclerView.setItemAnimator(null);
        //mXRecyclerView.setLoadingMoreProgressStyle(ProgressStyle.SquareSpin);
        mXRecyclerView.setLoadingListener(this);
        //mXRecyclerView.addItemDecoration(new RecycleViewDivider(getContext(),LinearLayoutManager.VERTICAL, 1,78, getResources().getColor(R.color.line_bg)));
        mAdapter = new CommonAdapter<TxHistory>(this, R.layout.tx_his_item_layout, datas) {
            @Override
            protected void convert(ViewHolder holder, TxHistory txHistory, int position) {
                doConvert(holder, txHistory, position);
            }
        };
        LinearLayoutManager layoutManager = new LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false);
        mXRecyclerView.setLayoutManager(layoutManager);
        mXRecyclerView.setAdapter(mAdapter);

        mXRecyclerView.addOnItemTouchListener(new OnRecyclerItemClickListener(mXRecyclerView) {

            @Override
            public void onItemClick(RecyclerView.ViewHolder vh) {
                int pos = vh.getAdapterPosition() - mXRecyclerView.getHeaders_includingRefreshCount();
                Log.e("BaseList", "pos=" + pos);
                if (pos >= 0 && pos < datas.size()) {
                    onItemViewClick(vh, datas.get(pos));
                }
            }

            @Override
            public void onItemLongClick(RecyclerView.ViewHolder vh) {
                Log.e(ItemTouchHelperCallBack.class.getSimpleName(), "onItemLongClick");
            }
        });
        mXRecyclerView.setLoadingMoreEnabled(true);
        mXRecyclerView.setPullRefreshEnabled(true);

    }

    private void doConvert(ViewHolder holder, TxHistory txHistory, int position) {
        int type = txHistory.getType();
        ProgressBar progressBar = holder.getView(R.id.block_progress);
        int lastBlockNum = txHistory.getLastBlockNumber();
        int blockNum = txHistory.getBlockNumber();
        int progress = 0;
        if (blockNum != -1) {
            progress = lastBlockNum - blockNum + 1;
        }
        Log.e("BasePresenterImpl", "createAdapter getBlockNumber= " + txHistory.getBlockNumber() + "   getLastBlockNumber=" + txHistory.getLastBlockNumber() + "   progress=" + progress + "   state=" + txHistory.getState());
        if (!entity.getAddress().equals(FakeDataHelper.getSctTokenAddr()) && !entity.getAddress().equals(FakeDataHelper.getEthTokenAddr())) {//非主币只要一个区块确认
            progressBar.setMax(1);
        } else {
            progressBar.setMax(12);
        }
        if (progress < 0) progress = 0;
        if (progress > 12) progress = 12;
        if (txHistory.getState() == 2) {
            holder.setVisible(R.id.pending_rl, true);
            holder.setText(R.id.wait_tv, "等待打包");
        } else if (txHistory.getState() == 1) {
            holder.setVisible(R.id.pending_rl, true);
            holder.setText(R.id.wait_tv, progress + "/" + progressBar.getMax());
        } else {
            holder.setVisible(R.id.pending_rl, false);
        }


        progressBar.setProgress(progress);
        String value = "";
        TextView valueTv = holder.getView(R.id.value_tv);
        String coinType = GlobConfig.getMainTokenType();

        if (TextUtils.isEmpty(txHistory.getTokenTransferTo())) {//非代币
            if (txHistory.getBlockNumber() == -1 || txHistory.getBlockNumber() == 1) {
                value = txHistory.getValue() + " " + coinType;
            } else {
                String valueStr = txHistory.getValue();
                BigDecimal valueBD = Convert.fromWei(valueStr, Convert.Unit.ETHER);
                value = valueBD.toString() + " " + coinType;
            }
        } else {//代币
            String valueStr = txHistory.getTokenTransfer();
            if ("0".equals(valueStr)) {
                value = valueStr + " " + symbol.toLowerCase();
            } else {
                if (txHistory.getBlockNumber() == -1 || txHistory.getBlockNumber() == 1) {//1是人为的
                    value = valueStr + " " + symbol.toLowerCase();
                } else {
                    BigDecimal valueBD = Convert.fromWei(valueStr, Convert.Unit.ETHER);
                    value = valueBD.toString() + " " + symbol.toLowerCase();
                }

            }
        }
        holder.setText(R.id.address_tv, txHistory.getPkHash());
        if (type == 1) {//交易类型 1 支出 2 入账
            valueTv.setTextColor(getResources().getColor(R.color.red_click));
            valueTv.setText("- " + value);
        } else {
            holder.setTextColor(R.id.value_tv, getResources().getColor(R.color.cyan_click));
            holder.setText(R.id.value_tv, "+ " + value);
        }
        holder.setText(R.id.time_tv, DateKit.friendlyFormat(txHistory.getBlockTimesStr()));

    }

    protected void onItemViewClick(RecyclerView.ViewHolder vh, TxHistory entity) {
        if (entity.getBlockNumber() == 0) {
            showToast("pengding状态不可查询");
            return;
        }
        Intent intent = new Intent(this, TransactionDetailActivity.class);
        intent.putExtra(IntentKey.TOKEN_NAME, symbol);
        intent.putExtra(IntentKey.TRANSACTION_HIS, entity);
        startActivity(intent);
    }


    public void onEventMainThread(WalletChangeEvent event) {
        mXRecyclerView.refresh();
    }

    public void onEventMainThread(TransferStartEvent event) {
//        datas.add(0,event.getHistory());
//        mAdapter.notifyItemInserted(0);
        //去查状态
        //  mPresenter.startGetPendingState(event.getHistory());
        mXRecyclerView.refresh();
    }

    @Override
    public void onRefresh() {
        isRefreshing = true;
        Observable.timer(500, TimeUnit.MILLISECONDS).observeOn(AndroidSchedulers.mainThread()).subscribe(new Consumer<Long>() {
            @Override
            public void accept(Long aLong) throws Exception {
                pageNumber = 1;
                fechData();
            }
        });

    }

    private void fechData() {

        Map<String, Object> params = new HashMap<>();
        params.put("pageNumber", pageNumber);
        params.put("pageSize", pageSize);
        symbol = entity.getSymbol();
        mPresenter.getNonceAndGetTxList(params, entity.getAddress());
        mPresenter.getBalance(entity.getAddress());
    }

    @Override
    public void onLoadMore() {
        fechData();
    }

    @Override
    public void onGetReceiptSuccess() {
        if (!isRefreshing) {
           // showToast("交易发送成功");
            mXRecyclerView.refresh();
            EventBus.getDefault().post(new TransferSuccessEvent());
        }
    }

    @Override
    public void onGetReceiptFailed(TxHistory txHistory) {
        mXRecyclerView.refresh();
    }

    @Override
    public void onGetBalanceSuccess(String balance) {
        balanceTv.setText(balance + " " + symbol);
        BigDecimal decimalMoney = PriceUtil.getMoney(entity.getAddress(), balance);
        decimalMoney = decimalMoney.setScale(2, BigDecimal.ROUND_HALF_UP);
        moneyTv.setText("¥ " + decimalMoney.toPlainString());
    }

    @Override
    public void onGetBalanceFailed(String msg) {
        balanceTv.setText(entity.getBalance());
        moneyTv.setText("¥ " + entity.getValue());
    }

    @Override
    public void onBlockNumSuccess(int num) {
        List<TxHistory> stateChangedList = new ArrayList<>();
        for (TxHistory history : datas) {
            if (history.getState() == 1) {
                history.setLastBlockNumber(num);
                int progress = history.getLastBlockNumber() - history.getBlockNumber() + 1;
                Log.e("onBlockNumSuccess", "address=" + history.getAddress() + "    progress=" + progress + "blockNum=" + history.getBlockNumber() + "  lastNum=" + num);
                if (GlobConfig.isMianTokenTransaction(history.getAddress()) && progress >= 12 && history.getBlockNumber() != -1) {//主币已经确认完成
                    history.setState(0);
                    stateChangedList.add(history);

                } else if (!GlobConfig.isMianTokenTransaction(history.getAddress()) && progress >= 1 && history.getBlockNumber() != -1) {//代币已经确认完成
                    history.setState(0);
                    stateChangedList.add(history);
                }
            }

        }
        mAdapter.notifyDataSetChanged();

        if (stateChangedList.size() > 0) {
            TxHistoryDBManager.getManager().insertTxHistoryListAsync(stateChangedList);
        }
    }

    @Override
    public void loadFailed(String msg) {
        mXRecyclerView.refreshComplete();
    }

    @Override
    public void loadSuccess(List<TxHistory> items) {
        isRefreshing = false;
        mXRecyclerView.refreshComplete();
        if (items == null || items.size() == 0) {
            datas.clear();
            mAdapter.notifyDataSetChanged();
            showEmptyView();
            return;
        }

        datas.clear();
        datas.addAll(items);
        pageNumber++;
        mAdapter.notifyDataSetChanged();
    }

    @Override
    public void loadMoreSuccess(List<TxHistory> moreDatas) {
        isRefreshing = false;
        mXRecyclerView.loadMoreComplete();
        mXRecyclerView.refreshComplete();
        if (moreDatas == null || moreDatas.size() == 0) {
            mXRecyclerView.setNoMore(true);
            return;
        }
        datas.addAll(moreDatas);
        mAdapter.notifyDataSetChanged();
        if (moreDatas.size() < pageSize) {
            mXRecyclerView.setNoMore(true);
        }
        pageNumber++;
    }

    @Override
    public void loadEmpty() {
        isRefreshing = false;
        //showEmptyView();
    }

    @Override
    public void loadMoreFailed() {
        isRefreshing = false;
        mXRecyclerView.loadMoreComplete();
        showToast("加载失败");
        // showEmptyView();
    }

    @Override
    public void showLoadingDialog() {

    }

    @Override
    public void hideLoadingDialog() {

    }

    @Override
    public void exit() {

    }

    private void showToast(String msg) {
        Toast.makeText(this, msg, Toast.LENGTH_LONG).show();
    }

    private void showEmptyView() {
        emptyView.setVisibility(View.VISIBLE);
    }

    private void showErrorView() {

    }
}
