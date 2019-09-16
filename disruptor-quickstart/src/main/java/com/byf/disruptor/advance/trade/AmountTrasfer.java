package com.byf.disruptor.advance.trade;

import com.lmax.disruptor.EventTranslator;

/**
 * 事件处理类-交易流水初始化
 *
 */
public class AmountTrasfer implements EventTranslator<TransactionEvent> {

    @Override
    public void translateTo(TransactionEvent arg0, long arg1) {
        arg0.setAmount(Math.random() * 99);
        arg0.setCallNumber(17088888888L);
        arg0.setSeq(System.currentTimeMillis());
        System.out.println("设置交易流水:" + arg0.getSeq());
    }
}