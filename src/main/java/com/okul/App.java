package com.okul;

import com.okul.service.AuthService;
import com.okul.util.ErrorHandler;
import javafx.application.Application;
import javafx.application.Platform;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;

public class App extends Application {

    @Override
    public void start(Stage stage) {
        try {

            new AuthService().ensureDefaultAdmin();

            Parent root = FXMLLoader.load(getClass().getResource("/fxml/login.fxml"));
            stage.setTitle("Okul Yonetim Sistemi - Giris");
            stage.setScene(new Scene(root));
            stage.show();
        } catch (Exception e) {
            ErrorHandler.handle(
                    "Uygulama baslatilamadi. 'serviceAccountKey.json' dosyasini ve internet " +
                    "baglantinizi kontrol edin.", e);
            Platform.exit();
        }
    }

    public static void main(String[] args) {
        launch(args);
    }
}
