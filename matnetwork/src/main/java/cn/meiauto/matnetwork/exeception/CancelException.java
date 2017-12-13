package cn.meiauto.matnetwork.exeception;

public class CancelException extends Exception {
    public CancelException() {
        super("请求被取消");
    }
}
