package com.example.melon_shake_webapp.data;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.math.BigDecimal;

@Getter
@Setter
@Entity
@NoArgsConstructor
@Table(name = "chart_bugs", schema = "public")
public class TotalChart {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long index;

    @Column(name = "track_name")
    private String trackName;

    @Column(name = "artist_names")
    private String artistNames;

    @Column(name = "album_name")
    private String albumName;

    @Column(name = "img_url")
    private String imgUrl;

    @Column(name = "points")
    private BigDecimal points;

    @Column(name = "track_id")
    private String trackId;

    @Column(name = "album_id")
    private String albumId;

    @Column(name = "artist_ids")
    private String artistIds;
}
