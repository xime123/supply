package bd.com.supply.module.wallet.ui;


import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;

import com.flyco.tablayout.SlidingTabLayout;

import java.util.ArrayList;

import bd.com.appcore.IntentKey;
import bd.com.appcore.ui.activity.BaseUiActivity;
import bd.com.appcore.ui.fragment.BaseFragment;
import bd.com.supply.R;
import bd.com.supply.module.wallet.presenter.ExportKeyStorePresenter;
import bd.com.supply.module.wallet.ui.fragment.KeyStoreFileFragment;
import bd.com.supply.module.wallet.ui.fragment.KeyStoreQrcodeFragment;
import bd.com.supply.module.wallet.view.IExportKeyStoreView;
import bd.com.supply.module.wallet.ui.fragment.KeyStoreFileFragment;
import bd.com.supply.module.wallet.ui.fragment.KeyStoreQrcodeFragment;
import bd.com.supply.module.wallet.view.IExportKeyStoreView;
import bd.com.walletdb.entity.WalletEntity;

/**
 * 导出keystore
 */
public class ExportKeyStoreActivity extends BaseUiActivity<ExportKeyStorePresenter, IExportKeyStoreView> implements IExportKeyStoreView {
    private SlidingTabLayout tabLayout;
    private ViewPager mViewPager;
    private ArrayList<BaseFragment> mFagments = new ArrayList<>();
    private String[] mTitles = {"KeyStore文件", "二维码"};

    private MyPagerAdapter adapter;

    @Override
    protected ExportKeyStorePresenter initPresenter() {
        return new ExportKeyStorePresenter();
    }

    @Override
    protected IExportKeyStoreView initView() {
        WalletEntity entity = getIntent().getParcelableExtra(IntentKey.WALLET_ENTITY);
        tabLayout = findViewById(R.id.tablayout);
        mViewPager = findViewById(R.id.view_pager);
        mFagments.add(KeyStoreFileFragment.newInstance(entity));
        mFagments.add(KeyStoreQrcodeFragment.newInstance(entity));
        //getChildFragmentManager() 如果是嵌套在fragment中就要用这个
        adapter = new MyPagerAdapter(getSupportFragmentManager());
        mViewPager.setAdapter(adapter);
        mViewPager.setOffscreenPageLimit(0);
        tabLayout.setViewPager(mViewPager, mTitles);
        return this;
    }

    @Override
    protected void initTooBar() {
        super.initTooBar();
        setTitle("导出keyStore");
    }

    @Override
    protected int getLayoutId() {
        return R.layout.activity_import;
    }

    private class MyPagerAdapter extends FragmentPagerAdapter {
        public MyPagerAdapter(FragmentManager fm) {
            super(fm);
        }

        @Override
        public int getCount() {
            return mFagments.size();
        }

        @Override
        public CharSequence getPageTitle(int position) {
            return mTitles[position];
        }

        @Override
        public Fragment getItem(int position) {
            return mFagments.get(position);
        }
    }

}
