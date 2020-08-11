package com.dv.chapter_01.imperative;

import com.dv.chapter_01.commons.Input;
import lombok.extern.slf4j.Slf4j;

@Slf4j
public class OrdersService {

    private final ShoppingCardService cardService;

    public OrdersService(ShoppingCardService cardService) {
        this.cardService = cardService;
    }

    public void process() {
        Input input = new Input();
        cardService.calculate(input);
        log.info("{} execution completed", cardService.getClass().getSimpleName());
    }
}
