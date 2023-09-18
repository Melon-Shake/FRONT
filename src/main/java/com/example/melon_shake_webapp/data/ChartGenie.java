package com.example.melon_shake_webapp.data;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.math.BigDecimal;
import java.sql.Timestamp;
import java.time.LocalDateTime;

@Getter
@Setter
@Entity
@NoArgsConstructor
@Table(name = "chart_genie", schema = "public")
public class ChartGenie {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "track_name")
    private String trackName;

    @Column(name = "album_name")
    private String albumName;

    @Column(name = "img_url")
    private String imgUrl;


    @Column(name = "artist_names")
    private String artistNames;

    @Column(name = "rank_no")
    private Integer rank_no;

    @Column(name = "pre_rank_no")
    private Integer pre_rank_no;

    @Column(name = "points")
    private BigDecimal points;

    @Column(name = "created_datetime")
    private LocalDateTime createdDatetime;

    @Column(name = "track_id")
    private String trackId;

    @Column(name = "album_id")
    private String albumId;

    @Column(name = "artist_ids")
    private String artistIds;


}
