package bd.com.supply.module.push.bean;

public class PushMessage {
    private String msgType;//消息类型，1001:转账成功，1002:收款通知，
    private String msgId; //消息ID，如果当前消息为非完成消息，则需要根据msgId从服务侧获取完整消息
    private String msgValid;//消息完整性 Y 完整消息，不需要再次获取，N 非完整消息，需要根据msgId从服务侧再次获取
    private String msgContent;//消息内容正文，msgValid=N时，为空串

    public String getMsgType() {
        return msgType;
    }

    public void setMsgType(String msgType) {
        this.msgType = msgType;
    }

    public String getMsgId() {
        return msgId;
    }

    public void setMsgId(String msgId) {
        this.msgId = msgId;
    }

    public String getMsgValid() {
        return msgValid;
    }

    public void setMsgValid(String msgValid) {
        this.msgValid = msgValid;
    }

    public String getMsgContent() {
        return msgContent;
    }

    public void setMsgContent(String msgContent) {
        this.msgContent = msgContent;
    }
}
