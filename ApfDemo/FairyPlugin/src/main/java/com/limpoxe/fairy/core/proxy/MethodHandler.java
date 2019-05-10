package com.limpoxe.fairy.core.proxy;

import android.util.Log;

import java.lang.reflect.InvocationHandler;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;

public class MethodHandler extends MethodDelegate implements InvocationHandler {

    private final Object mTarget;

    private final MethodDelegate mDelegate;

    public MethodHandler(Object target, MethodDelegate delegate) {
        this.mTarget = target;
        this.mDelegate = delegate;
    }

    @Override
    public Object beforeInvoke(Object target, Method method, Object[] args) {
        return mDelegate.beforeInvoke(target, method, args);
    }

    @Override
    public Object afterInvoke(Object target, Method method, Object[] args, Object beforeInvoke, Object invokeResult) {
        return mDelegate.afterInvoke(target, method, args, beforeInvoke, invokeResult);
    }

    @Override
    public Object invoke(Object proxy, Method method, Object[] args) throws InvocationTargetException, IllegalAccessException {
        Object before = null;
        try {
            before = beforeInvoke(mTarget, method, args);
        } catch (Throwable t) {
            Log.e("APF", "MethodHandler.invoke(beforeInvoke) cache exception and log here:");
            Log.e("APF", "|" + t.getMessage());
        }

        Object invokeResult = null;
        if (before == null) {
            if (!method.isAccessible()) {
                method.setAccessible(true);
            }
            try {
                invokeResult = method.invoke(mTarget, args);
            } catch (Throwable t) {
                Log.e("APF", "MethodHandler.invoke(invoke) cache exception and log here:");
                Log.e("APF", "|" + t.getMessage());
            }
        }

        Object after = null;
        try {
            after = afterInvoke(mTarget, method, args, before, invokeResult);
        } catch (Throwable t) {
            Log.e("APF", "MethodHandler.invoke(afterInvoke) cache exception and log here:");
            Log.e("APF", "|" + t.getMessage());
        }
        return after;
    }

}
