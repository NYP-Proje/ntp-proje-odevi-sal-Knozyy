package com.okul.util;

import java.awt.Desktop;
import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;

public class FileManager {

    private static final String UPLOAD_DIR = "uploads";

    private FileManager() {
    }

    public static String save(File source, String studentId) throws IOException {
        File dir = new File(UPLOAD_DIR);
        if (!dir.exists()) {
            dir.mkdirs();
        }
        String newName = studentId + "_" + System.currentTimeMillis() + "_" + source.getName();
        Path target = Paths.get(UPLOAD_DIR, newName);
        Files.copy(source.toPath(), target, StandardCopyOption.REPLACE_EXISTING);
        return target.toString();
    }

    public static void open(String filePath) throws IOException {
        File file = new File(filePath);
        if (!file.exists()) {
            throw new IOException("Dosya bulunamadi: " + filePath);
        }
        Desktop.getDesktop().open(file);
    }
}
