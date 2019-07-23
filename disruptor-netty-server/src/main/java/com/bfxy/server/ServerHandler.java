package com.bfxy.server;

import com.bfxy.disruptor.MessageProducer;
import com.bfxy.disruptor.RingBufferWorkerPoolFactory;
import com.bfxy.entity.TranslatorData;

import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelInboundHandlerAdapter;

public class ServerHandler extends ChannelInboundHandlerAdapter {

    @Override
    public void channelRead(ChannelHandlerContext ctx, Object msg) throws Exception {
    	TranslatorData request = (TranslatorData)msg;
    	// 自已的应用服务应该有一个ID生成规则
		// 机器码：SessionID：编号
    	String producerId = "code:sessionId:001";
    	MessageProducer messageProducer = RingBufferWorkerPoolFactory.getInstance().getMessageProducer(producerId);
    	messageProducer.onData(request, ctx);
    }
    
}
