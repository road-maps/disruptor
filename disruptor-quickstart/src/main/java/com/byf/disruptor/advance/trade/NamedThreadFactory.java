package com.byf.disruptor.advance.trade;

import java.util.concurrent.ThreadFactory;

public class NamedThreadFactory implements ThreadFactory {
    @Override
    public Thread newThread(Runnable r) {
        Thread t1 = new Thread("Trade-Disruptor-");
        t1.setDaemon(false);
        return t1;
    }
}
