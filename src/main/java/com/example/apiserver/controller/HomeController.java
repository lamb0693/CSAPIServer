package com.example.apiserver.controller;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Arrays;
import java.util.List;

@RestController
public class HomeController {
    @GetMapping("/")
    public List<Integer> home(){
        return Arrays.asList(5, 7 ,4);
    }
}
