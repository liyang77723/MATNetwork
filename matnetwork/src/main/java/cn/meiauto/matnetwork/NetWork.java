package cn.meiauto.matnetwork;

import android.content.Context;
import android.os.Handler;
import android.os.Looper;

import cn.meiauto.matnetwork.request.GetRequest;
import cn.meiauto.matnetwork.request.PostRequest;
import cn.meiauto.matnetwork.request.PostStringRequest;
import okhttp3.OkHttpClient;

public class NetWork {

    public static final String STATUS_SUCCCED = "SUCCEED";
    public static final String STATUS_FAILED = "FAILED";

    private OkHttpClient mOkHttpClient;
    private Handler mHandler;//ui线程

    private static NetWork sNetWork;

    public OkHttpClient client() {
        return mOkHttpClient;
    }

    public Handler handler() {
        return mHandler;
    }

    private NetWork(OkHttpClient okHttpClient) {
        if (okHttpClient == null) {
            mOkHttpClient = new OkHttpClient();
        } else {
            mOkHttpClient = okHttpClient;
        }
        mHandler = new Handler(Looper.getMainLooper());
    }

    public static void initClient(OkHttpClient okHttpClient) {
        if (sNetWork == null) {
            synchronized (NetWork.class) {
                if (sNetWork == null) {
                    sNetWork = new NetWork(okHttpClient);
                }
            }
        }
    }

    public static NetWork getInstance() {
        if (sNetWork == null) {
            sNetWork = new NetWork(null);
        }
        return sNetWork;
    }

    public static GetRequest.Builder get(String url) {
        return new GetRequest.Builder(url);
    }

    public static PostRequest.Builder post(String url) {
        return new PostRequest.Builder(url);
    }

    public static PostStringRequest.Builder postString(String url) {
        return new PostStringRequest.Builder(url);
    }

    public static void download(Context context, String filePath, String fileName) {

    }
}
