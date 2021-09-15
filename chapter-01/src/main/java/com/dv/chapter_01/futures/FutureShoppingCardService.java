package com.dv.chapter_01.futures;

import com.dv.chapter_01.commons.Input;
import com.dv.chapter_01.commons.Output;

import java.util.concurrent.Future;
import java.util.concurrent.FutureTask;

public class FutureShoppingCardService implements ShoppingCardService {
    @Override
    public Future<Output> calculate(Input in) {
        FutureTask<Output> future = new FutureTask<>(() -> {
            Thread.sleep(10000);
            return new Output();
        });
        new Thread(future).start();
        return future;
    }
}
