package com.example.univeus.domain.image.service;

import java.util.List;
import org.springframework.web.multipart.MultipartFile;

public interface ImageService {
    String uploadFile(MultipartFile file, String uploadPath);

    List<String> uploadFiles(List<MultipartFile> files, String uploadPath);

}
