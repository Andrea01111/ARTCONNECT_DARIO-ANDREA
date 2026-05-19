package com.project.artconnect.persistence;

import com.project.artconnect.dao.ExhibitionDao;
import com.project.artconnect.model.Artist;
import com.project.artconnect.model.Exhibition;
import com.project.artconnect.model.Gallery;
import com.project.artconnect.util.ConnectionManager;

import java.sql.*;
import java.util.ArrayList;
import java.util.IdentityHashMap;
import java.util.List;
import java.util.Map;

public class JdbcExhibitionDao implements ExhibitionDao {

    private Map<Exhibition, Integer> exhibitionIdMap = new IdentityHashMap<>();

    @Override
    public List<Exhibition> findAll() {
        List<Exhibition> exhibitions = new ArrayList<>();
        // On ne récupère que les expositions !
        String sql = "SELECT e.*, a.name AS artist_name " +
                     "FROM Event e JOIN Artist a ON e.artist_id = a.id " +
                     "WHERE e.eventType = 'exposition'";

        try (Connection conn = ConnectionManager.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql);
             ResultSet rs = pstmt.executeQuery()) {

            while (rs.next()) {
                exhibitions.add(mapResultSetToExhibition(rs));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return exhibitions;
    }

    @Override
    public void save(Exhibition exhibition) {
        int artistId = getArtistIdByName(exhibition.getArtist().getName());
        if (artistId == -1) return;

        // Insertion avec eventType forcé à 'exposition'
        String sql = "INSERT INTO Event (artist_id, title, description, startDate, endDate, location, eventType, imageUrl, capaciteMax) " +
                     "VALUES (?, ?, ?, ?, ?, ?, 'exposition', ?, ?)";

        try (Connection conn = ConnectionManager.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {

            conn.setAutoCommit(false);
            pstmt.setInt(1, artistId);
            pstmt.setString(2, exhibition.getTitle());
            pstmt.setString(3, exhibition.getDescription());
            pstmt.setTimestamp(4, Timestamp.valueOf(exhibition.getStartDate().atStartOfDay()));
            pstmt.setTimestamp(5, Timestamp.valueOf(exhibition.getEndDate().atStartOfDay()));
            
            // On extrait l'adresse de la fausse galerie
            String location = exhibition.getGallery() != null ? exhibition.getGallery().getName() : "Lieu inconnu";
            pstmt.setString(6, location);
            
            pstmt.setString(7, exhibition.getImageUrl());
            pstmt.setInt(8, exhibition.getCapaciteMax());

            pstmt.executeUpdate();

            try (ResultSet rs = pstmt.getGeneratedKeys()) {
                if (rs.next()) exhibitionIdMap.put(exhibition, rs.getInt(1));
            }
            conn.commit();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void update(Exhibition exhibition) {
        Integer id = exhibitionIdMap.get(exhibition);
        if (id == null) return;

        String sql = "UPDATE Event SET title=?, description=?, startDate=?, endDate=?, location=?, imageUrl=?, capaciteMax=? WHERE id=? AND eventType='exposition'";
        try (Connection conn = ConnectionManager.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {

            pstmt.setString(1, exhibition.getTitle());
            pstmt.setString(2, exhibition.getDescription());
            pstmt.setTimestamp(3, Timestamp.valueOf(exhibition.getStartDate().atStartOfDay()));
            pstmt.setTimestamp(4, Timestamp.valueOf(exhibition.getEndDate().atStartOfDay()));
            pstmt.setString(5, exhibition.getGallery() != null ? exhibition.getGallery().getName() : "");
            pstmt.setString(6, exhibition.getImageUrl());
            pstmt.setInt(7, exhibition.getCapaciteMax());
            pstmt.setInt(8, id);

            pstmt.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void delete(String title) {
        String sql = "DELETE FROM Event WHERE title = ? AND eventType = 'exposition'";
        try (Connection conn = ConnectionManager.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setString(1, title);
            pstmt.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    private Exhibition mapResultSetToExhibition(ResultSet rs) throws SQLException {
        Exhibition ex = new Exhibition();
        ex.setTitle(rs.getString("title"));
        ex.setDescription(rs.getString("description"));
        ex.setStartDate(rs.getTimestamp("startDate").toLocalDateTime().toLocalDate());
        ex.setEndDate(rs.getTimestamp("endDate").toLocalDateTime().toLocalDate());
        ex.setImageUrl(rs.getString("imageUrl"));
        ex.setCapaciteMax(rs.getInt("capaciteMax"));

        // Création de l'artiste lié
        Artist artist = new Artist();
        artist.setName(rs.getString("artist_name"));
        ex.setArtist(artist);

        // Fausse Galerie pour respecter l'UI Java
        Gallery gallery = new Gallery();
        gallery.setName(rs.getString("location"));
        ex.setGallery(gallery);

        exhibitionIdMap.put(ex, rs.getInt("id"));
        return ex;
    }

    private int getArtistIdByName(String artistName) {
        String sql = "SELECT id FROM Artist WHERE name = ?";
        try (Connection conn = ConnectionManager.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setString(1, artistName);
            try (ResultSet rs = pstmt.executeQuery()) {
                if (rs.next()) return rs.getInt("id");
            }
        } catch (SQLException e) { }
        return -1;
    }
}