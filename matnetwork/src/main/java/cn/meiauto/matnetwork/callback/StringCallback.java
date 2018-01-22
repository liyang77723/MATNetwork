package cn.meiauto.matnetwork.callback;

import android.content.Context;

import java.io.IOException;

import okhttp3.Response;
import okhttp3.ResponseBody;

public abstract class StringCallback extends ErrorHandleCallback<String> {
    public StringCallback(Context context) {
        super(context);
    }

    @Override
    public String responseTo(int id, Response response) throws IOException {
        ResponseBody responseBody = response.body();
        if (responseBody == null) {
            return "";
        } else {
            return responseBody.string();
        }
    }
}