package cn.meiauto.matnetwork.abstr.builder;

import java.util.HashMap;
import java.util.Map;

import cn.meiauto.matnetwork.abstr.RequestCall;

/**
 * author : LiYang
 * email  : yang.li@nx-engine.com
 * time   : 2018/2/1
 */
@SuppressWarnings("unchecked")
public abstract class BaseBuilder<CHILD_BUILDER extends BaseBuilder> {
    protected String basePath;
    protected String path;
    protected String url;
    protected Map<String, Object> headers;
    protected Map<String, Object> params;

    public BaseBuilder() {
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

    public abstract RequestCall build();
}
