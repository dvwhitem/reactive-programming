package com.dv.conversion_problem;

import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpMethod;
import org.springframework.http.MediaType;
import org.springframework.http.converter.ByteArrayHttpMessageConverter;
import org.springframework.http.converter.HttpMessageConverter;
import org.springframework.http.converter.StringHttpMessageConverter;
import org.springframework.http.converter.json.MappingJackson2HttpMessageConverter;
import org.springframework.util.concurrent.ListenableFuture;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.client.AsyncRestTemplate;
import org.springframework.web.client.HttpMessageConverterExtractor;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.CompletionStage;

@RestController
@Slf4j
public class MyController {
    private final List<HttpMessageConverter<?>> messageConverters = new ArrayList<>();

    {
        this.messageConverters.add(new ByteArrayHttpMessageConverter());
        this.messageConverters.add(new StringHttpMessageConverter());
        this.messageConverters.add(new MappingJackson2HttpMessageConverter());
    }

    /**
     * определяется метод обработки запросов,
     * который действует асинхронно и  возвращает ListenableFuture
     * для обработки результата выполнения неблокиру-
     * ющим способом
     * @return ListenableFuture
     */
    @GetMapping(produces = MediaType.TEXT_PLAIN_VALUE)
    public ListenableFuture<?> requestData() {
        AsyncRestTemplate template = new AsyncRestTemplate();
        AsyncDatabaseClient databaseClient = new FakeAsyncDatabaseClient();

        //чтобы сохранить результат выполнения
        //AsyncRestTemplate, его нужно преобразовать в CompletionStage
        CompletionStage<String> completionStage = AsyncAdapters.toCompletion(
                template.execute("http://localhost:8080/hello",
                        HttpMethod.GET,
                        null,
                        new HttpMessageConverterExtractor<>(String.class, messageConverters)
                ));

        //чтобы удовлетворить поддерживаемый API, нужно результат сохра-
        //нения преобразовать в ListenableFuture
        log.info("Database client store {}", completionStage);
        return AsyncAdapters.toListenable(databaseClient.store(completionStage));
    }
}
