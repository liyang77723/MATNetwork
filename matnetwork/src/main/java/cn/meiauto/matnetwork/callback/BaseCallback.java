package cn.meiauto.matnetwork.callback;

import java.io.IOException;

import cn.meiauto.matnetwork.NetWork;
import okhttp3.Call;
import okhttp3.Response;

public abstract class BaseCallback<T> {

    public boolean onBefore(Call call, int flag) {
        return false;
    }

    public void onAfter(int flag) {
    }

    public abstract void onError(Call call, int flag, Exception e);

    public abstract void onResponse(int flag, T result);

    public abstract T responseTo(int flag, Response response) throws IOException;


    public void sendBefore(final Call call, final int id) {
        NetWork.getInstance().handler().post(new Runnable() {
            @Override
            public void run() {
                onBefore(call, id);
            }
        });
    }

    public void sendAfter(final int id) {
        NetWork.getInstance().handler().post(new Runnable() {
            @Override
            public void run() {
                onAfter(id);
            }
        });
    }

    public void sendResponse(final int id, final Object object) {
        NetWork.getInstance().handler().post(new Runnable() {
            @Override
            public void run() {
                //noinspection unchecked
                onResponse(id, (T) object);
            }
        });
    }

    public void sendError(final Call call, final int id, final Exception e) {
        NetWork.getInstance().handler().post(new Runnable() {
            @Override
            public void run() {
                onError(call, id, e);
            }
        });
    }
}