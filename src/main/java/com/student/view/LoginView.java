package com.student.view;

import com.student.Logger;
import com.student.data.UserRepository;
import com.student.model.User;
import com.student.util.SceneSwitcher;
import javafx.geometry.Insets;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;
import org.mindrot.jbcrypt.BCrypt;

public class LoginView {
    private VBox view;

    public LoginView() {
        Label label = new Label("Вход в систему");
        TextField loginField = new TextField();
        loginField.setPromptText("Логин");
        PasswordField passwordField = new PasswordField();
        passwordField.setPromptText("Пароль");
        Button loginButton = new Button("Войти");
        Button registerButton = new Button("Регистрация");
        Label messageLabel = new Label();

        loginButton.setOnAction(e -> {
            String login = loginField.getText().trim();
            String password = passwordField.getText().trim();

            User user = UserRepository.findByUsername(login);
            if(user != null && BCrypt.checkpw(password, user.getPasswordHash())){
                if(!user.isActivated()){
                    messageLabel.setText("Аккаунт не активирован администратором!");
                    Logger.log("Попытка входа неактивированного пользователя: " + login);
                    return;
                }
                Logger.log("Успешный вход: " + user.getUsername());
                SceneSwitcher.switchToDashboard(user);

                ((Stage) loginButton.getScene().getWindow()).close();
            } else {
                Logger.log("Неудачный вход для: " + login);
                messageLabel.setText("Неверный логин или пароль");
            }
        });

        registerButton.setOnAction(e -> {
            Stage regStage = new Stage();
            RegistrationView regView = new RegistrationView();
            Scene scene = new Scene(regView.getView(), 400, 300);
            regStage.setTitle("Регистрация");
            regStage.setScene(scene);
            regStage.show();
        });

        view = new VBox(10, label, loginField, passwordField, loginButton, registerButton, messageLabel);
        view.setPadding(new Insets(20));
    }

    public VBox getView() {
        return view;
    }
}