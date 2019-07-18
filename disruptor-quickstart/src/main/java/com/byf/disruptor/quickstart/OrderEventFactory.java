package com.byf.disruptor.quickstart;

import com.lmax.disruptor.EventFactory;

public class OrderEventFactory implements EventFactory<OrderEvent> {
    @Override
    public OrderEvent newInstance() {
        // 这个方法就是为了返回空的OrderEvent对象
        return new OrderEvent();
    }
}
