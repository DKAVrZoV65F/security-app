package com.student.util;

import com.student.model.User;
import com.student.view.AdminView;
import com.student.view.AuditorView;
import com.student.view.EmployeeView;
import com.student.Main;
import com.student.view.LoginView;
import javafx.scene.Scene;
import javafx.stage.Stage;

public class SceneSwitcher {
    public static void switchToDashboard(User user) {
        Scene scene = switch (user.getRole()) {
            case ADMIN -> new Scene(new AdminView(user).getView(), 800, 600);
            case AUDITOR -> new Scene(new AuditorView(user).getView(), 800, 600);
            case EMPLOYEE -> new Scene(new EmployeeView(user).getView(), 800, 600);
        };

        Stage stage = new Stage();
        stage.setTitle("Добро пожаловать, " + user.getUsername());
        stage.setScene(scene);
        stage.show();
    }

    public static void switchToLogin(Stage currentStage) {
        currentStage.close();
        Stage stage = new Stage();
        LoginView loginView = new LoginView();
        Scene scene = new Scene(loginView.getView(), 400, 300);
        stage.setTitle("Security System - Авторизация");
        stage.setScene(scene);
        stage.show();
    }
}