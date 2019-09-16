package com.byf.disruptor.advance.chain;

import com.lmax.disruptor.BusySpinWaitStrategy;
import com.lmax.disruptor.EventFactory;
import com.lmax.disruptor.RingBuffer;
import com.lmax.disruptor.dsl.Disruptor;
import com.lmax.disruptor.dsl.ProducerType;
import lombok.extern.slf4j.Slf4j;

import java.util.concurrent.*;

@Slf4j
public class Main {
    public static void main(String[] args) throws InterruptedException {
        ExecutorService es1 = new ThreadPoolExecutor(1,16, 1000,TimeUnit.MILLISECONDS,
                new LinkedBlockingDeque<>(1000));
        ExecutorService es2 = new ThreadPoolExecutor(5,16, 1000,TimeUnit.MILLISECONDS,
                new LinkedBlockingDeque<>(1000));
        Disruptor<Trade> disruptor = new Disruptor<>(
                new EventFactory<Trade>() {
                    @Override
                    public Trade newInstance() {
                        return new Trade();
                    }
                },
                1024 * 1024,
                es2,
                ProducerType.SINGLE,
                new BusySpinWaitStrategy()
        );

        // 串行操作
        /*final EventHandlerGroup<Trade> tradeEventHandlerGroup = disruptor
                .handleEventsWith(new Handler1())
                .handleEventsWith(new Handler2())
                .handleEventsWith(new Handler3());*/

        // 并行操作
        // disruptor.handleEventsWith(new Handler1(),new Handler2(),new Handler3());

        // 菱形操作（一）
        // disruptor.handleEventsWith(new Handler1(),new Handler2()).handleEventsWith(new Handler3());
        // 菱形操作（二）
        // EventHandlerGroup<Trade> eventHandlerGroup = disruptor.handleEventsWith(new Handler1(), new Handler2());
        // eventHandlerGroup.then(new Handler3());

        // 六边形操作
        Handler1 handler1 = new Handler1();
        Handler2 handler2 = new Handler2();
        Handler3 handler3 = new Handler3();
        Handler4 handler4 = new Handler4();
        Handler5 handler5 = new Handler5();
        disruptor.handleEventsWith(handler1,handler4);
        disruptor.after(handler1).handleEventsWith(handler2);
        disruptor.after(handler4).handleEventsWith(handler5);
        disruptor.after(handler2,handler5).handleEventsWith(handler3);
        log.info("开始");
        long begin = System.currentTimeMillis();
        RingBuffer<Trade> ringBuffer = disruptor.start();
        CountDownLatch latch = new CountDownLatch(1);
        es1.submit(new TradePublisher(disruptor,latch));
        latch.await();
        disruptor.shutdown();
//        es1.shutdown();
//        es2.shutdown();
        log.info("总耗时：{}",(System.currentTimeMillis()-begin));
        es2.shutdown();
    }
}
