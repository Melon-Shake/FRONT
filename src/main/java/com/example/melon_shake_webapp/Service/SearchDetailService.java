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
        String sql = "select DISTINCT tr.id as id, tr.name as name, tr.duration_ms as duration_ms, ar.id as artist_id, ar.name as artist_name, ar.images_url as artist_image, al.name as album_name, al.images_url as album_image, tr.album_id as album_id\n" +
                "FROM spotify_tracks tr\n" +
                "JOIN spotify_albums al ON tr.album_id = al.id\n" +
                "JOIN spotify_artists ar ON ar.id = ANY(tr.artists_ids)\n" +
                "where album_id= ?\n" +
                "LIMIT 10";
        return jdbcTemplate.queryForList(sql, album_id);
    }

    public List<Map<String, Object>> getTracksInAlbumWOArtist(String album_id){
        String sql ="select DISTINCT tr.id as id, tr.name as name, tr.duration_ms as duration_ms, al.name as album_name, al.images_url as album_image, tr.album_id as album_id\n" +
                "FROM spotify_tracks tr\n" +
                "JOIN spotify_albums al ON tr.album_id = al.id\n" +
                "where album_id=?";
        return jdbcTemplate.queryForList(sql, album_id);
    }



    public List<Map<String, Object>> getTracksWithArtist(String artist_id){
        String sql = "select DISTINCT tr.id as id, tr.name as name, tr.duration_ms as duration_ms, ar.id as artist_id, ar.name as artist_name, ar.images_url as artist_image, al.name as album_name, al.images_url as album_image, tr.album_id as album_id\n" +
                "FROM spotify_tracks tr\n" +
                "JOIN spotify_albums al ON tr.album_id = al.id\n" +
                "JOIN spotify_artists ar ON ar.id = ANY(tr.artists_ids)\n" +
                "where ar.id = ?\n" +
                "LIMIT 10";
        return jdbcTemplate.queryForList(sql, artist_id);
    }

    public List<Map<String, Object>> getAlbumsWithArtist(String artist_id){
        String sql = "SELECT DISTINCT tr.album_id as album_id, al.name as album_name, al.images_url as album_image\n" +
                "FROM spotify_tracks tr\n" +
                "JOIN spotify_albums al ON al.id = tr.album_id\n" +
                "JOIN spotify_artists ar ON ar.id = ANY(tr.artists_ids)"+
                "WHERE ? = ANY(tr.artists_ids)";
        return jdbcTemplate.queryForList(sql, artist_id);
    }
}
