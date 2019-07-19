package com.byf.disruptor.advance.multi;

import com.lmax.disruptor.*;
import com.lmax.disruptor.dsl.ProducerType;
import lombok.extern.slf4j.Slf4j;

import java.util.UUID;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.Executors;

@Slf4j
public class Main {
    public static void main(String[] args) throws InterruptedException {
        RingBuffer<Order> ringBuffer =
                RingBuffer.create(ProducerType.MULTI,
                        new EventFactory<Order>() {
                            @Override
                            public Order newInstance() {
                                return new Order();
                            }
                        },
                        1024 * 1024,
                        new YieldingWaitStrategy());
        // 2 通过RingBuffer创建一个屏障
        SequenceBarrier sequenceBarrier = ringBuffer.newBarrier();

        // 3 构建多消费者
        Customer[] customers = new Customer[10];
        for (int i = 0; i < customers.length; i++) {
            customers[i] = new Customer("C" + i);
        }

        // 4 构建多消费者工作池
        WorkerPool<Order> workerPool = new WorkerPool<>(
                ringBuffer,
                sequenceBarrier,
                new EventExceptionHandler(),
                customers
        );

        // 5 设置多个消费者的Sequence序号，用于单独统计消费进度
        ringBuffer.addGatingSequences(workerPool.getWorkerSequences());

        // 6 启动workerPool
        workerPool.start(Executors.newFixedThreadPool(Runtime.getRuntime().availableProcessors()));

        CountDownLatch latch = new CountDownLatch(1);
        for (int i = 0; i < 100; i++) {
            Producer producer = new Producer(ringBuffer);
            new Thread(()->{
                try{
                    latch.await();
                } catch (Exception e){
                    log.error("exception",e);
                }
                for (int j = 0; j < 100; j++) {
                    producer.sendData(UUID.randomUUID().toString());
                }
            }).start();
        }

        Thread.sleep(2000);
        log.info("-----------线程创建完毕，开始生产数据--------------");
        latch.countDown();
        Thread.sleep(5000);
        log.info("第3个消费者处理总数：{}",customers[2].getCount());
    }
    static class EventExceptionHandler implements ExceptionHandler<Order>{

        @Override
        public void handleEventException(Throwable throwable, long l, Order order) {

        }

        @Override
        public void handleOnStartException(Throwable throwable) {

        }

        @Override
        public void handleOnShutdownException(Throwable throwable) {

        }
    }
}
