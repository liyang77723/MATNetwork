package cn.meiauto.matnetwork.sample;

import android.Manifest;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.os.Environment;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import java.io.File;

import cn.meiauto.matnetwork.callback.FileCallback;
import cn.meiauto.matnetwork.download.DownloadListener;
import cn.meiauto.matnetwork.download.DownloadManager;
import cn.meiauto.matnetwork.download.DownloadState;
import cn.meiauto.matnetwork.download.DownloadStatus;
import cn.meiauto.matnetwork.download.DownloadTask;
import cn.meiauto.matnetwork.download.service.DownloadMessager;
import cn.meiauto.matnetwork.request.GetRequest;
import cn.meiauto.matutils.FileUtil;
import cn.meiauto.matutils.LogUtil;

public class MainActivity extends AppCompatActivity implements DownloadListener {

    private TextView mTextView1, mTextView2;
    private DownloadTask mDownloadTask;
    private Button mButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        if (ContextCompat.checkSelfPermission(this, Manifest.permission.WRITE_EXTERNAL_STORAGE) == PackageManager.PERMISSION_DENIED) {
            ActivityCompat.requestPermissions(this, new String[]{
                    Manifest.permission.WRITE_EXTERNAL_STORAGE
            }, 0);
        }

        mButton = findViewById(R.id.btn_download);
        mTextView1 = findViewById(R.id.tv1);
        mTextView2 = findViewById(R.id.tv2);

        DownloadManager.getInstance().init(this);
        @DownloadStatus int status = DownloadManager.getInstance().getSavedStatus(Constants.DOWNLOAD_APK);
        if (status == DownloadState.PAUSE) {
            mButton.setText("继续下载");
        } else if (status == DownloadState.FINISH) {
            mButton.setText("点击打开");
        } else {
            mButton.setText("下载");
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        DownloadManager.getInstance().stopAllRunningTasks();
    }

    DownloadMessager mDownloadMessager = new DownloadMessager(this);

    public void requestBaidu(View view) {
//        final String url = "http://download.3g.joy.cn/video/236/60236853/1450837945724_hd.mp4";
        //        String url = "https://pro-app-qn.fir.im/436647c196e8733a58daaabe62242e5873ec2017.apk?attname=%E6%99%BA%E9%80%9F%E5%90%8C%E8%A1%8Cv1.0.3-25.apk_1.0.3.apk&e=1502870410&token=LOvmia8oXF4xnLh0IdH05XMYpH6ENHNpARlmPc-T:KrgDFvNGJuNHSzn1veToEFiTTpM=";
//        final String dir = Environment.getExternalStorageDirectory().getAbsolutePath();
//        DownloadService.startDownload(this, 0, url, dir, "11111", true, "111111");
//        new Handler().postDelayed(new Runnable() {
//            @Override
//            public void run() {
//                DownloadService.startDownload(
//                        MainActivity.this
//                        , 1
//                        , "https://pro-app-qn.fir.im/436647c196e8733a58daaabe62242e5873ec2017.apk?attname=%E6%99%BA%E9%80%9F%E5%90%8C%E8%A1%8Cv1.0.3-25.apk_1.0.3.apk&e=1502870410&token=LOvmia8oXF4xnLh0IdH05XMYpH6ENHNpARlmPc-T:KrgDFvNGJuNHSzn1veToEFiTTpM="
//                        , dir, "aa.apk", true, "22222");
//            }
//        }, 2000);

        File dirFile = getExternalFilesDir("download");
        if (dirFile == null) {
            dirFile = Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DOWNLOADS);
        }

        new GetRequest.Builder("http://download.3g.joy.cn/video/236/60236853/1450837945724_hd.mp4").id(111).build().execute(
                new FileCallback(MainActivity.this, dirFile.getAbsolutePath(), "123.mp4", true) {
                    @Override
                    public void inProgress(int flag, int maxProgress, int progress) {
                        LogUtil.warn("inProgress flag = " + flag + " maxProgress = " + maxProgress + " progress = " + progress);
                    }

                    @Override
                    public void onResponse(int flag, File result) {
                        LogUtil.debug("onResponse flag = " + flag + " file = " + result.getAbsolutePath());
                    }
                });

//        DownloadTask task = DownloadManager.getInstance().getDownloadTask(0);
//        if (task != null) {
//            LogUtil.debug("task status " + task.getStatus());
//        }
//        String url = "http://download.3g.joy.cn/video/236/60236853/1450837945724_hd.mp4";
//        mDownloadTask = new DownloadTask.Builder(0, url).setDownloadListener(this).setNotificationTitle("MP4下载").setFileName("ads.mp4").build();
//        DownloadManager.getInstance().download(mDownloadTask);
//        new Handler().postDelayed(new Runnable() {
//            @Override
//            public void run() {
//                String u = "http://download.3g.joy.cn/video/236/60236853/1450837945724_hd.mp4";
//                DownloadManager.getInstance().download(new DownloadTask.Builder(1, u).setForceDownload(true).enableNotificationTitle(MainActivity.this).setDownloadListener(MainActivity.this).build());
//    }
//        }, 2000);

    }

    @Override
    public void onStart(int id) {
        LogUtil.debug("任务(" + id + ")" + "开始");
    }

    @Override
    public void onDownloading(int id, long totalLength, long downloadLength) {
        if (id == 0) {
            mTextView1.setText("任务(" + id + ")" + "：totalLength=" + FileUtil.convertFileLenght(totalLength) + " downloadLength=" + FileUtil.convertFileLenght(downloadLength));
        } else {
            mTextView2.append("任务(" + id + ")" + "：maxProgress=" + FileUtil.convertFileLenght(totalLength) + " progress=" + FileUtil.convertFileLenght(downloadLength) + "\n");
        }
    }

    @Override
    public void onPause(int id) {
        LogUtil.debug("任务(" + id + ")" + "暂停");
    }

    @Override
    public void onError(int id, String reason) {
        LogUtil.error("任务(" + id + ")" + "错误:" + reason);
    }

    @Override
    public void onCancel(int id) {
        LogUtil.debug("任务(" + id + ")" + "取消");
    }

    @Override
    public void onFinish(int id, String filePath) {
        LogUtil.debug("任务(" + id + ")" + "完成:" + filePath);
    }
}
