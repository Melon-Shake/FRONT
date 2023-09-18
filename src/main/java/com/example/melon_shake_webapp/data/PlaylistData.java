package com.example.melon_shake_webapp.data;

import com.fasterxml.jackson.annotation.JsonProperty;

public class PlaylistData {
    @JsonProperty("email")
    private String email;
    public PlaylistData(String email){
        this.email = email;
    }

}
