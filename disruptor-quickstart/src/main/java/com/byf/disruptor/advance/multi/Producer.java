package com.byf.disruptor.advance.multi;

import com.lmax.disruptor.RingBuffer;
import lombok.AllArgsConstructor;

@AllArgsConstructor
public class Producer {
    private RingBuffer<Order> ringBuffer;
    public void sendData(String uuid){
        long sequence = ringBuffer.next();
        try{
            Order order = ringBuffer.get(sequence);
            order.setId(uuid);
        } finally {
            ringBuffer.publish(sequence);
        }
    }
}
