package cn.meiauto.matnetwork.download;

import android.content.Context;
import android.support.annotation.Nullable;
import android.util.SparseArray;

import java.io.File;
import java.util.concurrent.Executor;
import java.util.concurrent.Executors;

import cn.meiauto.matutils.LogUtil;
import cn.meiauto.matutils.preference.SecurePreferences;

/**
 * <pre>
 *  author : LiYang
 *  email  : yang.li@meiauto.cn
 *  time   : 2017-08-23
 */
@SuppressWarnings("WeakerAccess")
public class DownloadManager {

    public static final String KEY_DOWNLOAD_NAME = "donwload_name";
    public static final String KEY_DOWNLOAD_STATUS = "donwload_status";

    private static final int POOL_SIZE = 5;
    private final Executor mExecutor;
    private final SparseArray<DownloadTask> mTasks;

    private SecurePreferences mPreferences;

    public void download(DownloadTask downloadTask) {
        if (mPreferences == null) {
            throw new NullPointerException("SecurePreferences is null,plz init context");
        }
        int id = downloadTask.getId();
        DownloadTask task = mTasks.get(id);
        if (task == null) {
            mExecutor.execute(downloadTask);
            mTasks.put(downloadTask.getId(), downloadTask);
        } else {
            @DownloadStatus int status = task.getStatus();
            switch (status) {
                case DownloadState.DEFAULT:
                    mExecutor.execute(downloadTask);
                    break;
                case DownloadState.START:
                    LogUtil.debug("Download task is already start.");
                    break;
                case DownloadState.DOWNLOADING:
                    LogUtil.debug("Download task is already downloading.");
                    break;
                case DownloadState.PAUSE:
                    LogUtil.debug("Download task is already exist and paused, now restart the task.");
                    task.setStop(false);
                    task.setForceDownload(false);
                    mExecutor.execute(task);
                    break;
                case DownloadState.CANCEL:
                    LogUtil.debug("Download task is already exist and cancled, now restart the task.");
                    task.setCancel(false);
                    task.setForceDownload(true);
                    mExecutor.execute(task);
                    break;
                case DownloadState.ERROR:
                    LogUtil.debug("Download task is already exist and error, now restart the task.");
                    task.setStop(false);
                    task.setCancel(false);
                    downloadTask.setForceDownload(true);
                    mExecutor.execute(downloadTask);
                    break;
                case DownloadState.FINISH:
                    LogUtil.debug("Download task is already exist and finished.");
                    task.sendOnFinish(task.getFileDir() + File.separator + task.getFileName());
                    break;
            }
        }
    }

    public int getSavedStatus(int id) {
        return mPreferences.getInt(KEY_DOWNLOAD_STATUS + id, DownloadState.DEFAULT);
    }

    @Nullable
    public DownloadTask getDownloadTask(int id) {
        return mTasks.get(id);
    }

    /**
     * download file if it is exist in log,it will replaced the existed file.
     */
    public void download(int id) {
        DownloadTask downloadTask = mTasks.get(id);
        if (downloadTask != null) {
            downloadTask.setForceDownload(true);
            mExecutor.execute(downloadTask);
        }
    }

    public void continueDownload(int id) {
        DownloadTask downloadTask = mTasks.get(id);
        if (downloadTask != null) {
            downloadTask.setForceDownload(false);
            mExecutor.execute(downloadTask);
        }
    }

    public void cancel(int id) {
        DownloadTask downloadTask = mTasks.get(id);
        if (downloadTask != null) {
            downloadTask.setCancel(true);
        }
    }

    public void stopTask(int id) {
        DownloadTask downloadTask = mTasks.get(id);
        if (downloadTask != null) {
            downloadTask.setStop(true);
        }
    }

    public void stopAllRunningTasks() {
        int size = mTasks.size();
        DownloadTask task;
        for (int i = 0; i < size; i++) {
            task = mTasks.valueAt(0);
            if (task.getStatus() == DownloadState.DOWNLOADING) {
                task.setStop(true);
            }
        }
    }

    public SecurePreferences getPreferences() {
        return mPreferences;
    }

    public void init(Context context) {
        mPreferences = new SecurePreferences(context);
    }

    private DownloadManager() {
        mExecutor = Executors.newFixedThreadPool(POOL_SIZE);
        mTasks = new SparseArray<>();
    }

    public static DownloadManager getInstance() {
        return Holder.INSTANCE;
    }

    private static class Holder {
        static final DownloadManager INSTANCE = new DownloadManager();
    }
}
