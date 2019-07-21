package com.byf.disruptor.concurrent;

import java.util.concurrent.*;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.concurrent.locks.LockSupport;
import java.util.concurrent.locks.ReentrantLock;

public class Review {
    public static void main(String[] args) throws InterruptedException {
        /*ConcurrentHashMap<String,String> map = new ConcurrentHashMap<>();
        CopyOnWriteArrayList list = new CopyOnWriteArrayList();
        SynchronousQueue synchronousQueue = new SynchronousQueue();
        AtomicInteger atomicInteger = new AtomicInteger(0);
        boolean flag = atomicInteger.compareAndSet(0,1);
        System.out.println(flag);
        System.out.println(atomicInteger.get());*/
        Object lock = new Object();
        Thread A = new Thread(()->{
            int sum = 0;
            for (int i = 0; i < 10; i++) {
                sum += i;
            }
            try {
                Thread.sleep(2000);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            LockSupport.park();  // 后执行
            System.out.println("sum: " + sum);
        });
        A.start();
        Thread.sleep(1000);
        LockSupport.unpark(A);  // 限执行
        Executors.newCachedThreadPool();
        Executors.newFixedThreadPool(10);

        ThreadPoolExecutor executor = new ThreadPoolExecutor(
                5,
                Runtime.getRuntime().availableProcessors() * 2,
                60,
                TimeUnit.SECONDS,
                new ArrayBlockingQueue<>(200),
                new ThreadFactory() {
                    @Override
                    public Thread newThread(Runnable r) {
                        Thread t = new Thread(r);
                        t.setName("order-thread");
                        if (t.isDaemon()) {
                            t.setDaemon(false);
                        }
                        if (Thread.NORM_PRIORITY == t.getPriority()) {
                            t.setPriority(Thread.NORM_PRIORITY);
                        }
                        return t;
                    }
                },
                new RejectedExecutionHandler() {
                    @Override
                    public void rejectedExecution(Runnable r, ThreadPoolExecutor executor) {
                        System.out.println("拒绝策略" + r);
                    }
                }
        );

        executor.shutdown();

        ReentrantLock lock1 = new ReentrantLock(true);

    }
}
