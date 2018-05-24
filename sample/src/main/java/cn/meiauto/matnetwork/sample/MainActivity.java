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

import cn.meiauto.matrxretrofit.base.observer.BaseObserver;
import cn.meiauto.matrxretrofit.base.result.BaseDataResult;
import cn.meiauto.matrxretrofit.util.ComposeUtil;
import cn.meiauto.matrxretrofit.util.CustomGsonConverterFactory;
import cn.meiauto.matrxretrofit.util.ExceptionHandler;
import cn.meiauto.matrxretrofit.util.RequestInterceptor;
import cn.meiauto.matutils.LogUtil;
import okhttp3.OkHttpClient;
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

    public void requestBaidu(View view) {
        mTextView1.setText("start");
        mTextView2.setText("");

        mRetrofit.create(ApiGet.class)
                .queryUser()
                .compose(ComposeUtil.<BaseDataResult<ApiGet.QueryUserData>>schedulersTransformer())
                .subscribe(new BaseObserver<BaseDataResult<ApiGet.QueryUserData>>() {
                    @Override
                    public void onNext(BaseDataResult<ApiGet.QueryUserData> queryUserDataBaseDataResult) {
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

    public void toast(String text) {
        Toast.makeText(getApplicationContext(), text, Toast.LENGTH_SHORT).show();
    }
}