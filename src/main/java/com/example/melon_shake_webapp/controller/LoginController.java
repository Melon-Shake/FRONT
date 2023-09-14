package com.example.melon_shake_webapp.controller;

import ch.qos.logback.core.pattern.color.BoldCyanCompositeConverter;
import com.example.melon_shake_webapp.Repository.UserRepository;
import com.example.melon_shake_webapp.data.User;
import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpSession;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.security.crypto.bcrypt.BCrypt;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.net.http.HttpClient;
import java.sql.SQLException;

@RequiredArgsConstructor
@Controller
public class LoginController {
    BCrypt bCrypt;
    private final HttpClient client = HttpClient.newHttpClient();
    private final ObjectMapper objectMapper = new ObjectMapper();
    private final UserRepository userRepository;

    @GetMapping("/login")
    public String Login_page() {
        return "login";
    }

    @PostMapping("/login/in-process")
    public String login(
            @RequestParam(value = "userid", required = true) String userid,
            @RequestParam(value = "userpw", required = true) String userpw,
            HttpSession session,
            RedirectAttributes redirectAttributes,
            Model model
    ) throws SQLException {
        try {
            User user = userRepository.findByEmail(userid);
            if (user==null){
                return "등록되지 않은 이메일입니다.";
            }
            else if (bCrypt.checkpw(userpw,user.getPassword())) {

                session.setAttribute("userName",user.getName());
                session.setAttribute("userEmail",user.getEmail());
                String userName = (String) session.getAttribute("userName");
                model.addAttribute("userName",userName);
                System.out.println("로그인되었습니다.");
            }
//            System.out.println("============================================");
//            System.out.println(user.getPassword());
//            System.out.println(userpw);
//
//            System.out.println(bCrypt.checkpw(userpw,user.getPassword()));

        }
        catch(Exception e){
            redirectAttributes.addFlashAttribute("message", "로그인 실패");
            return "error";
        }

        return "redirect:/Home";
    }

    @PostMapping("/logout")
    public String logout(HttpSession session){
        if (session != null){
            session.invalidate();
        }

        return "redirect:/Home";
    }
}
