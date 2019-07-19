package com.byf.disruptor.advance.multi;

import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.concurrent.atomic.AtomicInteger;

/**
 * Disruptor中的Event
 */
@Data
@NoArgsConstructor
public class Order {
    private String id;
    private String name;
    private double price;
}
