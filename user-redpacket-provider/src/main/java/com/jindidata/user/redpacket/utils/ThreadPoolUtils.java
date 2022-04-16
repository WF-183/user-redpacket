package com.jindidata.user.redpacket.utils;

import com.google.common.util.concurrent.ThreadFactoryBuilder;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Map;
import java.util.concurrent.*;

/**
 *
 * @author： <a href="mailto:wangfei@tianyancha.com">wangfei</a>
 * @date: 2022/4/16
 * @version: 1.0.0
 */
public class ThreadPoolUtils {

    private static final Logger logger = LoggerFactory.getLogger(ThreadPoolUtils.class);

    /**
     * send记录保存线程池
     */
    public static String SEND_THREAD_POOL = "SEND_THREAD_POOL";

    /**
     * get记录保存线程池
     */
    public static String GET_THREAD_POOL = "GET_THREAD_POOL";

    private static final Map<String, ExecutorService> THREAD_POOL_EXECUTOR_MAP = new ConcurrentHashMap<>();

    static {
        //初始化线程池 线程池互相隔离
        //各个线程池配置可根据实际任务情况调整
        if (!THREAD_POOL_EXECUTOR_MAP.containsKey(SEND_THREAD_POOL)) {
            THREAD_POOL_EXECUTOR_MAP.put(SEND_THREAD_POOL,
                new ThreadPoolExecutor(Runtime.getRuntime().availableProcessors(), Runtime.getRuntime().availableProcessors() * 5, 2000, TimeUnit.MILLISECONDS,
                    new LinkedBlockingQueue<>(Runtime.getRuntime().availableProcessors() * 1000),
                    new ThreadFactoryBuilder().setNameFormat(SEND_THREAD_POOL + "-thread-%d").build(),
                    (r, executor) -> logger.error("[xxx] Failed to add new thread ， the pool [" + SEND_THREAD_POOL + "] is full")));
        }
        if (!THREAD_POOL_EXECUTOR_MAP.containsKey(GET_THREAD_POOL)) {
            THREAD_POOL_EXECUTOR_MAP.put(GET_THREAD_POOL,
                new ThreadPoolExecutor(Runtime.getRuntime().availableProcessors(), Runtime.getRuntime().availableProcessors() * 5, 2000, TimeUnit.MILLISECONDS,
                    new LinkedBlockingQueue<>(Runtime.getRuntime().availableProcessors() * 1000),
                    new ThreadFactoryBuilder().setNameFormat(GET_THREAD_POOL + "-thread-%d").build(),
                    (r, executor) -> logger.error("[xxx] Failed to add new thread ， the pool [" + GET_THREAD_POOL + "] is full")));
        }
    }

    /**
     * 获取指定线程池
     * @param poolName
     * @return
     */
    public static ExecutorService getExecutorService(String poolName) {
        return THREAD_POOL_EXECUTOR_MAP.get(poolName);
    }

    /**
     * 获取线程池总map
     * @return
     */
    public static Map<String, ExecutorService> getExecutorServiceMap() {
        return THREAD_POOL_EXECUTOR_MAP;
    }
}

