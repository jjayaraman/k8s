package com.jai.k8s.helloservice.controller;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import java.time.LocalTime;

@RestController
public class HelloController {

    @GetMapping(value = "/hello")
    public String hello() {
        return "Hello K8s " + LocalTime.now();
    }
}
