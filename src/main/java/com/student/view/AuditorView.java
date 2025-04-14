package com.student.view;

import com.student.Logger;
import com.student.model.User;
import com.student.util.SceneSwitcher;
import javafx.collections.FXCollections;
import javafx.geometry.Insets;
import javafx.scene.control.*;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;

public class AuditorView {
    private VBox view;


    public AuditorView(User user) {

        BorderPane root = new BorderPane();
        VBox header = new VBox(10);
        header.setPadding(new Insets(10));
        Label headerLabel = new Label("Панель аудитора. Пользователь: " + user.getUsername());
        Button logoutButton = new Button("Выйти");
        logoutButton.setOnAction(e -> SceneSwitcher.switchToLogin((Stage) logoutButton.getScene().getWindow()));
        header.getChildren().addAll(headerLabel, logoutButton);
        root.setTop(header);

        TabPane tabPane = new TabPane();

        Tab logsTab = new Tab("Логи");
        logsTab.setClosable(false);
        logsTab.setContent(createLogsView());

        Tab resourcesTab = new Tab("Ресурсы");
        resourcesTab.setClosable(false);
        ResourceView resourceView = new ResourceView(user);
        resourcesTab.setContent(resourceView.getView());

        tabPane.getTabs().addAll(logsTab, resourcesTab);
        root.setCenter(tabPane);

        view = new VBox(root);
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