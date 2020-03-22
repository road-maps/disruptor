package com.byf.disruptor.advance.cheesefactory.producer;

import com.byf.disruptor.advance.cheesefactory.coldstore.ColdStore;
import com.byf.disruptor.advance.cheesefactory.order.CheeseOrder;
import com.byf.disruptor.advance.cheesefactory.order.CheeseOrderTransfer;
import com.lmax.disruptor.dsl.Disruptor;
import lombok.AllArgsConstructor;

import java.util.concurrent.CountDownLatch;
import java.util.concurrent.Semaphore;
import java.util.concurrent.TimeUnit;

@AllArgsConstructor
public class CheeseOrderPublisher implements Runnable {
    private Disruptor<CheeseOrder> disruptor;
    private CountDownLatch latch;
    private Semaphore semaphore;
    private static int PUBLISH_COUNT = 100000;
    @Override
    public void run() {
        CheeseOrderTransfer eventTranslator = new CheeseOrderTransfer();
        for (int i = 0; i < PUBLISH_COUNT; i++) {
            try {
                semaphore.acquire();
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            disruptor.publishEvent(eventTranslator);
        }
        latch.countDown();
    }
}

