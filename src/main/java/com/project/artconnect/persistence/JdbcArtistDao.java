package com.project.artconnect.persistence;

import com.project.artconnect.dao.ArtistDao;
import com.project.artconnect.model.Artist;
import com.project.artconnect.util.ConnectionManager;

import java.sql.*;
import java.util.ArrayList;
import java.util.IdentityHashMap;
import java.util.List;
import java.util.Map;

public class JdbcArtistDao implements ArtistDao {


    private Map<Artist, Integer> artistIdMap = new IdentityHashMap<>();

    @Override
    public List<Artist> findAll() {
        List<Artist> artists = new ArrayList<>();
        String sql = "SELECT * FROM Artist";

        try (Connection conn = ConnectionManager.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql);
             ResultSet rs = pstmt.executeQuery()) {

            while (rs.next()) {
                Artist artist = mapResultSetToArtist(rs);
                artists.add(artist);
            }
        } catch (SQLException e) {
            e.printStackTrace();
            System.err.println("Erreur lors de la récupération des artistes.");
        }
        return artists;
    }

    @Override
    public List<Artist> findByCity(String city) {
        List<Artist> artists = new ArrayList<>();
        String sql = "SELECT * FROM Artist WHERE location = ?";

        try (Connection conn = ConnectionManager.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            
            pstmt.setString(1, city);
            try (ResultSet rs = pstmt.executeQuery()) {
                while (rs.next()) {
                    artists.add(mapResultSetToArtist(rs));
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return artists;
    }

    @Override
    public void save(Artist artist) {
        // Ajout de profileImageUrl dans l'INSERT
        String sql = "INSERT INTO Artist (name, bio, website, location, profileImageUrl) VALUES (?, ?, ?, ?, ?)";

        try (Connection conn = ConnectionManager.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {
            
            conn.setAutoCommit(false); 

            pstmt.setString(1, artist.getName());
            pstmt.setString(2, artist.getBio());
            pstmt.setString(3, artist.getWebsite());
            pstmt.setString(4, artist.getCity()); 
            pstmt.setString(5, artist.getProfileImageUrl()); // Nouveau champ !

            pstmt.executeUpdate();

            try (ResultSet generatedKeys = pstmt.getGeneratedKeys()) {
                if (generatedKeys.next()) {
                    int id = generatedKeys.getInt(1);
                    artistIdMap.put(artist, id); 
                }
            }
            conn.commit(); 

        } catch (SQLException e) {
            e.printStackTrace();
            System.err.println("Erreur lors de la sauvegarde de l'artiste: " + artist.getName());
        }
    }

    @Override
    public void update(Artist artist) {
        Integer id = artistIdMap.get(artist);
        
        if (id == null) {
            System.err.println("Mise à jour impossible : l'artiste n'a pas d'ID connu en BDD. Utilisez save() d'abord.");
            return;
        }

        // Ajout de profileImageUrl dans l'UPDATE
        String sql = "UPDATE Artist SET name = ?, bio = ?, website = ?, location = ?, profileImageUrl = ? WHERE id = ?";

        try (Connection conn = ConnectionManager.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {

            pstmt.setString(1, artist.getName());
            pstmt.setString(2, artist.getBio());
            pstmt.setString(3, artist.getWebsite());
            pstmt.setString(4, artist.getCity());
            pstmt.setString(5, artist.getProfileImageUrl()); // Nouveau champ !
            pstmt.setInt(6, id); // L'ID passe en 6ème position

            pstmt.executeUpdate();

        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void delete(String artistName) {
        String sql = "DELETE FROM Artist WHERE name = ?";

        try (Connection conn = ConnectionManager.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            
            pstmt.setString(1, artistName);
            pstmt.executeUpdate();

        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    // --- Méthode utilitaire ---

    private Artist mapResultSetToArtist(ResultSet rs) throws SQLException {
        Artist artist = new Artist();
        artist.setName(rs.getString("name"));
        artist.setBio(rs.getString("bio"));
        artist.setWebsite(rs.getString("website"));
        artist.setCity(rs.getString("location"));
        
        // --- Nouveaux champs mappés ---
        artist.setProfileImageUrl(rs.getString("profileImageUrl"));
        
        Timestamp createdAt = rs.getTimestamp("createdAt");
        if (createdAt != null) {
            artist.setCreatedAt(createdAt.toLocalDateTime());
        }
        // ------------------------------

        artistIdMap.put(artist, rs.getInt("id"));
        
        return artist;
    }
}