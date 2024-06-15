package com.backend.backendapp.config.impl;

import com.backend.backendapp.config.Fileupload;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;

@Service
public class FileServiceImpl implements Fileupload {

    @Override
    public void uploadFile(String path, String fileName, MultipartFile file) throws IOException {
        // Full file path
        String currentPath = System.getProperty("user.dir");
        String uploadDir = currentPath + File.separator + "backend" + File.separator + path;
        String filePath = uploadDir + File.separator + fileName.trim();

        // Create folder if not exists
        File folder = new File(uploadDir);
        if (!folder.exists()) {
            folder.mkdirs();
        }

        // Copy file
        Path destinationPath = Paths.get(filePath);
        Files.copy(file.getInputStream(), destinationPath, StandardCopyOption.REPLACE_EXISTING);
    }
    
}
