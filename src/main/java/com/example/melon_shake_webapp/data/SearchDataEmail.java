package com.example.melon_shake_webapp.data;

import com.fasterxml.jackson.annotation.JsonProperty;

public class SearchDataEmail {
    @JsonProperty("searchInput")
    private String searchInput;

    @JsonProperty("email")
    private String email;
    public SearchDataEmail(String searchInput, String email) {
        this.searchInput = searchInput;
        this.email = email;
    }

}
