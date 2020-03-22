package com.byf.disruptor.advance.cheesefactory;

import com.byf.disruptor.advance.cheesefactory.coldstore.ColdStore;
import com.byf.disruptor.advance.cheesefactory.model.Cheese;
import com.byf.disruptor.advance.cheesefactory.order.CheeseOrder;
import com.byf.disruptor.advance.cheesefactory.order.CheeseOrderFactory;
import com.byf.disruptor.advance.cheesefactory.producer.CheeseOrderPublisher;
import com.byf.disruptor.advance.cheesefactory.productionline.CheeseProductionLine;
import com.byf.disruptor.advance.cheesefactory.productionline.FermentProductionLine;
import com.byf.disruptor.advance.cheesefactory.productionline.MilkProductionLine;
import com.lmax.disruptor.YieldingWaitStrategy;
import com.lmax.disruptor.dsl.Disruptor;
import com.lmax.disruptor.dsl.EventHandlerGroup;
import com.lmax.disruptor.dsl.ProducerType;
import lombok.extern.slf4j.Slf4j;

import java.util.concurrent.*;

/**
 * 接收奶酪订单
 *
 */
@Slf4j
public class Main {
    private static final int BUFFER_SIZE = 1024;
    private static final int CAR_LOAD = 100;
    private static final int TRANSPORTATION_TIMES = 1000;
    // 生产限流，仓库超过阈值暂停下发订单
    private static Semaphore semaphore = new Semaphore(500);

    public static void main(String[] args) throws Exception {
        long start = System.currentTimeMillis();
        ExecutorService cheeseFactoryPool = Executors.newFixedThreadPool(2);
        CountDownLatch latch = new CountDownLatch(2);

        // 奶酪工厂开工
        cheeseFactoryPool.submit(()->{
            long startTime = System.currentTimeMillis();
            start();
            log.warn("==========>>所有奶酪生产完成，耗时： {}", (System.currentTimeMillis() - startTime));
            latch.countDown();
        });

        // 货车取货配送
        cheeseFactoryPool.submit(()->{
            long start1 = System.currentTimeMillis();
            int count = 0;
            int times = 0;
            while (true){
                count++;
                Cheese cheese = ColdStore.getInstence().takeCheese();
                semaphore.release();
                log.info("从冷库取奶酪，装进货车：{}", cheese);
                if (count == CAR_LOAD){
                    count = 0;
                    times++;
                    log.warn("奶酪装载完毕，货车第 {} 次配货中...", times);
                    //try { TimeUnit.MILLISECONDS.sleep(10); } catch (InterruptedException e) {e.printStackTrace();}
                    if (times == TRANSPORTATION_TIMES){
                        log.warn("==========>>所有奶酪配送完成. 耗时：{}", (System.currentTimeMillis() - start));
                        latch.countDown();
                    }
                }
            }
        });

        try {
            latch.await();
            cheeseFactoryPool.shutdown();
            log.info("本次生产及配送完成. 耗时：{}", (System.currentTimeMillis() - start));
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }

    public static void start(){
        //创建用于处理事件的线程池
        ThreadPoolExecutor pool2 = new ThreadPoolExecutor(4, 4,
                0L, TimeUnit.MILLISECONDS,
                new LinkedBlockingQueue<Runnable>());
        //创建disruptor对象
        /**
         * 用来指定数据生成者有一个还是多个，有两个可选值ProducerType.SINGLE和ProducerType.MULTI
         * BusySpinWaitStrategy是一种延迟最低，最耗CPU的策略。通常用于消费线程数小于CPU数的场景
         */
        Disruptor<CheeseOrder> disruptor2 = new Disruptor<CheeseOrder>(new CheeseOrderFactory(), BUFFER_SIZE, pool2, ProducerType.SINGLE, new YieldingWaitStrategy());
        //创建消费者组，先执行生产牛奶和发酵粉
        EventHandlerGroup<CheeseOrder> eventsWith = disruptor2.handleEventsWith(new MilkProductionLine(), new FermentProductionLine());
        //在进行生产奶酪
        eventsWith.then(new CheeseProductionLine());
        //启动disruptor
        disruptor2.start();
        //在线程能通过 await()之前，必须调用 countDown() 的次数
        CountDownLatch latch = new CountDownLatch(1);
        //将封装好的TransactionEventProducer类提交 new TradePublisher(disruptor,latch)
        pool2.submit(new CheeseOrderPublisher(disruptor2, latch, semaphore));
        //使当前线程在锁存器倒计数至零之前一直等待,以保证生产者任务完全消费掉
        try {
            latch.await();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        //关闭disruptor业务逻辑处理器
        disruptor2.shutdown();
        //销毁线程池
        pool2.shutdown();
    }
}