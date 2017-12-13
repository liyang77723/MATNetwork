package cn.meiauto.matnetwork.download.service;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.support.v4.content.LocalBroadcastManager;

import cn.meiauto.matnetwork.download.DownloadListener;
import cn.meiauto.matnetwork.download.DownloadState;
import cn.meiauto.matnetwork.download.DownloadStatus;

/**
 * local broadcast receiver
 */
public class DownloadMessager extends BroadcastReceiver {

    public static final String ACTION = "cn.meiauto.matnetwork.download.receiver.broadcast.action";

    public static final String EXTRA_REQUEST_STATUS = "cn.meiauto.matnetwork.download.receiver.extra.status";
    public static final String EXTRA_ID = "cn.meiauto.matnetwork.download.receiver.extra.id";

    public static final String EXTRA_MAX_PROGRESS = "cn.meiauto.matnetwork.download.receiver.extra.max_progress";
    public static final String EXTRA_PROGRESS = "cn.meiauto.matnetwork.download.receiver.extra.progress";
    public static final String EXTRA_FILE_PATH = "cn.meiauto.matnetwork.download.receiver.extra.file_path";
    public static final String EXTRA_ERROR = "cn.meiauto.matnetwork.download.receiver.extra.error";

    private DownloadListener mDownloadListener;

    public DownloadMessager(DownloadListener downloadListener) {
        mDownloadListener = downloadListener;
    }

    @Override
    public void onReceive(Context context, Intent intent) {
        if (mDownloadListener == null) {
            return;
        }
        if (intent == null) {
            return;
        }

        final int id = intent.getIntExtra(EXTRA_ID, 0);

        @DownloadStatus
        int status = intent.getIntExtra(EXTRA_REQUEST_STATUS, DownloadState.DEFAULT);
        switch (status) {
            case DownloadState.START:
                mDownloadListener.onStart(id);
                break;
            case DownloadState.DOWNLOADING:
                int progress = intent.getIntExtra(EXTRA_PROGRESS, 0);
                int maxProgress = intent.getIntExtra(EXTRA_MAX_PROGRESS, 0);
                mDownloadListener.onDownloading(id, maxProgress, progress);
                break;
            case DownloadState.PAUSE:
                mDownloadListener.onPause(id);
                break;
            case DownloadState.CANCEL:
                mDownloadListener.onCancel(id);
                break;
            case DownloadState.FINISH:
                String filePath = intent.getStringExtra(EXTRA_FILE_PATH);
                mDownloadListener.onFinish(id, filePath);
                break;
            case DownloadState.ERROR:
                String error = intent.getStringExtra(EXTRA_ERROR);
                mDownloadListener.onError(id, error);
                break;
        }

    }

    public static void sendInit(Context context, Intent intent, int id) {
        intent.putExtra(EXTRA_REQUEST_STATUS, DownloadState.DEFAULT);
        intent.putExtra(EXTRA_ID, id);
        LocalBroadcastManager.getInstance(context).sendBroadcast(intent);
    }

    public static void sendStart(Context context, Intent intent, int id) {
        intent.putExtra(EXTRA_REQUEST_STATUS, DownloadState.START);
        intent.putExtra(EXTRA_ID, id);
        LocalBroadcastManager.getInstance(context).sendBroadcast(intent);
    }

    public static void sendDownloading(Context context, Intent intent, int id, int maxProgress, int progress) {
        intent.putExtra(EXTRA_REQUEST_STATUS, DownloadState.DOWNLOADING);
        intent.putExtra(EXTRA_ID, id);
        intent.putExtra(EXTRA_MAX_PROGRESS, maxProgress);
        intent.putExtra(EXTRA_PROGRESS, progress);
        LocalBroadcastManager.getInstance(context).sendBroadcast(intent);
    }

    public static void sendPause(Context context, Intent intent, int id) {
        intent.putExtra(EXTRA_REQUEST_STATUS, DownloadState.PAUSE);
        intent.putExtra(EXTRA_ID, id);
        LocalBroadcastManager.getInstance(context).sendBroadcast(intent);
    }

    public static void sendCancel(Context context, Intent intent, int id) {
        intent.putExtra(EXTRA_REQUEST_STATUS, DownloadState.CANCEL);
        intent.putExtra(EXTRA_ID, id);
        LocalBroadcastManager.getInstance(context).sendBroadcast(intent);
    }

    public static void sendFinish(Context context, Intent intent, int id, String filePath) {
        intent.putExtra(EXTRA_REQUEST_STATUS, DownloadState.FINISH);
        intent.putExtra(EXTRA_ID, id);
        intent.putExtra(EXTRA_FILE_PATH, filePath);
        LocalBroadcastManager.getInstance(context).sendBroadcast(intent);
    }

    public static void sendError(Context context, Intent intent, int id, String error) {
        intent.putExtra(EXTRA_REQUEST_STATUS, DownloadState.ERROR);
        intent.putExtra(EXTRA_ID, id);
        intent.putExtra(EXTRA_ERROR, error);
        LocalBroadcastManager.getInstance(context).sendBroadcast(intent);
    }

    public static void registerReceiver(Context context, DownloadMessager downloadMessager) {
        IntentFilter filter = new IntentFilter();
        filter.addAction(ACTION);
        LocalBroadcastManager.getInstance(context).registerReceiver(downloadMessager, filter);
    }

    public static void unregisterReceiver(Context context, DownloadMessager downloadMessager) {
        LocalBroadcastManager.getInstance(context).unregisterReceiver(downloadMessager);
    }
}
