package cn.meiauto.matnetwork.request;

import android.support.annotation.NonNull;

import java.util.Set;

import cn.meiauto.matutils.EmptyUtil;
import okhttp3.FormBody;
import okhttp3.Request;

public class PostRequest extends BaseRequest {

    private PostRequest(Builder builder) {
        super(builder);
    }

    public static class Builder extends BaseRequest.Builder<Builder> {
        public Builder(@NonNull String url) {
            super(url);
        }

        @Override
        protected Request buildRequest() {
            Request.Builder builder = new Request.Builder();

            FormBody.Builder paramsBuilder = new FormBody.Builder();
            if (!EmptyUtil.isEmpty(mParams)) {
                Set<String> ketSet = mParams.keySet();
                for (String key : ketSet) {
                    paramsBuilder.add(key, String.valueOf(mParams.get(key)));
                }
            }
            builder.post(paramsBuilder.build());
            if (!EmptyUtil.isEmpty(mHeaders)) {
                Set<String> ketSet = mHeaders.keySet();
                for (String key : ketSet) {
                    builder.addHeader(key, String.valueOf(mHeaders.get(key)));
                }
            }

            builder.url(mUrl).tag(mId);
            return builder.build();
        }
    }
}
