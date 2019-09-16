package com.byf.disruptor.advance.chain;

import com.lmax.disruptor.EventHandler;
import lombok.extern.slf4j.Slf4j;

import java.util.UUID;

@Slf4j
public class Handler2 implements EventHandler<Trade> {
    @Override
    public void onEvent(Trade event, long l, boolean b) throws Exception {
        log.info("Handler2 : SET ID");
        event.setId(UUID.randomUUID().toString());
        //Thread.sleep(2000);
    }
}
