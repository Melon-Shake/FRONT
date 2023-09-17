package com.example.melon_shake_webapp.Service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;

@Service
public class SearchDetailService {

    @Autowired
    private JdbcTemplate jdbcTemplate;

    public List<Map<String,Object>> getTrack(String track_id, String album_id){
        String sql = "SELECT * FROM spotify_tracks WHERE id = ? AND album_id = ? LIMIT 1";
        return jdbcTemplate.queryForList(sql, track_id, album_id);
    }

    public List<Map<String,Object>> getAlbum(String album_id){
        String sql = "SELECT * FROM spotify_albums WHERE id = ? LIMIT 1";
        return jdbcTemplate.queryForList(sql,album_id);
    }
    public List<Map<String,Object>> getArtist(String artist_id){
        String sql = "SELECT * FROM spotify_artists WHERE id = ? LIMIT 1";
        return jdbcTemplate.queryForList(sql,artist_id);
    }

    public List<Map<String, Object>> getLyrics(String track_id){
        String sql = "SELECT content FROM lyrics WHERE id = ? LIMIT 1";
        return jdbcTemplate.queryForList(sql, track_id);
    }

    public List<Map<String, Object>> getFeatures(String track_id){
        String sql = "SELECT * FROM lyrics WHERE id = ? LIMIT 1";
        return jdbcTemplate.queryForList(sql, track_id);
    }

    public List<Map<String, Object>> getTracksInAlbum(String album_id){
        String sql = "SELECT DISTINCT id,name,duration_ms,popularity,artists_ids,album_id FROM spotify_tracks WHERE album_id = ? ORDER BY popularity DESC";
        return jdbcTemplate.queryForList(sql, album_id);
    }

    public List<Map<String, Object>> getTracksWithArtist(String artist_id){
        String sql = "SELECT DISTINCT id,name,duration_ms,popularity,artists_ids,album_id  FROM spotify_tracks WHERE ? = ANY(artists_ids) ORDER BY popularity DESC";
        return jdbcTemplate.queryForList(sql, artist_id);
    }

}
