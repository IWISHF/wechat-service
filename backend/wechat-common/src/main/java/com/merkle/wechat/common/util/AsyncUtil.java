package com.merkle.wechat.common.util;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.CompletableFuture;
import java.util.function.Supplier;

public class AsyncUtil {

    public static void asyncRun(Runnable runnable) throws Exception {
        CompletableFuture.runAsync(runnable, ExecutorUtil.getExecutor());
    }

    public static <T> CompletableFuture<T> asyncRunWithReturn(Supplier<T> supplier) throws Exception {
        return CompletableFuture.supplyAsync(supplier, ExecutorUtil.getExecutor());
    }

    public static <T> List<CompletableFuture<T>> buildFuture(@SuppressWarnings("unchecked") Supplier<T>... suppliers)
            throws Exception {
        List<CompletableFuture<T>> futures = new ArrayList<>();
        for (Supplier<T> s : suppliers) {
            futures.add(asyncRunWithReturn(s));
        }
        return futures;
    }
}
