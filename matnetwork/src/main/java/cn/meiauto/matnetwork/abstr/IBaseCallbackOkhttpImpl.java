package cn.meiauto.matnetwork.abstr;

import cn.meiauto.matnetwork.Result;
import okhttp3.Call;

/**
 * author : LiYang
 * email  : yang.li@nx-engine.com
 * time   : 2018/2/1
 */
public abstract class IBaseCallbackOkhttpImpl<DATA> implements IBaseCallback<Call, Result<DATA>> {

    @Override
    public boolean onBefore(IBaseCall request) {
        return false;
    }

    @Override
    public void onAfter(Call call) {

    }

    @Override
    public void onSucceed(Call call, Result<DATA> dataResult) {

    }

    @Override
    public void onFailed(Call call, Exception e) {

    }
}
