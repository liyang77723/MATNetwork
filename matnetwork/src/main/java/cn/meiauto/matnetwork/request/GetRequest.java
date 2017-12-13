package cn.meiauto.matnetwork.request;

import android.net.Uri;
import android.support.annotation.NonNull;
import android.text.TextUtils;

import java.util.Set;

import cn.meiauto.matutils.EmptyUtil;
import okhttp3.Request;

public class GetRequest extends BaseRequest {

    protected GetRequest(Builder builder) {
        super(builder);
    }

    public static class Builder extends BaseRequest.Builder<GetRequest.Builder> {

        public Builder(@NonNull String url) {
            super(url);
        }

        @Override
        protected Request buildRequest() {
            if (TextUtils.isEmpty(mUrl)) {
                throw new NullPointerException("request url is null");
            }

            Request.Builder builder = new Request.Builder().get();

            String url = mUrl;
            //url
            if (!EmptyUtil.isEmpty(mParams)) {
                Uri.Builder urlBuilder = Uri.parse(mUrl).buildUpon();
                Set<String> keys = mParams.keySet();
                for (String key : keys) {
                    urlBuilder.appendQueryParameter(key, String.valueOf(mParams.get(key)));
                }
                url = urlBuilder.build().toString();
            }
            builder.url(url);

            //headers
            if (!EmptyUtil.isEmpty(mHeaders)) {
                Set<String> ketSet = mHeaders.keySet();
                for (String key : ketSet) {
                    builder.addHeader(key, String.valueOf(mHeaders.get(key)));
                }
            }
            builder.tag(mId);
            return builder.build();
        }
    }
}