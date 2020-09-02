package com.dv.conversion_problem;

import lombok.extern.slf4j.Slf4j;
import org.springframework.util.concurrent.ListenableFuture;
import org.springframework.util.concurrent.SettableListenableFuture;

import java.util.concurrent.CompletableFuture;
import java.util.concurrent.CompletionStage;

@Slf4j
public class AsyncAdapters {
    /**
     * Метод преобразования экземпляра ListenableFuture в  CompletionStage.
     */
    public static <T> CompletionStage<T> toCompletion(ListenableFuture<T> future) {
        //чтобы обеспечить ручное управление экземпляром
        //CompletionStage, создается его прямая реализация CompletableFuture вы-
        //зовом конструктора без аргументов.
        CompletableFuture<T> completableFuture = new CompletableFuture<>();
        //Для интеграции с  ListenableFuture
        //нужно добавить обратный вызов
        future.addCallback(completableFuture::complete, completableFuture::completeExceptionally);

        return completableFuture;
    }

    /**
     * Метод преобразования экземпляра CompletionStage в ListenableFuture
     */
    public static <T> ListenableFuture<T> toListenable(CompletionStage<T> stage) {
        //объявляется специализированная реализация Listenable
        //Future – SettableListenableFuture.
        SettableListenableFuture<T> future = new SettableListenableFuture<>();
        //Она позволяет вручную передать результат
        // выполнения CompletionStage
        stage
                .whenComplete((v, t) -> {
                    if (t == null) {
                        log.info("stage.whenComplete(v:{})", v);
                        future.set(v);
                    } else {
                        log.info("stage.whenComplete(t:{})", t);
                        future.setException(t);
                    }
                });

        return future;
    }

}
