package com.dv.chapter_01.callbacks;

import com.dv.chapter_01.commons.Input;
import com.dv.chapter_01.commons.Output;

import java.util.function.Consumer;

public class AsyncShoppingCardService implements ShoppingCardService {
    @Override
    public void calculate(Input in, Consumer<Output> out) {
        // blocking operation is presented, better to provide answer asynchronously
        new Thread(() -> {
            try {
                Thread.sleep(3000);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }

            out.accept(new Output());
        }).start();
    }
}
