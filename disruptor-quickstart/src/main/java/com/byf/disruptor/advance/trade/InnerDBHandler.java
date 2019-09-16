package com.byf.disruptor.advance.trade;

import com.lmax.disruptor.EventHandler;
import com.lmax.disruptor.WorkHandler;

/**
 * 交易流水入库操作
 *
 */
public class InnerDBHandler implements EventHandler<TransactionEvent>, WorkHandler<TransactionEvent> {

    @Override
    public void onEvent(TransactionEvent arg0, long arg1, boolean arg2) throws Exception {
        // TODO Auto-generated method stub
        this.onEvent(arg0);
    }

    @Override
    public void onEvent(TransactionEvent arg0) throws Exception {
        arg0.setSeq(arg0.getSeq() * 10000);
        System.out.println("拦截入库流水号------------  " + arg0.getSeq());
    }
}