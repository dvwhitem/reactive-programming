package com.dv.chapter_01.callbacks;

import lombok.extern.slf4j.Slf4j;

@Slf4j
public class Main {

    public static void main(String[] args) throws InterruptedException {
        long start= System.currentTimeMillis();

        OrdersService ordersServiceAsync = new OrdersService(new AsyncShoppingCardService());
        OrdersService ordersServiceSync = new OrdersService(new SyncShoppingCardService());

        ordersServiceAsync.process();
        ordersServiceAsync.process();
        ordersServiceSync.process();

        log.info("Total elapsed time in millis is : {}", (System.currentTimeMillis() - start));

        Thread.sleep(2000);
    }
}
