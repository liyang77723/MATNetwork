package cn.meiauto.matrxretrofit.base.observer;

import io.reactivex.Observer;
import io.reactivex.disposables.Disposable;

/**
 * author : LiYang
 * email  : yang.li@nx-engine.com
 * time   : 2018/5/4
 */
public abstract class BaseObserver<T> implements Observer<T> {
    @Override
    public void onSubscribe(Disposable d) {

    }

    @Override
    public void onError(Throwable e) {

    }

    @Override
    public void onComplete() {

    }
}
