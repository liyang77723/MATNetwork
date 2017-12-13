package cn.meiauto.matnetwork.callback;

import android.content.Context;
import android.widget.Toast;

import java.io.FileNotFoundException;
import java.net.SocketTimeoutException;
import java.net.UnknownHostException;

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
            e.printStackTrace();
            return;
        }
        String desc;
        if (e instanceof SocketTimeoutException) {
            desc = "服务器连接超时，请稍后重试";
        } else if (e instanceof UnknownHostException) {
            desc = "无网络，请打开后重试";
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
}
