package com.backend.backendapp.config;

import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;

@Service
public interface Fileupload {
    void uploadFile(String path, String fileName, MultipartFile file) throws IOException;
}
