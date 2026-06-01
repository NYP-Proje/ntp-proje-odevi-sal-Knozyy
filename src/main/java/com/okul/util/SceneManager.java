package com.okul.util;

import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;

import java.io.IOException;

public class SceneManager {

    private SceneManager() {
    }

    public static void switchTo(Node nodeInWindow, String fxmlPath, String title) {
        try {
            Parent root = FXMLLoader.load(SceneManager.class.getResource(fxmlPath));
            Stage stage = (Stage) nodeInWindow.getScene().getWindow();
            stage.setScene(new Scene(root));
            stage.setTitle(title);
        } catch (IOException e) {
            ErrorHandler.handle("Ekran yuklenemedi: " + fxmlPath, e);
        }
    }
}
