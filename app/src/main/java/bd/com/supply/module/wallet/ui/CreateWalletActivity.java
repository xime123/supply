package bd.com.supply.module.wallet.ui;

import android.content.Intent;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;

import bd.com.appcore.ui.activity.BaseUiActivity;
import bd.com.supply.MainActivity;
import bd.com.supply.R;
import bd.com.supply.module.wallet.presenter.CreateWalletPresenter;
import bd.com.supply.module.wallet.view.ICreateWalletView;
import bd.com.supply.MainActivity;
import bd.com.supply.module.wallet.view.ICreateWalletView;


public class CreateWalletActivity extends BaseUiActivity<CreateWalletPresenter,ICreateWalletView> implements ICreateWalletView{
    private EditText nameEt;
    private EditText passwordEt;
    private EditText passwordAgainnameEt;
    private TextView importTv;
    @Override
    protected CreateWalletPresenter initPresenter() {
        return new CreateWalletPresenter();
    }

    @Override
    protected ICreateWalletView initView() {
        nameEt=findViewById(R.id.wallet_name_tv);
        passwordEt=findViewById(R.id.password_edit_tv);
        passwordAgainnameEt=findViewById(R.id.password_edit_again_tv);
        importTv=findViewById(R.id.import_wallet_tv);
        importTv.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent=new Intent(CreateWalletActivity.this,ImportWalletActivity.class);
                startActivity(intent);
            }
        });
        return this;
    }

    @Override
    protected void initTooBar() {
        super.initTooBar();
        setTitle("创建钱包");
    }

    @Override
    protected int getLayoutId() {
         return R.layout.activity_create_wallet;
    }

    public void createWallet(View view) {
        String name=nameEt.getText().toString().trim();
        String pwd=passwordEt.getText().toString().trim();
        String pwdAgain=passwordAgainnameEt.getText().toString().trim();

        mPresenter.createWallet(name,pwd,pwdAgain);
    }

    @Override
    public void onCreateWalletSuccess() {
        Intent intent=new Intent(this, MainActivity.class);
        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK|Intent.FLAG_ACTIVITY_CLEAR_TASK);
        startActivity(intent);
    }

    @Override
    public void onCreateWalletFailed(String msg) {
        showTips(msg);

    }

    @Override
    public void showTips(String tips) {
        showToast(tips);
    }
}
