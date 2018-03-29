package cn.meiauto.matnetwork.hello;

/**
 * author : LiYang
 * email  : yang.li@nx-engine.com
 * time   : 2018/3/21
 */
public class NetCall<CALL extends Call> {

    private CALL mCALL;

    public NetCall(CALL CALL) {
        mCALL = CALL;
    }

    public void execute() {
        mCALL.execute();
    }

    public void enqueue(NetCallBack netCallBack) {
        mCALL.enqueue(netCallBack);
    }
}
