package com.okul.util;

import com.google.auth.oauth2.GoogleCredentials;
import com.google.cloud.firestore.Firestore;
import com.google.firebase.FirebaseApp;
import com.google.firebase.FirebaseOptions;
import com.google.firebase.cloud.FirestoreClient;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.FileInputStream;

public class FirebaseConfig {

    private static final Logger log = LoggerFactory.getLogger(FirebaseConfig.class);
    private static final String KEY_FILE = "serviceAccountKey.json";
    private static Firestore db;

    private FirebaseConfig() {
    }

    public static Firestore getDb() {
        if (db == null) {
            baslat();
        }
        return db;
    }

    private static void baslat() {
        try {

            if (FirebaseApp.getApps().isEmpty()) {
                FileInputStream anahtar = new FileInputStream(KEY_FILE);
                FirebaseOptions options = FirebaseOptions.builder()
                        .setCredentials(GoogleCredentials.fromStream(anahtar))
                        .build();
                FirebaseApp.initializeApp(options);
                log.info("Firebase baglantisi kuruldu.");
            }
            db = FirestoreClient.getFirestore();
        } catch (Exception e) {
            log.error("Firebase baslatilamadi: {}", e.getMessage());
            throw new RuntimeException(
                    "Firebase baglantisi kurulamadi. 'serviceAccountKey.json' dosyasi " +
                    "proje kok klasorunde mi, kontrol et.", e);
        }
    }
}
