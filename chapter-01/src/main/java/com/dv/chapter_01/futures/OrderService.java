package com.dv.chapter_01.futures;

import com.dv.chapter_01.commons.Input;
import com.dv.chapter_01.commons.Output;
import lombok.extern.slf4j.Slf4j;

import java.util.concurrent.ExecutionException;
import java.util.concurrent.Future;

@Slf4j
public class OrderService {

    private ShoppingCardService cardService;

    public OrderService(ShoppingCardService cardService) {
        this.cardService = cardService;
    }

    public void process() {
        Input input = new Input();
        Future<Output> result = cardService.calculate(input);
        log.info("{} execution completed", cardService.getClass().getSimpleName());

        try {
            result.get();
        } catch (InterruptedException | ExecutionException e) {
            e.printStackTrace();
        }
    }
}
