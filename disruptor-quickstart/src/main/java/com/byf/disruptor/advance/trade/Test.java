package com.byf.disruptor.advance.trade;

/**
 * 测试类
 *
 */
public class Test {
    public static TransactionEventProducer producer = new TransactionEventProducer();
    public static void main(String[] args) throws Exception {
        long startTime = System.currentTimeMillis();
        for (int i = 0; i < 100000; i++) {
            producer.disruptorManage();
        }
        System.out.println("--------------------------------------------------: " + (System.currentTimeMillis() - startTime));
    }
}