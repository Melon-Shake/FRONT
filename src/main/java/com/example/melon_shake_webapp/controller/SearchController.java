package com.example.melon_shake_webapp.controller;

import com.example.melon_shake_webapp.Service.SearchDetailService;
import com.example.melon_shake_webapp.data.SearchData;
import com.example.melon_shake_webapp.data.SearchDataEmail;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.servlet.http.HttpSession;
import lombok.RequiredArgsConstructor;
import org.postgresql.jdbc.PgArray;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.io.IOException;
import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.nio.charset.StandardCharsets;
import java.sql.Array;
import java.sql.SQLException;
import java.util.*;

import static java.lang.Math.round;

@RequiredArgsConstructor
@Controller
public class SearchController {
    private final HttpClient client = HttpClient.newHttpClient();
    private final ObjectMapper objectMapper = new ObjectMapper();

    @Autowired
    private SearchDetailService searchDetailService;

    @GetMapping("/search")
    public String search_page(){
        return "search";
    }

    @PostMapping("/search")
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
            Map<String,List<Map<String,String>>> searchResult = objectMapper.readValue("{\n" +
                    "    \"tracks\": [\n" +
                    "      {\n" +
                    "        \"id\": \"5F6nAnNIsRk9QbPOx9t11B\",\n"+
                    "        \"album_id\": \"4B3UIkrohpUIxyVCCgLrEI\",\n"+
                    "        \"name\": \"Secret Garden\",\n" +
                    "        \"img\": \"https://i.scdn.co/image/ab67616d0000b273dbd063ae065db06970b022d7\",\n" +
                    "        \"artist\": \"IU\",\n" +
                    "        \"duration\": \"03:44\"\n" +
                    "      },\n" +
                    "      {\n" +
                    "        \"id\": \"4EaQ0ouIydfeAgQUz284EF\",\n"+
                    "        \"album_id\": \"1l12B55qdesQorPcQLJDRo\",\n"+
                    "        \"name\": \"People Pt.2 (feat. IU)\",\n" +
                    "        \"img\": \"https://i.scdn.co/image/ab67616d0000b273bff049a2215c768b6432499f\",\n" +
                    "        \"artist\": \"Agust D, IU\",\n" +
                    "        \"duration\": \"03:33\"\n" +
                    "      },\n" +
                    "      {\n" +
                    "        \"id\": \"4EaQ0ouIydfeAgQUz284EF\",\n"+
                    "        \"album_id\": \"446ROKmKfpEwkbi2SjELVX\",\n"+
                    "        \"name\": \"People Pt.2 (feat. IU)\",\n" +
                    "        \"img\": \"https://i.scdn.co/image/ab67616d0000b273fa9247b68471b82d2125651e\",\n" +
                    "        \"artist\": \"Agust D, IU\",\n" +
                    "        \"duration\": \"03:33\"\n" +
                    "      }\n" +
                    "    ],\n" +
                    "    \"albums\": [\n" +
                    "      {\n" +
                    "        \"id\": \"4B3UIkrohpUIxyVCCgLrEI\",\n"+
                    "        \"name\": \"A flower bookmark\",\n" +
                    "        \"img\": \"https://i.scdn.co/image/ab67616d0000b273dbd063ae065db06970b022d7\",\n" +
                    "        \"artist\": \"IU\",\n" +
                    "        \"release_year\": \"2017-09-22\"\n" +
                    "      },\n" +
                    "      {\n" +
                    "        \"id\": \"1l12B55qdesQorPcQLJDRo\",\n"+
                    "        \"name\": \"People Pt.2 (feat. IU)\",\n" +
                    "        \"img\": \"https://i.scdn.co/image/ab67616d0000b273bff049a2215c768b6432499f\",\n" +
                    "        \"artist\": \"Agust D, IU\",\n" +
                    "        \"release_year\": \"2023-04-07\"\n" +
                    "      },\n" +
                    "      {\n" +
                    "        \"id\": \"446ROKmKfpEwkbi2SjELVX\",\n"+
                    "        \"name\": \"D-DAY\",\n" +
                    "        \"img\": \"https://i.scdn.co/image/ab67616d0000b273fa9247b68471b82d2125651e\",\n" +
                    "        \"artist\": \"Agust D\",\n" +
                    "        \"release_year\": \"2023-04-21\"\n" +
                    "      }\n" +
                    "    ],\n" +
                    "    \"artists\": [\n" +
                    "      {\n" +
                    "        \"id\": \"3HqSLMAZ3g3d5poNaI7GOU\",\n"+
                    "        \"name\": \"IU\",\n" +
                    "        \"img\": \"https://i.scdn.co/image/ab6761610000e5eb006ff3c0136a71bfb9928d34\"\n" +
                    "      }\n" +
                    "    ]\n" +
                    "  }\n",new TypeReference<Map<String,List<Map<String,String>>>>() {});
            List<Map<String,String>> tracks = searchResult.get("tracks");
            List<Map<String,String>> albums = searchResult.get("albums");
            List<Map<String,String>> artists = searchResult.get("artists");
//            System.out.println(searchResult);
            model.addAttribute("tracks",tracks);
            model.addAttribute("albums",albums);
            model.addAttribute("artists",artists);
            if (searchResult.isEmpty()){

                return "searchError";
            }
//            long end2_time = System.currentTimeMillis();
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
//    @GetMapping("/svelte")
//    public String sveltePage(){
//        return "index.html";
//    }
    @GetMapping("/search/track/{track_id}/{album_id}")
    public String searchDetailPage(
            @PathVariable(value = "track_id") String track_id,
            @PathVariable(value = "album_id") String album_id,
            Model model,
            HttpSession session
    ) throws SQLException {
        List<Map<String,Object>> artists_info = new ArrayList<>();
     List<List<Map<String,Object>>> tracksWithArtist = new ArrayList<>();
        model.addAttribute("userName",(String) session.getAttribute("userName"));

        List<Map<String,Object>> track_info = searchDetailService.getTrack(track_id,album_id);
        List<Map<String,Object>> album_info = searchDetailService.getAlbum(album_id);
        List<Map<String, Object>> tracksInAlbum = searchDetailService.getTracksInAlbum(album_id);
        PgArray pgArray = (PgArray) track_info.get(0).get("artists_ids");
        List<String> artists_id =Arrays.asList((String[]) pgArray.getArray());
        for (int i=0; i<artists_id.size();i++){
            artists_info.add(searchDetailService.getArtist(artists_id.get(i)).get(0));
            tracksWithArtist.add(searchDetailService.getTracksWithArtist(artists_id.get(i)));
        }
//        System.out.println("================================");
//        System.out.println(tracksWithArtist);


        List<Map<String, Object>> lyrics = searchDetailService.getLyrics(track_id);
        List<Map<String, Object>> features = searchDetailService.getFeatures(track_id);
//        곡 재생시간 m:ss 형태로 변환
//        검색곡
        Integer duration_m1 = (Integer)track_info.get(0).get("duration_ms")/60000;
        Integer duration_s1 = (Integer)track_info.get(0).get("duration_ms")%60000/1000;
        String duration1 = duration_m1 + ":" + duration_s1;
        track_info.get(0).put("duration",duration1);
//        앨범 수록곡
        for(int i=0;i<tracksInAlbum.size();i++) {
            Integer duration_m2 = (Integer) tracksInAlbum.get(i).get("duration_ms") / 60000;
            Integer duration_s2 = (Integer) tracksInAlbum.get(i).get("duration_ms") % 60000 / 1000;
            String duration2 = duration_m2 + ":" + duration_s2;
            tracksInAlbum.get(i).put("duration", duration2);
        }
//        아티스트의 다른 곡
        for(int i=0;i<tracksWithArtist.size();i++) {
            for(int j=0; j<tracksWithArtist.get(i).size();j++) {
                tracksWithArtist.get(i).get(j).put("artistName",(String)artists_info.get(i).get("name"));
                tracksWithArtist.get(i).get(j).put("imgUrl",(String)artists_info.get(i).get("images_url"));
                Integer duration_m3 = (Integer) tracksWithArtist.get(i).get(j).get("duration_ms") / 60000;
                Integer duration_s3 = (Integer) tracksWithArtist.get(i).get(j).get("duration_ms") % 60000 / 1000;
                String duration3 = duration_m3 + ":" + duration_s3;
                tracksWithArtist.get(i).get(j).put("duration", duration3);
            }
        }

//        발매 날짜를 발매 년도로 변환
        String release_date_str = (String)album_info.get(0).get("release_date");
        if (release_date_str != null) {
            String[] release_date = release_date_str.split("-");
            String release_year = release_date[0];
            album_info.get(0).put("release_year",release_year);
        }
        else{
            System.out.println("발매일 데이터가 없습니다.");
        }

        model.addAttribute("track_info",track_info);
        model.addAttribute("album_info",album_info);
        model.addAttribute("artists_info",artists_info);
        model.addAttribute("lyrics",lyrics);
        model.addAttribute("features",features);
        model.addAttribute("tracksInAlbum",tracksInAlbum);
        model.addAttribute("tracksWithArtist",tracksWithArtist);


        return "searchTrack";
    }


}
