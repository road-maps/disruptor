package com.byf.disruptor.concurrent.delayqueue;

import lombok.ToString;

import java.util.concurrent.Delayed;
import java.util.concurrent.TimeUnit;

@ToString
public class DelayTask implements Delayed {
    private String name;
    private long start = System.currentTimeMillis();
    private long time;
    public DelayTask(String name,long time) {
        this.name = name;
        this.time = time;
    }
    /**
     * 需要实现的接口，获得延迟时间   用过期时间-当前时间
     * @param unit
     * @return
     */
    @Override
    public long getDelay(TimeUnit unit) {
        return unit.convert((start+time) - System.currentTimeMillis(),TimeUnit.MILLISECONDS);
    }


    @Override
    public int compareTo(Delayed o) {
        DelayTask task = (DelayTask) o;
        return (int) (this.getDelay(TimeUnit.MILLISECONDS) - o.getDelay(TimeUnit.MILLISECONDS));
    }
}
