package bd.com.appcore.qrcode;

/**
 * Created by 徐敏 on 2018/1/3.
 */

public class QrCodeImportEvent {
    private int type;
    private String result;
    public QrCodeImportEvent(String result, int type) {
        this.result = result;
        this.type=type;
    }

    public String getResult() {
        return result;
    }

    public int getType() {
        return type;
    }
}
