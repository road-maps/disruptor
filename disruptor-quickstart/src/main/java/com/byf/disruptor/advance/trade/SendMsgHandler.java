package com.byf.disruptor.advance.trade;

import com.lmax.disruptor.EventHandler;

/**
 * 发送验证短信
 */
public class SendMsgHandler implements EventHandler<TransactionEvent> {

    @Override
    public void onEvent(TransactionEvent arg0, long arg1, boolean arg2) throws Exception {        // TODO Auto-generated method stub
        System.out.println("向手机号:" + arg0.getCallNumber() + "发送验证短信......");

    }
}