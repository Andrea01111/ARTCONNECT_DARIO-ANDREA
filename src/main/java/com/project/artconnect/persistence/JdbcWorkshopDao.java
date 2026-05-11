package com.project.artconnect.persistence;

import com.project.artconnect.dao.WorkshopDao;
import com.project.artconnect.model.Artist;
import com.project.artconnect.model.Workshop;
import com.project.artconnect.util.ConnectionManager;

import java.sql.*;
import java.util.ArrayList;
import java.util.IdentityHashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;

public class JdbcWorkshopDao implements WorkshopDao {

    private Map<Workshop, Integer> workshopIdMap = new IdentityHashMap<>();

    @Override
    public Optional<Workshop> findById(Long id) {
        String sql = "SELECT e.*, a.name AS artist_name " +
                     "FROM Event e JOIN Artist a ON e.artist_id = a.id " +
                     "WHERE e.id = ? AND e.eventType = 'atelier'";
                     
        try (Connection conn = ConnectionManager.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {

            pstmt.setLong(1, id);
            try (ResultSet rs = pstmt.executeQuery()) {
                if (rs.next()) {
                    return Optional.of(mapResultSetToWorkshop(rs));
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return Optional.empty();
    }

    @Override
    public List<Workshop> findAll() {
        List<Workshop> workshops = new ArrayList<>();

        String sql = "SELECT e.*, a.name AS artist_name " +
                     "FROM Event e JOIN Artist a ON e.artist_id = a.id " +
                     "WHERE e.eventType = 'atelier'";

        try (Connection conn = ConnectionManager.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql);
             ResultSet rs = pstmt.executeQuery()) {

            while (rs.next()) {
                workshops.add(mapResultSetToWorkshop(rs));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return workshops;
    }

    private Workshop mapResultSetToWorkshop(ResultSet rs) throws SQLException {
        Workshop w = new Workshop();
        w.setTitle(rs.getString("title"));
        w.setDescription(rs.getString("description"));
        w.setDate(rs.getTimestamp("startDate").toLocalDateTime());
        w.setLocation(rs.getString("location"));
        w.setMaxParticipants(rs.getInt("capaciteMax"));
        w.setImageUrl(rs.getString("imageUrl"));

        // Calcul de la durée en minutes entre startDate et endDate
        Timestamp start = rs.getTimestamp("startDate");
        Timestamp end = rs.getTimestamp("endDate");
        if (start != null && end != null) {
            long diff = end.getTime() - start.getTime();
            w.setDurationMinutes((int) (diff / (60 * 1000)));
        }

        // Création de l'instructeur (Artist)
        Artist instructor = new Artist();
        instructor.setName(rs.getString("artist_name"));
        w.setInstructor(instructor);

        workshopIdMap.put(w, rs.getInt("id"));
        return w;
    }
}