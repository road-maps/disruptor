package com.byf.disruptor.quickstart;

import com.lmax.disruptor.RingBuffer;
import lombok.AllArgsConstructor;
import lombok.Data;

import java.nio.ByteBuffer;

@Data
@AllArgsConstructor
public class OrderEventProducer {
    private RingBuffer<OrderEvent> ringBuffer;
    public void setData(ByteBuffer byteBuffer){
        // 1 在生产者发送消息的时候，首先需要从ringBuffer中获取一个可用的序号
        long sequence = ringBuffer.next();
        try{
            // 2 根据这个序号，找到具体OrderEvent元素，注意：此时获取的OrderEvent对象是一个没有被赋值空对象
            OrderEvent event = ringBuffer.get(sequence);
            // 3 进行实际的赋值处理
            event.setValue(byteBuffer.getLong(0));
        } finally {
            // 4 提交发布操作
            ringBuffer.publish(sequence);
        }
    }
}
