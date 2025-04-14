package com.student.view;

import com.student.model.User;
import com.student.util.SceneSwitcher;
import javafx.geometry.Insets;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;

public class EmployeeView {
    private VBox view;

    public EmployeeView(User user) {
        BorderPane root = new BorderPane();
        VBox header = new VBox(10);
        header.setPadding(new Insets(10));
        Label headerLabel = new Label("Панель сотрудника. Пользователь: " + user.getUsername());
        Button logoutButton = new Button("Выйти");
        logoutButton.setOnAction(e -> SceneSwitcher.switchToLogin((Stage) logoutButton.getScene().getWindow()));
        header.getChildren().addAll(headerLabel, logoutButton);

        VBox content = new VBox(10);
        content.setPadding(new Insets(10));
        ResourceView resourceView = new ResourceView(user);
        content.getChildren().add(resourceView.getView());

        root.setTop(header);
        root.setCenter(content);
        view = new VBox(root);
    }

    public VBox getView() {
        return view;
    }
}