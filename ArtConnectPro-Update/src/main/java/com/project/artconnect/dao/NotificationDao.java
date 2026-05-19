package com.project.artconnect.dao;

import com.project.artconnect.model.Notification;
import java.util.List;

public interface NotificationDao {
    List<Notification> findByMemberId(int memberId);
    void markAsRead(int notificationId);
}
