package cn.meiauto.matnetwork.download;

import android.content.Context;

public interface DownloadNotifyClickListener {
    void onClick(Context context, int taskId, @DownloadStatus int status);
}