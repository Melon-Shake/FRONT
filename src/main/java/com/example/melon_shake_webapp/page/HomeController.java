package com.example.melon_shake_webapp.page;

import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;

import java.net.http.HttpClient;

import java.util.Map;

@RequiredArgsConstructor
@Controller
public class HomeController {
    private final HttpClient client = HttpClient.newHttpClient();
    private final ObjectMapper objectMapper = new ObjectMapper();

    @GetMapping("/")
    public String default_page(){

        return "redirect:/Home";
    }
    @GetMapping("/Home")
    public String home_page(){

        return "home";
    }
    
    @PostMapping("/")
    public ResponseEntity<String> track_ranking_chart(@RequestBody Map<String,String> trackRanking ){

        System.out.println(trackRanking.get("aa"));

        return ResponseEntity.ok("success");
    }

//    @PostMapping("/login")
//    public String
}
