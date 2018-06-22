package cn.meiauto.matnetwork.sample;

import android.Manifest;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import com.google.gson.Gson;

import cn.meiauto.matrxretrofit.base.observer.BaseErrorObserver;
import cn.meiauto.matrxretrofit.base.observer.BaseObserver;
import cn.meiauto.matrxretrofit.base.result.BaseResult;
import cn.meiauto.matrxretrofit.base.result.MATResult;
import cn.meiauto.matrxretrofit.util.ComposeUtil;
import cn.meiauto.matrxretrofit.util.CustomGsonConverterFactory;
import cn.meiauto.matrxretrofit.util.ExceptionHandler;
import cn.meiauto.matrxretrofit.util.RequestInterceptor;
import cn.meiauto.matutils.LogUtil;
import okhttp3.MediaType;
import okhttp3.OkHttpClient;
import okhttp3.RequestBody;
import retrofit2.Retrofit;
import retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory;

public class MainActivity extends AppCompatActivity {

    private TextView mTextView1, mTextView2;
    private Retrofit mRetrofit;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        if (ContextCompat.checkSelfPermission(this, Manifest.permission.WRITE_EXTERNAL_STORAGE) == PackageManager.PERMISSION_DENIED) {
            ActivityCompat.requestPermissions(this, new String[]{
                    Manifest.permission.WRITE_EXTERNAL_STORAGE
            }, 0);
        }

        mTextView1 = findViewById(R.id.tv1);
        mTextView2 = findViewById(R.id.tv2);

        OkHttpClient client = new OkHttpClient.Builder()
                .addInterceptor(new RequestInterceptor())
                .build();


        mRetrofit = new Retrofit.Builder()
                .client(client)
                .baseUrl("http://dflqtsp.sh.1255612167.clb.myqcloud.com:8005/")
                .addConverterFactory(CustomGsonConverterFactory.create())
                .addCallAdapterFactory(RxJava2CallAdapterFactory.create())
                .build();
    }

    public void toast(String text) {
        Toast.makeText(getApplicationContext(), text, Toast.LENGTH_SHORT).show();
    }

    public void onGet(View view) {
        mRetrofit.create(ApiGet.class)
                .queryUser()
                .compose(ComposeUtil.<MATResult<ApiGet.QueryUserData>>schedulersTransformer())
                .subscribe(new BaseObserver<MATResult<ApiGet.QueryUserData>>() {
                    @Override
                    public void onNext(MATResult<ApiGet.QueryUserData> queryUserDataBaseDataResult) {
                        super.onNext(queryUserDataBaseDataResult);
                        LogUtil.debug("onNext() called with: queryUserDataBaseDataResult = [" + queryUserDataBaseDataResult + "]");
                    }

                    @Override
                    public void onError(Throwable e) {
                        LogUtil.error(ExceptionHandler.handle(e));
                        toast(ExceptionHandler.handle(e).getErrorMessage());
                    }
                });
    }

    public void onPost(View view) {
        mRetrofit.create(ApiPost.class)
                .login("18dadasd", "adasd", "dasda")
                .compose(ComposeUtil.<MATResult<ApiPost.LoginData>>schedulersTransformer())
                .subscribe(new BaseErrorObserver<MATResult<ApiPost.LoginData>>(MainActivity.this));
    }

    public void onPostString(View view) {
        final RequestBody body = RequestBody.create(
                MediaType.parse("application/json; charset=utf-8"),
                new Gson().toJson(new ApiPostString.UpdateFence("", "", 0, 0, 0, 0, 0, 0, "", "", "")));
        mRetrofit.create(ApiPostString.class)
                .updateFence(body)
                .compose(ComposeUtil.<BaseResult>schedulersTransformer())
                .subscribe(new BaseErrorObserver<BaseResult>(MainActivity.this));
    }
}