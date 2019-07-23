package com.bfxy.client;

import com.bfxy.disruptor.MessageProducer;
import com.bfxy.disruptor.RingBufferWorkerPoolFactory;
import com.bfxy.entity.TranslatorData;

import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelInboundHandlerAdapter;
import io.netty.util.ReferenceCountUtil;

public class ClientHandler extends ChannelInboundHandlerAdapter {

	@Override
    public void channelRead(ChannelHandlerContext ctx, Object msg) throws Exception {
    	TranslatorData response = (TranslatorData)msg;
    	// 机器码：SessionID：编号
    	String producerId = "code:seesionId:002";
    	MessageProducer messageProducer = RingBufferWorkerPoolFactory.getInstance().getMessageProducer(producerId);
    	messageProducer.onData(response, ctx);
    }
}
