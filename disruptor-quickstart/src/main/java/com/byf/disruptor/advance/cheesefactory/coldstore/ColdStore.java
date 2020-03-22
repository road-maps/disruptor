package com.byf.disruptor.advance.cheesefactory.coldstore;

import com.byf.disruptor.advance.cheesefactory.model.Cheese;

import java.util.concurrent.ArrayBlockingQueue;
import java.util.concurrent.BlockingQueue;

public class ColdStore {
    private static volatile ColdStore coldStore = null;
    private static BlockingQueue<Cheese> store = new ArrayBlockingQueue(1000);
    private ColdStore(){}

    public static ColdStore getInstence()
    {
        if(coldStore ==null){
            synchronized (ColdStore.class){
                if(coldStore ==null)
                {
                    coldStore = new ColdStore();
                }

            }
        }
        return coldStore;
    }
    public BlockingQueue getStore()
    {
        return store;
    }
    public void addCheese(Cheese cheese)
    {
        try {
            store.put(cheese);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }
    public Cheese takeCheese()
    {
        Cheese obj = null;
        try {
            obj =  store.take();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        return obj;
    }

    public int size()
    {
        return this.getStore().size();
    }
}