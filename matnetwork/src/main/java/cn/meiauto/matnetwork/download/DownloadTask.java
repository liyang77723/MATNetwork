package cn.meiauto.matnetwork.download;

import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.os.Environment;
import android.support.v4.app.NotificationCompat;
import android.util.Log;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;

import cn.meiauto.matnetwork.NetWork;
import cn.meiauto.matnetwork.request.GetRequest;
import cn.meiauto.matutils.EmptyUtil;
import cn.meiauto.matutils.FileUtil;
import cn.meiauto.matutils.LogUtil;
import cn.meiauto.matutils.MD5Util;
import okhttp3.Response;
import okhttp3.ResponseBody;

@SuppressWarnings({"unused", "WeakerAccess"})
public class DownloadTask implements Runnable {

    public static final int DEFAULT_SEND_DURATION = 100;

    private final int mId;
    private final String mUrl;

    private String mFileDir;
    private String mFileName;
    private DownloadListener mDownloadListener;
    private int mSendDuration;//下载更新间隔
    private String mNotificationTitle;
    private boolean mForceDownload;
    private Context mContext;

    private int mStatus;
    private boolean mDelaySendDownload;
    private boolean mStop, mCancel;
    private NotificationCompat.Builder mBuilder;
    private NotificationManager mNotificationManager;
    private Intent mClickIntent;
    private boolean firstDownloading = true;

    @Override
    public String toString() {
        return "DownloadTask"
                + "\n\tmId=" + mId
                + "\n\tmUrl=" + mUrl
                + "\n\tmFileDir=" + mFileDir
                + "\n\tmFileName=" + mFileName
                + "\n\tmSendDuration=" + mSendDuration
                + "\n\tmNotificationTitle=" + mNotificationTitle
                + "\n\tmForceDownload=" + mForceDownload
                + "\n\tmStatus=" + mStatus
                + "\n\tmStop=" + mStop;
    }

    private DownloadTask(Builder builder) {
        mId = builder.id;
        mUrl = builder.url;

        mFileDir = builder.fileDir;
        mFileName = builder.fileName;
        mDownloadListener = builder.mDownloadListener;
        mSendDuration = builder.sendDuration;
        mNotificationTitle = builder.notificationTitle;
        mForceDownload = builder.forceDownload;
        mContext = builder.context;

        init();
    }

    private void init() {

        check();

        mStatus = DownloadState.DEFAULT;
        DownloadManager.getInstance().getPreferences().edit()
                .putString(DownloadManager.KEY_DOWNLOAD_NAME + mId, mFileName)
                .putInt(DownloadManager.KEY_DOWNLOAD_STATUS + mId, mStatus)
                .apply();

        if (mBuilder != null) {
            mBuilder.setSmallIcon(android.R.drawable.stat_sys_download_done)
                    .setContentText("准备下载")
                    .setContentIntent(buildPendingIntent());
            mNotificationManager.notify(mId, mBuilder.build());
        }
    }

    @Override
    public void run() {
        File file = FileUtil.makeDirsIfNotExist(mFileDir);
        file = new File(file, mFileName);

        long readLength;
        if (mForceDownload) {
            readLength = 0;
        } else {
            if (file.exists()) {
                long contentLength = getContentLength(mUrl);
                if (file.length() == contentLength) {//文件长度一致
                    sendOnFinish(file.getAbsolutePath());
                    return;
                } else {
                    readLength = file.length();
                }
            } else {
                readLength = 0;
            }
        }

        LogUtil.debug("exist=" + file.exists() + " readLength=" + readLength);

        Response response = NetWork.get(mUrl).addHeader("RANGE", "bytes=" + readLength + "-").build().execute();

        if (response == null) {
            sendOnError("response is null");
            LogUtil.error("response is null");
            return;
        }
        if (!response.isSuccessful()) {
            sendOnError("response is failed");
            LogUtil.error("response is null");
            return;
        }
        ResponseBody body = response.body();
        if (body == null) {
            sendOnError("response body is null");
            LogUtil.error("response body is null");
            return;
        }

        sendOnStart();

        final long totalLength = body.contentLength() + readLength;
        LogUtil.debug("contentLength=" + body.contentLength() + " readLength=" + readLength);

        InputStream is = null;
        FileOutputStream fos = null;
        int len;
        byte[] bytes = new byte[1024];
        try {
            is = body.byteStream();
            fos = new FileOutputStream(file, !mForceDownload);

            while ((len = is.read(bytes)) != -1) {
                if (mStop) {
                    sendOnPause();
                    return;
                }
                if (mCancel) {
                    sendOnCancel();
                    return;
                }
                readLength += len;
                fos.write(bytes, 0, len);
                if (!mDelaySendDownload) {
                    sendOnDownload(totalLength, readLength);
                }
                if (readLength == totalLength) {
                    if (mBuilder != null) {
                        mBuilder.setSmallIcon(android.R.drawable.stat_sys_download)
                                .setProgress(1000, 1000, false)
                                .setContentText(FileUtil.convertFileLenght(totalLength).concat("/").concat(FileUtil.convertFileLenght(totalLength)));
                        mNotificationManager.notify(mId, mBuilder.build());
                    }
                    mDelaySendDownload = false;
                    NetWork.getInstance().handler().post(new Runnable() {
                        @Override
                        public void run() {
                            if (mDownloadListener != null) {
                                mDownloadListener.onDownloading(mId, totalLength, totalLength);
                            }
                        }
                    });
                    break;
                }
            }
            fos.flush();
            fos.close();
            is.close();
            sendOnFinish(file.getAbsolutePath());
        } catch (IOException e) {
            e.printStackTrace();
            sendOnError(Log.getStackTraceString(e));
        } finally {
            try {
                body.close();
                if (is != null) {
                    is.close();
                }
                if (fos != null) {
                    fos.close();
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    public void setStop(boolean stop) {
        mStop = stop;
    }

    public void setCancel(boolean cancel) {
        mCancel = cancel;
    }

    public boolean isRunning() {
        return !mStop;
    }

    private void check() {
        if (EmptyUtil.isEmpty(mUrl)) {
            throw new NullPointerException("Download url is null,please check");
        }
        if (EmptyUtil.isEmpty(mFileDir)) {
            mFileDir = Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DOWNLOADS).getAbsolutePath();
        }
        if (EmptyUtil.isEmpty(mFileName)) {
            mFileName = MD5Util.MD5(mUrl);
        }
        if (mSendDuration == 0) {
            mSendDuration = DEFAULT_SEND_DURATION;
        }
        if (mContext != null) {
            if (EmptyUtil.isEmpty(mNotificationTitle)) {
                mNotificationTitle = "文件下载";
            }
            mNotificationManager = (NotificationManager) mContext.getSystemService(Context.NOTIFICATION_SERVICE);
            mClickIntent = new Intent(NotifyNotifyClickReceiver.ACTION);
            mBuilder = new NotificationCompat.Builder(mContext, mId + "").setContentTitle(mNotificationTitle);
        }
    }

    private PendingIntent buildPendingIntent() {
        mClickIntent.putExtra(NotifyNotifyClickReceiver.EXTRA_TASK_ID, mId);
        mClickIntent.putExtra(NotifyNotifyClickReceiver.EXTRA_TASK_STATUS, mStatus);
        return PendingIntent.getBroadcast(mContext, mId, mClickIntent, PendingIntent.FLAG_UPDATE_CURRENT);
    }

    //计算文件长度
    private long getContentLength(String fileUrl) {
        Response response = new GetRequest.Builder(fileUrl).build().execute();
        if (response == null) {
            return 0;
        }
        if (!response.isSuccessful()) {
            return 0;
        }
        ResponseBody responseBody = response.body();
        if (responseBody == null) {
            return 0;
        }
        long contentLength = responseBody.contentLength();
        response.close();
        return contentLength;
    }

    public int getId() {
        return mId;
    }

    public String getUrl() {
        return mUrl;
    }

    public String getFileDir() {
        return mFileDir;
    }

    public void setFileDir(String fileDir) {
        mFileDir = fileDir;
    }

    public String getFileName() {
        return mFileName;
    }

    public void setFileName(String fileName) {
        mFileName = fileName;
    }

    public DownloadListener getDownloadListener() {
        return mDownloadListener;
    }

    public void setDownloadListener(DownloadListener downloadListener) {
        mDownloadListener = downloadListener;
    }

    public int getSendDuration() {
        return mSendDuration;
    }

    public void setSendDuration(int sendDuration) {
        mSendDuration = sendDuration;
    }

    public String getNotificationTitle() {
        return mNotificationTitle;
    }

    public void setNotificationTitle(String notificationTitle) {
        mNotificationTitle = notificationTitle;
    }

    public boolean isForceDownload() {
        return mForceDownload;
    }

    public void setForceDownload(boolean forceDownload) {
        mForceDownload = forceDownload;
    }

    public int getStatus() {
        return mStatus;
    }

    public void setStatus(int status) {
        mStatus = status;
    }

    public void sendOnStart() {
        mStatus = DownloadState.START;
        DownloadManager.getInstance().getPreferences().edit()
                .putString(DownloadManager.KEY_DOWNLOAD_NAME + mId, mFileName)
                .putInt(DownloadManager.KEY_DOWNLOAD_STATUS + mId, mStatus)
                .apply();

        if (mBuilder != null) {
            mBuilder.setSmallIcon(android.R.drawable.stat_sys_download_done)
                    .setContentText("开始下载")
                    .setContentIntent(buildPendingIntent());
            mNotificationManager.notify(mId, mBuilder.build());
        }

        if (mDownloadListener == null) {
            return;
        }
        NetWork.getInstance().handler().post(new Runnable() {
            @Override
            public void run() {
                mDownloadListener.onStart(mId);
            }
        });
    }

    public void sendOnDownload(final long maxProgress, final long progress) {
        mStatus = DownloadState.DOWNLOADING;

        if (firstDownloading) {
            DownloadManager.getInstance().getPreferences().edit()
                    .putString(DownloadManager.KEY_DOWNLOAD_NAME + mId, mFileName)
                    .putInt(DownloadManager.KEY_DOWNLOAD_STATUS + mId, mStatus)
                    .apply();

            if (mBuilder != null) {
                mBuilder.setSmallIcon(android.R.drawable.stat_sys_download)
                        .setContentIntent(buildPendingIntent());
                firstDownloading = false;
            }
        }
        mDelaySendDownload = true;
        NetWork.getInstance().handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                if (mDelaySendDownload) {
                    mDelaySendDownload = false;
                    if (mBuilder != null) {
                        mBuilder.setProgress(1000, (int) (progress * 1000 / maxProgress), false)
                                .setContentText(FileUtil.convertFileLenght(progress).concat("/").concat(FileUtil.convertFileLenght(maxProgress)));
                        mNotificationManager.notify(mId, mBuilder.build());
                    }
                    if (mDownloadListener != null) {
                        mDownloadListener.onDownloading(mId, maxProgress, progress);
                    }
                }
            }
        }, mSendDuration);
    }

    public void sendOnPause() {
        mStatus = DownloadState.PAUSE;
        DownloadManager.getInstance().getPreferences().edit()
                .putString(DownloadManager.KEY_DOWNLOAD_NAME + mId, mFileName)
                .putInt(DownloadManager.KEY_DOWNLOAD_STATUS + mId, mStatus)
                .apply();

        if (mBuilder != null) {
            mBuilder.setSmallIcon(android.R.drawable.stat_sys_download_done)
                    .setContentText("下载暂停")
                    .setContentIntent(buildPendingIntent());
            mNotificationManager.notify(mId, mBuilder.build());
        }
        if (mDownloadListener == null) {
            return;
        }
        NetWork.getInstance().handler().post(new Runnable() {
            @Override
            public void run() {
                mDownloadListener.onPause(mId);
            }
        });
    }

    public void sendOnError(final String error) {
        mStatus = DownloadState.ERROR;
        DownloadManager.getInstance().getPreferences().edit()
                .putString(DownloadManager.KEY_DOWNLOAD_NAME + mId, mFileName)
                .putInt(DownloadManager.KEY_DOWNLOAD_STATUS + mId, mStatus)
                .apply();

        if (mBuilder != null) {
            mBuilder.setSmallIcon(android.R.drawable.stat_sys_download_done)
                    .setContentText("下载错误")
                    .setContentIntent(buildPendingIntent());
            mNotificationManager.notify(mId, mBuilder.build());
        }
        if (mDownloadListener == null) {
            return;
        }
        NetWork.getInstance().handler().post(new Runnable() {
            @Override
            public void run() {
                mDownloadListener.onError(mId, error);
            }
        });
    }

    public void sendOnCancel() {
        mStatus = DownloadState.CANCEL;

        DownloadManager.getInstance().getPreferences().edit()
                .putString(DownloadManager.KEY_DOWNLOAD_NAME + mId, mFileName)
                .putInt(DownloadManager.KEY_DOWNLOAD_STATUS + mId, mStatus)
                .apply();
        if (mDownloadListener == null) {
            return;
        }
        NetWork.getInstance().handler().post(new Runnable() {
            @Override
            public void run() {
                mDownloadListener.onCancel(mId);
            }
        });
    }

    public void sendOnFinish(final String filePath) {
        mStatus = DownloadState.FINISH;
        DownloadManager.getInstance().getPreferences().edit()
                .putString(DownloadManager.KEY_DOWNLOAD_NAME + mId, mFileName)
                .putInt(DownloadManager.KEY_DOWNLOAD_STATUS + mId, mStatus)
                .apply();

        if (mBuilder != null) {
            mBuilder.setSmallIcon(android.R.drawable.stat_sys_download_done)
                    .setContentText("下载成功")
                    .setContentIntent(buildPendingIntent());
            mNotificationManager.notify(mId, mBuilder.build());
        }
        if (mDownloadListener == null) {
            return;
        }
        NetWork.getInstance().handler().post(new Runnable() {
            @Override
            public void run() {
                mDownloadListener.onFinish(mId, filePath);
            }
        });
    }

    public static final class Builder {
        private final int id;
        private final String url;
        private String fileDir;
        private String fileName;
        private DownloadListener mDownloadListener;
        private int sendDuration;
        private String notificationTitle;
        private boolean forceDownload;
        private Context context;

        public Builder(int id, String url) {
            this.id = id;
            this.url = url;
        }

        public Builder setFileDir(String fileDir) {
            this.fileDir = fileDir;
            return this;
        }

        public Builder setFileName(String fileName) {
            this.fileName = fileName;
            return this;
        }

        public Builder setDownloadListener(DownloadListener downloadListener) {
            mDownloadListener = downloadListener;
            return this;
        }

        public Builder setSendDuration(int sendDuration) {
            this.sendDuration = sendDuration;
            return this;
        }

        public Builder setNotificationTitle(String notificationTitle) {
            this.notificationTitle = notificationTitle;
            return this;
        }

        public Builder setForceDownload(boolean forceDownload) {
            this.forceDownload = forceDownload;
            return this;
        }

        public Builder enableNotificationTitle(Context context) {
            this.context = context.getApplicationContext();
            return this;
        }

        public DownloadTask build() {
            return new DownloadTask(this);
        }
    }
}
