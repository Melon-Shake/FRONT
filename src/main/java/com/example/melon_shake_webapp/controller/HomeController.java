package com.example.melon_shake_webapp.controller;

import com.example.melon_shake_webapp.Repository.*;
import com.example.melon_shake_webapp.Service.FastApiService;
import com.example.melon_shake_webapp.Service.SearchDetailService;
import com.example.melon_shake_webapp.data.*;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.servlet.http.HttpSession;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.parameters.P;
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
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RequiredArgsConstructor
@Controller
public class HomeController {
    @Autowired
    private SearchDetailService searchDetailService;
    private final HttpClient client = HttpClient.newHttpClient();
    private final ObjectMapper objectMapper = new ObjectMapper();
    private final BugsChartRepository bugsChartRepository;
    private final MelonChartRepository melonChartRepository;
    private final VibeChartRepository vibeChartRepository;
    private final FloChartRepository floChartRepository;
    private final GenieChartRepository genieChartRepository;
    private final TotalChartRepository totalChartRepository;

    @GetMapping("/")
    public String default_page() {

        return "redirect:/Home";
    }

    @GetMapping("/Home")
    public String home_page(Model model, HttpSession session) {
        model.addAttribute("userName", (String) session.getAttribute("userName")); // 세션 정보 전달

        List<ChartBugs> chartBugs = bugsChartRepository.findTop100ChartBugs();
        List<ChartFlo> chartFlo = floChartRepository.findTop100ChartFlo();
        List<ChartVibe> chartVibe = vibeChartRepository.findTop100ChartVibe();
        List<ChartMelon> chartMelon = melonChartRepository.findTop100ChartMelon();
        List<ChartGenie> chartGenie = genieChartRepository.findTop100ChartGenie();
        List<TotalChart> totalChart = totalChartRepository.findTop100TotalChart();


        model.addAttribute("chartFlo", chartFlo);
        model.addAttribute("chartVibe", chartVibe);
        model.addAttribute("chartGenie", chartGenie);
        model.addAttribute("chartMelon", chartMelon);
        model.addAttribute("chartBugs", chartBugs);
        model.addAttribute("totalChart", totalChart);

        HttpRequest request2 = HttpRequest.newBuilder()
//                .uri(URI.create("http://192.168.70.60:8000/daily_search_ranking/"))
                .uri(URI.create("http://ec2-3-114-214-196.ap-northeast-1.compute.amazonaws.com:8000/daily_search_ranking/"))
                .POST(HttpRequest.BodyPublishers.ofString(""))
                .build();


        try {
            HttpResponse<String> response2 = client.send(request2, HttpResponse.BodyHandlers.ofString(StandardCharsets.UTF_8));
            System.out.println(response2);
            Map<String,String> keyword_ranking_chart = objectMapper.readValue(response2.body(),new TypeReference<Map<String,String>>() {});
            model.addAttribute("keyword_ranking_chart",keyword_ranking_chart);
            // 예외가 발생하지 않은 경우 이후의 로직을 작성
        } catch (IOException | InterruptedException e) {
            // 예외 처리 로직
            e.printStackTrace(); // 예외 정보 출력
            return "redirect:/Home";
        }

//        ====================================================
        if (session.getAttribute("userEmail") != null) {
            PlaylistData playlistData = new PlaylistData((String) session.getAttribute("userEmail"));
            String jsonBody;
//
            try {
                jsonBody = objectMapper.writeValueAsString(playlistData);

            } catch (Exception e) {
                e.printStackTrace();
                return "error";
            }
            HttpRequest request = HttpRequest.newBuilder()
                    .uri(URI.create("http://192.168.70.61:8000/playlist/"))
//                .uri(URI.create("http://ec2-3-114-214-196.ap-northeast-1.compute.amazonaws.com:8000/daily_search_ranking/"))
                    .POST(HttpRequest.BodyPublishers.ofString(jsonBody))
                    .header("Content-Type", "application/json")
                    .build();

            try {
                HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString(StandardCharsets.UTF_8));
                List<String> playlist = objectMapper.readValue(response.body(), new TypeReference<List<String>>() {
                });
                System.out.println(response.body());
                List<List<Map<String,Object>>> albumImgs = new ArrayList<>();
                List<List<Map<String,Object>>> artistsNames = new ArrayList<>();
                List<List<Map<String,Object>>> track_Ps = new ArrayList<>();
                List<List<Map<String,Object>>> All_infos = new ArrayList<>();
                for (int i = 0; i<playlist.size(); i++) {
                    List<Map<String, Object>> all_info = searchDetailService.getAllByTrack(playlist.get(i));
                    All_infos.add(all_info);
                    List<Map<String, Object>> albumImage = searchDetailService.getAlbumImage(playlist.get(i));
                    List<Map<String, Object>> artistsName = searchDetailService.getArtistsName(playlist.get(i));
                    List<Map<String, Object>> track_P = searchDetailService.getTrack_P(playlist.get(i));

                    Integer duration_m = (Integer) track_P.get(0).get("duration_ms") / 60000;
                    Integer duration_s = (Integer) track_P.get(0).get("duration_ms") % 60000 / 1000;
                    String duration = duration_m + ":" + String.format("%02d", duration_s);
                    track_P.get(0).put("duration", duration);

                    albumImgs.add(albumImage);
                    artistsNames.add(artistsName);
                    track_Ps.add(track_P);
                }
                model.addAttribute("albumImgs",albumImgs);
                model.addAttribute("artistsNames",artistsNames);
                model.addAttribute("track_Ps",track_Ps);
                model.addAttribute("All_infos",All_infos);
                System.out.println(albumImgs);
                System.out.println(artistsNames);
                System.out.println(track_Ps);


                // 예외가 발생하지 않은 경우 이후의 로직을 작성
            } catch (IOException | InterruptedException e) {
                // 예외 처리 로직
                e.printStackTrace(); // 예외 정보 출력
                return "redirect:/Home";
            }
//        List<Map<String,Object>> albumImage = searchDetailService.getAlbumImage();

            return "home";
        }
        return "home";
    }
}

