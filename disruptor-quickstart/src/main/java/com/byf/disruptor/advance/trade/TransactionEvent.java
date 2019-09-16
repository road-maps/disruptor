package com.byf.disruptor.advance.trade;

import lombok.Data;

/**
 * 事件(Event)就是通过 Disruptor 进行交换的数据类型。
 *
 */
@Data
public class TransactionEvent {
    private long seq;
    private double amount;
    private long callNumber;
}