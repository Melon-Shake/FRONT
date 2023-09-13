package com.example.melon_shake_webapp.controller;

import com.example.melon_shake_webapp.data.RegistrationData;
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
import java.sql.SQLException;

@RequiredArgsConstructor
@Controller
public class RegisterController {

    private final HttpClient client = HttpClient.newHttpClient();
    private final ObjectMapper objectMapper = new ObjectMapper();

    @GetMapping("/register")
    public String Register_page() {
        return "register";
    }

    @PostMapping("/register/new_register")
    public String register(
            @RequestParam(name="email", required=true) String email,
            @RequestParam(name="password", required=true) String password,
            @RequestParam(name="name", required=true) String name,
            @RequestParam(name="gender", required=true) String gender,
            @RequestParam(name="birthdate", required=true) String birthdate,
            @RequestParam(name="mbti", required=true) String mbti,
            @RequestParam(name="favorite_tracks", required=true) String favorite_tracks,
            @RequestParam(name="favorite_artists", required=true) String favorite_artists,
            Model model,
            HttpSession session) throws SQLException {
        model.addAttribute("userName",(String) session.getAttribute("userName")); // 세션 정보 전달
        RegistrationData registrationData = new RegistrationData(email, password, name, gender, birthdate, mbti, favorite_tracks, favorite_artists);
        String jsonBody;

        try {
            jsonBody = objectMapper.writeValueAsString(registrationData);
        } catch (Exception e) {
            e.printStackTrace();
            return "error";
        }

        HttpRequest request = HttpRequest.newBuilder()
                .uri(URI.create("http://ec2-3-114-214-196.ap-northeast-1.compute.amazonaws.com:8000/get_user_data/"))
//                .uri(URI.create("http://192.168.70.60:8000/get_user_data/"))
                .POST(HttpRequest.BodyPublishers.ofString(jsonBody))
                .header("Content-Type", "application/json")
                .build();

        try {
            HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString(StandardCharsets.UTF_8));
            System.out.println(response);
//            System.out.println(response.body());
            // 예외가 발생하지 않은 경우 이후의 로직을 작성
        } catch (IOException | InterruptedException e) {
            // 예외 처리 로직
            e.printStackTrace(); // 예외 정보 출력
            return "redirect:/Home";
        }

        return "redirect:/Home";
    }

}


