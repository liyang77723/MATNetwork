package cn.meiauto.matnetwork.callback;

import android.content.Context;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;

import cn.meiauto.matnetwork.NetWork;
import cn.meiauto.matutils.LogUtil;
import okhttp3.Response;
import okhttp3.ResponseBody;

//TODO toast 根据 id
@SuppressWarnings({"WeakerAccess", "unused"})
public class SimpleCallback extends ErrorHandleCallback<String> {

    public SimpleCallback(Context context) {
        super(context);
    }

    @Override
    public void onResponse(int id, String json) {
        try {
            JSONObject jsonObject = new JSONObject(json);
            String status = jsonObject.optString("status");
            if (status.equals(NetWork.STATUS_SUCCCED)) {
                sendSucceed(id, json);
            } else {
                String errorCode = jsonObject.optString("errorCode");
                String errorMessage = jsonObject.optString("errorMessage");
                sendFailed(id, errorCode, errorMessage);
            }
        } catch (JSONException e) {
            LogUtil.error("json转换错误", e);
        }
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

    public void onFailed(int flag, String errorCode, String errorMessage) {
    }

    public void onSucceed(int flag, String json) {
    }

    protected void sendSucceed(final int flag, final String json) {
//        LogUtil.debug("【request onSucceed】" + id + "\n" + json);
        NetWork.getInstance().handler().post(new Runnable() {
            @Override
            public void run() {
                onSucceed(flag, json);
            }
        });
    }

    protected void sendFailed(final int flag, final String errorCode, final String errorMessage) {
//        LogUtil.debug("【request onFailed】" + id + "\n errorCode=" + errorCode + " errorMessage=" + errorMessage);
        NetWork.getInstance().handler().post(new Runnable() {
            @Override
            public void run() {
                onFailed(flag, errorCode, errorMessage);
            }
        });
    }
}