package cn.meiauto.matnetwork.util;

import android.support.annotation.NonNull;
import android.text.TextUtils;

import com.google.gson.Gson;

import java.io.IOException;

import cn.meiauto.matnetwork.Result;
import cn.meiauto.matnetwork.ServerException;
import cn.meiauto.matutils.LogUtil;
import okhttp3.FormBody;
import okhttp3.Interceptor;
import okhttp3.MediaType;
import okhttp3.Request;
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
        Request request = chain.request();
        Response response = chain.proceed(request);

        long startTime = System.currentTimeMillis();
        long endTime = System.currentTimeMillis();
        long duration = endTime - startTime;
        MediaType mediaType = response.body().contentType();
        String content = response.body().string();
        LogUtil.verbose("\n");
        LogUtil.verbose("----------Start----------------");
        LogUtil.verbose("| " + request.toString());
        String method = request.method();
        if ("POST".equals(method)) {
            StringBuilder sb = new StringBuilder();
            if (request.body() instanceof FormBody) {
                FormBody body = (FormBody) request.body();
                for (int i = 0; i < body.size(); i++) {
                    sb.append(body.encodedName(i) + "=" + body.encodedValue(i) + ",");
                }
                sb.delete(sb.length() - 1, sb.length());
                LogUtil.verbose("| RequestParams:{" + sb.toString() + "}");
            }
        }
        LogUtil.verbose("| Response:" + content);
        LogUtil.verbose("----------End:" + duration + "毫秒----------");

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

        return response.newBuilder()
                .body(okhttp3.ResponseBody.create(mediaType, content))
                .build();
    }
}
