package cn.meiauto.matrxretrofit.base.observer;

import android.content.Context;
import android.text.TextUtils;
import android.widget.Toast;

import cn.meiauto.matrxretrofit.util.ExceptionHandler;
import cn.meiauto.matrxretrofit.util.ServerException;
import cn.meiauto.matutils.LogUtil;

/**
 * author : LiYang
 * email  : yang.li@nx-engine.com
 * time   : 2018/5/4
 */
public abstract class BaseErrorObserver<T> extends BaseObserver<T> {
    private Context mContext;

    public BaseErrorObserver(Context context) {
        mContext = context;
    }

    @Override
    public void onError(Throwable e) {
        ServerException exception = ExceptionHandler.handle(e);
        LogUtil.error(exception);
        String errorCode = exception.getErrorCode();
        String errorMessage = exception.getErrorMessage();

        if (!TextUtils.isEmpty(errorMessage)) {
            if (!TextUtils.isEmpty(errorCode)) {
                if (errorCode.contains("SYS")) {
                    errorMessage = "服务器错误，" + errorMessage + "(" + exception.getExtMessage() + ")";
                }
            }
            Toast.makeText(mContext, errorMessage, Toast.LENGTH_SHORT).show();
        }
    }
}
