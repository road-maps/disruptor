package com.byf.disruptor.advance.cheesefactory.productionline;

import com.byf.disruptor.advance.cheesefactory.order.CheeseOrder;
import com.lmax.disruptor.EventHandler;
import com.lmax.disruptor.WorkHandler;
import lombok.extern.slf4j.Slf4j;

import java.util.concurrent.TimeUnit;

/**
 * 牛奶生产线
 */
@Slf4j
public class MilkProductionLine implements EventHandler<CheeseOrder>, WorkHandler<CheeseOrder> {

    @Override
    public void onEvent(CheeseOrder arg0) throws Exception {
        log.info("【牛奶生产线】拦截奶酪订单：{}", arg0.getSeq());
        try {
            // 生产牛奶
            TimeUnit.SECONDS.sleep(arg0.getMilk());
            log.info("牛奶数量：{}, 生产完毕.", arg0.getMilk());
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void onEvent(CheeseOrder arg0, long arg1, boolean arg2) throws Exception {
        // TODO Auto-generated method stub
        this.onEvent(arg0);
    }

}