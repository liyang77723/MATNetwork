package cn.meiauto.matrxretrofit.util;

import android.support.annotation.NonNull;
import android.text.TextUtils;

import com.google.gson.Gson;

import java.io.IOException;

import cn.meiauto.matrxretrofit.base.result.BaseResult;
import cn.meiauto.matutils.LogUtil;
import okhttp3.Headers;
import okhttp3.Interceptor;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;
import okhttp3.ResponseBody;
import okio.Buffer;

/**
 * author : LiYang
 * email  : yang.li@nx-engine.com
 * time   : 2018/2/8
 */
public class RequestInterceptor implements Interceptor {
    public Response intercept(@NonNull Chain chain) throws IOException {
        Request request = chain.request();
        Response response = chain.proceed(request);

        ResponseBody body = response.body();

        if (body == null) {
            throw new ServerException().errorMessage("服务器错误");
        }

        String json = body.string();

        if (TextUtils.isEmpty(json)) {
            throw new ServerException().errorMessage("服务器错误");
        }

        log(request, json);

        BaseResult result;
        try {
            result = new Gson().fromJson(json, BaseResult.class);
        } catch (Exception e) {
            LogUtil.error(e);
            throw new ServerException().errorMessage("服务器错误");
        }

        if (TextUtils.equals(result.status, "FAILED")) {
            String code = result.errorCode;
            throw new ServerException().errorCode(code)
                    .errorMessage(result.errorMessage)
                    .extMessage(result.extMessage);
        }

        return response.newBuilder()
                .body(ResponseBody.create(body.contentType(), json))
                .build();
    }

    private void log(Request request, String json) {
        StringBuilder stringBuilder =
                new StringBuilder("----------------start----------------\n");

        String method = request.method();

        stringBuilder.append("method: ").append(method).append("\n");
//        if (TextUtils.equals("POST", method)) {
//            if (request.body() instanceof FormBody) {
//                FormBody body = (FormBody) request.body();
//                if (body != null) {
//                    for (int i = 0; i < body.size(); i++) {
//                        stringBuilder.append("    ")
//                                .append(body.encodedName(i))
//                                .append("=")
//                                .append(body.encodedValue(i))
//                                .append("\n");
//                    }
//                }
//            }
//        }

        stringBuilder.append(bodyToString(request));
        stringBuilder.append("url: ").append(request.url()).append("\n");

        Headers headers = request.headers();
        stringBuilder.append("headers:");
        StringBuilder headerStr = new StringBuilder();
        for (int i = 0, size = headers.size(); i < size; i++) {
            headerStr.append("\n    ").append(headers.name(i))
                    .append(": ").append(headers.value(i));
        }
        stringBuilder.append(headerStr).append("\n");

        stringBuilder
                .append("response: ").append(json).append("\n")
                .append("-----------------end-----------------");

        LogUtil.verbose(stringBuilder);
    }

    protected String bodyToString(final Request request) {
        try {
            final Request copy = request.newBuilder().build();
            RequestBody body = copy.body();
            if (body == null) {
                return "";
            }
            final Buffer buffer = new Buffer();
            body.writeTo(buffer);
            String bodyStr = buffer.readUtf8();
            return TextUtils.isEmpty(bodyStr) ? "" : bodyStr + "\n";
        } catch (final IOException e) {
            return "";
        }
    }

    protected boolean isPostString(final Request request) {
        return request.newBuilder().build().body() == null;
    }
}
