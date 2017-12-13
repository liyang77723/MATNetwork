package cn.meiauto.matnetwork.callback;

import java.io.IOException;

import okhttp3.Call;
import okhttp3.Request;
import okhttp3.Response;

public abstract class BaseCallback<T> {

    public void onBefore(Request request, int flag) {
    }

    public void onAfter(int flag) {
    }

    public abstract void onError(Call call, int flag, Exception e);

    public abstract void onResponse(int flag, T result);

    public abstract T responseTo(int flag, Response response) throws IOException;
}