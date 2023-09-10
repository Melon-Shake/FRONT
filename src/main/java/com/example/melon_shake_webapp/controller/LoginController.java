package com.example.melon_shake_webapp.controller;

import com.example.melon_shake_webapp.data.LoginData;
import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpSession;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.io.IOException;
import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.nio.charset.StandardCharsets;

@Controller
public class LoginController {

    private final HttpClient client = HttpClient.newHttpClient();
    private final ObjectMapper objectMapper = new ObjectMapper();
    @GetMapping("/login")
    public String Login_page() {
        return "login";
    }

    @PostMapping("/login/in-process")
    public String login(
            @RequestParam(value = "userid",required = true) String userid,
            @RequestParam(value = "userpw",required = true) String userpw,
            Model model,
            HttpSession session,
            RedirectAttributes redirectAttributes
    ) {

        LoginData loginData = new LoginData(userid,userpw);
        String jsonBody;
        try {
            jsonBody = objectMapper.writeValueAsString(loginData);
        } catch (Exception e) {
            e.printStackTrace();
            return "error";
        }
        HttpRequest request = HttpRequest.newBuilder()
                .uri(URI.create("http://ec2-3-114-214-196.ap-northeast-1.compute.amazonaws.com:8000/get_user_data/"))
//                .uri(URI.create("http://192.168.70.60:8000/login/"))
                .POST(HttpRequest.BodyPublishers.ofString(jsonBody))
                .header("Content-Type", "application/json")
                .build();

        try {
            HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString(StandardCharsets.UTF_8));
            System.out.println(response);
            if (response.body() == "True") {
                // 로그인 성공 시 세션에 사용자 정보를 저장합니다.
                session.setAttribute("user", userid);
                return "redirect:/Home"; // 로그인 성공 시 이동할 페이지
            } else {
                // 로그인 실패 시 메시지를 설정하여 다시 로그인 페이지로 이동합니다.
                redirectAttributes.addFlashAttribute("message", "로그인 실패");
                return "redirect:/login";
            }
//            System.out.println(response.body());
            // 예외가 발생하지 않은 경우 이후의 로직을 작성
        } catch (IOException | InterruptedException e) {
            // 예외 처리 로직
            e.printStackTrace(); // 예외 정보 출력
            return "redirect:/Home";
        }


    }
}
