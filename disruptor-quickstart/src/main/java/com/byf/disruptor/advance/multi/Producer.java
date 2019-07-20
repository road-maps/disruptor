package com.byf.disruptor.advance.multi;

import com.lmax.disruptor.RingBuffer;
import lombok.AllArgsConstructor;

import java.util.concurrent.BrokenBarrierException;

@AllArgsConstructor
public class Producer {
    private RingBuffer<Order> ringBuffer;
    public void sendData(String uuid) throws BrokenBarrierException, InterruptedException {
        long sequence = ringBuffer.next();
        try{
            Order order = ringBuffer.get(sequence);
            order.setId(uuid);
        } finally {
            ringBuffer.publish(sequence);
        }
    }
}
