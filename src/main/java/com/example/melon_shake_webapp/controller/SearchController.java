package com.example.melon_shake_webapp.controller;

import com.example.melon_shake_webapp.Service.SearchDetailService;
import com.example.melon_shake_webapp.data.SearchData;
import com.example.melon_shake_webapp.data.SearchDataEmail;
import com.example.melon_shake_webapp.data.SearchLogEmail;
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
                .uri(URI.create("http://ec2-3-114-214-196.ap-northeast-1.compute.amazonaws.com:8000/search/"))
//                .uri(URI.create("http://192.168.70.60:8000/search/track/"))
                .POST(HttpRequest.BodyPublishers.ofString(jsonBody))
                .header("Content-Type", "application/json")
                .build();
        HttpRequest request3 = HttpRequest.newBuilder()
                .uri(URI.create("http://ec2-3-114-214-196.ap-northeast-1.compute.amazonaws.com:8000/load/"))
//                .uri(URI.create("http://192.168.70.60:8000/get_keyword_data/"))
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
            client.send(request3, HttpResponse.BodyHandlers.ofString(StandardCharsets.UTF_8));
            Map<String,List<Map<String,String>>> searchResult = objectMapper.readValue(response.body(),new TypeReference<Map<String,List<Map<String,String>>>>() {});
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

    @GetMapping("/search/track/{track_id}/{album_id}")
    public String searchTrack(
            @PathVariable(value = "track_id") String track_id,
            @PathVariable(value = "album_id") String album_id,
            Model model,
            HttpSession session
    ) throws SQLException {
        List<Map<String,Object>> artists_info = new ArrayList<>();
        List<List<Map<String,Object>>> tracksWithArtist = new ArrayList<>();
        List<List<Map<String, Object>>> albumsWithArtist = new ArrayList<>();
        model.addAttribute("userName",(String) session.getAttribute("userName"));
        List<Map<String, Object>> tracksInAlbumWOArtists = searchDetailService.getTracksInAlbumWOArtist(album_id);
        List<Map<String,Object>> track_info = searchDetailService.getTrack(track_id,album_id);
        List<Map<String,Object>> album_info = searchDetailService.getAlbum(album_id);
        List<Map<String, Object>> tracksInAlbum = searchDetailService.getTracksInAlbum(album_id);
        PgArray pgArray = (PgArray) track_info.get(0).get("artists_ids");
        List<String> artists_id =Arrays.asList((String[]) pgArray.getArray());
        for (int i=0; i<artists_id.size();i++){
            artists_info.add(searchDetailService.getArtist(artists_id.get(i)).get(0));
            tracksWithArtist.add(searchDetailService.getTracksWithArtist(artists_id.get(i)));
            albumsWithArtist.add(searchDetailService.getAlbumsWithArtist(artists_id.get(i)));
        }
//        System.out.println("================================");
//        System.out.println(tracksWithArtist);


        List<Map<String, Object>> lyrics = searchDetailService.getLyrics(track_id);
        List<Map<String, Object>> features = searchDetailService.getFeatures(track_id);
//        곡 재생시간 m:ss 형태로 변환
//        검색곡
        Integer duration_m1 = (Integer)track_info.get(0).get("duration_ms")/60000;
        Integer duration_s1 = (Integer)track_info.get(0).get("duration_ms")%60000/1000;
        String duration1 = duration_m1 + ":" + String.format("%02d",duration_s1);
        track_info.get(0).put("duration",duration1);
        for (int i=0;i<tracksInAlbumWOArtists.size();i++) {
            Integer duration_m4 = (Integer) tracksInAlbumWOArtists.get(i).get("duration_ms") / 60000;
            Integer duration_s4 = (Integer) tracksInAlbumWOArtists.get(i).get("duration_ms") % 60000 / 1000;
            String duration4 = duration_m4 + ":" + String.format("%02d", duration_s4);
            tracksInAlbumWOArtists.get(i).put("duration", duration4);
        }
//        앨범 수록곡
        for(int i=0;i<tracksInAlbum.size();i++) {
            Integer duration_m2 = (Integer) tracksInAlbum.get(i).get("duration_ms") / 60000;
            Integer duration_s2 = (Integer) tracksInAlbum.get(i).get("duration_ms") % 60000 / 1000;
            String duration2 = duration_m2 + ":" + String.format("%02d",duration_s2);
            tracksInAlbum.get(i).put("duration", duration2);
        }
//        아티스트의 다른 곡
        for(int i=0;i<tracksWithArtist.size();i++) {
            for(int j=0; j<tracksWithArtist.get(i).size();j++) {
                Integer duration_m3 = (Integer) tracksWithArtist.get(i).get(j).get("duration_ms") / 60000;
                Integer duration_s3 = (Integer) tracksWithArtist.get(i).get(j).get("duration_ms") % 60000 / 1000;
                String duration3 = duration_m3 + ":" + String.format("%02d",duration_s3);
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

        System.out.println("===============");
        System.out.println(albumsWithArtist);
        model.addAttribute("albumsWithArtist",albumsWithArtist);
        model.addAttribute("tracksInAlbumWOArtists",tracksInAlbumWOArtists);
        model.addAttribute("track_info",track_info);
        model.addAttribute("album_info",album_info);
        model.addAttribute("artists_info",artists_info);
        model.addAttribute("lyrics",lyrics);
        model.addAttribute("features",features);
        model.addAttribute("tracksInAlbum",tracksInAlbum);
        model.addAttribute("tracksWithArtist",tracksWithArtist);

        String track_title = (String)track_info.get(0).get("name");
        SearchLogEmail searchLogEmail;
        if (session.getAttribute("userEmail") != null) {
            searchLogEmail = new SearchLogEmail((String) session.getAttribute("userEmail"),track_title);
            String jsonBody;
            System.out.println(searchLogEmail);

            try {
                jsonBody = objectMapper.writeValueAsString(searchLogEmail);
            } catch (Exception e) {
                e.printStackTrace();
                return "error";
            }

            HttpRequest request = HttpRequest.newBuilder()
                .uri(URI.create("http://ec2-3-114-214-196.ap-northeast-1.compute.amazonaws.com:8000/search/track/"))
//                    .uri(URI.create("http://192.168.70.61:8000/get_use_data/"))
                    .POST(HttpRequest.BodyPublishers.ofString(jsonBody))
                    .header("Content-Type", "application/json")
                    .build();
            try {
                HttpResponse<String> response2 = client.send(request, HttpResponse.BodyHandlers.ofString(StandardCharsets.UTF_8));
                System.out.println(response2);
                // 예외가 발생하지 않은 경우 이후의 로직을 작성
            } catch (IOException | InterruptedException e) {
                // 예외 처리 로직
                e.printStackTrace(); // 예외 정보 출력

            }

        }



        return "searchTrack";
    }

    @GetMapping("/search/album/{album_id}")
    public String searchAlbum(
            @PathVariable(value = "album_id") String album_id,
            Model model,
            HttpSession session) throws SQLException {
        model.addAttribute("userName",(String) session.getAttribute("userName"));

        List<Map<String,Object>> tracksInAlbumWOArtists= searchDetailService.getTracksInAlbumWOArtist(album_id);
        List<Map<String,Object>> artistWithAlbum = searchDetailService.getArtistWithAlbum(album_id);
        for (int i=0;i<tracksInAlbumWOArtists.size();i++) {
            Integer duration_m = (Integer) tracksInAlbumWOArtists.get(i).get("duration_ms") / 60000;
            Integer duration_s = (Integer) tracksInAlbumWOArtists.get(i).get("duration_ms") % 60000 / 1000;
            String duration4 = duration_m + ":" + String.format("%02d", duration_s);
            tracksInAlbumWOArtists.get(i).put("duration", duration4);
        }
        List<List<Map<String,Object>>> tracksWithArtist = new ArrayList<>();
        for (int i=0; i<artistWithAlbum.size();i++){
            tracksWithArtist.add(searchDetailService.getTracksWithArtist((String)artistWithAlbum.get(i).get("artist_id")));
        }
        String release_date_str = (String) tracksInAlbumWOArtists.get(0).get("release_date");
        String[] release_date = release_date_str.split("-");
        String release_year = release_date[0];
        tracksInAlbumWOArtists.get(0).put("release_year",release_year);

        System.out.println(tracksWithArtist);
        System.out.println("===============================");
        model.addAttribute("tracksWithArtist",tracksWithArtist);
        model.addAttribute("tracksInAlbumWOArtists",tracksInAlbumWOArtists);
        model.addAttribute("artistWithAlbum",artistWithAlbum);

        return "searchAlbum";

    }

    @GetMapping("/search/artist/{artist_id}")
    public String searchArtist(
            @PathVariable(value = "artist_id") String artist_id,
            Model model,
            HttpSession session) throws SQLException {

        model.addAttribute("userName",(String) session.getAttribute("userName"));
        List<Map<String,Object>> artist = searchDetailService.getArtist(artist_id);
        List<Map<String,Object>> tracksWithArtist = searchDetailService.getTracksWithArtist(artist_id);
        List<Map<String,Object>> albumWithArtist = searchDetailService.getAlbumsWithArtist(artist_id);
        for (int i=0;i<tracksWithArtist.size();i++) {
            Integer duration_m = (Integer) tracksWithArtist.get(i).get("duration_ms") / 60000;
            Integer duration_s = (Integer) tracksWithArtist.get(i).get("duration_ms") % 60000 / 1000;
            String duration = duration_m + ":" + String.format("%02d", duration_s);
            tracksWithArtist.get(i).put("duration", duration);
        }
        for (int i=0; i<albumWithArtist.size();i++) {
            String release_date_str = (String) albumWithArtist.get(i).get("release_date");
            String[] release_date = release_date_str.split("-");
            String release_year = release_date[0];
            albumWithArtist.get(i).put("release_year", release_year);
        }

        model.addAttribute("artist",artist);
        model.addAttribute("tracksWithArtist",tracksWithArtist);
        model.addAttribute("albumWithArtist",albumWithArtist);

        return "searchArtist";

    }


}
