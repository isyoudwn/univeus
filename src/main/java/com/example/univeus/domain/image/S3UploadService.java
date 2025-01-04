package com.example.univeus.domain.image;

import com.example.univeus.domain.image.ImageService;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;
import lombok.RequiredArgsConstructor;
import org.apache.tomcat.util.http.fileupload.FileUploadException;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import org.springframework.web.multipart.MultipartFile;
import software.amazon.awssdk.core.sync.RequestBody;
import software.amazon.awssdk.services.s3.S3Client;
import software.amazon.awssdk.services.s3.model.PutObjectRequest;

@Component
@RequiredArgsConstructor
public class S3UploadService implements ImageService {

    @Value("${aws.bucket-name}")
    private String bucketName;
    private final S3Client s3Client;


    @Override
    public String uploadFile(MultipartFile file) throws FileUploadException {
        String keyName = createFileName(file.getOriginalFilename());

        PutObjectRequest putObjectRequest = PutObjectRequest.builder()
                .bucket(bucketName)
                .key(keyName)
                .build();

        try (InputStream inputStream = file.getInputStream()) {
            s3Client.putObject(
                    putObjectRequest,
                    RequestBody.fromInputStream(inputStream, file.getSize())
            );
        } catch (IOException e) {
            throw new FileUploadException("S3 파일 업로드 실패: " + file.getOriginalFilename(), e);
        }

        return keyName;
    }


    @Override
    public List<String> uploadFiles(List<MultipartFile> files) {
        List<String> result = new ArrayList<>();

        for (MultipartFile file : files) {
            try {
                String key = uploadFile(file);
                result.add(key);
            } catch (FileUploadException e) {
                System.err.println("파일 업로드 실패: " + e.getMessage());
            }
        }

        return result;
    }

    @Override
    public String createFileName(String originalFileName) {
        return String.format("images/%s-%s", UUID.randomUUID(), originalFileName);
    }
}
