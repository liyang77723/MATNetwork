package cn.meiauto.matrxretrofit.base.result;

/**
 * contain data only
 * <p>
 * author : LiYang
 * email  : yang.li@nx-engine.com
 * time   : 2018/5/3
 */
public class BaseDataResult<DATA> extends BaseResult {

    public DATA data;

    @Override
    public String toString() {
        return "↓BaseDataResult↓" +
                "\n    data=" + data +
                "\n    status=" + status +
                "\n    errorCode=" + errorCode +
                "\n    errorMessage=" + errorMessage +
                "\n    extMessage=" + extMessage +
                "\n↑BaseDataResult↑";
    }
}
