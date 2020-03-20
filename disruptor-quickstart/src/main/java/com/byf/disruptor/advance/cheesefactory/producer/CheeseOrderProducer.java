package com.byf.disruptor.advance.cheesefactory.producer;

import com.byf.disruptor.advance.cheesefactory.order.CheeseOrder;
import com.byf.disruptor.advance.cheesefactory.order.CheeseOrderFactory;
import com.byf.disruptor.advance.cheesefactory.order.CheeseOrderTransfer;
import com.byf.disruptor.advance.cheesefactory.productionline.CheeseProductionLine;
import com.byf.disruptor.advance.cheesefactory.productionline.FermentProductionLine;
import com.byf.disruptor.advance.cheesefactory.productionline.MilkProductionLine;
import com.byf.disruptor.advance.trade.*;
import com.lmax.disruptor.*;
import com.lmax.disruptor.dsl.Disruptor;
import com.lmax.disruptor.dsl.EventHandlerGroup;
import com.lmax.disruptor.dsl.ProducerType;

import java.util.concurrent.*;

/**
 * 奶酪订单发布
 */
public class CheeseOrderProducer implements Runnable {
    // 线程同步辅助类 - 允许一个或多个线程一直等待
    CountDownLatch cdl;
    Disruptor disruptor;

    public CheeseOrderProducer(CountDownLatch cdl, Disruptor disruptor) {
        super();
        this.cdl = cdl;
        this.disruptor = disruptor;
    }

    public CheeseOrderProducer() {
        super();
        // TODO Auto-generated constructor stub
    }


    @Override
    public void run() {
        CheeseOrderTransfer th;
        try {
            //Event对象初始化类
            th = new CheeseOrderTransfer();
            //发布事件
            disruptor.publishEvent(th);
        } finally {
            // 递减锁存器的计数 -如果计数到达零，则释放所有等待的线程。
            cdl.countDown();
        }
    }

    // 定义环大小，2的倍数
    private static final int BUFFER_SIZE = 1024;
    // 定义处理事件的线程或线程池
    ExecutorService pool = Executors.newFixedThreadPool(7);

    /**
     * 批处理模式
     *
     * @throws Exception
     */
    public void BatchDeal() throws Exception {
        //创建一个单生产者的ringBuffer
        final RingBuffer<TransactionEvent> ringBuffer = RingBuffer.createSingleProducer(new EventFactory<TransactionEvent>() {

            @Override
            public TransactionEvent newInstance() {
                return new TransactionEvent();
            }
            //设置等待策略，YieldingWaitStrategy 的性能是最好的，适合用于低延迟的系统。
        }, BUFFER_SIZE, new YieldingWaitStrategy());
        //创建SequenceBarrier
        SequenceBarrier barrier = ringBuffer.newBarrier();
        //创建消息处理器
        BatchEventProcessor<TransactionEvent> eventProcessor = new BatchEventProcessor<TransactionEvent>(ringBuffer, barrier, new InnerDBHandler());
        //构造反向依赖，eventProcessor之间没有依赖关系则可以将Sequence直接加入
        ringBuffer.addGatingSequences(eventProcessor.getSequence());
        //提交消息处理器
        pool.submit(eventProcessor);
        //提交一个有返回值的任务用于执行，返回一个表示任务的未决结果的 Future。
        Future<Void> submit = pool.submit(new Callable<Void>() {
            //计算结果，如果无法计算结果则抛出异常
            @Override
            public Void call() throws Exception {
                long seq;
                for (int i = 0; i < 7000; i++) {
                    System.out.println("生产者：" + i);
                    //环里一个可用的区块
                    seq = ringBuffer.next();
                    //为环里的对象赋值
                    ringBuffer.get(seq).

                            setAmount(Math.random() * 10);
                    System.out.println("TransactionEvent:   " + ringBuffer.get(seq).

                            toString());
                    //发布这个区块的数据，
                    ringBuffer.publish(seq);
                }
                return null;
            }
        });
        //等待计算完成，然后获取其结果。
        submit.get();
        Thread.sleep(10000);
        //关闭消息处理器
        eventProcessor.halt();
        //关闭线程池
        pool.shutdown();
    }

    /**
     * 工作池模式
     *
     * @throws Exception
     */
    public void poolDeal() throws Exception {
        RingBuffer<CheeseOrder> ringBuffer = RingBuffer.createSingleProducer(new EventFactory<CheeseOrder>() {

            @Override
            public CheeseOrder newInstance() {
                return new CheeseOrder();
            }
        }, BUFFER_SIZE, new YieldingWaitStrategy());
        SequenceBarrier barrier = ringBuffer.newBarrier();
        //创建一个定长的线程池
        ExecutorService pool2 = Executors.newFixedThreadPool(5);
        //订单入库操作
        WorkHandler<CheeseOrder> milkProductionLine = new MilkProductionLine();
        ExceptionHandler arg2;
        WorkerPool<CheeseOrder> workerPool = new WorkerPool<CheeseOrder>(ringBuffer, barrier, new IgnoreExceptionHandler(), milkProductionLine);
        workerPool.start(pool2);
        long seq;
        for (int i = 0; i < 7; i++) {
            seq = ringBuffer.next();
            ringBuffer.get(seq);
            ringBuffer.publish(seq);
        }
        Thread.sleep(10000);
        workerPool.halt();
        pool2.shutdown();
    }

    /**
     * disruptor处理器用来组装生产者和消费者
     *
     * @throws Exception
     */
    public void disruptorManage() throws Exception {
        //创建用于处理事件的线程池
        NamedThreadFactory namedThreadFactory = new NamedThreadFactory();

        /*ThreadPoolExecutor pool2 = new ThreadPoolExecutor(5,
        20,
        1000,
        TimeUnit.MILLISECONDS,
        new LinkedBlockingQueue<>(1024),
                namedThreadFactory);*/
        ExecutorService pool2 = Executors.newFixedThreadPool(7);
                //创建disruptor对象
        /**
         * 用来指定数据生成者有一个还是多个，有两个可选值ProducerType.SINGLE和ProducerType.MULTI
         * BusySpinWaitStrategy是一种延迟最低，最耗CPU的策略。通常用于消费线程数小于CPU数的场景
         */
        Disruptor<CheeseOrder> disruptor2 = new Disruptor<CheeseOrder>(new CheeseOrderFactory(), BUFFER_SIZE, pool2, ProducerType.SINGLE, new BusySpinWaitStrategy());
        //创建消费者组，先执行生产牛奶和发酵粉
        EventHandlerGroup<CheeseOrder> eventsWith = disruptor2.handleEventsWith(new MilkProductionLine(), new FermentProductionLine());
        //在进行生产奶酪
        eventsWith.then(new CheeseProductionLine());
        //启动disruptor
        disruptor2.start();
        //在线程能通过 await()之前，必须调用 countDown() 的次数
        CountDownLatch latch = new CountDownLatch(1);
        //将封装好的TransactionEventProducer类提交
        pool2.submit(new CheeseOrderProducer(latch, disruptor2));
        //使当前线程在锁存器倒计数至零之前一直等待,以保证生产者任务完全消费掉
        latch.await();
        //关闭disruptor业务逻辑处理器
        disruptor2.shutdown();
        //销毁线程池
        pool2.shutdown();
    }
}