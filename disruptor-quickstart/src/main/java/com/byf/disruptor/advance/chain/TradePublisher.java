package com.byf.disruptor.advance.chain;

import com.lmax.disruptor.EventTranslator;
import com.lmax.disruptor.dsl.Disruptor;
import lombok.AllArgsConstructor;

import java.util.Random;
import java.util.concurrent.CountDownLatch;

@AllArgsConstructor
public class TradePublisher implements Runnable {
    private Disruptor<Trade> disruptor;
    private CountDownLatch latch;
    private static int PUBLISH_COUNT = 1;
    @Override
    public void run() {
        TradeEventTranslator eventTranslator = new TradeEventTranslator();
        for (int i = 0; i < PUBLISH_COUNT; i++) {
            disruptor.publishEvent(eventTranslator);
        }
        latch.countDown();
    }
}

class TradeEventTranslator implements EventTranslator<Trade>{
    private Random random = new Random();
    @Override
    public void translateTo(Trade event, long sequence) {
        this.generateTrade(event);
    }

    private void generateTrade(Trade event) {
        event.setPrice(random.nextDouble() * 9999);
    }
}