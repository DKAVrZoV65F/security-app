package com.student;

import com.student.db.DBInitializer;
import com.student.view.LoginView;
import javafx.application.Application;
import javafx.scene.Scene;
import javafx.stage.Stage;

public class Main extends Application {

    public static void main(String[] args) {
        launch(args);
    }

    @Override
    public void start(Stage primaryStage) {
        DBInitializer.initialize();

        LoginView loginView = new LoginView();
        Scene scene = new Scene(loginView.getView(), 400, 300);
        primaryStage.setTitle("Security System");
        primaryStage.setScene(scene);
        primaryStage.show();
    }
}

// --module-path /Users/kamchatka/.m2/repository/org/openjfx/javafx-controls/21.0.2;/Users/kamchatka/.m2/repository/org/openjfx/javafx-graphics/21.0.2;/Users/kamchatka/.m2/repository/org/openjfx/javafx-base/21.0.2 --add-modules javafx.controls,javafx.graphics,javafx.base
