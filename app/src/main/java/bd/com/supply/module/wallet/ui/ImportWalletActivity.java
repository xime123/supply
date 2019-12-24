package bd.com.supply.module.wallet.ui;



import android.content.Intent;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.view.View;

import com.flyco.tablayout.SlidingTabLayout;

import java.util.ArrayList;

import bd.com.supply.module.transaction.ui.ScanQrcodeActivity;
import bd.com.appcore.ui.activity.BaseUiActivity;
import bd.com.appcore.ui.fragment.BaseFragment;
import bd.com.supply.R;
import bd.com.appcore.IntentKey;
import bd.com.supply.module.wallet.presenter.ImportWalletPresenter;
import bd.com.supply.module.wallet.ui.fragment.KeyStoreImportFragment;
import bd.com.supply.module.wallet.ui.fragment.PrivateKeyImportFragment;
import bd.com.supply.module.wallet.view.IImportWalletView;
import bd.com.supply.module.transaction.ui.ScanQrcodeActivity;
import bd.com.supply.module.wallet.ui.fragment.KeyStoreImportFragment;
import bd.com.supply.module.wallet.ui.fragment.PrivateKeyImportFragment;
import bd.com.supply.module.wallet.view.IImportWalletView;

public class ImportWalletActivity extends BaseUiActivity<ImportWalletPresenter,IImportWalletView>implements IImportWalletView {
    //https://blog.csdn.net/lufanzheng/article/details/60872068
    private SlidingTabLayout tabLayout;
    private ViewPager mViewPager;
    private ArrayList<BaseFragment> mFagments = new ArrayList<>();
    private String[] mTitles = {"官方钱包", "私钥"};

    private MyPagerAdapter adapter;
    @Override
    protected ImportWalletPresenter initPresenter() {
        return new ImportWalletPresenter();
    }

    @Override
    protected IImportWalletView initView() {
        tabLayout=findViewById(R.id.tablayout);
        mViewPager=findViewById(R.id.view_pager);
        mFagments.add(new KeyStoreImportFragment());
        mFagments.add(new PrivateKeyImportFragment());
        //getChildFragmentManager() 如果是嵌套在fragment中就要用这个
        adapter = new MyPagerAdapter(getSupportFragmentManager());
        mViewPager.setAdapter(adapter);
        tabLayout.setViewPager(mViewPager, mTitles);
        return this;
    }

    @Override
    protected void initTooBar() {
        super.initTooBar();
        setTitle("导入钱包");
        actionBar.setMenu2Resource(R.mipmap.camera);
        actionBar.setOnMenu2ClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                int type=tabLayout.getCurrentTab();
                Intent intent=new Intent(ImportWalletActivity.this, ScanQrcodeActivity.class);
                intent.putExtra(IntentKey.QRCODE_TYPE,type);
                startActivity(intent);
            }
        });
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
