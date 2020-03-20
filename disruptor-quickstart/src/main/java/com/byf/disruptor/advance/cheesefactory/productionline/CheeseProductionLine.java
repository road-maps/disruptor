package com.byf.disruptor.advance.cheesefactory.productionline;

import com.byf.disruptor.advance.cheesefactory.order.CheeseOrder;
import com.lmax.disruptor.EventHandler;
import lombok.extern.slf4j.Slf4j;

import java.util.concurrent.TimeUnit;

/**
 * 奶酪生产线
 */
@Slf4j
public class CheeseProductionLine implements EventHandler<CheeseOrder> {

    @Override
    public void onEvent(CheeseOrder cheeseOrder, long arg1, boolean arg2) throws Exception {
        log.info("【奶酪生产线】拦截奶酪订单：{}", cheeseOrder.getSeq());
        try {
            log.info("倒入牛奶：{}, 倒入发酵剂：{}", cheeseOrder.getMilk(), cheeseOrder.getFerment());
            TimeUnit.SECONDS.sleep(1);
            log.info("奶酪生产完毕, 放入冷库.");
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

    }
}