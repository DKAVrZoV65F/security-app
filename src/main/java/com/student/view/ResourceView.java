package com.student.view;

import com.student.Logger;
import com.student.data.ResourceRepository;
import com.student.data.PermissionRepository;
import com.student.model.Permission;
import com.student.model.Resource;
import com.student.model.Role;
import com.student.model.User;
import javafx.beans.property.SimpleStringProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.geometry.Insets;
import javafx.scene.control.*;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.scene.text.Text;

import java.util.List;
import java.util.stream.Collectors;

public class ResourceView {
    private final VBox view;
    private final TableView<Resource> table;
    private final ObservableList<Resource> resourceList;

    private final TextField nameField;
    private final TextField typeField;

    public ResourceView(User currentUser) {
        view = new VBox(10);
        view.setPadding(new Insets(10));

        Text title = new Text("Доступные ресурсы");

        table = new TableView<>();
        resourceList = FXCollections.observableArrayList();

        TableColumn<Resource, String> nameCol = new TableColumn<>("Название");
        nameCol.setCellValueFactory(c -> new SimpleStringProperty(c.getValue().getName()));

        TableColumn<Resource, String> typeCol = new TableColumn<>("Тип");
        typeCol.setCellValueFactory(c -> new SimpleStringProperty(c.getValue().getType()));

        TableColumn<Resource, String> ownerCol = new TableColumn<>("Владелец");
        ownerCol.setCellValueFactory(c -> new SimpleStringProperty(c.getValue().getOwner()));

        table.getColumns().addAll(nameCol, typeCol, ownerCol);
        table.setColumnResizePolicy(TableView.CONSTRAINED_RESIZE_POLICY);
        table.setItems(resourceList);

        nameField = new TextField();
        nameField.setPromptText("Название");

        typeField = new TextField();
        typeField.setPromptText("Тип");

        Button addButton = new Button("Добавить");
        Button deleteButton = new Button("Удалить");
        Button editButton = new Button("Редактировать");
        Button refreshButton = new Button("Обновить");

        HBox controls = new HBox(10, nameField, typeField, addButton, editButton, deleteButton, refreshButton);
        controls.setPadding(new Insets(10, 0, 0, 0));

        addButton.setOnAction(e -> {
            String name = nameField.getText().trim();
            String type = typeField.getText().trim();
            if (name.isEmpty() || type.isEmpty()) return;

            if (ResourceRepository.getByName(name) != null) {
                Alert alert = new Alert(Alert.AlertType.ERROR, "Ресурс с таким названием уже существует!");
                alert.showAndWait();
                return;
            }

            Resource res = new Resource(name, type, currentUser.getUsername());
            ResourceRepository.addResource(res);

            if (currentUser.getRole() == Role.ADMIN) {
                PermissionRepository.grantPermission(new Permission(name, Role.ADMIN, true, true, true));
            } else {
                PermissionRepository.grantPermission(new Permission(name, currentUser.getRole(), true, false, false));
            }
            Logger.log("Пользователь " + currentUser.getUsername() + " добавил ресурс " + name);
            refreshTable(currentUser);
            nameField.clear();
            typeField.clear();
        });

        editButton.setOnAction(e -> {
            Resource selected = table.getSelectionModel().getSelectedItem();
            if (selected == null) return;

            nameField.setText(selected.getName());
            typeField.setText(selected.getType());

            Button saveEdit = new Button("Сохранить изменения");
            saveEdit.setOnAction(ev -> {
                String newName = nameField.getText().trim();
                String newType = typeField.getText().trim();
                if (newName.isEmpty() || newType.isEmpty()) return;

                selected.setName(newName);
                selected.setType(newType);
                ResourceRepository.updateResource(selected);
                Logger.log("Пользователь " + currentUser.getUsername() + " отредактировал ресурс: " + newName);
                refreshTable(currentUser);
                nameField.clear();
                typeField.clear();

                controls.getChildren().remove(saveEdit);
            });

            if(!controls.getChildren().contains(saveEdit)){
                controls.getChildren().add(saveEdit);
            }
        });

        deleteButton.setOnAction(e -> {
            Resource selected = table.getSelectionModel().getSelectedItem();
            if (selected == null) return;


            if (currentUser.getRole() == Role.EMPLOYEE &&
                    (selected.getOwner().equalsIgnoreCase("admin") || selected.getOwner().equalsIgnoreCase("audit"))) {
                Alert alert = new Alert(Alert.AlertType.ERROR, "Сотрудник не может удалять файлы, созданные admin/audit!");
                alert.showAndWait();
                return;
            }

            if (currentUser.getRole() == Role.AUDITOR && selected.getOwner().equalsIgnoreCase("admin")) {
                Alert alert = new Alert(Alert.AlertType.ERROR, "Аудитор не может удалять файлы, созданные admin!");
                alert.showAndWait();
                return;
            }

            ResourceRepository.deleteResource(selected.getId());
            Logger.log("Пользователь " + currentUser.getUsername() + " удалил ресурс " + selected.getName());
            refreshTable(currentUser);
        });

        refreshButton.setOnAction(e -> refreshTable(currentUser));

        view.getChildren().addAll(title, table, controls);
        refreshTable(currentUser);
    }

    private void refreshTable(User currentUser) {
        List<Resource> allResources = ResourceRepository.getAllResources();
        List<Resource> accessible = allResources.stream().filter(res -> {

            if (currentUser.getRole() == Role.ADMIN) {
                return true;
            }

            if (res.getOwner().equals(currentUser.getUsername()))
                return true;

            Permission p = PermissionRepository.getPermission(res.getName(), currentUser.getRole());
            return p != null && p.canRead();
        }).collect(Collectors.toList());
        resourceList.setAll(accessible);
    }

    public VBox getView() {
        return view;
    }
}