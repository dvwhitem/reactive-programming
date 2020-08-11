package com.dv.chapter_01.completion_stage;

import lombok.extern.slf4j.Slf4j;

@Slf4j
public class Main {

    public static void main(String[] args) throws InterruptedException {
        long start = System.currentTimeMillis();

        OrderService orderService = new OrderService(new CompletionStageShoppingCardService());

        orderService.process();
        orderService.process();

        log.info("Total elapsed time in millis is : {}", (System.currentTimeMillis() - start));
        Thread.sleep(8000);
    }
}
