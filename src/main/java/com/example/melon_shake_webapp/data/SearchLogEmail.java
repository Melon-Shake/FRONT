package com.example.melon_shake_webapp.data;

import com.fasterxml.jackson.annotation.JsonProperty;

public class SearchLogEmail {
    @JsonProperty("track_title")
    private String track_title;

    @JsonProperty("email")
    private String email;
    public SearchLogEmail(String email, String track_title) {
        this.track_title = track_title;
        this.email = email;
    }

}
