package com.byf.disruptor.concurrent.delayqueue;

import java.util.concurrent.DelayQueue;
import java.util.concurrent.Delayed;

public class Main {
    private static DelayQueue delayQueue  = new DelayQueue();

    public static void main(String[] args) throws InterruptedException {
        DelayTask dt1 = new DelayTask("dt1", 1000);
        DelayTask dt2 = new DelayTask("dt2", 2000);
        DelayTask dt3 = new DelayTask("dt3", 4000);
        DelayTask dt4 = new DelayTask("dt4", 6000);
        DelayTask dt5 = new DelayTask("dt5", 3000);
        DelayTask dt6 = new DelayTask("dt6", 5000);

        new Thread(()->{
            delayQueue.offer(dt1);
            delayQueue.offer(dt2);
            delayQueue.offer(dt3);
            delayQueue.offer(dt4);
            delayQueue.offer(dt5);
            delayQueue.offer(dt6);
        }).start();

        while (true){
            Delayed take = delayQueue.take();
            System.out.println(take);
            if (delayQueue.size() == 0) {
                break;
            }
        }
    }
}
