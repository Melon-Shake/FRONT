package com.example.melon_shake_webapp.controller;
// ApiController.java

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api")
public class ApiController {

    @GetMapping("/data")
    public String getData() {
        // 원하는 데이터를 생성하거나 데이터베이스에서 가져와서 JSON 형식으로 반환
        return "{\"message\": \"Hello from Spring Boot!\"}";
    }
}



