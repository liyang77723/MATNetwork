package cn.meiauto.matnetwork.sample;

import com.google.gson.JsonParseException;

import org.json.JSONException;

import java.net.ConnectException;
import java.text.ParseException;

import cn.meiauto.matnetwork.ServerException;
import retrofit2.HttpException;

public class ExceptionEngine {

    //对应HTTP的状态码
    private static final int UNAUTHORIZED = 401;
    private static final int FORBIDDEN = 403;
    private static final int NOT_FOUND = 404;
    private static final int REQUEST_TIMEOUT = 408;
    private static final int INTERNAL_SERVER_ERROR = 500;
    private static final int BAD_GATEWAY = 502;
    private static final int SERVICE_UNAVAILABLE = 503;
    private static final int GATEWAY_TIMEOUT = 504;

    public static ServerException handleException(Throwable e) {
        ServerException serverException = new ServerException();
        if (e instanceof HttpException) {             //HTTP错误
            HttpException httpException = (HttpException) e;
            int code = httpException.code();
            serverException.setErrorCode(String.valueOf(code));

            String message;
            switch (code) {
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
                    message = "请求超时";
                    break;
                case GATEWAY_TIMEOUT:
                case INTERNAL_SERVER_ERROR:
                case BAD_GATEWAY:
                case SERVICE_UNAVAILABLE:
                    message = "服务器错误";
                    break;
                default:
                    message = "网络错误";
                    break;
            }
            serverException.setErrorMessage(message);  //均视为网络错误
        } else if (e instanceof ServerException) {
            return (ServerException) e;
        } else if (e instanceof JsonParseException
                || e instanceof JSONException
                || e instanceof ParseException) {
            serverException.setErrorCode(String.valueOf(ERROR.PARSE_ERROR));
            serverException.setErrorMessage("解析错误");  //均视为解析错误
        } else if (e instanceof ConnectException) {
            serverException.setErrorCode(String.valueOf(ERROR.PARSE_ERROR));
            serverException.setErrorMessage("连接失败");//均视为网络错误
        } else {
            serverException.setErrorCode(String.valueOf(ERROR.UNKNOWN));
            serverException.setErrorMessage("未知错误");
        }
        return serverException;
    }

    /**
     * 约定异常
     */

    public static class ERROR {
        /**
         * 未知错误
         */
        public static final int UNKNOWN = 1000;
        /**
         * 解析错误
         */
        public static final int PARSE_ERROR = 1001;
        /**
         * 网络错误
         */
        public static final int NETWORD_ERROR = 1002;
        /**
         * 协议出错
         */
        public static final int HTTP_ERROR = 1003;
    }
}