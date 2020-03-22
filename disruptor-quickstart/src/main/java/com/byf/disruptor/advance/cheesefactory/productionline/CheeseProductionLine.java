package com.byf.disruptor.advance.cheesefactory.productionline;

import com.byf.disruptor.advance.cheesefactory.model.Cheese;
import com.byf.disruptor.advance.cheesefactory.coldstore.ColdStore;
import com.byf.disruptor.advance.cheesefactory.order.CheeseOrder;
import com.lmax.disruptor.EventHandler;
import lombok.extern.slf4j.Slf4j;

import java.util.Date;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicLong;

/**
 * 奶酪生产线
 */
@Slf4j
public class CheeseProductionLine implements EventHandler<CheeseOrder> {

    private static AtomicLong sum = new AtomicLong(1L);
    @Override
    public void onEvent(CheeseOrder cheeseOrder, long arg1, boolean arg2) throws Exception {
        log.info("【奶酪生产线】拦截奶酪订单：{}", cheeseOrder.getSeq());
        log.info("倒入牛奶：{}, 倒入发酵剂：{}", cheeseOrder.getMilk(), cheeseOrder.getFerment());
        // 生产奶酪
        //TimeUnit.MILLISECONDS.sleep(1);
        ColdStore.getInstence().addCheese(new Cheese(new Date(), cheeseOrder.getSeq(), "Win.D"));
        log.info("奶酪生产完毕, 第 {} 次放入冷库. 当前冷库容量：{}", sum.getAndIncrement(), ColdStore.getInstence().getStore().size());

    }
}