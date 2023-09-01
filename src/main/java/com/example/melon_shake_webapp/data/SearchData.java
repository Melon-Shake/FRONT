package com.example.melon_shake_webapp.data;

import com.fasterxml.jackson.annotation.JsonProperty;

public class SearchData {
    @JsonProperty("searchInput")
    private String searchInput;

    public SearchData(String searchInput) {
        this.searchInput = searchInput;
    }
}
