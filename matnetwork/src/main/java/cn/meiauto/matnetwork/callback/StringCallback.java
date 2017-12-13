package cn.meiauto.matnetwork.callback;

import java.io.IOException;

import okhttp3.Response;
import okhttp3.ResponseBody;

public abstract class StringCallback extends BaseCallback<String> {
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