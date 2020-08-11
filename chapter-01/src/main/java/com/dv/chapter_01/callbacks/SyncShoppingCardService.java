package com.dv.chapter_01.callbacks;

import com.dv.chapter_01.commons.Input;
import com.dv.chapter_01.commons.Output;

import java.util.function.Consumer;

public class SyncShoppingCardService implements ShoppingCardService {
    @Override
    public void calculate(Input in, Consumer<Output> out) {
        // No blocking operation, better to immediately provide answer
        out.accept(new Output());
    }
}
