package bd.com.supply.module.push.manager;

import android.content.Context;
import android.util.Log;

import java.util.List;

import bd.com.supply.module.push.model.PushModel;
import bd.com.supply.module.push.model.PushModel;
import bd.com.walletdb.entity.WalletEntity;
import bd.com.walletdb.manager.WalletDBManager;
import cn.jpush.android.api.JPushInterface;

public class PushManager {

    public static void init(Context context) {
        List<WalletEntity> walletEntities = WalletDBManager.getManager().getAllWalletList();
        StringBuilder addressSB = new StringBuilder();
        if (walletEntities != null && walletEntities.size() > 0) {
            for (int i = 0; i < walletEntities.size(); i++) {
                addressSB.append(walletEntities.get(i).getAddress());
                if (i != walletEntities.size() - 1) {
                    addressSB.append(",");
                }
            }
        }

        String address = addressSB.toString();
        Log.i("PushManager", "address==================>" + address);
        String registerID = JPushInterface.getRegistrationID(context);
        PushModel.getPushModel().registerAddress(address, registerID);
    }
}
