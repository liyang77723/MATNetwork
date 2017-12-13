package cn.meiauto.matnetwork.download;

import android.support.annotation.IntDef;

import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;

import cn.meiauto.matnetwork.download.service.Task;

@SuppressWarnings("WeakerAccess")
@Retention(RetentionPolicy.SOURCE)
@IntDef({DownloadState.DEFAULT, DownloadState.START, DownloadState.DOWNLOADING, DownloadState.PAUSE, DownloadState.CANCEL, DownloadState.FINISH, DownloadState.ERROR})
public @interface DownloadStatus {
}