package com.merkle.wechat.common.util;

import java.util.concurrent.Executor;
import java.util.concurrent.Executors;
import java.util.concurrent.ThreadFactory;

/**
 * 
 * @author tyao
 *
 */
public class ExecutorUtil {
    private static Executor executor;
    private static final int MAX_THREAD = 10;

    public static synchronized Executor getExecutor() throws Exception {
        if (null == executor) {
            executor = Executors.newFixedThreadPool(MAX_THREAD, new ThreadFactory() {

                @Override
                public Thread newThread(Runnable r) {
                    Thread t = new Thread(r);
                    t.setDaemon(true);
                    return t;
                }
            });
        }

        return executor;
    }
}
