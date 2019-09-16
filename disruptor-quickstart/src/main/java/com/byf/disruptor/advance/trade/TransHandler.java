package com.byf.disruptor.advance.trade;

import com.lmax.disruptor.EventHandler;
import com.lmax.disruptor.WorkHandler;

/**
 * 消费者–定义事件处理的具体实现
 * 拦截交易流水
 *
 */
public class TransHandler implements EventHandler<TransactionEvent>, WorkHandler<TransactionEvent> {

    @Override
    public void onEvent(TransactionEvent transactionEvent) throws Exception {
        System.out.println("交易流水号为：" + transactionEvent.getSeq() + "||交易金额为:" + transactionEvent.getAmount());
    }

    @Override
    public void onEvent(TransactionEvent arg0, long arg1, boolean arg2) throws Exception {        // TODO Auto-generated method stub
        this.onEvent(arg0);
    }
}