package bd.com.supply.module.wallet.ui.fragment;

import android.content.Context;
import android.content.Intent;
import android.text.TextUtils;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;

import org.web3j.crypto.WalletUtils;

import bd.com.appcore.qrcode.QrCodeImportEvent;
import bd.com.appcore.rx.LogicException;
import bd.com.appcore.rx.RxTaskCallBack;
import bd.com.appcore.rx.RxTaskScheduler;
import bd.com.appcore.ui.fragment.BaseFragment;
import bd.com.supply.MainActivity;
import bd.com.supply.R;
import bd.com.supply.module.wallet.ui.MyWalletUtil;
import bd.com.supply.MainActivity;
import de.greenrobot.event.EventBus;
import io.reactivex.disposables.Disposable;


public class PrivateKeyImportFragment extends BaseFragment {
    private EditText privateKeyEt;
    private EditText pwdEt;
    private EditText pwdAgainEt;
    private TextView importTv;

    @Override
    protected int getLayoutId() {
        return R.layout.wallet_private_import_layout;
    }

    @Override
    protected void initView(View content) {
        super.initView(content);
        privateKeyEt = content.findViewById(R.id.private_key_edit);
        pwdEt = content.findViewById(R.id.pwd_pri_et);
        pwdAgainEt = content.findViewById(R.id.pwd_pri_again_et);
        importTv = content.findViewById(R.id.import_privatekey_tv);
        importTv.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                importWalletByPriKey(v);
            }
        });
    }

    public void importWalletByPriKey(View view) {
        String privateKey = privateKeyEt.getText().toString().trim();
        String pwdStr = pwdEt.getText().toString().trim();
        String pwdAgainStr = pwdAgainEt.getText().toString().trim();
        if (TextUtils.isEmpty(privateKey)) {
            safetyToast("私钥不能为空");
            return;
        }

        if (privateKey.startsWith("0x")) {
            privateKey = privateKey.replace("0x", "");
        }

        if (TextUtils.isEmpty(pwdStr) || TextUtils.isEmpty(pwdAgainStr)) {
            safetyToast("密码不能为空");
            return;
        }

        if(pwdStr.length()<6){
            safetyToast("密码长度至少6位字符");
            return;
        }
        if (!TextUtils.equals(pwdStr, pwdAgainStr)) {
            safetyToast("两次密码不一致");
            return;
        }



        if (!WalletUtils.isValidPrivateKey(privateKey)) {
            safetyToast("私钥不合法");
        }

        startImport(privateKey, pwdStr);
    }

    private void startImport(final String privateKey, final String pwdStr) {
        RxTaskScheduler.postIoMainTask(new RxTaskCallBack<Boolean>() {
            @Override
            public void onFailed(LogicException e) {
                super.onFailed(e);
                safetyToast("导入失败");
            }

            @Override
            public void onStart(Disposable disposable) {
                super.onStart(disposable);
                safetyToast("开始导入");
            }

            @Override
            public void onSuccess(Boolean aBoolean) {
                super.onSuccess(aBoolean);
                if (aBoolean) {
                    safetyToast("导入成功");
                    Intent intent = new Intent(getActivity(), MainActivity.class);
                    intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                    startActivity(intent);
                }
            }

            @Override
            public Boolean doWork() throws Exception {
                return MyWalletUtil.importWalletByPrivateKey(privateKey, pwdStr);
            }
        });
    }

    /**
     * 收到对端钱包地址 开始转账
     *
     * @param event
     */
    public void onEventMainThread(QrCodeImportEvent event) {
        if (event.getType() == 1) {
            String keystore = event.getResult();
            privateKeyEt.setText(keystore);
        }
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        EventBus.getDefault().register(this);
    }

    @Override
    public void onDetach() {
        super.onDetach();
        EventBus.getDefault().unregister(this);
    }
}
