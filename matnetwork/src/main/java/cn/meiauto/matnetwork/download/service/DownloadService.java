package cn.meiauto.matnetwork.download.service;

import android.app.NotificationManager;
import android.app.PendingIntent;
import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.os.IBinder;
import android.support.annotation.Nullable;
import android.support.v4.app.NotificationCompat;
import android.util.SparseArray;

import java.io.File;

import cn.meiauto.matnetwork.NetWork;
import cn.meiauto.matnetwork.callback.FileCallback;
import cn.meiauto.matnetwork.download.DownloadListener;
import cn.meiauto.matnetwork.download.DownloadState;
import cn.meiauto.matnetwork.download.NotifyNotifyClickReceiver;
import cn.meiauto.matnetwork.exeception.CancelException;
import cn.meiauto.matutils.LogUtil;
import okhttp3.Call;
import okhttp3.Request;

public class DownloadService extends Service implements DownloadListener {

    public static final String EXTRA_ID = "cn.meiauto.matnetwork.download.id";
    public static final String EXTRA_URL = "cn.meiauto.matnetwork.download.url";
    public static final String EXTRA_DIR = "cn.meiauto.matnetwork.download.dir";
    public static final String EXTRA_NAME = "cn.meiauto.matnetwork.download.name";
    public static final String EXTRA_FORCE_DOWNLOAD = "cn.meiauto.matnetwork.download.force_download";
    public static final String EXTRA_NOTIFY_TITLE = "cn.meiauto.matnetwork.download.notify_title";

    private NotificationManager mNotifyManager;
    private Intent mClickIntent;
    private SparseArray<Task> mTasks;
    private Intent mDownloadResultIntent;

    public static void startDownload(Context context, int id, String url, String fileDir, String fileName) {
        startDownload(context, id, url, fileDir, fileName, false, "文件下载");
    }

    public static void startDownload(Context context, int id, String url, String fileDir, String fileName, boolean forceDownload) {
        startDownload(context, id, url, fileDir, fileName, forceDownload, "文件下载");
    }

    /**
     * @param forceDownload 是否强制下载，如果文件已存在的话
     * @param notifyTitle   通知栏标题，如果为空，则不在通知栏显示下载进度
     */
    public static void startDownload(Context context, int id, String url, String fileDir, String fileName, boolean forceDownload, String notifyTitle) {
        Intent intent = new Intent(context, DownloadService.class);
        intent.putExtra(EXTRA_ID, id);
        intent.putExtra(EXTRA_URL, url);
        intent.putExtra(EXTRA_DIR, fileDir);
        intent.putExtra(EXTRA_NAME, fileName);
        intent.putExtra(EXTRA_FORCE_DOWNLOAD, forceDownload);
        intent.putExtra(EXTRA_NOTIFY_TITLE, notifyTitle);
        context.startService(intent);
    }

    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        LogUtil.debug("onBind");
        return null;
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        if (intent == null) {
            return Service.START_STICKY;
        }

        int id = intent.getIntExtra(EXTRA_ID, 0);

        String url = intent.getStringExtra(EXTRA_URL);
        String dir = intent.getStringExtra(EXTRA_DIR);
        String name = intent.getStringExtra(EXTRA_NAME);
        boolean forceDownload = intent.getBooleanExtra(EXTRA_FORCE_DOWNLOAD, false);
        String notifyTitle = intent.getStringExtra(EXTRA_NOTIFY_TITLE);

        Task downloadTask = mTasks.valueAt(id);

        if (downloadTask == null) {
            mTasks.put(id, downloadTask = new Task(id, url, dir, name, forceDownload, notifyTitle));

            download(id);
        } else {
            int status = downloadTask.getStatus();
            switch (status) {
                case DownloadState.DEFAULT:
                    LogUtil.debug("初始状态");
                    download(id);
                    break;
                case DownloadState.START:
                    LogUtil.debug("开始状态");
                    download(id);
                    break;
                case DownloadState.DOWNLOADING:
                    LogUtil.debug(downloadTask + "\n已经在下载了，请等待");
                    break;
                case DownloadState.PAUSE:
                    LogUtil.debug("此任务之前被暂停了，现在继续下载");
                    break;
                case DownloadState.CANCEL:
                    download(id);
                    LogUtil.debug("此任务之前被取消了，现在重新下载");
                    break;
                case DownloadState.ERROR:
                    download(id);
                    LogUtil.debug("此任务之前下载失败了，现在重试");
                    break;
                case DownloadState.FINISH:
                    LogUtil.debug("此任务已经下载成功");
                    break;
            }
        }

        LogUtil.debug(downloadTask);
        return super.onStartCommand(intent, flags, startId);
    }

    private void download(int id) {
        DownloadMessager.sendInit(this, mDownloadResultIntent, id);
        Task downloadDownloadTask = mTasks.get(id);
        NetWork.get(downloadDownloadTask.getUrl()).id(id).build().execute(new FileCallback(
                getApplicationContext(),
                downloadDownloadTask.getFileDir(),
                downloadDownloadTask.getFileName(),
                downloadDownloadTask.isForceDownload()) {
            private NotificationCompat.Builder mBuilder;
            boolean firstComing = true;

            @Override
            public void onBefore(Request request, int flag) {
                mBuilder = new NotificationCompat.Builder(DownloadService.this, flag + "");
                mBuilder.setSmallIcon(android.R.drawable.stat_sys_download_done);
                mNotifyManager.notify(flag, mBuilder.build());

                mTasks.get(flag).setStatus(DownloadState.START);
                DownloadMessager.sendStart(DownloadService.this, mDownloadResultIntent, flag);
                if (mTasks.get(flag).isShowNotify()) {
                    mBuilder.setContentTitle(mTasks.get(flag).getNotifyTitle());
                }
            }

            @Override
            public void onError(Call call, int flag, Exception e) {
                mBuilder.setSmallIcon(android.R.drawable.stat_sys_download_done);
                mBuilder.setContentText("下载失败");
                mNotifyManager.notify(flag, mBuilder.build());

                if (e instanceof CancelException) {
                    mTasks.get(flag).setStatus(DownloadState.CANCEL);
                    DownloadMessager.sendCancel(DownloadService.this, mDownloadResultIntent, flag);
                } else {
                    mTasks.get(flag).setStatus(DownloadState.ERROR);
                    DownloadMessager.sendError(DownloadService.this, mDownloadResultIntent, flag, e.getMessage());
                }
            }

            @Override
            public void inProgress(int flag, int maxProgress, int progress) {
                Task task = mTasks.get(flag);
                if (firstComing) {
                    task.setStatus(DownloadState.DOWNLOADING);
                    if (task.isShowNotify()) {
                        LogUtil.debug("set notify id = " + flag);
                        mBuilder
                                .setSmallIcon(android.R.drawable.stat_sys_download)
                                .setContentIntent(buildPendingIntent(flag));
                    }
                    firstComing = false;
                }

                DownloadMessager.sendDownloading(DownloadService.this, mDownloadResultIntent, flag, maxProgress, progress);

                if (task.isShowNotify()) {
                    String content;
                    if (progress == 0) {
                        content = "开始下载";
                    } else if (progress == maxProgress) {
                        content = task.getFileName() + "下载完成";
                    } else {
                        content = "下载中:" + progress + "/" + maxProgress;
                    }
                    mBuilder
                            .setContentTitle(task.getNotifyTitle())
                            .setContentText(content)
                            .setProgress(maxProgress, progress, false);
                    mNotifyManager.notify(flag, mBuilder.build());
                }
            }

            @Override
            public void onResponse(int flag, File result) {
                mNotifyManager.notify(flag, mBuilder.setSmallIcon(android.R.drawable.stat_sys_download_done).build());

                mTasks.get(flag).setStatus(DownloadState.FINISH);
                DownloadMessager.sendFinish(DownloadService.this, mDownloadResultIntent, flag, result.getAbsolutePath());
                LogUtil.debug("onResponse file " + result.toString());
            }
        });
    }

    @Override
    public void onCreate() {
        super.onCreate();
        initNotification();
        mTasks = new SparseArray<>();
        mDownloadResultIntent = new Intent(DownloadMessager.ACTION);
        LogUtil.debug("onCreate");
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        LogUtil.debug("onDestroy");
    }

    private void initNotification() {
        mNotifyManager = (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);
        mClickIntent = new Intent(NotifyNotifyClickReceiver.ACTION);
    }

    private PendingIntent buildPendingIntent(int taskId) {
        mClickIntent.putExtra(NotifyNotifyClickReceiver.EXTRA_TASK_ID, taskId);
        return PendingIntent.getBroadcast(this, taskId, mClickIntent, PendingIntent.FLAG_UPDATE_CURRENT);
    }

    private void removeTask(int id) {
        mTasks.remove(id);
    }

    @Override
    public void onStart(int id) {

    }

    @Override
    public void onDownloading(int id, long totalLength, long downloadLength) {

    }

    @Override
    public void onPause(int id) {

    }

    @Override
    public void onError(int id, String reason) {

    }

    @Override
    public void onCancel(int id) {

    }

    @Override
    public void onFinish(int id, String filePath) {

    }
}
