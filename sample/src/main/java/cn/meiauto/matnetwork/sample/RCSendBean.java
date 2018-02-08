package cn.meiauto.matnetwork.sample;

/**
 * author : LiYang
 * email  : yang.li@nx-engine.com
 * time   : 2018/1/25
 */
public class RCSendBean {
    String cmdCode, deviceId, deviceType, pin, params;

    public RCSendBean(String cmdCode, String deviceId, String deviceType, String pin, String params) {
        this.cmdCode = cmdCode;
        this.deviceId = deviceId;
        this.deviceType = deviceType;
        this.pin = pin;
        this.params = params;
    }

    public RCSendBean(String cmdCode, String deviceId, String deviceType, String pin) {
        this.cmdCode = cmdCode;
        this.deviceId = deviceId;
        this.deviceType = deviceType;
        this.pin = pin;
    }
}
