package com.student.view;

import com.student.data.UserRepository;
import com.student.Logger;
import com.student.model.Role;
import com.student.model.User;
import javafx.geometry.Insets;
import javafx.scene.control.*;
import javafx.scene.layout.VBox;

public class RegistrationView {
    private VBox view;

    public RegistrationView() {
        Label header = new Label("Регистрация нового пользователя");

        TextField loginField = new TextField();
        loginField.setPromptText("Логин");

        PasswordField passwordField = new PasswordField();
        passwordField.setPromptText("Пароль");

        Button registerButton = new Button("Зарегистрироваться");
        Label messageLabel = new Label();

        registerButton.setOnAction(e -> {
            String login = loginField.getText().trim();
            String password = passwordField.getText().trim();

            if(login.isEmpty() || password.isEmpty()) {
                messageLabel.setText("Заполните все поля!");
                return;
            }

            if(UserRepository.findByUsername(login) != null) {
                messageLabel.setText("Пользователь с таким логином уже существует!");
                return;
            }

            User newUser = UserRepository.addUser(login, password, Role.EMPLOYEE);
            newUser.setActivated(false);
            messageLabel.setText("Регистрация прошла успешно. Ожидайте одобрения администратором.");
            Logger.log("Новая регистрация: " + login + " (EMPLOYEE). Аккаунт не активирован.");

            loginField.clear();
            passwordField.clear();
        });

        view = new VBox(10, header, loginField, passwordField, registerButton, messageLabel);
        view.setPadding(new Insets(20));
    }

    public VBox getView() {
        return view;
    }
}