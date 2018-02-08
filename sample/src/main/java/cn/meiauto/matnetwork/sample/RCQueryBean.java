package cn.meiauto.matnetwork.sample;

import com.google.gson.annotations.SerializedName;

/**
 * author : LiYang
 * email  : yang.li@nx-engine.com
 * time   : 2018/1/30
 */
public class RCQueryBean {
    private int sendId;
    private String deviceId;
    private String deviceType;
    private String cmdCode;
    private long sendTime;
    private long responseTime;
    @SerializedName("status")
    private String statusX;

    public int getSendId() {
        return sendId;
    }

    public void setSendId(int sendId) {
        this.sendId = sendId;
    }

    public String getDeviceId() {
        return deviceId;
    }

    public void setDeviceId(String deviceId) {
        this.deviceId = deviceId;
    }

    public String getDeviceType() {
        return deviceType;
    }

    public void setDeviceType(String deviceType) {
        this.deviceType = deviceType;
    }

    public String getCmdCode() {
        return cmdCode;
    }

    public void setCmdCode(String cmdCode) {
        this.cmdCode = cmdCode;
    }

    public long getSendTime() {
        return sendTime;
    }

    public void setSendTime(long sendTime) {
        this.sendTime = sendTime;
    }

    public long getResponseTime() {
        return responseTime;
    }

    public void setResponseTime(long responseTime) {
        this.responseTime = responseTime;
    }

    public String getStatusX() {
        return statusX;
    }

    public void setStatusX(String statusX) {
        this.statusX = statusX;
    }

    @Override
    public String toString() {
        return "RCQueryBean{" +
                "sendId=" + sendId +
                ", deviceId='" + deviceId + '\'' +
                ", deviceType='" + deviceType + '\'' +
                ", cmdCode='" + cmdCode + '\'' +
                ", sendTime=" + sendTime +
                ", responseTime=" + responseTime +
                ", statusX='" + statusX + '\'' +
                '}';
    }
}
