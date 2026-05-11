package com.project.artconnect.persistence;

import com.project.artconnect.dao.CommunityMemberDao;
import com.project.artconnect.model.CommunityMember;
import com.project.artconnect.util.ConnectionManager;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.IdentityHashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;

public class JdbcCommunityMemberDao implements CommunityMemberDao {


    private Map<CommunityMember, Integer> memberIdMap = new IdentityHashMap<>();

    @Override
    public Optional<CommunityMember> findById(Long id) {
        String sql = "SELECT * FROM Member WHERE id = ?";
        
        try (Connection conn = ConnectionManager.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {

            pstmt.setLong(1, id);
            
            try (ResultSet rs = pstmt.executeQuery()) {
                if (rs.next()) {
                    // Si on trouve le membre, on l'emballe dans un Optional
                    return Optional.of(mapResultSetToMember(rs));
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
            System.err.println("Erreur lors de la récupération du membre avec l'ID: " + id);
        }
        
        // Si on ne trouve rien ou s'il y a une erreur, on retourne un Optional vide (très propre)
        return Optional.empty();
    }

    @Override
    public List<CommunityMember> findAll() {
        List<CommunityMember> members = new ArrayList<>();
        String sql = "SELECT * FROM Member";

        try (Connection conn = ConnectionManager.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql);
             ResultSet rs = pstmt.executeQuery()) {

            while (rs.next()) {
                members.add(mapResultSetToMember(rs));
            }
        } catch (SQLException e) {
            e.printStackTrace();
            System.err.println("Erreur lors de la récupération de la liste des membres.");
        }
        
        return members;
    }

    // --- Méthode utilitaire ---

    private CommunityMember mapResultSetToMember(ResultSet rs) throws SQLException {
        CommunityMember member = new CommunityMember();
        
        // --- Champs communs originaux ---
        member.setName(rs.getString("name"));
        member.setEmail(rs.getString("email"));
        
        // --- Nouveaux champs mappés ---
        member.setPasswordHash(rs.getString("passwordHash"));
        member.setRole(rs.getString("role"));
        member.setBio(rs.getString("bio"));
        
        Timestamp joinDate = rs.getTimestamp("joinDate");
        if (joinDate != null) {
            member.setJoinDate(joinDate.toLocalDateTime());
        }
        // ------------------------------
        
        // Mémorisation secrète de l'ID 
        memberIdMap.put(member, rs.getInt("id"));

        return member;
    }
}