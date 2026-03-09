package org.iesalixar.daw2.pvp.dwese2526_ticketlogger_api_pvp.services;

import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;

@Service
public class FileStorageService {

    private final String uploadDir = "uploads";

    public String saveFile(MultipartFile file) {

        try {

            Path uploadPath = Paths.get(uploadDir);

            if (!Files.exists(uploadPath)) {
                Files.createDirectories(uploadPath);
            }

            String fileName = System.currentTimeMillis() + "_" + file.getOriginalFilename();

            Path filePath = uploadPath.resolve(fileName);

            Files.copy(file.getInputStream(), filePath);

            return uploadDir + "/" + fileName;

        } catch (IOException e) {
            e.printStackTrace();
            return null;
        }
    }

    public void deleteFile(String filePath) {

        try {

            Path path = Paths.get(filePath);

            if (Files.exists(path)) {
                Files.delete(path);
            }

        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}