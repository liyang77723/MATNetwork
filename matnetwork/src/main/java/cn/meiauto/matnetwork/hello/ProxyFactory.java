package cn.meiauto.matnetwork.hello;

import java.lang.reflect.InvocationHandler;
import java.lang.reflect.Method;
import java.lang.reflect.Proxy;

/**
 * author : LiYang
 * email  : yang.li@nx-engine.com
 * time   : 2018/3/21
 */
public class ProxyFactory {
    private Object mTarget;

    public ProxyFactory(Object target) {
        mTarget = target;
    }

    public Object getInstance() {
        return Proxy.newProxyInstance(
                mTarget.getClass().getClassLoader(),
                mTarget.getClass().getInterfaces(),
                new InvocationHandler() {
                    @Override
                    public Object invoke(Object proxy, Method method, Object[] args) throws Throwable {
                        return method.invoke(mTarget, args);
                    }
                }
        );
    }
}
