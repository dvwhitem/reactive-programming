package com.dv.chapter_01.imperative;

import lombok.extern.slf4j.Slf4j;

@Slf4j
public class Main {

    public static void main(String[] args) {
        long start = System.currentTimeMillis();

        new OrdersService(new BlockingShoppingCardService()).process();
        new OrdersService(new BlockingShoppingCardService()).process();

        log.info("Total elapsed time in millis is : {}", (System.currentTimeMillis() - start));
    }
}
