package com.example.melon_shake_webapp.controller;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;

import java.io.IOException;
import java.net.URI;
import java.net.http.HttpClient;

import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.nio.charset.StandardCharsets;
import java.util.List;
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
    public String home_page(Model model){

        HttpRequest request = HttpRequest.newBuilder()
                .uri(URI.create("http://192.168.70.41:8000/chart/melon_chart/"))
//                .uri(URI.create("http://192.168.70.67:8121/"))
                .POST(HttpRequest.BodyPublishers.ofString(""))
                .build();
        HttpRequest request2 = HttpRequest.newBuilder()
//                .uri(URI.create("http://192.168.70.65:9799/chart/melon_chart/"))
                .uri(URI.create("http://192.168.70.41:8000/daily_search_ranking/"))
                .POST(HttpRequest.BodyPublishers.ofString(""))
                .build();

        try {
            HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString(StandardCharsets.UTF_8));
            System.out.println(response);
            Map<String,List<String>> track_ranking_chart = objectMapper.readValue(response.body(),new TypeReference<Map<String,List<String>>>() {});
            System.out.println(track_ranking_chart);
            model.addAttribute("track_ranking_chart",track_ranking_chart);
            // 예외가 발생하지 않은 경우 이후의 로직을 작성
        } catch (IOException | InterruptedException e) {
            // 예외 처리 로직
            e.printStackTrace(); // 예외 정보 출력
            return "redirect:/Home";
        }

        try {
            HttpResponse<String> response2 = client.send(request2, HttpResponse.BodyHandlers.ofString(StandardCharsets.UTF_8));
            System.out.println(response2);
            Map<String,String> keyword_ranking_chart = objectMapper.readValue(response2.body(),new TypeReference<Map<String,String>>() {});
            System.out.println(keyword_ranking_chart);
            model.addAttribute("keyword_ranking_chart",keyword_ranking_chart);
            // 예외가 발생하지 않은 경우 이후의 로직을 작성
        } catch (IOException | InterruptedException e) {
            // 예외 처리 로직
            e.printStackTrace(); // 예외 정보 출력
            return "redirect:/Home";
        }
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
