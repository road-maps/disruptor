package com.byf.disruptor.advance.chain;

import com.lmax.disruptor.EventHandler;
import lombok.extern.slf4j.Slf4j;

@Slf4j
public class Handler3 implements EventHandler<Trade> {
    @Override
    public void onEvent(Trade event, long l, boolean b) throws Exception {
        log.info("Handler3 : NAME:{}, ID:{}, INSTANCE:{}",event.getName(),event.getId(),event.toString());
        // Thread.sleep(3000);
    }
}
