package com.example.melon_shake_webapp.data;

import com.fasterxml.jackson.annotation.JsonProperty;

public class RegistrationData {
    @JsonProperty("email")
    private String email;
    @JsonProperty("password")
    private String password;
    @JsonProperty("name")
    private String name;
    @JsonProperty("gender")
    private String gender;
    @JsonProperty("birthdate")
    private String birthdate;
    @JsonProperty("mbti")
    private String mbti;
    @JsonProperty("favorite_tracks")
    private String favorite_tracks;
    @JsonProperty("favorite_artists")
    private String favorite_artists;
    public RegistrationData(String email, String password, String name, String gender,
                            String birthdate, String mbti, String favorite_tracks,
                            String favorite_artists) {
        this.email = email;
        this.password = password;
        this.name = name;
        this.gender = gender;
        this.birthdate = birthdate;
        this.mbti = mbti;
        this.favorite_tracks = favorite_tracks;
        this.favorite_artists = favorite_artists;
    }

}
