package com.dv.chapter_01.spring_futures;

import com.dv.chapter_01.commons.Example;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("api/v2/resource/b")
public class AsyncServiceTwo {

    @GetMapping
    public Example process() throws InterruptedException {
        Thread.sleep(3000);
        Example example = new Example();
        example.setName("Spring ListenableFuture example");
        return example;
    }
}
