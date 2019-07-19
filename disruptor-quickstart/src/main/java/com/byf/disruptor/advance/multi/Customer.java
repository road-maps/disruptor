package com.byf.disruptor.advance.multi;

import com.lmax.disruptor.WorkHandler;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.extern.slf4j.Slf4j;

import java.util.Random;
import java.util.concurrent.atomic.AtomicInteger;

@Data
@Slf4j
public class Customer implements WorkHandler<Order> {
    private String customerId;
    private static AtomicInteger count = new AtomicInteger(0);
    private Random random = new Random();

    public Customer(String customerId){
        this.customerId = customerId;
    }

    @Override
    public void onEvent(Order event) throws Exception {
        Thread.sleep(1 * random.nextInt(5));
        log.info("Customer:{}, event ID:{}",this.customerId,event.getId());
        count.getAndIncrement();
    }

    public int getCount(){
        return count.get();
    }
}
