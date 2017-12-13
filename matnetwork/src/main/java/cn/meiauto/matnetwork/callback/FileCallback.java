package cn.meiauto.matnetwork.callback;

import android.app.NotificationManager;
import android.content.Context;
import android.support.annotation.Nullable;
import android.support.v4.app.NotificationCompat;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;

import cn.meiauto.matnetwork.NetWork;
import cn.meiauto.matutils.FileUtil;
import okhttp3.Response;
import okhttp3.ResponseBody;

@SuppressWarnings({"unused", "WeakerAccess"})
public abstract class FileCallback extends ErrorHandleCallback<File> {

    private static final int MAX_PROGRESS = 1000;

    private final boolean mForceDownload;
    private String destDir;
    private String destName;
    private NotificationManager mNotifyManager;
    private NotificationCompat.Builder mBuilder;
    private boolean isDelayPost;

    public FileCallback(Context context, String destDir, String destName) {
        this(context, destDir, destName, false);
    }

    public FileCallback(Context context, String destDir, String destName, boolean forceDownload) {
        super(context);
        this.destDir = destDir;
        this.destName = destName;
        mForceDownload = forceDownload;
    }

    @Override
    @Nullable
    public File responseTo(final int flag, Response response) throws IOException {

        ResponseBody requestBody = response.body();
        if (requestBody == null) {
            return null;
        }

        final long total = requestBody.contentLength();

        File dirFile = FileUtil.makeDirsIfNotExist(destDir);

        File file = new File(dirFile, destName);
        //非强制下载，文件存在且大小相同
        if (!mForceDownload && FileUtil.isExist(file, total)) {
            return file;
        }

        InputStream is = null;
        byte[] buf = new byte[1024];
        int len;
        FileOutputStream fos = null;

        try {
            is = requestBody.byteStream();

            long sum = 0;

            fos = new FileOutputStream(file);
            while ((len = is.read(buf)) != -1) {
                sum += len;
                fos.write(buf, 0, len);
                if (total == sum) {
                    isDelayPost = false;
                    NetWork.getInstance().handler().post(new Runnable() {
                        @Override
                        public void run() {
                            inProgress(flag, MAX_PROGRESS, MAX_PROGRESS);
                        }
                    });
                    break;
                } else {
                    if (!isDelayPost) {
                        isDelayPost = true;
                        sendDelayProgress(flag, (int) (sum * MAX_PROGRESS * 1F / total));
                    }
                }
            }
            fos.flush();
            return file;
        } finally {
            try {
                requestBody.close();
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

    public abstract void inProgress(int flag, int maxProgress, int progress);

    private void sendDelayProgress(final int flag, final int progress) {
//        LogUtil.debug("【request onFailed】" + flag + "\n errorCode=" + errorCode + " errorMessage=" + errorMessage);
        NetWork.getInstance().handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                if (isDelayPost) {
                    isDelayPost = false;
                    inProgress(flag, MAX_PROGRESS, progress);
                }
            }
        }, 233);
    }
}
