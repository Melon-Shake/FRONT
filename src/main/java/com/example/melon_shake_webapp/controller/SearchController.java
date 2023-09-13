package com.example.melon_shake_webapp.controller;

import com.example.melon_shake_webapp.data.SearchData;
import com.example.melon_shake_webapp.data.SearchDataEmail;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.servlet.http.HttpSession;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

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
public class SearchController {
    private final HttpClient client = HttpClient.newHttpClient();
    private final ObjectMapper objectMapper = new ObjectMapper();

    @GetMapping("/search")
    public String search_page(){
        return "search";
    }

    @PostMapping("/search/more_info/in-process")
    public String search_more_info_page(){
        return "search_more_info";
    }


    @PostMapping("/search/in-process")
    public String show_search(@RequestParam("searchInput") String searchInput, Model model, HttpSession session){
//        long start_time = System.currentTimeMillis();
        model.addAttribute("userName",(String) session.getAttribute("userName")); // 세션 정보 전달
        model.addAttribute("searchInput",searchInput);

        SearchData searchData = new SearchData(searchInput);
        SearchDataEmail searchDataEmail = new SearchDataEmail(searchInput,"");
        String jsonBody;
        String jsonBody2;

        try {
            jsonBody = objectMapper.writeValueAsString(searchData);
        } catch (Exception e) {
            e.printStackTrace();
            return "error";
        }

        try {
            jsonBody2 = objectMapper.writeValueAsString(searchDataEmail);
        } catch (Exception e) {
            e.printStackTrace();
            return "error";
        }

//        long end_time = System.currentTimeMillis();
//        System.out.println(end_time - start_time);
        HttpRequest request = HttpRequest.newBuilder()
                .uri(URI.create("http://ec2-3-114-214-196.ap-northeast-1.compute.amazonaws.com:8000/search/track/"))
//                .uri(URI.create("http://192.168.70.60:8000/search/track/"))
                .POST(HttpRequest.BodyPublishers.ofString(jsonBody))
                .header("Content-Type", "application/json")
                .build();
        HttpRequest request2 = HttpRequest.newBuilder()
                .uri(URI.create("http://ec2-3-114-214-196.ap-northeast-1.compute.amazonaws.com:8000/get_keyword_data/"))
//                .uri(URI.create("http://192.168.70.60:8000/get_keyword_data/"))
                .POST(HttpRequest.BodyPublishers.ofString(jsonBody2))
                .header("Content-Type", "application/json")
                .build();


        try {
            HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString(StandardCharsets.UTF_8));
            System.out.println(response);
//            System.out.println(response.body());
            Map<String, List<List<String>>> searchResult = objectMapper.readValue(response.body(),new TypeReference<Map<String, List<List<String>>>>() {});
            model.addAttribute("searchResult",searchResult);
            if (searchResult.isEmpty()){

                return "searchError";
            }
            long end2_time = System.currentTimeMillis();
//            System.out.println(end2_time - start_time);

            // 예외가 발생하지 않은 경우 이후의 로직을 작성
        } catch (IOException | InterruptedException e) {
            // 예외 처리 로직
            e.printStackTrace(); // 예외 정보 출력
            return "redirect:/Home";
        }

        try {
            HttpResponse<String> response2 = client.send(request2, HttpResponse.BodyHandlers.ofString(StandardCharsets.UTF_8));
            System.out.println(response2);
            // 예외가 발생하지 않은 경우 이후의 로직을 작성
        } catch (IOException | InterruptedException e) {
            // 예외 처리 로직
            e.printStackTrace(); // 예외 정보 출력
            return "redirect:/Home";
        }

        return "search";
    }


}
