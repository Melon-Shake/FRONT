package com.example.melon_shake_webapp.data;

import com.fasterxml.jackson.annotation.JsonProperty;

public class LoginData {
    @JsonProperty("email")
    private String email;
    @JsonProperty("password")
    private String password;

    public LoginData(String email, String password) {
        this.email = email;
        this.password = password;
    }

}
