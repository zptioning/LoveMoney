package com.zptioning.module_funds.net_work;

import java.util.concurrent.ArrayBlockingQueue;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.Callable;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;
import java.util.concurrent.RejectedExecutionHandler;
import java.util.concurrent.ScheduledThreadPoolExecutor;
import java.util.concurrent.ThreadFactory;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;

/**
 * @ClassName ThreadUtils
 * @Author zptioning
 * @Date 2019-11-06 17:26
 * @Version 1.0
 * @Description 线程管理工具类
 * 参考文章：https://www.cnblogs.com/CarpenterLee/p/9558026.html
 */
public class ThreadUtils<T> {
    private T mContent;

    private ExecutorService mThreadPoolExecutor;
    private ExecutorService mScheduledThreadPoolExecutor;

    private ThreadUtils() {
        initThreadPoolExecutor();
        initScheduledThreadPoolExecutor();
    }


    private static class ThreadUtilsHolder {
        private static ThreadUtils instance = new ThreadUtils();
    }

    public static ThreadUtils getInstance() {
        return ThreadUtilsHolder.instance;
    }

    /**
     * Java线程池的完整构造函数
     * 不要使用Executors.newXXXThreadPool()快捷方法创建线程池，
     * 因为这种方式会使用无界的任务队列，为避免OOM，
     * 我们应该使用ThreadPoolExecutor的构造方法手动指定队列的最大长度：
     * <p>
     * int corePoolSize, // 正式工数量
     * int maximumPoolSize, // 工人数量上限，包括正式工和临时工
     * long keepAliveTime, TimeUnit unit, // 临时工游手好闲的最长时间，超过这个时间将被解雇
     * BlockingQueue<Runnable> workQueue, // 排期队列
     * ThreadFactory threadFactory, // 招人渠道
     * RejectedExecutionHandler handler) // 拒单方式
     */
    private void initThreadPoolExecutor() {
        int poolSize = Runtime.getRuntime().availableProcessors() * 2;
        // 线程池长期维持的线程数，即使线程处于Idle状态，也不会回收。
        int corePoolSize = poolSize;
        // 线程数的上限
        int maximumPoolSize = poolSize * 10;
        // 超过corePoolSize的线程的idle时长，超过这个时间，多余的线程会被回收。
        long keepAliveTime = 0;
        TimeUnit unit = TimeUnit.SECONDS;
        // 任务的排队队列
        BlockingQueue<Runnable> workQueue = new ArrayBlockingQueue<>(512);
        // 新线程的产生方式
        ThreadFactory threadFactory = Executors.defaultThreadFactory();
        // 拒绝策略
        RejectedExecutionHandler handler = new ThreadPoolExecutor.AbortPolicy();
        mThreadPoolExecutor = new ThreadPoolExecutor(corePoolSize,
                maximumPoolSize,
                keepAliveTime,
                unit,
                workQueue/*,
                threadFactory,
                handler*/
        );
    }

    /**
     * 定时任务
     */
    private void initScheduledThreadPoolExecutor() {
        mScheduledThreadPoolExecutor = new ScheduledThreadPoolExecutor(2);
    }

    /**
     * 可以拿到返回结果
     *
     * @param callable
     * @return
     */
    public T submit(Callable<T> callable) {
        Future<T> future = mThreadPoolExecutor.submit(callable);

        try {
            T result = future.get();
            return result;
        } catch (ExecutionException e) {
            e.printStackTrace();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

        return null;
    }

    /**
     * 虽然返回Future，但是其get()方法总是返回null
     *
     * @param runnable
     * @return
     */
    public Object submit(Runnable runnable) {
        Future<?> future = mThreadPoolExecutor.submit(runnable);

        try {
            Object result = future.get();// 始终返回null
            return result;
        } catch (ExecutionException e) {
            e.printStackTrace();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

        return null;
    }


    /**
     * 不关心返回结果
     *
     * @param runnable
     */
    public void execute(Runnable runnable) {
        mThreadPoolExecutor.execute(runnable);
    }
}
