package com.byf.disruptor.ability;

import lombok.extern.slf4j.Slf4j;

import java.util.concurrent.ArrayBlockingQueue;

@Slf4j
public class ArrayBlockingQueue4Test {
    public static void main(String[] args) {
        final ArrayBlockingQueue<Message> queue = new ArrayBlockingQueue<>(Constants.EVENT_NUM_OM);
        final long startTime = System.currentTimeMillis();
        new Thread(()->{
            long i = 0;
            while (i < Constants.EVENT_NUM_OHM) {
                Message message = new Message(i, "c" + i);
                try {
                    queue.put(message);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
                i++;
            }
        }).start();

        new Thread(()->{
            int k = 0;
            while (k < Constants.EVENT_NUM_OHM) {
                try {
                    queue.take();
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
                k++;
            }
            long endTime = System.currentTimeMillis();
            System.out.println("ArrayBlockingQueue costTime = " + (endTime - startTime) + "ms");
        }).start();
    }
}
