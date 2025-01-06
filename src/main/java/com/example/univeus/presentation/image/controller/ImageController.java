package com.example.univeus.presentation.image.controller;

import com.example.univeus.common.response.Response;
import com.example.univeus.common.response.ResponseMessage;
import com.example.univeus.domain.image.service.ImageService;
import com.example.univeus.presentation.image.dto.response.ImageResponse;
import com.example.univeus.presentation.image.dto.response.ImageResponse.ImageUris;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1/image")
public class ImageController {

    private final ImageService imageService;

    @PostMapping("/poly")
    public ResponseEntity<Response<ImageResponse.ImageUris>> uploadImages(List<MultipartFile> files) {
        List<String> uris = imageService.uploadFiles(files, "post");
        ImageUris imageUris = ImageUris.of(uris);

        return ResponseEntity
                .ok()
                .body(Response.success(
                        ResponseMessage.IMAGE_UPLOAD_SUCCESS.getCode(),
                        ResponseMessage.IMAGE_UPLOAD_SUCCESS.getMessage(),
                        imageUris
                ));
    }
}
