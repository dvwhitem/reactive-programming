package com.dv.chapter_01.futures;

import com.dv.chapter_01.commons.Input;
import com.dv.chapter_01.commons.Output;

import java.util.concurrent.Future;

public interface ShoppingCardService {
    Future<Output> calculate(Input in);
}
