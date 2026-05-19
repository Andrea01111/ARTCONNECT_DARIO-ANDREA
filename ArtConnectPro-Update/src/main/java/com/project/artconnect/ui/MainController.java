package com.project.artconnect.ui;

import com.project.artconnect.model.CommunityMember;
import com.project.artconnect.persistence.JdbcNotificationDao;
import com.project.artconnect.util.SessionContext;
import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.scene.control.TabPane;
import javafx.application.Platform;

import java.util.List;

public class MainController {
    @FXML private TabPane mainTabPane;
    @FXML private Label notifBadge;

    @FXML
    public void initialize() {
        if (SessionContext.isLoggedIn()) {
            CommunityMember user = SessionContext.getCurrentUser();
            List<?> notifs = new JdbcNotificationDao().findByMemberId(user.getId());
            int count = notifs.size();
            if (count > 0) {
                notifBadge.setText("🔔 " + count + " notification" + (count > 1 ? "s" : ""));
            }
        }
    }

    @FXML
    private void handleExit() {
        Platform.exit();
    }
}
