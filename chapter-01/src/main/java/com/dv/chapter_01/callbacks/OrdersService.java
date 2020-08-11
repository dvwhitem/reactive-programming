package com.dv.chapter_01.callbacks;

import com.dv.chapter_01.commons.Input;
import lombok.extern.slf4j.Slf4j;

@Slf4j
public class OrdersService {

    private final ShoppingCardService cardService;

    public OrdersService(ShoppingCardService cardService) {
        this.cardService = cardService;
    }

    void process() {
        Input input = new Input();
        cardService.calculate(input, output -> log.info("{} execution completed", cardService.getClass().getSimpleName()));
    }
}
