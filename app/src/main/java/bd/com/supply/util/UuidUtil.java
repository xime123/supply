package bd.com.supply.util;

import org.spongycastle.jcajce.provider.digest.SHA3;
import org.web3j.crypto.ECKeyPair;
import org.web3j.crypto.Keys;
import org.web3j.crypto.Wallet;
import org.web3j.crypto.WalletFile;
import org.web3j.utils.Numeric;

import java.security.KeyPair;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

public class UuidUtil {
    public static String getRandomUuid() {
        ECKeyPair ecKeyPair = null;
        WalletFile walletFile = null;
        try {
            ecKeyPair = Keys.createEcKeyPair();
            String priKey = Numeric.toHexStringWithPrefix(ecKeyPair.getPrivateKey());
            String hexKey = sha1(priKey);
            return hexKey;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return "";
    }

    public static String sha1(String data) throws NoSuchAlgorithmException {
        MessageDigest md = MessageDigest.getInstance("SHA1");
        md.update(data.getBytes());
        StringBuffer buf = new StringBuffer();
        byte[] bits = md.digest();
        for (int i = 0; i < bits.length; i++) {
            int a = bits[i];
            if (a < 0) a += 256;
            if (a < 16) buf.append("0");
            buf.append(Integer.toHexString(a));
        }
        return buf.toString();
    }
}
