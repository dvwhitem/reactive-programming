package com.dv.chapter_01.callbacks;

import com.dv.chapter_01.commons.Input;
import com.dv.chapter_01.commons.Output;

import java.util.function.Consumer;

public interface ShoppingCardService {
    void calculate(Input in, Consumer<Output> out);
}
