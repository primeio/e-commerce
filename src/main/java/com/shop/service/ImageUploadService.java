package com.shop.service;

import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;


import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;

@Service

public class ImageUploadService {

    private final Path baseUploadPath;

    public ImageUploadService(@Value("${file.upload-dir:uploads}") String uploadDir) {
        this.baseUploadPath = Paths.get(uploadDir).toAbsolutePath().normalize();
        try {
            if (Files.notExists(baseUploadPath))
                Files.createDirectories(this.baseUploadPath);
        } catch (Exception e) {
            throw new RuntimeException("Could not create upload directory: " + baseUploadPath, e);
        }

    }
    public String uploadImage(MultipartFile file, String folder) {
        if (file == null || file.isEmpty()) {
            throw new RuntimeException("Failed to upload empty file");
        }
        String safeFolder = folder == null ? "" : folder.replaceAll("[^a-zA-Z0-9]", "");
        Path pathDir = this.baseUploadPath.resolve(safeFolder).normalize();
        try {
            if(!pathDir.startsWith(baseUploadPath)){
                throw new RuntimeException("Invalid folder path");
            }
            if (Files.notExists(pathDir)) {
                Files.createDirectories(pathDir);
            }
            String original = file.getOriginalFilename()== null ? "file" : Paths
                    .get(file.getOriginalFilename()).getFileName().toString();
            String fileName =  System.currentTimeMillis()+ original.replaceAll("[^a-zA-Z0-9]", "_");
            Path Target = pathDir.resolve(fileName);
            Files.copy(file.getInputStream(), Target , StandardCopyOption.REPLACE_EXISTING);
            return "/"+ baseUploadPath.getFileName().toString()+"/"+safeFolder+"/"+fileName;
        }catch (Exception e){
            throw new RuntimeException("Failed to upload file: " + file.getOriginalFilename(), e);


        }
    }
}