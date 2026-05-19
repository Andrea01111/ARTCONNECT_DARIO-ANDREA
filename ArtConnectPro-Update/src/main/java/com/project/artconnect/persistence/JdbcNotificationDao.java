package com.project.artconnect.persistence;

import com.project.artconnect.dao.NotificationDao;
import com.project.artconnect.model.Notification;
import com.project.artconnect.util.ConnectionManager;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class JdbcNotificationDao implements NotificationDao {

    @Override
    public List<Notification> findByMemberId(int memberId) {
        List<Notification> notifications = new ArrayList<>();
        String sql = "SELECT * FROM Notification WHERE member_id = ? AND isRead = FALSE ORDER BY createdAt DESC";
        try (Connection conn = ConnectionManager.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setInt(1, memberId);
            try (ResultSet rs = pstmt.executeQuery()) {
                while (rs.next()) {
                    notifications.add(mapRow(rs));
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return notifications;
    }

    @Override
    public void markAsRead(int notificationId) {
        String sql = "UPDATE Notification SET isRead = TRUE WHERE id = ?";
        try (Connection conn = ConnectionManager.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setInt(1, notificationId);
            pstmt.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    private Notification mapRow(ResultSet rs) throws SQLException {
        Notification n = new Notification();
        n.setId(rs.getInt("id"));
        n.setMemberId(rs.getInt("member_id"));
        n.setEventId(rs.getInt("event_id"));
        n.setMessage(rs.getString("message"));
        n.setRead(rs.getBoolean("isRead"));
        Timestamp ts = rs.getTimestamp("createdAt");
        if (ts != null) n.setCreatedAt(ts.toLocalDateTime());
        return n;
    }
}
