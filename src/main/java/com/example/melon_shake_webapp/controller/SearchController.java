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
        SearchDataEmail searchDataEmail;
        if (session.getAttribute("userEmail") == null) {
            searchDataEmail = new SearchDataEmail(searchInput, "");
        } else {
            searchDataEmail = new SearchDataEmail(searchInput, (String) session.getAttribute("userEmail"));
        }
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
//            Map<String, List<List<String>>> searchResult = objectMapper.readValue(response.body(),new TypeReference<Map<String, List<List<String>>>>() {});
            Map<String,List<Map<String,String>>> searchResult = objectMapper.readValue("{\n" +
                    "    \"tracks\": [\n" +
                    "      {\n" +
                    "        \"name\": \"Secret Garden\",\n" +
                    "        \"img\": \"https://i.scdn.co/image/ab67616d0000b273dbd063ae065db06970b022d7\",\n" +
                    "        \"artist\": \"IU\",\n" +
                    "        \"duration\": \"03:44\"\n" +
                    "      },\n" +
                    "      {\n" +
                    "        \"name\": \"People Pt.2 (feat. IU)\",\n" +
                    "        \"img\": \"https://i.scdn.co/image/ab67616d0000b273bff049a2215c768b6432499f\",\n" +
                    "        \"artist\": \"Agust D, IU\",\n" +
                    "        \"duration\": \"03:33\"\n" +
                    "      },\n" +
                    "      {\n" +
                    "        \"name\": \"People Pt.2 (feat. IU)\",\n" +
                    "        \"img\": \"https://i.scdn.co/image/ab67616d0000b273fa9247b68471b82d2125651e\",\n" +
                    "        \"artist\": \"Agust D, IU\",\n" +
                    "        \"duration\": \"03:33\"\n" +
                    "      }\n" +
                    "    ],\n" +
                    "    \"albums\": [\n" +
                    "      {\n" +
                    "        \"name\": \"A flower bookmark\",\n" +
                    "        \"img\": \"https://i.scdn.co/image/ab67616d0000b273dbd063ae065db06970b022d7\",\n" +
                    "        \"artist\": \"IU\",\n" +
                    "        \"release_year\": \"2017-09-22\"\n" +
                    "      },\n" +
                    "      {\n" +
                    "        \"name\": \"People Pt.2 (feat. IU)\",\n" +
                    "        \"img\": \"https://i.scdn.co/image/ab67616d0000b273bff049a2215c768b6432499f\",\n" +
                    "        \"artist\": \"Agust D, IU\",\n" +
                    "        \"release_year\": \"2023-04-07\"\n" +
                    "      },\n" +
                    "      {\n" +
                    "        \"name\": \"D-DAY\",\n" +
                    "        \"img\": \"https://i.scdn.co/image/ab67616d0000b273fa9247b68471b82d2125651e\",\n" +
                    "        \"artist\": \"Agust D\",\n" +
                    "        \"release_year\": \"2023-04-21\"\n" +
                    "      }\n" +
                    "    ],\n" +
                    "    \"artists\": [\n" +
                    "      {\n" +
                    "        \"name\": \"IU\",\n" +
                    "        \"img\": \"https://i.scdn.co/image/ab6761610000e5eb006ff3c0136a71bfb9928d34\"\n" +
                    "      }\n" +
                    "    ]\n" +
                    "  }\n",new TypeReference<Map<String,List<Map<String,String>>>>() {});
            List<Map<String,String>> tracks = searchResult.get("tracks");
            List<Map<String,String>> albums = searchResult.get("albums");
            List<Map<String,String>> artists = searchResult.get("artists");
            System.out.println(searchResult);
            model.addAttribute("tracks",tracks);
            model.addAttribute("albums",albums);
            model.addAttribute("artists",artists);
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
    @GetMapping("/svelte")
    public String sveltePage(){
        return "index.html";
    }

}
