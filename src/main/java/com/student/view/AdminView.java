package com.student.view;

import com.student.Logger;
import com.student.data.UserRepository;
import com.student.model.Role;
import com.student.model.User;
import com.student.util.SceneSwitcher;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.geometry.Insets;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;

public class AdminView {
    private VBox view;


    public AdminView(User admin) {

        BorderPane root = new BorderPane();

        VBox header = new VBox(10);
        header.setPadding(new Insets(10));
        Label headerLabel = new Label("Панель администратора. Пользователь: " + admin.getUsername());
        Button logoutButton = new Button("Выйти");
        logoutButton.setOnAction(e -> SceneSwitcher.switchToLogin((Stage) logoutButton.getScene().getWindow()));
        header.getChildren().addAll(headerLabel, logoutButton);
        root.setTop(header);

        TabPane tabPane = new TabPane();

        Tab usersTab = new Tab("Пользователи");
        usersTab.setClosable(false);
        usersTab.setContent(createUsersManagement(admin));

        Tab resourcesTab = new Tab("Ресурсы");
        resourcesTab.setClosable(false);
        ResourceView resourceView = new ResourceView(admin);
        resourcesTab.setContent(resourceView.getView());

        Tab logsTab = new Tab("Логи");
        logsTab.setClosable(false);
        logsTab.setContent(createLogsView());

        tabPane.getTabs().addAll(usersTab, resourcesTab, logsTab);
        root.setCenter(tabPane);

        view = new VBox(root);
    }

    private VBox createUsersManagement(User admin) {
        ObservableList<User> userList = FXCollections.observableArrayList(UserRepository.getAllUsers());
        TableView<User> table = new TableView<>(userList);

        TableColumn<User, String> usernameCol = new TableColumn<>("Логин");
        usernameCol.setCellValueFactory(c -> new javafx.beans.property.SimpleStringProperty(c.getValue().getUsername()));

        TableColumn<User, String> roleCol = new TableColumn<>("Роль");
        roleCol.setCellValueFactory(c -> new javafx.beans.property.SimpleStringProperty(c.getValue().getRole().name()));

        TableColumn<User, String> activatedCol = new TableColumn<>("Активирован");
        activatedCol.setCellValueFactory(c ->
                new javafx.beans.property.SimpleStringProperty(c.getValue().isActivated() ? "Да" : "Нет"));

        table.getColumns().addAll(usernameCol, roleCol, activatedCol);
        table.setColumnResizePolicy(TableView.CONSTRAINED_RESIZE_POLICY);

        TextField usernameField = new TextField();
        usernameField.setPromptText("Логин");

        PasswordField passwordField = new PasswordField();
        passwordField.setPromptText("Пароль");

        Button addButton = new Button("Добавить");
        addButton.setOnAction(e -> {
            String username = usernameField.getText().trim();
            String password = passwordField.getText().trim();

            if (!username.isEmpty() && !password.isEmpty()) {
                User newUser = UserRepository.addUser(username, password, Role.EMPLOYEE);
                newUser.setActivated(false);
                userList.setAll(UserRepository.getAllUsers());
                Logger.log("Админ " + admin.getUsername() + " добавил пользователя " + newUser.getUsername() + " c ролью EMPLOYEE");
            }
        });

        Button deleteButton = new Button("Удалить");
        deleteButton.setOnAction(e -> {
            User selected = table.getSelectionModel().getSelectedItem();
            if (selected != null) {
                if (selected.getUsername().equals(admin.getUsername())) {
                    Alert alert = new Alert(Alert.AlertType.WARNING, "Вы не можете удалить свой аккаунт!");
                    alert.showAndWait();
                    return;
                }
                UserRepository.deleteUser(selected.getUsername());
                userList.setAll(UserRepository.getAllUsers());
                Logger.log("Админ " + admin.getUsername() + " удалил пользователя " + selected.getUsername());
            }
        });

        Button activateButton = new Button("Активировать");
        activateButton.setOnAction(e -> {
            User selected = table.getSelectionModel().getSelectedItem();
            if (selected != null && !selected.isActivated()) {
                UserRepository.updateUserActivation(selected.getUsername(), true);
                selected.setActivated(true);
                userList.setAll(UserRepository.getAllUsers());
                Logger.log("Админ " + admin.getUsername() + " активировал пользователя " + selected.getUsername());
            }
        });

        Button deactivateButton = new Button("Деактивировать");
        deactivateButton.setOnAction(e -> {
            User selected = table.getSelectionModel().getSelectedItem();
            if (selected != null) {
                if (selected.getUsername().equals(admin.getUsername())) {
                    Alert alert = new Alert(Alert.AlertType.WARNING, "Вы не можете деактивировать собственный аккаунт!");
                    alert.showAndWait();
                    return;
                }
                if (selected.isActivated()) {
                    UserRepository.updateUserActivation(selected.getUsername(), false);
                    selected.setActivated(false);
                    userList.setAll(UserRepository.getAllUsers());
                    Logger.log("Админ " + admin.getUsername() + " деактивировал пользователя " + selected.getUsername());
                }
            }
        });

        Button changeRoleButton = new Button("Изменить роль");
        changeRoleButton.setOnAction(e -> {
            User selected = table.getSelectionModel().getSelectedItem();
            if (selected == null) return;

            if (selected.getUsername().equals(admin.getUsername())) {
                Alert alert = new Alert(Alert.AlertType.WARNING, "Вы не можете изменить свою роль!");
                alert.showAndWait();
                return;
            }

            Stage dialog = new Stage();
            VBox dialogVbox = new VBox(10);
            dialogVbox.setPadding(new Insets(10));
            Label label = new Label("Выберите новую роль для пользователя: " + selected.getUsername());
            ComboBox<Role> roleCombo = new ComboBox<>(FXCollections.observableArrayList(Role.values()));
            roleCombo.setValue(selected.getRole());
            Button okButton = new Button("ОК");
            okButton.setOnAction(ev -> {
                Role newRole = roleCombo.getValue();
                UserRepository.updateUserRole(selected.getUsername(), newRole);
                selected.setRole(newRole);
                userList.setAll(UserRepository.getAllUsers());
                Logger.log("Админ " + admin.getUsername() + " изменил роль пользователя " + selected.getUsername() + " на " + newRole);
                dialog.close();
            });
            dialogVbox.getChildren().addAll(label, roleCombo, okButton);
            Scene dialogScene = new Scene(dialogVbox, 300, 150);
            dialog.setScene(dialogScene);
            dialog.setTitle("Смена роли");
            dialog.show();
        });

        HBox form = new HBox(10, usernameField, passwordField, addButton, deleteButton, activateButton, deactivateButton, changeRoleButton);
        form.setPadding(new Insets(10));
        VBox usersManagement = new VBox(10, new Label("Панель управления пользователями"), table, form);
        usersManagement.setPadding(new Insets(10));
        return usersManagement;
    }

    private VBox createLogsView() {
        VBox logsBox = new VBox(10);
        logsBox.setPadding(new Insets(10));
        Label label = new Label("Логи системы");

        ListView<String> logsList = new ListView<>();
        Runnable loadLogs = () -> {
            logsList.setItems(FXCollections.observableArrayList(Logger.getLogEntries()));
        };
        loadLogs.run();

        Button refreshButton = new Button("Обновить");
        refreshButton.setOnAction(e -> loadLogs.run());

        logsBox.getChildren().addAll(label, logsList, refreshButton);
        return logsBox;
    }

    public VBox getView() {
        return view;
    }
}