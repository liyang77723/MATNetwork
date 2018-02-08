package cn.meiauto.matnetwork.util;

import io.reactivex.Observable;
import io.reactivex.ObservableSource;
import io.reactivex.ObservableTransformer;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.schedulers.Schedulers;

/**
 * author : LiYang
 * email  : yang.li@nx-engine.com
 * time   : 2018/2/8
 */
public class ComposeUtil {

    public static <T> ObservableTransformer<T, T> schedulersTransformer() {
        ObservableTransformer schedulersTransformer = new ObservableTransformer() {
            @Override
            public ObservableSource apply(Observable upstream) {
                return upstream
                        .subscribeOn(Schedulers.io())
                        .observeOn(AndroidSchedulers.mainThread());
            }
        };
        //noinspection unchecked
        return (ObservableTransformer<T, T>) schedulersTransformer;
    }
}
