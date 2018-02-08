package cn.meiauto.matnetwork.sample;

/**
 * author : LiYang
 * email  : yang.li@nx-engine.com
 * time   : 2018/1/30
 */
public class SendCommandData {

    private int sendId;

    public int getSendId() {
        return sendId;
    }

    public void setSendId(int sendId) {
        this.sendId = sendId;
    }

    @Override
    public String toString() {
        return "SendCommandData{" +
                "sendId=" + sendId +
                '}';
    }
}
