package cn.meiauto.matnetwork.abstr;

/**
 * author : LiYang
 * email  : yang.li@nx-engine.com
 * time   : 2018/2/1
 */
public interface IBaseCall {
    IBaseResponse execute();

    void enqueue(IBaseCallback callback);
}