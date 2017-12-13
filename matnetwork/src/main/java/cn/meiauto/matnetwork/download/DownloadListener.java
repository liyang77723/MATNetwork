package cn.meiauto.matnetwork.download;

public interface DownloadListener {
    void onStart(int id);

    void onDownloading(int id, long totalLength, long downloadLength);

    void onPause(int id);

    void onError(int id, String reason);

    void onCancel(int id);

    void onFinish(int id, String filePath);
}