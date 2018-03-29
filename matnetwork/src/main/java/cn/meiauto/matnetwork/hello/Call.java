package cn.meiauto.matnetwork.hello;

/**
 * author : LiYang
 * email  : yang.li@nx-engine.com
 * time   : 2018/3/21
 */
public interface Call {
    void execute();

    void enqueue(NetCallBack netCallBack);


}
