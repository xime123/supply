package bd.com.appcore.rx;

/**
 * author:     labixiaoxin
 * date:       2018/5/4
 * email:      labixiaoxin2@qq.cn
 * 业务逻辑异常封装
 */
public class LogicException extends Exception {
    private int code;
    private String errorMsg;

    public LogicException(Throwable throwable, int code){
        this.code=code;
        this.errorMsg=throwable.getMessage();
    }

    public int getCode() {
        return code;
    }

    public String getErrorMsg() {
        return errorMsg;
    }

}
