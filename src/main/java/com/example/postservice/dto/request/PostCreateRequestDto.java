package com.example.postservice.dto.request;

import java.util.List;

public record PostCreateRequestDto(String userLink, Long personalPostId, String postVoiceFileUrl, Long categoryId, Long subCategoryId, String subject, String title, String content, String thumbnail, String thumbnailImageUrl, List<String> tags, Boolean accessibility) {
}
