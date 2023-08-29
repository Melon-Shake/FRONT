package com.example.melon_shake_webapp.page;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

@RequiredArgsConstructor
@Controller
public class Register {
    @GetMapping("/register")
    public String Register_page(){
        return "register";
    }
}
