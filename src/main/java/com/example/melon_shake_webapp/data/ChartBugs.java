package com.example.melon_shake_webapp.data;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.math.BigDecimal;
import java.sql.Timestamp;
import java.time.LocalDateTime;
import java.util.List;

@Getter
@Setter
@Entity
@NoArgsConstructor
@Table(name = "chart_bugs", schema = "public")
public class ChartBugs {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "track_name")
    private String trackName;

    @Column(name = "album_name")
    private String albumName;

    @Column(name = "img_url")
    private String imgUrl;

    @Column(name = "release_date")
    private String releaseDate;

    @Column(name = "release_local_date")
    private String releaseLocalDate;

    @Column(name = "artist_names")
    private String artistNames;

    @Column(name = "genres_name")
    private String genresName;

    @Column(name = "likes_count")
    private Integer likesCount;

    @Column(name = "rank")
    private Integer rank;

    @Column(name = "rank_peak")
    private Integer rankPeak;

    @Column(name = "rank_last")
    private Integer rankLast;

    @Column(name = "points")
    private BigDecimal points;

    @Column(name = "created_datetime")
    private LocalDateTime createdDatetime;

}
