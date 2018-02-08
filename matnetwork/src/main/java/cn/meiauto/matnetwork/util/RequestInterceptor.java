package cn.meiauto.matnetwork.util;

import android.support.annotation.NonNull;
import android.text.TextUtils;

import com.google.gson.Gson;

import java.io.IOException;

import cn.meiauto.matnetwork.Result;
import cn.meiauto.matnetwork.ServerException;
import cn.meiauto.matutils.LogUtil;
import okhttp3.Interceptor;
import okhttp3.Response;
import okhttp3.ResponseBody;

/**
 * author : LiYang
 * email  : yang.li@nx-engine.com
 * time   : 2018/2/8
 */
public class RequestInterceptor implements Interceptor {
    @Override
    public Response intercept(@NonNull Chain chain) throws IOException {
        Response response = chain.proceed(chain.request());
        int httpCode = response.code();
        if (httpCode != 200) {
            ResponseBody body = response.body();
            if (body == null) {
                throw new NullPointerException("response body is null");
            } else {
                String json = body.string();
                if (!TextUtils.isEmpty(json)) {
                    LogUtil.warn(json);
                    Result result = new Gson().fromJson(json, Result.class);
                    throw new ServerException()
                            .setHttpCode(httpCode)
                            .setErrorCode(result.getErrorCode())
                            .setErrorMessage(result.getErrorMessage() + " ( " + result.getExtMessage() + " ).");
                }
            }
        }
        return response;
    }
}
