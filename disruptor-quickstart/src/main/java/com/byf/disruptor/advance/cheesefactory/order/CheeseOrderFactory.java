package com.byf.disruptor.advance.cheesefactory.order;

import com.lmax.disruptor.EventFactory;

/**
 * 奶酪订单工厂
 */
public class CheeseOrderFactory implements EventFactory<CheeseOrder> {

    @Override
    public CheeseOrder newInstance() {
        // TODO Auto-generated method stub
        return new CheeseOrder();
    }
}