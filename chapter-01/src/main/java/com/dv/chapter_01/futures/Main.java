package com.dv.chapter_01.futures;

import lombok.extern.slf4j.Slf4j;

@Slf4j
public class Main {

    public static void main(String[] args) {
        long start = System.currentTimeMillis();

        OrderService orderService = new OrderService(new FutureShoppingCardService());

        orderService.process();
        orderService.process();

        log.info("Total elapsed time in millis is : {}", (System.currentTimeMillis() - start));

    }
}
