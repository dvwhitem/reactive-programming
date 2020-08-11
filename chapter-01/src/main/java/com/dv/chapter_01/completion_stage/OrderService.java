package com.dv.chapter_01.completion_stage;

import com.dv.chapter_01.commons.Input;
import lombok.extern.slf4j.Slf4j;

@Slf4j
public class OrderService {

    private final ShoppingCardService shoppingCardService;

    public OrderService(ShoppingCardService shoppingCardService) {
        this.shoppingCardService = shoppingCardService;
    }

    public void process() {
        Input input = new Input();

        shoppingCardService
                .calculate(input)
                .thenAccept(output -> log.info("{} execution completed", shoppingCardService.getClass().getSimpleName()));

        log.info("{} calculate called", shoppingCardService.getClass().getSimpleName());
    }
}
