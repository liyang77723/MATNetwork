package cn.meiauto.matrxretrofit.util;

import java.net.ConnectException;
import java.net.SocketTimeoutException;
import java.net.UnknownHostException;

import retrofit2.HttpException;

public class ExceptionHandler {

    //对应HTTP的状态码
    private static final int GRAMMATICAL_ERROR = 400;
    private static final int UNAUTHORIZED = 401;
    private static final int FORBIDDEN = 403;
    private static final int NOT_FOUND = 404;
    private static final int REQUEST_TIMEOUT = 408;
    private static final int INTERNAL_SERVER_ERROR = 500;
    private static final int BAD_GATEWAY = 502;
    private static final int SERVICE_UNAVAILABLE = 503;
    private static final int GATEWAY_TIMEOUT = 504;

    private ExceptionHandler() {
    }

    public static ServerException handle(Throwable e) {
        ServerException serverException;
        if (e instanceof HttpException) {
            serverException = http2ServerEx((HttpException) e);
        } else if (e instanceof ServerException) {
            serverException = (ServerException) e;
        } else if (e instanceof UnknownHostException || e instanceof ConnectException) {
            serverException = new ServerException().errorMessage("网络错误，请检查后重试");
        } else if (e instanceof SocketTimeoutException) {
            serverException = new ServerException().errorMessage("请求超时，请重试");
        } else {
            serverException = other2ServerEx(e);
        }
        return serverException;
    }

    private static ServerException http2ServerEx(HttpException e) {
        ServerException serverException = new ServerException().throwable(e);

        int code = e.code();
        serverException.httpCode(code);
        serverException.errorCode(String.valueOf(code));

        String message;
        switch (code) {
            case GRAMMATICAL_ERROR:
                message = "请求语法错误";
                break;
            case UNAUTHORIZED:
                message = "认证失败";
                break;
            case FORBIDDEN:
                message = "服务器拒绝";
                break;
            case NOT_FOUND:
                message = "请求失败";
                break;
            case REQUEST_TIMEOUT:
                message = "请求超时，请稍后重试";
                break;
            case GATEWAY_TIMEOUT:
            case INTERNAL_SERVER_ERROR:
            case BAD_GATEWAY:
            case SERVICE_UNAVAILABLE:
                message = "服务器错误，请稍后重试";
                break;
            default:
                message = "未知错误";
                break;
        }
        serverException.errorMessage(message);
        return serverException;
    }

    private static ServerException other2ServerEx(Throwable e) {
        return new ServerException().throwable(e);
    }
}