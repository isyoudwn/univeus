package com.example.univeus.common.util;

import java.util.UUID;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class S3FileManager {
    private static final String S3_URI_FORMAT = "https://%s.s3.%s.amazonaws.com/%s";

    public String createFileName(String originalFileName, String path) {
        return String.format(path + "/%s-%s", UUID.randomUUID(), originalFileName);
    }

    public String getUri(String fileName, String bucketName, String region) {
        return String.format(
                S3_URI_FORMAT,
                bucketName,
                region,
                fileName);
    }
}
