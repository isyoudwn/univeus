package com.example.univeus.presentation.image.dto.response;

import java.util.List;

public class ImageResponse {

    public record ImageUris(
            List<String> uris
    ) {
        public static ImageUris of(List<String> uris) {
            return new ImageUris(uris);
        }
    }
}
