package cn.meiauto.matnetwork;

import java.util.List;

/**
 * 服务器统一返回数据格式
 * author : LiYang
 * email  : yang.li@nx-engine.com
 * time   : 2018/1/30
 */
public class ResultList<DATA> {

    private String status;
    private String errorCode;
    private String errorMessage;
    private String extMessage;

    private int pageCount;
    private int pageIndex;
    private int pageSize;
    private int totalCount;

    private List<DATA> data;

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getErrorCode() {
        return errorCode;
    }

    public void setErrorCode(String errorCode) {
        this.errorCode = errorCode;
    }

    public String getErrorMessage() {
        return errorMessage;
    }

    public void setErrorMessage(String errorMessage) {
        this.errorMessage = errorMessage;
    }

    public String getExtMessage() {
        return extMessage;
    }

    public void setExtMessage(String extMessage) {
        this.extMessage = extMessage;
    }

    public int getPageCount() {
        return pageCount;
    }

    public void setPageCount(int pageCount) {
        this.pageCount = pageCount;
    }

    public int getPageIndex() {
        return pageIndex;
    }

    public void setPageIndex(int pageIndex) {
        this.pageIndex = pageIndex;
    }

    public int getPageSize() {
        return pageSize;
    }

    public void setPageSize(int pageSize) {
        this.pageSize = pageSize;
    }

    public int getTotalCount() {
        return totalCount;
    }

    public void setTotalCount(int totalCount) {
        this.totalCount = totalCount;
    }

    public List<DATA> getData() {
        return data;
    }

    public void setData(List<DATA> data) {
        this.data = data;
    }


    @Override
    public String toString() {
        return "↓Result↓" +
                "\nstatus=" + status +
                "\nerrorCode=" + errorCode +
                "\nerrorMessage=" + errorMessage +
                "\nextMessage=" + extMessage +
                "\ndata=" + data +
                "\n↑Result↑";
    }
}
