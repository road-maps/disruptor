package com.byf.disruptor.advance.cheesefactory.order;

import com.lmax.disruptor.EventTranslator;
import lombok.extern.slf4j.Slf4j;

import java.util.concurrent.atomic.AtomicLong;

/**
 * 奶酪订单初始化
 */
@Slf4j
public class CheeseOrderTransfer implements EventTranslator<CheeseOrder> {

    private static AtomicLong ORDER_SEQ = new AtomicLong(1L);

    @Override
    public void translateTo(CheeseOrder arg0, long arg1) {
        arg0.setSeq(ORDER_SEQ.getAndIncrement());
        arg0.setMilk(2);
        arg0.setFerment(1);
        log.info("奶酪订单流水：{}", arg0.getSeq());
    }
}