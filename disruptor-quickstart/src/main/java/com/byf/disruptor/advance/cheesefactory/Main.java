package com.byf.disruptor.advance.cheesefactory;

import com.byf.disruptor.advance.cheesefactory.producer.CheeseOrderProducer;
import com.byf.disruptor.advance.trade.TransactionEventProducer;

/**
 * 接收奶酪订单
 *
 */
public class Main {
    public static CheeseOrderProducer producer = new CheeseOrderProducer();
    public static void main(String[] args) throws Exception {
        long startTime = System.currentTimeMillis();
        for (int i = 0; i < 100000; i++) {
            producer.disruptorManage();
        }
        System.out.println("--------------------------------------------------: " + (System.currentTimeMillis() - startTime));
    }
}