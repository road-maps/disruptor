package com.byf.disruptor.ability;

import com.lmax.disruptor.EventHandler;

public class DataConsumer implements EventHandler<Message> {

    private long startTime;
    private int i;

    public DataConsumer() {
        this.startTime = System.currentTimeMillis();
    }

    public void onEvent(Message data, long seq, boolean bool)
            throws Exception {
        i++;
        if (i == Constants.EVENT_NUM_FM) {
            long endTime = System.currentTimeMillis();
            System.out.println("Disruptor costTime = " + (endTime - startTime) + "ms");
        }
    }
}
