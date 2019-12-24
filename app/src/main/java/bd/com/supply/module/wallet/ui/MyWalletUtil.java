package bd.com.supply.module.wallet.ui;

import android.text.TextUtils;
import android.util.Log;

import org.json.JSONObject;
import org.web3j.crypto.Credentials;
import org.web3j.crypto.ECKeyPair;
import org.web3j.crypto.Keys;
import org.web3j.crypto.Wallet;
import org.web3j.crypto.WalletFile;
import org.web3j.crypto.WalletUtils;
import org.web3j.utils.Numeric;

import java.io.File;
import java.math.BigInteger;

import bd.com.appcore.util.AppSettings;
import bd.com.appcore.util.FileUtil;
import bd.com.appcore.util.GsonUtil;
import bd.com.supply.module.common.AppFilePath;
import bd.com.walletdb.action.WalletAction;
import bd.com.walletdb.entity.WalletEntity;


public class MyWalletUtil {
    private static final String TAG=MyWalletUtil.class.getSimpleName();
    public static boolean createWallet(String password,String userName){
        ECKeyPair ecKeyPair = null;
        WalletFile walletFile = null;
        try {
            ecKeyPair = Keys.createEcKeyPair();
            walletFile = Wallet.create(password, ecKeyPair, 16, 1); // WalletUtils. .generateNewWalletFile();
            long id=inputDB(ecKeyPair,walletFile,userName,password);
            return true;
        } catch (Exception e) {
            e.printStackTrace();
            return false;

        }

    }

    private static long inputDB(ECKeyPair ecKeyPair,WalletFile walletFile,String name,String password) {
        WalletEntity walletEntity=new WalletEntity();
        String pubkey= Numeric.toHexStringWithPrefix(ecKeyPair.getPublicKey());
        String priKey=Numeric.toHexStringWithPrefix(ecKeyPair.getPrivateKey());
        String keyStore= GsonUtil.objectToJson(walletFile,WalletFile.class);
        String address=walletFile.getAddress();
        int num=(int)(Math.random()*31);
        String iconStr="ic_category_"+num;
        if(!TextUtils.isEmpty(address)&&!address.startsWith("0x")){
            address="0x"+address;
        }

        walletEntity.setIconStr(iconStr);
        walletEntity.setAddress(address);
        walletEntity.setPublicKey(pubkey);
        walletEntity.setPrivateKey(priKey);
        walletEntity.setKeystore(keyStore);
        walletEntity.setName(name);
        walletEntity.setPassword(password);
        WalletAction action=new WalletAction();
        long id=action.insertOrReplace(walletEntity);
        AppSettings.getAppSettings().setCurrentAddress(address);
        Log.i(TAG,"id="+id);
        return id;
    }


    /**
     * 根据公钥获取地址
     * @param pubKey
     * @return
     */
    public static String getAddressByPub(String pubKey){
        return null;
    }

    /**
     * 校验keystore的合法性
     * @param keysotre
     * @return
     */
    public static boolean validateKeyStore(String keysotre){
        if(TextUtils.isEmpty(keysotre)){
            return false;
        }else {
            try {
                JSONObject jsonObject=new JSONObject(keysotre);
                String address=jsonObject.optString("address");
                if(TextUtils.isEmpty(address)){
                    return false;
                }else {
                    return WalletUtils.isValidAddress(address);
                }

            }catch (Exception e){
                e.printStackTrace();
                return false;
            }

        }
    }

    /**
     * keystore导入钱包
     * @param keystore
     * @param pwd
     */
    public static boolean importWalletByKeyStore(String keystore,String pwd){
        try {
            //不支持gson直接转化,先写入文件
            File file=new File(AppFilePath.Wallet_DIR,"bd.json");
            FileUtil.writeFileSdcardFileByPath(keystore,file.getPath());
            Credentials credentials=WalletUtils.loadCredentials(pwd,file);
           // WalletFile walletFile=GsonUtil.jsonToObject(keystore,WalletFile.class);
            ECKeyPair ecKeyPair=credentials.getEcKeyPair();
            WalletFile walletFile = Wallet.create(pwd, ecKeyPair, 16, 1);
            inputDB(ecKeyPair,walletFile,"新导入钱包",pwd);
            return true;
        }catch (Exception e){
            e.printStackTrace();
            return false;
        }

    }

    /**
     * 私钥导入钱包
     * @param privateKey
     * @param password
     */
    public static boolean importWalletByPrivateKey(String privateKey,String password){
        ECKeyPair ecKeyPair=ECKeyPair.create(new BigInteger(privateKey,16));
        WalletFile walletFile = null;
        try {
            walletFile = Wallet.create(password, ecKeyPair, 16, 1); // WalletUtils. .generateNewWalletFile();
            long id=inputDB(ecKeyPair,walletFile,"超导导入新钱包",password);
            return true;
        } catch (Exception e) {
            e.printStackTrace();
            return false;

        }

    }
}
