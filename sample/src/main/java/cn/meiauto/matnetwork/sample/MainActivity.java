package cn.meiauto.matnetwork.sample;

import android.Manifest;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.text.TextUtils;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import com.google.gson.Gson;

import java.util.Locale;
import java.util.concurrent.TimeUnit;

import cn.meiauto.matnetwork.Result;
import cn.meiauto.matnetwork.util.ComposeUtil;
import cn.meiauto.matnetwork.util.CustomGsonConverterFactory;
import cn.meiauto.matnetwork.util.RequestInterceptor;
import cn.meiauto.matutils.LogUtil;
import io.reactivex.Observable;
import io.reactivex.ObservableSource;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.Disposable;
import io.reactivex.functions.Consumer;
import io.reactivex.functions.Function;
import io.reactivex.observers.DefaultObserver;
import io.reactivex.schedulers.Schedulers;
import okhttp3.MediaType;
import okhttp3.OkHttpClient;
import okhttp3.RequestBody;
import okhttp3.logging.HttpLoggingInterceptor;
import retrofit2.Retrofit;
import retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory;

public class MainActivity extends AppCompatActivity {

    private TextView mTextView1, mTextView2;
    private RequestApi mRequestApi;

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

        HttpLoggingInterceptor.Level level = HttpLoggingInterceptor.Level.HEADERS;
//        // 新建log拦截器
//        HttpLoggingInterceptor loggingInterceptor = new HttpLoggingInterceptor();
//        loggingInterceptor.setLevel(level);

        OkHttpClient client = new OkHttpClient.Builder()
                .addInterceptor(new RequestInterceptor())
                .build();


        Retrofit mRetrofit = new Retrofit.Builder()
                .client(client)
                .baseUrl("http://172.20.0.6:8091/")
                .addConverterFactory(CustomGsonConverterFactory.create())
                .addCallAdapterFactory(RxJava2CallAdapterFactory.create())
                .build();

        mRequestApi = mRetrofit.create(RequestApi.class);

    }

    String[] sss = {"1234", "4321"};
    int oneOrTwo;

    public void requestBaidu(View view) {
        mTextView1.setText("start");
        mTextView2.setText("");

//        mRequestApi
//                .instance()
//                .subscribeOn(Schedulers.io())
//                .observeOn(AndroidSchedulers.mainThread())
//                .doOnSubscribe(new Consumer<Disposable>() {
//                    @Override
//                    public void accept(Disposable disposable) throws Exception {
//                        mTextView1.setText("loading...");
//                    }
//                })
//                .subscribe(new DefaultObserver<Result>() {
//                    @Override
//                    public void onNext(Result response) {
//                        mTextView2.setText(response.toString());
//                    }
//
//                    @Override
//                    public void onFailed(Throwable e) {
//                        mTextView1.setText(e.toString());
////                        mTextView1.setText(ExceptionEngine.handleException(e).toString());
//                    }
//
//                    @Override
//                    public void onComplete() {
//                        mTextView1.setText("over");
//                    }
//                });

//        mRequestApi
//                .checkPin("18935028851", sss[oneOrTwo++ % 2])
//                .compose(ComposeUtil.<Result>schedulersTransformer())
//                .doOnSubscribe(new Consumer<Disposable>() {
//                    @Override
//                    public void accept(Disposable disposable) throws Exception {
//                        mTextView1.setText("loading...");
//                    }
//                })
//                .subscribe(new DefaultObserver<Result>() {
//                    @Override
//                    public void onNext(Result response) {
//                        mTextView2.setText(response.toString());
//                    }
//
//                    @Override
//                    public void onError(Throwable e) {
//                        mTextView2.setText(e.toString());
//                        mTextView1.setText("over");
//                    }
//
//                    @Override
//                    public void onComplete() {
//                        mTextView1.setText("over");
//                    }
//                });


        final RequestBody body = RequestBody.create(
                MediaType.parse("application/json; charset=utf-8"),
                new Gson().toJson(new RCSendBean(
                        "00122"
                        , "Tbox_Vin_00000001"
                        , "VEHICLE"
                        , "1234"
                        , "0,0,0,0"
                )));

        mRequestApi.sendCommand("4601061756", body)
                .compose(ComposeUtil.<Result<SendCommandData>>schedulersTransformer())
                .doOnSubscribe(new Consumer<Disposable>() {
                    @Override
                    public void accept(Disposable disposable) throws Exception {
                        mTextView1.setText("loading...");
                    }
                })
                .subscribe(new DefaultObserver<Result>() {
                    @Override
                    public void onNext(Result response) {
                        mTextView2.setText(response.toString());
                    }

                    @Override
                    public void onError(Throwable e) {
                        LogUtil.error(e);
                        mTextView1.setText(ExceptionEngine.handleException(e).toString());
                    }

                    @Override
                    public void onComplete() {
                        mTextView1.setText("over");
                    }
                });

        Observable<Result<RCQueryBean>> queryCommand = mRequestApi.queryCommand("473");
        queryCommand
                .map(new Function<Result<RCQueryBean>, RCQueryBean>() {
                    @Override
                    public RCQueryBean apply(Result<RCQueryBean> rcQueryBeanResult) throws Exception {
                        if (TextUtils.equals(rcQueryBeanResult.getStatus(), "SUCCEED")) {
                            return rcQueryBeanResult.getData();
                        } else {
                            throw new NullPointerException(rcQueryBeanResult.toString());
                        }
                    }
                })
                .repeatWhen(new Function<Observable<Object>, ObservableSource<?>>() {
                    @Override
                    public ObservableSource<?> apply(Observable<Object> objectObservable) throws Exception {
                        return objectObservable.flatMap(new Function<Object, ObservableSource<?>>() {
                            @Override
                            public ObservableSource<?> apply(Object o) throws Exception {
                                if (oneOrTwo > 4) {
                                    // 此处选择发送onError事件以结束轮询，因为可触发下游观察者的onError（）方法回调
//                                    return Observable.error(new NullPointerException("over"));
                                    return Observable.error(new Throwable("wa ha ha"));
                                }
                                // 若轮询次数＜4次，则发送1Next事件以继续轮询
                                // 注：此处加入了delay操作符，作用 = 延迟一段时间发送（此处设置 = 2s），以实现轮询间间隔设置
                                return Observable.just(1).delay(500, TimeUnit.MILLISECONDS);
                            }
                        });
                    }
                })
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .doOnSubscribe(new Consumer<Disposable>() {
                    @Override
                    public void accept(Disposable disposable) throws Exception {
                        mTextView1.setText("loading...");
                    }
                })
                .subscribe(new DefaultObserver<RCQueryBean>() {
                    @Override
                    public void onNext(RCQueryBean rcQueryBean) {
                        mTextView2.append(String.format(Locale.getDefault(), "%02d", ++oneOrTwo) + ". " + rcQueryBean.getStatusX() + "\n");
                    }

                    @Override
                    public void onError(Throwable e) {
                        mTextView1.setText("error");
                        LogUtil.error(e);
                        e.printStackTrace();
                    }

                    @Override
                    public void onComplete() {
                        mTextView1.setText("over");
                    }
                });
    }

//        Observable
//                .intervalRange(0, 5, 0, 1, TimeUnit.SECONDS)
//                .observeOn(AndroidSchedulers.mainThread())
//                .doOnNext(new Consumer<Long>() {
//                    @Override
//                    public void accept(Long aLong) throws Exception {
//                        mRequestApi.sendCommand("4601061756", body)
//                                .subscribeOn(Schedulers.io())
//                                .observeOn(AndroidSchedulers.mainThread())
//                                .doOnSubscribe(new Consumer<Disposable>() {
//                                    @Override
//                                    public void accept(Disposable disposable) throws Exception {
//                                        mTextView1.setText("loading...");
//                                    }
//                                })
//                                .subscribe(new DefaultObserver<Result<SendCommandData>>() {
//                                    @Override
//                                    public void onNext(Result<SendCommandData> sendCommandDataResult) {
//                                        mTextView2.append(++oneOrTwo + ". " + sendCommandDataResult.toString() + "\n");
//                                    }
//
//                                    @Override
//                                    public void onFailed(Throwable e) {
//                                        mTextView1.setText("error");
//                                        LogUtil.error(e);
//                                        e.printStackTrace();
//                                    }
//
//                                    @Override
//                                    public void onComplete() {
//                                        mTextView1.setText("over");
//                                    }
//                                });
//                    }
//                }).subscribe(new Consumer<Long>() {
//            @Override
//            public void accept(Long aLong) throws Exception {
//
//            }
//        }, new Consumer<Throwable>() {
//            @Override
//            public void accept(Throwable throwable) throws Exception {
//
//            }
//        }, new Action() {
//            @Override
//            public void run() throws Exception {
//
//            }
//        });
//    }

    public void toast(String text) {
        Toast.makeText(getApplicationContext(), text, Toast.LENGTH_SHORT).show();
    }
}