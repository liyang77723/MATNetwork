package cn.meiauto.matnetwork.abstr.request;

import android.text.TextUtils;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

import cn.meiauto.matnetwork.NetWork;
import cn.meiauto.matnetwork.callback.BaseCallback;
import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.Request;
import okhttp3.Response;

/**
 * author : LiYang
 * email  : yang.li@nx-engine.com
 * time   : 2018/2/1
 */
public class BaseRequest {
    private String basePath;
    private String path;
    private String url;
    private Map<String, Object> headers;
    private Map<String, Object> params;

    private Request mRequest;
    private Call mCall;

    BaseRequest(Builder builder) {
        basePath = builder.basePath;
        path = builder.path;
        url = builder.url;
        headers = builder.headers;
        params = builder.params;

        initUrl();
        initRequest(builder);
    }

    private void initRequest(Builder builder) {
        mRequest = builder.buildRequest();
        if (mRequest == null) {
            throw new NullPointerException("mRequest is null");
        }
    }

    private void initUrl() {
        if (TextUtils.isEmpty(url)) {
            url = basePath + path;
        }
        if (TextUtils.isEmpty(url)) {
            throw new NullPointerException("request url is null");
        }
    }

    public void enqueue(final BaseCallback callback) {
        buildCall();

        mCall.enqueue(new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {
            }

            @Override
            public void onResponse(Call call, Response response) throws IOException {

            }
        });
    }

    public Response execute() {
        buildCall();

        try {
            return mCall.execute();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return null;
    }

    private void buildCall() {
        mCall = NetWork.getInstance().client().newCall(mRequest);
    }

    @SuppressWarnings({"WeakerAccess", "unchecked"})
    protected abstract class Builder<CHILD_BUILDER extends Builder> {
        protected String basePath;
        protected String path;
        protected String url;
        protected Map<String, Object> headers;
        protected Map<String, Object> params;

        protected Builder() {
        }

        /**
         * a complete url like this:
         * <scheme>://<host>:<port><path>?<query>#<fragment>
         *
         * @param basePath <scheme>://<host>:<port>
         */
        public CHILD_BUILDER basePath(String basePath) {
            this.basePath = basePath;
            return (CHILD_BUILDER) this;
        }

        /**
         * a complete url like this:
         * <scheme>://<host>:<port><path>?<query>#<fragment>
         *
         * @param path <path>
         */
        public CHILD_BUILDER path(String path) {
            this.path = path;
            return (CHILD_BUILDER) this;
        }

        /**
         * a complete url like this:
         * <scheme>://<host>:<port><path>?<query>#<fragment>
         *
         * @param url <scheme>://<host>:<port><path>
         */
        public CHILD_BUILDER url(String url) {
            this.url = url;
            return (CHILD_BUILDER) this;
        }

        /**
         * set request headers
         *
         * @param headers request headers
         */
        public CHILD_BUILDER headers(Map<String, Object> headers) {
            this.headers = headers;
            return (CHILD_BUILDER) this;
        }

        /**
         * add request header
         *
         * @param key   header's key
         * @param value header's value
         */
        public CHILD_BUILDER addHeader(String key, Object value) {
            if (headers == null) {
                headers = new HashMap<>();
            }
            headers.put(key, value);
            return (CHILD_BUILDER) this;
        }

        /**
         * set request query params
         *
         * @param params request query params
         */
        public CHILD_BUILDER params(Map<String, Object> params) {
            this.params = params;
            return (CHILD_BUILDER) this;
        }

        /**
         * add request query param
         *
         * @param key   param's key
         * @param value param's value
         */
        public CHILD_BUILDER addParams(String key, Object value) {
            if (params == null) {
                params = new HashMap<>();
            }
            params.put(key, value);
            return (CHILD_BUILDER) this;
        }

        public BaseRequest build() {
            return new BaseRequest(this);
        }

        protected abstract Request buildRequest();
    }
}
