package com.example.melon_shake_webapp.page;

import com.example.melon_shake_webapp.data.TrackRanking;
import com.fasterxml.jackson.databind.util.JSONPObject;
import lombok.RequiredArgsConstructor;
import org.springframework.boot.jackson.JsonObjectDeserializer;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.ui.Model;

import java.util.Arrays;
import java.util.Map;
import java.util.Dictionary;
@RequiredArgsConstructor
@Controller
public class Home {
    @GetMapping("/")
    public String default_page(){
        return "redirect:/Home";
    }
    @GetMapping("/Home")
    public String home_page(){
        return "home";
    }

    @PostMapping("/search")
    public String show_search(@RequestParam("searchInput") String searchInput, Model model){
        model.addAttribute("searchInput",searchInput);
        System.out.println("");
        return "tmp";
    }


    @PostMapping("/")
    public ResponseEntity<String> track_ranking_chart(@RequestBody Map<String,String> trackRanking ){

        System.out.println(trackRanking);

        return ResponseEntity.ok("success");
    }
}
