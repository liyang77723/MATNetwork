package cn.meiauto.matnetwork.callback;

import android.content.Context;
import android.widget.Toast;

import java.io.FileNotFoundException;
import java.net.SocketTimeoutException;
import java.net.UnknownHostException;

import cn.meiauto.matnetwork.NetWork;
import cn.meiauto.matnetwork.exeception.CancelException;
import cn.meiauto.matnetwork.exeception.RequestException;
import okhttp3.Call;

@SuppressWarnings("WeakerAccess")
public abstract class ErrorHandleCallback<T> extends BaseCallback<T> {
    protected Context mContext;

    public ErrorHandleCallback(Context context) {
        mContext = context;
    }

    @Override
    public void onError(Call call, int id, Exception e) {
        if (e instanceof CancelException) {
            return;
        }
        if (e instanceof UnknownHostException) {
            sendOnNoInternet(id);
            return;
        }

        String desc;
        if (e instanceof SocketTimeoutException) {
            desc = "服务器连接超时，请稍后重试";
        } else if (e instanceof FileNotFoundException) {
            desc = "未开启文件权限，请开启后重试";
        } else if (e instanceof RequestException) {
            desc = "请求失败(" + e.getMessage() + ")";
        } else {
            desc = "服务器忙，请稍后再试";
        }
        e.printStackTrace();
        Toast.makeText(mContext.getApplicationContext(), desc, Toast.LENGTH_SHORT).show();
    }

    protected void sendOnNoInternet(final int id) {
        NetWork.getInstance().handler().post(new Runnable() {
            @Override
            public void run() {
                onNoInternet(id);
            }
        });
    }

    public void onNoInternet(int id) {
        Toast.makeText(mContext.getApplicationContext(), "无网络连接，请稍后重试", Toast.LENGTH_SHORT).show();
    }
}
