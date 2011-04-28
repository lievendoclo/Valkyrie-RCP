package org.valkyriercp.util;

import java.util.concurrent.Callable;

public abstract class CachedCallable<T> implements Callable<T> {
    private T cached;

    public T call() throws Exception {
        if(cached == null)
            cached = doCall();
        return cached;
    }

    public T safeCall() {
        try {
            return call();
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    protected abstract T doCall();
}
