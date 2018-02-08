package cn.meiauto.matnetwork.abstr;

/**
 * author : LiYang
 * email  : yang.li@nx-engine.com
 * time   : 2018/2/1
 *
 * @param <CALL> 具体某次请求对象（可执行，或者取消某次请求）
 * @param <BEAN>    成功后的实体类
 */
public interface IBaseCallback<CALL, BEAN> {

    /**
     * @return 是否拦截某次请求
     */
    boolean onBefore(IBaseCall request);

    void onAfter(CALL call);

    void onSucceed(CALL call, BEAN bean);

    void onFailed(CALL call, Exception e);
}
