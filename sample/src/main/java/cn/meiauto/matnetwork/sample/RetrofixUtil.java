package cn.meiauto.matnetwork.sample;

import java.util.concurrent.TimeUnit;

import cn.meiauto.matrxretrofit.util.CustomGsonConverterFactory;
import cn.meiauto.matrxretrofit.util.RequestInterceptor;
import okhttp3.OkHttpClient;
import retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory;

/**
 * author : LiYang
 * email  : yang.li@nx-engine.com
 * time   : 2018/5/3
 */
public class RetrofixUtil {
    private static final int DEFAULT_TIMEOUT = 8;

    private static retrofit2.Retrofit retrofit;

    public synchronized static retrofit2.Retrofit get() {
        //初始化retrofit的配置
        if (retrofit == null) {
            synchronized (RetrofixUtil.class) {
                OkHttpClient client = new OkHttpClient.Builder()
                        .connectTimeout(DEFAULT_TIMEOUT, TimeUnit.SECONDS)
                        .readTimeout(DEFAULT_TIMEOUT, TimeUnit.SECONDS)
                        .writeTimeout(DEFAULT_TIMEOUT, TimeUnit.SECONDS)
                        .addInterceptor(new RequestInterceptor())
                        .build();

                retrofit = new retrofit2.Retrofit.Builder()
                        .baseUrl("http://dflqtsp.sh.1255612167.clb.myqcloud.com:8005/")
                        .client(client)
                        .addConverterFactory(CustomGsonConverterFactory.create())
                        .addCallAdapterFactory(RxJava2CallAdapterFactory.create())
                        .build();
            }
        }
        return retrofit;
    }
}
