package com.dv.jdk9;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class Jdk9Controller {

    @GetMapping("/")
    public String home() {
        return "Welcome";
    }
}
