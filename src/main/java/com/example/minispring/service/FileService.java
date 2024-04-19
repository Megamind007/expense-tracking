package com.example.minispring.service;

import java.io.IOException;

import org.springframework.core.io.Resource;
import org.springframework.web.multipart.MultipartFile;

public interface FileService {
    String saveFile(MultipartFile file) throws IOException;
    Resource getFileByFileName(String fileName) throws IOException;
}
