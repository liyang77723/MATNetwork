package cn.meiauto.matnetwork;

/**
 * author : LiYang
 * email  : yang.li@nx-engine.com
 * time   : 2018/2/1
 */
public class ServerException extends RuntimeException {

    private int httpCode;

    private String errorCode;
    private String errorMessage;

    public ServerException() {
    }

    public String getErrorCode() {
        return errorCode;
    }

    public ServerException setErrorCode(String errorCode) {
        this.errorCode = errorCode;
        return this;
    }

    public String getErrorMessage() {
        return errorMessage;
    }

    public ServerException setErrorMessage(String errorMessage) {
        this.errorMessage = errorMessage;
        return this;
    }

    public int getHttpCode() {
        return httpCode;
    }

    public ServerException setHttpCode(int httpCode) {
        this.httpCode = httpCode;
        return this;
    }

    @Override
    public String toString() {
        return "ServerException\n" +
                "httpCode = " + httpCode + '\n' +
                "errorCode = " + errorCode + '\n' +
                "errorMessage = " + errorMessage;
    }
}
