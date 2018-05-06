package com.example.demo;

import java.util.concurrent.atomic.AtomicLong;

import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class GreetingController {

    private static final String template = "Hello, %s!";
    private final AtomicLong counter = new AtomicLong();

    @RequestMapping("/api/greeting")
    public Greeting greeting(
            @RequestParam(value="name", defaultValue="World") String name,
            @RequestParam(value="annee", defaultValue="") int annee,
            @RequestParam(value="lastname") String lastname) {
        return new Greeting(counter.incrementAndGet(), String.format(template, name), annee, lastname);
    }
}

