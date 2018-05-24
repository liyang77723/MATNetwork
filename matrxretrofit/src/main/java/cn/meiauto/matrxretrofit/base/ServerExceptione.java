package cn.meiauto.matrxretrofit.base;

/**
 * author : LiYang
 * email  : yang.li@nx-engine.com
 * time   : 2018/5/4
 */
public class ServerExceptione {
    //http error
    private int mHttpCode = -1;

    //server error
    private String mErrorCode = "-1";
    private String mErrorMessage;
    private String mExtMessage;

    //error
    private Throwable mThrowable;

    public ServerExceptione() {
    }

    public ServerExceptione(int httpCode, String errorMessage) {
        mHttpCode = httpCode;
        mErrorMessage = errorMessage;
    }

    public ServerExceptione(String errorCode, String errorMessage, String extMessage) {
        mErrorCode = errorCode;
        mErrorMessage = errorMessage;
        mExtMessage = extMessage;
    }

    public ServerExceptione(int httpCode, String errorCode, String errorMessage, String extMessage, Throwable throwable) {
        mHttpCode = httpCode;
        mErrorCode = errorCode;
        mErrorMessage = errorMessage;
        mExtMessage = extMessage;
        mThrowable = throwable;
    }
}
