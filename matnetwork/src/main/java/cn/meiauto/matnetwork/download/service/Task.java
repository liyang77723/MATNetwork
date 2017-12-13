package cn.meiauto.matnetwork.download.service;

import cn.meiauto.matnetwork.download.DownloadStatus;
import cn.meiauto.matutils.EmptyUtil;

public class Task {
    private int id;
    private String url;
    private String fileDir;
    private String fileName;
    private boolean forceDownload;
    private String notifyTitle;

    private int status;
    private boolean showNotify;

    public Task(int id, String url, String fileDir, String fileName, boolean forceDownload, String notifyTitle) {
        this.id = id;
        this.url = url;
        this.fileDir = fileDir;
        this.fileName = fileName;
        this.forceDownload = forceDownload;
        this.notifyTitle = notifyTitle;
        this.showNotify = !EmptyUtil.isEmpty(notifyTitle);
    }

    @Override
    public String toString() {
        return "Task"
                + "\n\tid=" + id
                + "\n\turl=" + url
                + "\n\tsetFileDir=" + fileDir
                + "\n\tsetFileName=" + fileName
                + "\n\tsetForceDownload=" + forceDownload
                + "\n\tnotifyTitle=" + notifyTitle
                + "\n\tstatus=" + status
                + "\n\tshowNotify=" + showNotify;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public String getFileDir() {
        return fileDir;
    }

    public void setFileDir(String fileDir) {
        this.fileDir = fileDir;
    }

    public String getFileName() {
        return fileName;
    }

    public void setFileName(String fileName) {
        this.fileName = fileName;
    }

    public boolean isForceDownload() {
        return forceDownload;
    }

    public void setForceDownload(boolean forceDownload) {
        this.forceDownload = forceDownload;
    }

    public String getNotifyTitle() {
        return notifyTitle;
    }

    public void setNotifyTitle(String notifyTitle) {
        this.notifyTitle = notifyTitle;
    }

    @DownloadStatus
    public int getStatus() {
        return status;
    }

    public void setStatus(@DownloadStatus int status) {
        this.status = status;
    }

    public boolean isShowNotify() {
        return showNotify;
    }

    public void setShowNotify(boolean showNotify) {
        this.showNotify = showNotify;
    }
}
