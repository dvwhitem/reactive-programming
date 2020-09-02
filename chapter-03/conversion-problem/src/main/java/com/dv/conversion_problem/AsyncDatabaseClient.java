package com.dv.conversion_problem;

import java.util.concurrent.CompletionStage;

public interface AsyncDatabaseClient {
    <T> CompletionStage<T> store(CompletionStage<T> stage);
}
