package cn.meiauto.matnetwork.hello;

/**
 * author : LiYang
 * email  : yang.li@nx-engine.com
 * time   : 2018/3/21
 *
 * @param <RC>     requestCall
 * @param <RESULT> response data
 */
public interface NetCallBack<RC, RESULT> {
    void onBefore(RC requestCall);

    void onAfter(RC requestCall);

    void onSucceed(RC requestCall, RESULT data);

    void onFailed(RC requestCall, Throwable throwable);
}
