package com.byf.disruptor.advance.cheesefactory.order;

import lombok.Data;

/**
 * 奶酪订单
 */
@Data
public class CheeseOrder {
    private long seq;
    private int milk;
    private int ferment;
}