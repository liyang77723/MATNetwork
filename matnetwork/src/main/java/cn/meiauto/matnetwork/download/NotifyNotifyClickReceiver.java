package cn.meiauto.matnetwork.download;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;

import java.io.File;

import cn.meiauto.matnetwork.NetWork;

public class NotifyNotifyClickReceiver extends BroadcastReceiver implements DownloadNotifyClickListener {

    public static final String ACTION = "cn.meiauto.matnetwork.download.notification.action.click";

    public static final String EXTRA_TASK_ID = "cn.meiauto.matnetwork.download.notification.extra.task_id";
    public static final String EXTRA_TASK_STATUS = "cn.meiauto.matnetwork.download.notification.extra.task_status";

    private DownloadOverClickListener mClickListener;

    //system used it
    @SuppressWarnings("unused")
    public NotifyNotifyClickReceiver() {
    }

    public NotifyNotifyClickReceiver(DownloadOverClickListener overClickListener) {
        mClickListener = overClickListener;
    }

    @Override
    public void onReceive(final Context context, Intent intent) {
        final int taskId = intent.getIntExtra(EXTRA_TASK_ID, 0);
        @DownloadStatus final int downloadStatus = intent.getIntExtra(EXTRA_TASK_STATUS, 0);

        switch (downloadStatus) {
            case DownloadState.DOWNLOADING:
                DownloadManager.getInstance().stopTask(taskId);
                break;
            case DownloadState.PAUSE:
                DownloadManager.getInstance().continueDownload(taskId);
                break;
            case DownloadState.CANCEL:
                DownloadManager.getInstance().download(taskId);
                break;
            case DownloadState.FINISH:
                if (mClickListener != null) {
                    NetWork.getInstance().handler().post(new Runnable() {
                        @Override
                        public void run() {
                            DownloadTask task = DownloadManager.getInstance().getDownloadTask(taskId);
                            if (task != null) {
                                mClickListener.onFinishClick(taskId, task.getFileDir() + File.separator + task.getFileName());
                            }
                        }
                    });
                }
                break;
        }
        onClick(context, taskId, downloadStatus);
    }

    public static void registerReceiver(Context context, NotifyNotifyClickReceiver clickReceiver) {
        IntentFilter filter = new IntentFilter();
        filter.addAction(ACTION);
        context.registerReceiver(clickReceiver, filter);
    }

    public static void unregisterReceiver(Context context, NotifyNotifyClickReceiver clickReceiver) {
        context.unregisterReceiver(clickReceiver);
    }

    @Override
    public void onClick(Context context, int taskId, @DownloadStatus int status) {

    }
}