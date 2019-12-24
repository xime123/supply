package bd.com.supply.module.wallet.ui.fragment;

import android.content.Context;
import android.content.Intent;
import android.text.TextUtils;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;

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


public class KeyStoreImportFragment extends BaseFragment {
    private EditText keyStoreEt;
    private EditText pwdEt;
    private TextView importTv;
    @Override
    protected int getLayoutId() {
        return R.layout.wallet_keystore_import_layout;
    }

    @Override
    protected void initView(View content) {
        super.initView(content);
        keyStoreEt=content.findViewById(R.id.keystore_edit);
        pwdEt=content.findViewById(R.id.keystore_pwd_et);
        importTv=content.findViewById(R.id.import_keystore_tv);
        importTv.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                importWalletByKeyStore(v);
            }
        });
    }

    public void importWalletByKeyStore(View view){
        String keystore=keyStoreEt.getText().toString().trim();
        String pwd=pwdEt.getText().toString().trim();
        if(TextUtils.isEmpty(keystore)){
            safetyToast("keystore不能为空");
            return;
        }
        if(TextUtils.isEmpty(pwd)){
            safetyToast("keystore密码不能为空");
            return;
        }

        if(!MyWalletUtil.validateKeyStore(keystore)){
            safetyToast("keystore不合法");
            return;
        }

        startImport( keystore, pwd);
    }

    private void startImport(final String keystore, final String pwd){
        RxTaskScheduler.postIoMainTask(new RxTaskCallBack<Boolean>() {
            @Override
            public void onStart(Disposable disposable) {
                super.onStart(disposable);
                safetyToast("开始导入...");
            }

            @Override
            public void onSuccess(Boolean aBoolean) {
                super.onSuccess(aBoolean);
                if(aBoolean){
                    safetyToast("导入成功");
                    Intent intent=new Intent(getActivity(), MainActivity.class);
                    intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK|Intent.FLAG_ACTIVITY_CLEAR_TASK);
                    startActivity(intent);
                }else {
                    safetyToast("导入失败");
                }
            }

            @Override
            public void onFailed(LogicException e) {
                super.onFailed(e);
                safetyToast("onFailed 导入失败");
            }

            @Override
            public Boolean doWork() throws Exception {
                return MyWalletUtil.importWalletByKeyStore(keystore,pwd);
            }
        });
    }
    /**收到对端钱包地址 开始转账
     * @param event
     */
    public void onEventMainThread(QrCodeImportEvent event) {
        if(event.getType()==0){
            String keystore=event.getResult();
            keyStoreEt.setText(keystore);
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
