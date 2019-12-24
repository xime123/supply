package bd.com.supply.module.common;

import android.app.Activity;
import android.text.TextUtils;
import android.view.View;
import android.widget.AdapterView;

import java.util.List;

import bd.com.supply.module.transaction.SoSoConfig;
import bd.com.supply.module.transaction.model.EnvModel;
import bd.com.supply.module.transaction.model.domian.SosoEnv;
import bd.com.supply.module.wallet.ApiConfig;
import bd.com.supply.widget.SelectSosoDialog;
import bd.com.supply.module.transaction.SoSoConfig;
import bd.com.supply.module.transaction.model.EnvModel;
import bd.com.supply.module.transaction.model.domian.SosoEnv;
import bd.com.supply.module.wallet.ApiConfig;
import bd.com.supply.widget.SelectSosoDialog;

/**
 * date:       2019/7/14
 */
public abstract class SosoHookClickListener implements View.OnClickListener {
    private SelectSosoDialog dialog;
    private Activity activity;

    public SosoHookClickListener(Activity activity) {
        this.activity = activity;
    }

    List<SosoEnv> sosoEnvList = EnvModel.getInstance().getSosoEnvs();

    @Override
    public void onClick(View v) {
        configSosoEnv();
    }

    private void configSosoEnv() {
        if (sosoEnvList.size() == 0/* || !SoSoConfig.NEED_SWITCH_ENV*/) {
            onHookClick();
            return;
        }
        dialog = new SelectSosoDialog(activity);
        String currentChainName = "当前溯源环境为：";
        for (SosoEnv entity : sosoEnvList) {
            if (ApiConfig.getSosoBaseUrl().equals(entity.getPrefix())) {
                currentChainName = currentChainName + entity.getAlias();
                break;
            }
        }
        dialog.setData(sosoEnvList, "请选择溯源系统环境", currentChainName);
        dialog.setOnItemClickListener(new OnSelectOnlineItemClick());
        dialog.show();
    }

    /**
     * 链选择
     */
    class OnSelectOnlineItemClick implements AdapterView.OnItemClickListener {

        @Override
        public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
            SosoEnv env = sosoEnvList.get(position);
            String prefix = env.getPrefix();
            if (!TextUtils.isEmpty(prefix)) {
                SoSoConfig.NEED_SWITCH_ENV = false;
                ApiConfig.setSosoBaseUrl(prefix);
                ApiConfig.setSosoSource(env.getResPrefix());
            }
            if (dialog != null) {
                dialog.dismiss();
                dialog = null;
            }
            onHookClick();
        }
    }

    public abstract void onHookClick();
}
