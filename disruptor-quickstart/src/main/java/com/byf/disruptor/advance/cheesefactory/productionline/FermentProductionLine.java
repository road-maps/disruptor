package com.byf.disruptor.advance.cheesefactory.productionline;

import com.byf.disruptor.advance.cheesefactory.order.CheeseOrder;
import com.byf.disruptor.advance.trade.TransactionEvent;
import com.lmax.disruptor.EventHandler;
import com.lmax.disruptor.WorkHandler;
import lombok.extern.slf4j.Slf4j;

import java.util.concurrent.TimeUnit;

/**
 * 发酵剂生产线
 */
@Slf4j
public class FermentProductionLine implements EventHandler<CheeseOrder>, WorkHandler<CheeseOrder> {

    @Override
    public void onEvent(CheeseOrder cheeseOrder) throws Exception {
        log.info("【发酵剂生产线】拦截奶酪订单：{}", cheeseOrder.getSeq());
        // 生产牛奶
        //TimeUnit.MILLISECONDS.sleep(cheeseOrder.getFerment());
        log.info("发酵剂数量：{}, 生产完毕." ,cheeseOrder.getFerment());
    }

    @Override
    public void onEvent(CheeseOrder arg0, long arg1, boolean arg2) throws Exception {
        // TODO Auto-generated method stub
        this.onEvent(arg0);
    }
}