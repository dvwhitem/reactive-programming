package com.dv.chapter_01.imperative;

import com.dv.chapter_01.commons.Input;
import com.dv.chapter_01.commons.Output;

public class BlockingShoppingCardService implements ShoppingCardService {
    @Override
    public Output calculate(Input in) {
        try {
            Thread.sleep(5000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

        return new Output();
    }
}
