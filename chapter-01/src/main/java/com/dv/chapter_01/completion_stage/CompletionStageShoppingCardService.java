package com.dv.chapter_01.completion_stage;

import com.dv.chapter_01.commons.Input;
import com.dv.chapter_01.commons.Output;

import java.util.concurrent.CompletableFuture;
import java.util.concurrent.CompletionStage;

public class CompletionStageShoppingCardService implements ShoppingCardService {
    @Override
    public CompletionStage<Output> calculate(Input in) {
        return CompletableFuture.supplyAsync(() -> {
            try {
                Thread.sleep(4000);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            return new Output();
        });
    }
}
