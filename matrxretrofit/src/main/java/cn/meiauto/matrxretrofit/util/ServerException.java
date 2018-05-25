package cn.meiauto.matrxretrofit.util;

/**
 * author : LiYang
 * email  : yang.li@nx-engine.com
 * time   : 2018/2/1
 */
public class ServerException extends RuntimeException {

    //http error
    private int mHttpCode = -1;

    //server error
    private String mErrorCode = "-1";
    private String mErrorMessage;
    private String mExtMessage;

    //error
    private Throwable mThrowable;

    public ServerException() {
    }

    public ServerException httpCode(int mHttpCode) {
        this.mHttpCode = mHttpCode;
        return this;
    }

    public ServerException errorCode(String mErrorCode) {
        this.mErrorCode = mErrorCode;
        return this;
    }

    public ServerException errorMessage(String mErrorMessage) {
        this.mErrorMessage = mErrorMessage;
        return this;
    }

    public ServerException extMessage(String mExtMessage) {
        this.mExtMessage = mExtMessage;
        return this;
    }

    public ServerException throwable(Throwable mThrowable) {
        this.mThrowable = mThrowable;
        return this;
    }

    public int getHttpCode() {
        return mHttpCode;
    }

    public String getErrorCode() {
        return mErrorCode;
    }

    public String getErrorMessage() {
        return mErrorMessage;
    }

    public String getExtMessage() {
        return mExtMessage;
    }

    public Throwable getThrowable() {
        return mThrowable;
    }

    @Override
    public String toString() {
        if (mThrowable != null) {
            mThrowable.printStackTrace();
        }
        return "\n↓ServerException↓" +
                "\n    mHttpCode=" + mHttpCode +
                "\n    mErrorCode=" + mErrorCode +
                "\n    mErrorMessage=" + mErrorMessage +
                "\n    mExtMessage=" + mExtMessage +
                "\n    mThrowable=" + mThrowable +
                "\n↑ServerException↑\n";
    }
}
