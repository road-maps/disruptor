package com.byf.disruptor.ability;

import com.lmax.disruptor.EventFactory;
import com.lmax.disruptor.RingBuffer;
import com.lmax.disruptor.YieldingWaitStrategy;
import com.lmax.disruptor.dsl.Disruptor;
import com.lmax.disruptor.dsl.ProducerType;

import java.util.concurrent.Executors;

public class DisruptorSingle4Test {
    public static void main(String[] args) {
        int ringBufferSize = 65536;
        final Disruptor<Message> disruptor = new Disruptor<>(
                new EventFactory<Message>() {
                    @Override
                    public Message newInstance() {
                        return new Message();
                    }
                },
                ringBufferSize,
                Executors.newSingleThreadExecutor(),
                ProducerType.SINGLE,
                new YieldingWaitStrategy()
        );
        DataConsumer consumer = new DataConsumer();
        disruptor.handleEventsWith(consumer);
        disruptor.start();
        new Thread(()->{
            RingBuffer<Message> ringBuffer = disruptor.getRingBuffer();
            for (long i = 0; i < Constants.EVENT_NUM_FM; i++) {
                long seq = ringBuffer.next();
                Message data = ringBuffer.get(seq);
                data.setId(i);
                data.setName("c" + i);
                ringBuffer.publish(seq);
            }
        }).start();
    }
}
