package com.example.melon_shake_webapp.data;

import com.fasterxml.jackson.annotation.JsonProperty;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import lombok.NoArgsConstructor;

@Entity
@NoArgsConstructor
public class LoginData {

    @Id
    @JsonProperty("email")
    private String email;
    @JsonProperty("password")
    private String password;

    public LoginData(String email, String password) {
        this.email = email;
        this.password = password;
    }

}
