package com.example.univeus.domain.image;

import java.util.List;
import org.apache.tomcat.util.http.fileupload.FileUploadException;
import org.springframework.web.multipart.MultipartFile;

public interface ImageService {
    String uploadFile(MultipartFile file) throws FileUploadException;

    List<String> uploadFiles(List<MultipartFile> files);

    String createFileName(String fileName);
}
