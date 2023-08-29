package com.example.melon_shake_webapp.data;

import org.springframework.stereotype.Controller;

@Controller
public class RegisterController {
    @PostMapping("/register")
    public String register(
            @RequestParam(name="email", required=true) String email,
            @RequestParam(name="password", required=true) String password,
            @RequestParam(name="name", required=true) String name,
            @RequestParam(name="gender", required=false) String gender
            @RequestParam(name="age", required=false) int age,
            @RequestParam(name="mbti", required=false) String mbti,
            @RequestParam(name="favorite_tracks", required=false) String favorite_tracks,
            @RequestParam(name="favorite_artists", required=false) String favorite_artists,
            Model model) throws SQLException {

        model.addAttribute("email", email);
        model.addAttribute("password", password);
        model.addAttribute("name", name);
        model.addAttribute("gender", gender);
        model.addAttribute("age", age);
        model.addAttribute("mbti", mbti);
        model.addAttribute("favorite_tracks", favorite_tracks);
        model.addAttribute("favorite_artists", favorite_artists);
        return "registration_success";
    }

}
