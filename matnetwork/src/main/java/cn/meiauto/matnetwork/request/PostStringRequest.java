package cn.meiauto.matnetwork.request;

import android.support.annotation.NonNull;
import android.text.TextUtils;

import java.util.Set;

import cn.meiauto.matutils.EmptyUtil;
import okhttp3.MediaType;
import okhttp3.Request;
import okhttp3.RequestBody;

public class PostStringRequest extends BaseRequest {
    private PostStringRequest(Builder builder) {
        super(builder);
    }

    public static class Builder extends BaseRequest.Builder<Builder> {
        private String mContent;

        public Builder(@NonNull String url) {
            super(url);
        }

        public Builder content(String content) {
            mContent = content;
            return this;
        }

        @Override
        protected Request buildRequest() {
            if (TextUtils.isEmpty(mUrl)) {
                throw new NullPointerException("url is null");
            }

            if (EmptyUtil.isEmpty(mContent)) {
                throw new NullPointerException("content is null");
            }

            Request.Builder builder = new Request.Builder();
            RequestBody requestBody = RequestBody.create(MediaType.parse("application/json; charset=utf-8"), mContent);
            builder.post(requestBody);
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
