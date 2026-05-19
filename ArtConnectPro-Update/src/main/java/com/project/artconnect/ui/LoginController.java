package com.project.artconnect.ui;

import com.project.artconnect.model.CommunityMember;
import com.project.artconnect.service.AuthService;
import com.project.artconnect.util.ServiceProvider;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.control.Label;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.stage.Stage;

import java.util.Optional;

public class LoginController {

    @FXML private TextField emailField;
    @FXML private PasswordField passwordField;
    @FXML private Label errorLabel;

    private final AuthService authService = ServiceProvider.getAuthService();

    @FXML
    private void handleLogin() {
        String email = emailField.getText().trim();
        String password = passwordField.getText();

        Optional<CommunityMember> result = authService.login(email, password);
        if (result.isPresent()) {
            try {
                FXMLLoader loader = new FXMLLoader(
                    getClass().getResource("/com/project/artconnect/ui/MainView.fxml"));
                Scene scene = new Scene(loader.load(), 1200, 800);
                Stage stage = (Stage) emailField.getScene().getWindow();
                stage.setScene(scene);
            } catch (Exception e) {
                e.printStackTrace();
                errorLabel.setText("Erreur lors du chargement de l'application.");
            }
        } else {
            errorLabel.setText("Email ou mot de passe incorrect.");
        }
    }
}
