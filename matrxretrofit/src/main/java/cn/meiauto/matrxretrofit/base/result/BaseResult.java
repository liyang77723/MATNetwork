package cn.meiauto.matrxretrofit.base.result;

/**
 * basic data returned by the server
 * <p>
 * author : LiYang
 * email  : yang.li@nx-engine.com
 * time   : 2018/5/3
 */
public class BaseResult {

    public String status;
    public String errorCode;
    public String errorMessage;
    public String extMessage;

    @Override
    public String toString() {
        return "↓BaseResult↓" +
                "\n    status=" + status +
                "\n    errorCode=" + errorCode +
                "\n    errorMessage=" + errorMessage +
                "\n    extMessage=" + extMessage +
                "\n↑BaseResult↑";
    }
}