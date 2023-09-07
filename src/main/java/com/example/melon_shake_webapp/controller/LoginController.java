package com.example.melon_shake_webapp.controller;
//import jakarta.servlet.http.HttpSession;
//import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.context.annotation.Bean;
//import org.springframework.security.config.Customizer;
//import org.springframework.security.config.annotation.web.builders.HttpSecurity;
//import org.springframework.security.web.SecurityFilterChain;
//import org.springframework.stereotype.Controller;
//import org.springframework.ui.Model;
//import org.springframework.web.HttpSessionRequiredException;
//import org.springframework.web.bind.annotation.ModelAttribute;
//import org.springframework.web.bind.annotation.SessionAttributes;
//
//
//@Controller
//@SessionAttributes
//public class LoginController {
//
//    @Autowired
//    private HttpSession session;
//
//    @ModelAttribute("myAttribute")
//    public String addAttribute(){
//        return "a";
//    }
//
//    public SecurityFilterChain filterChain(HttpSecurity http) {
//        http
//                .authorizeRequests()
//                .requestMatchers("/public/**").permitAll()
//                .requestMatchers("/private/**").authenticated()
//                .and()
//                .formLogin(login -> login
//                        .loginPage("/login")
//                        .permitAll()
//                )
//                .logout(Customizer.withDefaults());
//
//    }
//
//}
