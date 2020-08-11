package com.dv.chapter_01.completion_stage;

import com.dv.chapter_01.commons.Input;
import com.dv.chapter_01.commons.Output;

import javax.swing.text.html.Option;
import java.util.concurrent.CompletionStage;

public interface ShoppingCardService {
    CompletionStage<Output>calculate(Input in);
}
