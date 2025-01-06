package com.example.univeus.domain.image.service;

import static com.example.univeus.common.response.ResponseMessage.IMAGE_UPLOAD_FAIL;

import com.example.univeus.common.config.S3Config;
import com.example.univeus.common.util.S3FileManager;
import com.example.univeus.domain.image.exception.ImageException;
import com.example.univeus.domain.image.service.ImageService;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import org.springframework.web.multipart.MultipartFile;
import software.amazon.awssdk.core.sync.RequestBody;
import software.amazon.awssdk.services.s3.S3Client;
import software.amazon.awssdk.services.s3.model.PutObjectRequest;

@Component
@RequiredArgsConstructor
public class S3UploadService implements ImageService {

    private final S3Client s3Client;
    private final S3Config s3Config;
    private final S3FileManager s3FileManager;


    @Override
    public String uploadFile(MultipartFile file, String uploadPath) {
        String fileName = s3FileManager.createFileName(
                file.getOriginalFilename(),
                uploadPath);

        PutObjectRequest putObjectRequest = PutObjectRequest.builder()
                .bucket(s3Config.getBucketName())
                .key(fileName)
                .contentType(file.getContentType())
                .build();

        try (InputStream inputStream = file.getInputStream()) {
            s3Client.putObject(
                    putObjectRequest,
                    RequestBody.fromInputStream(inputStream, file.getSize())
            );
        } catch (IOException e) {
            throw new ImageException(IMAGE_UPLOAD_FAIL);
        }

        return s3FileManager.getUri(fileName, s3Config.getBucketName(), s3Config.getRegion());
    }


    @Override
    public List<String> uploadFiles(List<MultipartFile> files, String uploadPath) {
        List<String> uris = new ArrayList<>();

        for (MultipartFile file : files) {
            String uri = uploadFile(file, uploadPath);
            uris.add(uri);
        }

        return uris;
    }
}
