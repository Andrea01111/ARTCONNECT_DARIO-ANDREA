package com.project.artconnect.persistence;

import com.project.artconnect.dao.ArtworkDao;
import com.project.artconnect.model.Artist;
import com.project.artconnect.model.Artwork;
import com.project.artconnect.util.ConnectionManager;

import java.sql.*;
import java.util.ArrayList;
import java.util.IdentityHashMap;
import java.util.List;
import java.util.Map;

/**
 * JDBC implementation for ArtworkDao.
 */
public class JdbcArtworkDao implements ArtworkDao {

    // Toujours notre astuce pour stocker l'ID de l'oeuvre sans polluer le modèle Java
    private Map<Artwork, Integer> artworkIdMap = new IdentityHashMap<>();

    @Override
    public List<Artwork> findAll() {
        List<Artwork> artworks = new ArrayList<>();
        // On utilise un JOIN pour récupérer les infos de l'oeuvre ET de son artiste
        String sql = "SELECT aw.*, a.name AS artist_name, a.location AS artist_location " +
                     "FROM Artwork aw " +
                     "JOIN Artist a ON aw.artist_id = a.id";

        try (Connection conn = ConnectionManager.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql);
             ResultSet rs = pstmt.executeQuery()) {

            while (rs.next()) {
                artworks.add(mapResultSetToArtwork(rs));
            }
        } catch (SQLException e) {
            e.printStackTrace();
            System.err.println("Erreur lors de la récupération des oeuvres.");
        }
        return artworks;
    }

    @Override
    public List<Artwork> findByArtistName(String artistName) {
        List<Artwork> artworks = new ArrayList<>();
        String sql = "SELECT aw.*, a.name AS artist_name, a.location AS artist_location " +
                     "FROM Artwork aw " +
                     "JOIN Artist a ON aw.artist_id = a.id " +
                     "WHERE a.name = ?";

        try (Connection conn = ConnectionManager.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {

            pstmt.setString(1, artistName);
            try (ResultSet rs = pstmt.executeQuery()) {
                while (rs.next()) {
                    artworks.add(mapResultSetToArtwork(rs));
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return artworks;
    }

    @Override
    public void save(Artwork artwork) {
        // ÉTAPE 1 : Retrouver l'ID de l'artiste en BDD grâce à son nom
        int artistId = getArtistIdByName(artwork.getArtist().getName());
        
        if (artistId == -1) {
            System.err.println("Impossible de sauvegarder l'oeuvre : l'artiste n'existe pas en base de données.");
            return;
        }

        // ÉTAPE 2 : Insérer l'oeuvre avec la clé étrangère artist_id (ajout de imageUrl)
        String sql = "INSERT INTO Artwork (artist_id, title, description, year, medium, imageUrl) VALUES (?, ?, ?, ?, ?, ?)";

        try (Connection conn = ConnectionManager.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {

            conn.setAutoCommit(false); // Début de transaction

            pstmt.setInt(1, artistId);
            pstmt.setString(2, artwork.getTitle());
            pstmt.setString(3, artwork.getDescription());
            
            if (artwork.getCreationYear() != null) {
                pstmt.setInt(4, artwork.getCreationYear());
            } else {
                pstmt.setNull(4, Types.INTEGER);
            }
            pstmt.setString(5, artwork.getMedium());
            pstmt.setString(6, artwork.getImageUrl()); // Nouveau champ !

            pstmt.executeUpdate();

            // Mémorisation de l'ID généré
            try (ResultSet rs = pstmt.getGeneratedKeys()) {
                if (rs.next()) {
                    artworkIdMap.put(artwork, rs.getInt(1));
                }
            }
            conn.commit(); // Validation transaction

        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void update(Artwork artwork) {
        Integer id = artworkIdMap.get(artwork);
        if (id == null) {
            System.err.println("Mise à jour impossible : l'oeuvre n'a pas d'ID connu.");
            return;
        }

        // Ajout de imageUrl dans l'UPDATE
        String sql = "UPDATE Artwork SET title = ?, description = ?, year = ?, medium = ?, imageUrl = ? WHERE id = ?";

        try (Connection conn = ConnectionManager.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {

            pstmt.setString(1, artwork.getTitle());
            pstmt.setString(2, artwork.getDescription());
            
            if (artwork.getCreationYear() != null) {
                pstmt.setInt(3, artwork.getCreationYear());
            } else {
                pstmt.setNull(3, Types.INTEGER);
            }
            
            pstmt.setString(4, artwork.getMedium());
            pstmt.setString(5, artwork.getImageUrl()); // Nouveau champ !
            pstmt.setInt(6, id); // L'ID passe en 6ème position

            pstmt.executeUpdate();

        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void delete(String title) {
        String sql = "DELETE FROM Artwork WHERE title = ?";
        try (Connection conn = ConnectionManager.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            
            pstmt.setString(1, title);
            pstmt.executeUpdate();

        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    @Override
    public List<Artwork> searchFullText(String keyword) {
        List<Artwork> artworks = new ArrayList<>();
        String sql = "SELECT aw.*, a.name AS artist_name, a.location AS artist_location, " +
                     "MATCH(aw.title, aw.description) AGAINST(? IN BOOLEAN MODE) AS score " +
                     "FROM Artwork aw " +
                     "JOIN Artist a ON aw.artist_id = a.id " +
                     "WHERE MATCH(aw.title, aw.description) AGAINST(? IN BOOLEAN MODE) " +
                     "ORDER BY score DESC";
        try (Connection conn = ConnectionManager.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setString(1, keyword);
            pstmt.setString(2, keyword);
            try (ResultSet rs = pstmt.executeQuery()) {
                while (rs.next()) artworks.add(mapResultSetToArtwork(rs));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return artworks;
    }

    // --- Méthodes Utilitaires Privées ---

    /**
     * Reconstruit le graphe d'objet (Artwork + Artist associé) depuis un ResultSet.
     */
    private Artwork mapResultSetToArtwork(ResultSet rs) throws SQLException {
        // 1. Instanciation d'un objet Artist "fictif" mais suffisant pour l'interface Java
        Artist artist = new Artist();
        artist.setName(rs.getString("artist_name"));
        artist.setCity(rs.getString("artist_location"));

        // 2. Instanciation de l'Oeuvre
        Artwork artwork = new Artwork();
        artwork.setTitle(rs.getString("title"));
        artwork.setDescription(rs.getString("description"));
        artwork.setCreationYear(rs.getInt("year"));
        artwork.setMedium(rs.getString("medium"));
        
        // --- Nouveaux champs mappés ---
        artwork.setImageUrl(rs.getString("imageUrl"));
        
        Timestamp createdAt = rs.getTimestamp("createdAt");
        if (createdAt != null) {
            artwork.setCreatedAt(createdAt.toLocalDateTime());
        }
        // ------------------------------
        
        // 3. Liaison (Bidirectionnelle si possible, mais on se contente d'Oeuvre -> Artiste ici)
        artwork.setArtist(artist);

        // 4. Mémorisation de l'ID secret
        artworkIdMap.put(artwork, rs.getInt("id"));

        return artwork;
    }

    /**
     * Retrouve l'ID d'un artiste en base de données à partir de son nom.
     * C'est nécessaire car l'objet Java Artist n'expose pas de getArtistId().
     */
    private int getArtistIdByName(String artistName) {
        String sql = "SELECT id FROM Artist WHERE name = ?";
        try (Connection conn = ConnectionManager.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            
            pstmt.setString(1, artistName);
            try (ResultSet rs = pstmt.executeQuery()) {
                if (rs.next()) {
                    return rs.getInt("id");
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return -1; // Retourne -1 si non trouvé
    }
}