package com.byf.disruptor.advance.cheesefactory.producer;

import java.util.concurrent.ThreadFactory;

public class CheeseThreadFactory implements ThreadFactory {
    @Override
    public Thread newThread(Runnable r) {
        Thread t1 = new Thread("Cheese-Disruptor-");
        t1.setDaemon(false);
        return t1;
    }
}
