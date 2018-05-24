package cn.meiauto.matrxretrofit.base.result;

import java.util.Arrays;
import java.util.List;

/**
 * contain data and list
 * <p>
 * author : LiYang
 * email  : yang.li@nx-engine.com
 * time   : 2018/5/3
 */
public class BaseDLResult<DATA, DATAS> extends BaseResult {

    public DATA data;
    public List<DATAS> datas;

    public int pageCount;
    public int pageIndex;
    public int pageSize;
    public int totalCount;

    @Override
    public String toString() {
        return "↓BaseDLResult↓" +
                "\n    data=" + data +
                "\n    datas=" + Arrays.toString(datas.toArray()) +
                "\n    pageCount=" + pageCount +
                "\n    pageIndex=" + pageIndex +
                "\n    pageSize=" + pageSize +
                "\n    totalCount=" + totalCount +
                "\n    status=" + status +
                "\n    errorCode=" + errorCode +
                "\n    errorMessage=" + errorMessage +
                "\n    extMessage=" + extMessage +
                "\n↑BaseDLResult↑";
    }
}
