package cn.meiauto.matnetwork.request;

import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.text.TextUtils;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

import cn.meiauto.matnetwork.NetWork;
import cn.meiauto.matnetwork.callback.BaseCallback;
import cn.meiauto.matnetwork.exeception.RequestException;
import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.Request;
import okhttp3.Response;
import okhttp3.ResponseBody;

public class BaseRequest {
    private final String mUrl;
    private final int mId;
    private final Request mRequest;

    private Call mCall;

    BaseRequest(Builder builder) {
        mUrl = builder.mUrl;
        mId = builder.mId;
        mRequest = builder.buildRequest();
    }

    public void cancel() {
        if (mCall.isExecuted() && !mCall.isCanceled()) {
            mCall.cancel();
        }
    }

    //网络请求
    public void execute(final BaseCallback callback) {
        checkBeforeExecute();

        mCall = NetWork.getInstance().client().newCall(mRequest);

        callback.sendBefore(mCall, mId);

        mCall.enqueue(new Callback() {
            @Override
            public void onFailure(@NonNull Call call, @NonNull final IOException e) {
                callback.sendError(call, mId, e);
            }

            @Override
            public void onResponse(@NonNull final Call call, @NonNull final Response response) {
                try {
                    if (call.isCanceled()) {
                        callback.sendAfter(mId);
                        return;
                    }

                    if (!response.isSuccessful()) {
                        callback.sendError(call, mId, new RequestException(String.valueOf(response.code())));
                        callback.sendAfter(mId);
                        return;
                    }

                    Object o = callback.responseTo(mId, response);
                    callback.sendResponse(mId, o);

                } catch (IOException e) {
                    callback.sendError(call, mId, e);
                } finally {

                    callback.sendAfter(mId);
                    ResponseBody responseBody = response.body();
                    if (responseBody != null) {
                        responseBody.close();
                    }
                }
            }
        });
    }

    @Nullable
    public Response execute() {
        checkBeforeExecute();
        mCall = NetWork.getInstance().client().newCall(mRequest);
        try {
            return mCall.execute();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return null;
    }

    private void checkBeforeExecute() {
        if (TextUtils.isEmpty(mUrl)) {
            throw new NullPointerException("Request url is null. Please check the url param when you are creating Builder.");
        }
        if (mRequest == null) {
            throw new NullPointerException("Request is null. If you are creating custom request, you must override Builder's buildRequest method. You can refer to GetRequest.java.");
        }

    }

    @SuppressWarnings("WeakerAccess")
    protected static abstract class Builder<T extends Builder> {
        String mUrl;
        int mId;
        Map<String, Object> mParams;
        Map<String, Object> mHeaders;

        public Builder(@NonNull String url) {
            mUrl = url;
        }

        public T id(int id) {
            mId = id;
            //noinspection unchecked
            return (T) this;
        }

        public T params(@Nullable Map<String, Object> params) {
            mParams = params;
            //noinspection unchecked
            return (T) this;
        }

        public T headers(@Nullable Map<String, Object> headers) {
            mHeaders = headers;
            //noinspection unchecked
            return (T) this;
        }

        public T addParam(@NonNull String key, @NonNull Object value) {
            if (mParams == null) {
                mParams = new HashMap<>();
            }
            mParams.put(key, value);
            //noinspection unchecked
            return (T) this;
        }

        public T addHeader(@NonNull String key, @NonNull Object value) {
            if (mHeaders == null) {
                mHeaders = new HashMap<>();
            }
            mHeaders.put(key, value);
            //noinspection unchecked
            return (T) this;
        }

        public BaseRequest build() {
            return new BaseRequest(this);
        }

        protected abstract Request buildRequest();
    }
}
